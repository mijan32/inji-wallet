import {NativeModules} from 'react-native';
import {__AppId} from '../GlobalVariables';
import {VC} from '../../machines/VerifiableCredential/VCMetaMachine/vc';
import {getJWT} from '../cryptoutil/cryptoUtil';
import {getJWK} from '../openId4VCI/Utils';
import getAllConfigurations from '../api';

export const OpenID4VP_Key_Ref = 'OpenID4VP_KeyPair';
export const OpenID4VP_Proof_Sign_Algo_Suite = 'Ed25519Signature2018';
export const OpenID4VP_Domain = 'OpenID4VP';
export const OpenID4VP_Proof_Sign_Algo = 'EdDSA';

export class OpenID4VP {
  static InjiOpenID4VP = NativeModules.InjiOpenID4VP;

  static initialize() {
    OpenID4VP.InjiOpenID4VP.init(__AppId.getValue());
  }

  static async authenticateVerifier(
    urlEncodedAuthorizationRequest: string,
    trustedVerifiersList: any,
  ) {
    const shouldValidateClient = await isClientValidationRequired();
    const authenticationResponse =
      await OpenID4VP.InjiOpenID4VP.authenticateVerifier(
        urlEncodedAuthorizationRequest,
        trustedVerifiersList,
        shouldValidateClient,
      );
    return JSON.parse(authenticationResponse);
  }

  static async constructVerifiablePresentationToken(
    selectedVCs: Record<string, VC[]>,
  ) {
    let updatedSelectedVCs = {};
    Object.keys(selectedVCs).forEach(inputDescriptorId => {
      updatedSelectedVCs[inputDescriptorId] = selectedVCs[
        inputDescriptorId
      ].map(vc => JSON.stringify(vc));
    });

    const vpToken =
      await OpenID4VP.InjiOpenID4VP.constructVerifiablePresentationToken(
        updatedSelectedVCs,
      );
    return vpToken;
  }

  static async shareVerifiablePresentation(
    vpResponseMetadata: Record<string, string>,
  ) {
    return await OpenID4VP.InjiOpenID4VP.shareVerifiablePresentation(
      vpResponseMetadata,
    );
  }

  static sendErrorToVerifier(error: string) {
    OpenID4VP.InjiOpenID4VP.sendErrorToVerifier(error);
  }
}

export async function constructProofJWT(
  publicKey: any,
  privateKey: any,
  vpToken: Object,
  keyType: string,
): Promise<string> {
  const jwtHeader = {
    alg: OpenID4VP_Proof_Sign_Algo,
    jwk: await getJWK(publicKey, keyType),
  };

  const jwtPayload = createJwtPayload(vpToken);

  return await getJWT(
    jwtHeader,
    jwtPayload,
    OpenID4VP_Key_Ref,
    privateKey,
    keyType,
  );
}

function createJwtPayload(vpToken: {[key: string]: any}) {
  const {'@context': context, type, verifiableCredential, id, holder} = vpToken;
  return {
    '@context': context,
    type,
    verifiableCredential,
    id,
    holder,
  };
}

export async function isClientValidationRequired() {
  const config = await getAllConfigurations();
  return config.openid4vpClientValidation === 'true';
}
