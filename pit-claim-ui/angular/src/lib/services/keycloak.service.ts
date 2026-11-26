import { Injectable } from '@angular/core';
import Keycloak from 'keycloak-js';

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {

  private keycloak = new Keycloak({

    url: 'https://dev.loginproxy.gov.bc.ca/auth',

    realm: 'standard',

    clientId: 'nr-brmb-pit-claim-ui-dev-prototype-6816'

  });

  async init(): Promise<boolean> {
    return await this.keycloak.init({

      onLoad: 'login-required',

      flow: 'standard',

      pkceMethod: 'S256',

      checkLoginIframe: false

    });

  }

  async login(): Promise<void> {

    await this.keycloak.login();

  }

  async logout(): Promise<void> {

    await this.keycloak.logout({

      redirectUri: window.location.origin

    });

  }

  async refreshToken(): Promise<boolean> {

    try {

      await this.keycloak.updateToken(30);

      return this.keycloak.authenticated ?? false;

    } catch (error) {

      console.error('Keycloak token refresh failed', error);

      await this.logout();

      return false;

    }

  }

  getToken(): string | undefined {
    return this.keycloak.token;
  }

  isAuthenticated(): boolean {
    return this.keycloak.authenticated ?? false;
  }

  getUsername(): string | undefined {
    return this.keycloak.tokenParsed?.['preferred_username'];
  }

  getTokenExpiration(): number | undefined {
    return this.keycloak.tokenParsed?.exp;
  }

}