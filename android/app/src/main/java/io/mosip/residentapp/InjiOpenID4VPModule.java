package io.mosip.residentapp;

import static io.mosip.openID4VP.authorizationResponse.AuthorizationResponseUtilsKt.toJsonString;
import static io.mosip.openID4VP.constants.FormatType.LDP_VC;
import static io.mosip.openID4VP.constants.FormatType.MSO_MDOC;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.mosip.openID4VP.authorizationRequest.clientMetadata.ClientMetadata;
import io.mosip.openID4VP.authorizationRequest.clientMetadata.ClientMetadataSerializer;
import io.mosip.openID4VP.constants.ClientIdScheme;
import io.mosip.openID4VP.constants.ContentEncryptionAlgorithm;
import io.mosip.openID4VP.constants.KeyManagementAlgorithm;
import io.mosip.openID4VP.constants.RequestSigningAlgorithm;
import io.mosip.openID4VP.constants.ResponseType;
import io.mosip.openID4VP.constants.VPFormatType;
import io.mosip.openID4VP.exceptions.OpenID4VPExceptions;

import static io.mosip.openID4VP.common.OpenID4VPErrorCodes.ACCESS_DENIED;
import static io.mosip.openID4VP.common.OpenID4VPErrorCodes.INVALID_TRANSACTION_DATA;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.function.Function;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.mosip.openID4VP.OpenID4VP;
import io.mosip.openID4VP.authorizationRequest.AuthorizationRequest;
import io.mosip.openID4VP.authorizationRequest.VPFormatSupported;
import io.mosip.openID4VP.authorizationRequest.Verifier;
import io.mosip.openID4VP.authorizationRequest.WalletMetadata;
import io.mosip.openID4VP.authorizationResponse.unsignedVPToken.UnsignedVPToken;
import io.mosip.openID4VP.authorizationResponse.vpTokenSigningResult.VPTokenSigningResult;
import io.mosip.openID4VP.authorizationResponse.vpTokenSigningResult.types.ldp.LdpVPTokenSigningResult;
import io.mosip.openID4VP.authorizationResponse.vpTokenSigningResult.types.mdoc.DeviceAuthentication;
import io.mosip.openID4VP.authorizationResponse.vpTokenSigningResult.types.mdoc.MdocVPTokenSigningResult;
import io.mosip.openID4VP.constants.FormatType;
import kotlinx.serialization.json.Json;

public class InjiOpenID4VPModule extends ReactContextBaseJavaModule {
    private static final String TAG = "InjiOpenID4VPModule";
    private static final String MODULE_NAME = "InjiOpenID4VP";

    private OpenID4VP openID4VP;
    private Gson gson;

