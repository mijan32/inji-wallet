import {request} from '../../shared/request';
import getAllConfigurations, {API_URLS} from '../../shared/api';
import {ESIGNET_BASE_URL} from '../../shared/constants';
import {
  isHardwareKeystoreExists,
  getJWT,
} from '../../shared/cryptoutil/cryptoUtil';
import {getPrivateKey} from '../../shared/keystore/SecureKeystore';

export const QrLoginServices = {
  linkTransaction: async context => {
    const response = await request(
      API_URLS.linkTransaction.method,
      API_URLS.linkTransaction.buildURL(),
      {
        requestTime: String(new Date().toISOString()),
        request: {
          linkCode: context.linkCode,
        },
      },
      ESIGNET_BASE_URL,
    );
    return response.response;
  },

  sendAuthenticate: async context => {
    let privateKey;
    const individualId = context.selectedVc.vcMetadata.id;
    if (!isHardwareKeystoreExists) {
      privateKey = await getPrivateKey(
        context.selectedVc.walletBindingResponse?.walletBindingId,
      );
    }

    var config = await getAllConfigurations();
    const header = {
      alg: 'RS256',
      'x5t#S256': context.thumbprint,
    };

    const payload = {
      iss: config.issuer,
      sub: individualId,
      aud: config.audience,
      iat: Math.floor(new Date().getTime() / 1000),
      exp: Math.floor(new Date().getTime() / 1000) + 18000,
    };

    const jwt = await getJWT(header, payload, individualId, privateKey);

    const response = await request(
      API_URLS.authenticate.method,
      API_URLS.authenticate.buildURL(),
      {
        requestTime: String(new Date().toISOString()),
        request: {
          linkedTransactionId: context.linkTransactionId,
          individualId: individualId,
          challengeList: [
            {
              authFactorType: 'WLA',
              challenge: jwt,
              format: 'jwt',
            },
          ],
        },
      },
      ESIGNET_BASE_URL,
    );
    return response.response;
  },

  sendConsent: async context => {
    let privateKey;
    const individualId = context.selectedVc.vcMetadata.id;
    if (!isHardwareKeystoreExists) {
      privateKey = await getPrivateKey(
        context.selectedVc.walletBindingResponse?.walletBindingId,
      );
    }

    const header = {
      alg: 'RS256',
      'x5t#S256': context.thumbprint,
    };
    const payload = {
      accepted_claims: context.essentialClaims
        .concat(context.selectedVoluntaryClaims)
        .sort(),
      permitted_authorized_scopes: context.authorizeScopes,
    };

    const JWT = await getJWT(header, payload, individualId, privateKey);
    const jwtComponents = JWT.split('.');
    const detachedSignature = jwtComponents[0] + '.' + jwtComponents[2];

    const resp = await request(
      API_URLS.sendConsent.method,
      API_URLS.sendConsent.buildURL(),
      {
        requestTime: String(new Date().toISOString()),
        request: {
          linkedTransactionId: context.linkedTransactionId,
          acceptedClaims: context.essentialClaims
            .concat(context.selectedVoluntaryClaims)
            .sort(),
          permittedAuthorizeScopes: context.authorizeScopes,
          signature: detachedSignature,
        },
      },
      ESIGNET_BASE_URL,
    );
  },
};