    InjiOpenID4VPModule(@Nullable ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @NonNull
    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @SuppressLint("LogNotTimber")
    @ReactMethod
    public void initSdk(String appId, ReadableMap walletMetadata ) {
        Log.d(TAG, "Initializing InjiOpenID4VPModule with " + appId);

        WalletMetadata metadata = parseWalletMetadata(walletMetadata);

        openID4VP = new OpenID4VP(appId, metadata);
        gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .disableHtmlEscaping()
                .create();
    }

    @ReactMethod
    public void authenticateVerifier(String urlEncodedAuthorizationRequest,
                                     ReadableArray trustedVerifiers,
                                     Boolean shouldValidateClient,
                                     Promise promise) {
        try {
            List<Verifier> verifierList = parseVerifiers(trustedVerifiers);

            AuthorizationRequest authRequest = openID4VP.authenticateVerifier(
                    urlEncodedAuthorizationRequest,
                    verifierList,
                    shouldValidateClient
            );

            String authRequestJson = gson.toJson(authRequest, AuthorizationRequest.class);
            promise.resolve(authRequestJson);
        } catch (Exception e) {
            rejectWithOpenID4VPExceptions(e, promise);
        }
    }

    @ReactMethod
    public void constructUnsignedVPToken(ReadableMap selectedVCs, String holderId, String signatureSuite, Promise promise) {
        try {
            Map<String, Map<FormatType, List<Object>>> selectedVCsMap = parseSelectedVCs(selectedVCs);
            Map<FormatType, UnsignedVPToken> vpTokens = openID4VP.constructUnsignedVPToken(selectedVCsMap, holderId, signatureSuite);
            promise.resolve(toJsonString(vpTokens));
        } catch (Exception e) {
            rejectWithOpenID4VPExceptions(e, promise);
        }
    }

    @ReactMethod
    public void shareVerifiablePresentation(ReadableMap vpTokenSigningResultMap, Promise promise) {
        try {
            Map<FormatType, VPTokenSigningResult> authContainer = parseVPTokenSigningResult(vpTokenSigningResultMap);
            String response = openID4VP.shareVerifiablePresentation(authContainer);
            promise.resolve(response);
        } catch (Exception e) {
            rejectWithOpenID4VPExceptions(e, promise);
        }
    }

    @ReactMethod
    private static void rejectWithOpenID4VPExceptions(Exception e, Promise promise) {
        if (e instanceof OpenID4VPExceptions) {
            OpenID4VPExceptions ex = (OpenID4VPExceptions) e;
            promise.reject(ex.getErrorCode(), ex.getMessage(), ex);
        } else {
            promise.reject("ERR_UNKNOWN", e.getMessage(), e);
        }
    }

    @ReactMethod
    public void sendErrorToVerifier(String errorMessage, String errorCode) {
        OpenID4VPExceptions exception;

        switch (errorCode) {
            case ACCESS_DENIED:
                exception = new OpenID4VPExceptions.AccessDenied(errorMessage, "InjiOpenID4VPModule");
                break;
            case INVALID_TRANSACTION_DATA:
                exception = new OpenID4VPExceptions.InvalidTransactionData(errorMessage, "InjiOpenID4VPModule");
                break;
            default:
                exception = new OpenID4VPExceptions.GenericFailure(errorMessage, "InjiOpenID4VPModule");
                break;
        }

        openID4VP.sendErrorToVerifier(exception);
    }

    private WalletMetadata parseWalletMetadata(ReadableMap walletMetadata) {
        Boolean presentationDefinitionUriSupported = walletMetadata.hasKey("presentation_definition_uri_supported")
                ? walletMetadata.getBoolean("presentation_definition_uri_supported")
                : null;

        Map<VPFormatType, VPFormatSupported> vpFormatsSupportedMap = parseVpFormatsSupported(walletMetadata);

        return  new WalletMetadata(
                presentationDefinitionUriSupported,
                vpFormatsSupportedMap,
                convertReadableArrayToEnumList(walletMetadata, "client_id_schemes_supported", ClientIdScheme.Companion::fromValue),
                convertReadableArrayToEnumList(walletMetadata, "request_object_signing_alg_values_supported", RequestSigningAlgorithm.Companion::fromValue),
                convertReadableArrayToEnumList(walletMetadata, "authorization_encryption_alg_values_supported", KeyManagementAlgorithm.Companion::fromValue),
                convertReadableArrayToEnumList(walletMetadata, "authorization_encryption_enc_values_supported", ContentEncryptionAlgorithm.Companion::fromValue),
                convertReadableArrayToEnumList(walletMetadata, "response_type_supported", ResponseType.Companion::fromValue)
        );
    }

    private Map<VPFormatType, VPFormatSupported> parseVpFormatsSupported(ReadableMap walletMetadata) {
        Map<VPFormatType, VPFormatSupported> vpFormatsSupportedMap = new HashMap<>();
        if (walletMetadata.hasKey("vp_formats_supported")) {
            ReadableMap vpFormatsMap = walletMetadata.getMap("vp_formats_supported");
            if (vpFormatsMap != null) {
                addVpFormatSupported(vpFormatsMap, "ldp_vc", vpFormatsSupportedMap);
                addVpFormatSupported(vpFormatsMap, "mso_mdoc", vpFormatsSupportedMap);
            }
        }
        return vpFormatsSupportedMap;
    }

    private <T> List<T> convertReadableArrayToEnumList(ReadableMap readableMap, String key, Function<String, T> converter) {
        if (!readableMap.hasKey(key)) return null;
        ReadableArray readableArray = readableMap.getArray(key);
        List<T> list = new ArrayList<>();
        for (int i = 0; i < Objects.requireNonNull(readableArray).size(); i++) {
            list.add(converter.apply(readableArray.getString(i)));
        }
        return list;
    }


    private void addVpFormatSupported(ReadableMap vpFormatsMap, String key, Map<VPFormatType, VPFormatSupported> vpFormatsSupportedMap) {
        if (vpFormatsMap.hasKey(key)) {
            ReadableMap formatMap = vpFormatsMap.getMap(key);
            if (formatMap != null && formatMap.hasKey("alg_values_supported")) {
                ReadableArray algArray = formatMap.getArray("alg_values_supported");
                List<String> algValuesList = algArray != null ? convertReadableArrayToList(algArray) : null;
                vpFormatsSupportedMap.put(VPFormatType.Companion.fromValue(key), new VPFormatSupported(algValuesList));
            }
        }
    }



    private List<Verifier> parseVerifiers(ReadableArray verifiersArray) {
        List<Verifier> verifiers = new ArrayList();

        for (int i = 0; i < verifiersArray.size(); i++) {
            ReadableMap verifierMap = verifiersArray.getMap(i);
            String clientId = verifierMap.getString("client_id");
            ReadableArray responseUris = verifierMap.getArray("response_uris");
            List<String> responseUriList = convertReadableArrayToList(responseUris);
            ClientMetadata clientMetadata = null;
            if (verifierMap.hasKey("client_metadata") && !verifierMap.isNull("client_metadata")) {
                try {
                    ReadableMap metadataMap = verifierMap.getMap("client_metadata");
                    String metadataJsonString = readableMapToJson(metadataMap).toString();
                    clientMetadata = Json.Default.decodeFromString(ClientMetadataSerializer.INSTANCE, metadataJsonString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    
            verifiers.add(new Verifier(clientId, responseUriList, clientMetadata));
        }

        return verifiers;
    }
    private static JSONObject readableMapToJson(ReadableMap readableMap) {
        JSONObject jsonObject = new JSONObject();
        ReadableMapKeySetIterator iterator = readableMap.keySetIterator();
    
        while (iterator.hasNextKey()) {
            String key = iterator.nextKey();
            ReadableType type = readableMap.getType(key);
            try {
                switch (type) {
                    case String:
                        jsonObject.put(key, readableMap.getString(key));
                        break;
                    case Number:
                        jsonObject.put(key, readableMap.getDouble(key));
                        break;
                    case Boolean:
                        jsonObject.put(key, readableMap.getBoolean(key));
                        break;
                    case Map:
                        jsonObject.put(key, readableMapToJson(readableMap.getMap(key)));
                        break;
                    case Array:
                        jsonObject.put(key, readableArrayToJson(readableMap.getArray(key)));
                        break;
                    case Null:
                        jsonObject.put(key, JSONObject.NULL);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    
        return jsonObject;
    }
    
    private static JSONArray readableArrayToJson(ReadableArray readableArray) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < readableArray.size(); i++) {
            ReadableType type = readableArray.getType(i);
            try {
                switch (type) {
                    case String:
                        jsonArray.put(readableArray.getString(i));
                        break;
                    case Number:
                        jsonArray.put(readableArray.getDouble(i));
                        break;
                    case Boolean:
                        jsonArray.put(readableArray.getBoolean(i));
                        break;
                    case Map:
                        jsonArray.put(readableMapToJson(readableArray.getMap(i)));
                        break;
                    case Array:
                        jsonArray.put(readableArrayToJson(readableArray.getArray(i)));
                        break;
                    case Null:
                        jsonArray.put(JSONObject.NULL);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }
    

    private Map<String, Map<FormatType, List<Object>>> parseSelectedVCs(ReadableMap selectedVCs) {
        if (selectedVCs == null) {
            return Collections.emptyMap();
        }
        Map<String, Map<FormatType, List<Object>>> selectedVCsMap = new HashMap<>();
        ReadableMapKeySetIterator iterator = selectedVCs.keySetIterator();
        while (iterator.hasNextKey()) {
            String inputDescriptorId = iterator.nextKey();
            ReadableMap formatMap = selectedVCs.getMap(inputDescriptorId);
            if (formatMap == null) {
                continue;
            }
            Map<FormatType, List<Object>> formatTypeCredentialsMap = new EnumMap<>(FormatType.class);
            ReadableMapKeySetIterator formatIterator = formatMap.keySetIterator();

            while (formatIterator.hasNextKey()) {
                String formatStr = formatIterator.nextKey();
                ReadableArray vcsArray = formatMap.getArray(formatStr);
                if (vcsArray == null) {
                    continue;
                }
                FormatType formatType = getFormatType(formatStr);
                if (formatType != null) {
                    List<Object> vcsList = convertReadableArrayToListOfCredential(formatType, vcsArray);
                    formatTypeCredentialsMap.put(formatType, vcsList);
                }
            }

            if (!formatTypeCredentialsMap.isEmpty()) {
                selectedVCsMap.put(inputDescriptorId, formatTypeCredentialsMap);
            }
        }
        return selectedVCsMap;
    }

    private Map<FormatType, VPTokenSigningResult> parseVPTokenSigningResult(ReadableMap vpTokenSigningResultMap) {
        if (vpTokenSigningResultMap == null) {
            return Collections.emptyMap();
        }
        Map<FormatType, VPTokenSigningResult> formattedMetadata = new EnumMap<>(FormatType.class);
        ReadableMapKeySetIterator iterator = vpTokenSigningResultMap.keySetIterator();
        while (iterator.hasNextKey()) {
            String formatStr = iterator.nextKey();
            ReadableMap metadata = vpTokenSigningResultMap.getMap(formatStr);
            if (metadata == null) {
                continue;
            }
            FormatType formatType = getFormatType(formatStr);
            VPTokenSigningResult vpTokenSigningResult = createVPTokenSigningResult(formatType, metadata);
            if (vpTokenSigningResult != null) {
                formattedMetadata.put(formatType, vpTokenSigningResult);
            }
        }

        return formattedMetadata;
    }

    private VPTokenSigningResult createVPTokenSigningResult(FormatType formatType, ReadableMap metadata) {
        switch (formatType) {
            case LDP_VC: {
                String jws =metadata.getString("jws");
                String proofValue =metadata.getString("proofValue");
                String signatureAlgorithm =metadata.getString("signatureAlgorithm");
                return new LdpVPTokenSigningResult(jws, proofValue, signatureAlgorithm );
            }
            case MSO_MDOC: {
                Map<String, DeviceAuthentication> signatureData = new HashMap<>();
                ReadableMapKeySetIterator docTypeIterator = metadata.keySetIterator();
                while (docTypeIterator.hasNextKey()) {
                    String docType = docTypeIterator.nextKey();
                    ReadableMap deviceAuthenticationMap = metadata.getMap(docType);
                    if (deviceAuthenticationMap != null) {
                        String signature = requireNonNullString(deviceAuthenticationMap, "signature");
                        String algorithm = requireNonNullString(deviceAuthenticationMap, "mdocAuthenticationAlgorithm");
                        DeviceAuthentication deviceAuthentication = new DeviceAuthentication(
                                signature = signature,
                                algorithm = algorithm
                        );
                        signatureData.put(docType, deviceAuthentication);
                    }
                }
                return new MdocVPTokenSigningResult(signatureData);
            }
            default:
                return null;
        }
    }

    private List<Object> convertReadableArrayToListOfCredential(FormatType formatType, ReadableArray credentialList) {
        switch (formatType) {
            case LDP_VC: {
                List<Object> ldpVcList = new ArrayList<>();
                for (int i = 0; i < credentialList.size(); i++) {
                    ReadableMap credentialMap = credentialList.getMap(i);
                    ldpVcList.add(credentialMap.toHashMap());
                }
                return ldpVcList;
            }
            case MSO_MDOC: {
                List<Object> mdocVcList = new ArrayList<>();
                for (int i = 0; i < credentialList.size(); i++) {
                    String credential = credentialList.getString(i);
                    mdocVcList.add(credential);
                }
                return mdocVcList;

            }
            default:
                return null;
        }
    }

    private FormatType getFormatType(String formatStr) {
        if (LDP_VC.getValue().equals(formatStr)) {
            return LDP_VC;
        } else if (MSO_MDOC.getValue().equals(formatStr)) {
            return MSO_MDOC;
        }
        throw new UnsupportedOperationException("Credential format '" + formatStr + "' is not supported");
    }

    private List<String> convertReadableArrayToList(ReadableArray readableArray) {
        List<String> list = new ArrayList<>();

        for (int i = 0; i < readableArray.size(); i++) {
            list.add(readableArray.getString(i));
        }

        return list;
    }

    private String requireNonNullString(ReadableMap map, String key) {
        String value = map.getString(key);
        return Objects.requireNonNull(value, key + " cannot be null");
    }
}
