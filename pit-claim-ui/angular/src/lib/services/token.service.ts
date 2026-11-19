import { HttpClient, HttpHandler, HttpHeaders } from "@angular/common/http";
import { Injectable, Injector } from "@angular/core";
import { OAuthService } from "angular-oauth2-oidc";
import { AsyncSubject, Observable } from "rxjs";
import { catchError } from "rxjs/operators";
import { AppConfigService } from "./app-config.service";

@Injectable({
    providedIn: 'root',
})
export class TokenService {

    private oauth: any;
    private tokenDetails: any;

    private credentials = new AsyncSubject<any>();
    private authToken = new AsyncSubject<string>();
    public credentialsEmitter: Observable<any> = this.credentials.asObservable();
    public authTokenEmitter: Observable<string> = this.authToken.asObservable();

    constructor(private injector: Injector, protected appConfigService: AppConfigService) {

        this.checkForToken(undefined);

    }

    /*
     * Check the local OIDC session and initiate browser login when needed.
     */
    public checkForToken(redirectUri?: string, lazyAuth?: boolean, allowLocalExpiredToken?: boolean) {
        this.initAuthorizationCodeFlow(redirectUri);
    }

    public isTokenExpired() {
        let now = new Date()
        
        if ( !this.oauth?.expires_in ) 
            return false

        if ( !this.oauth.expireTime ) {
            
            const now = new Date();
            this.oauth.expireTime = now.getTime() + (this.oauth.expires_in * 1000)
            return false
        }

        if ( now.getTime() < this.oauth.expireTime ) 
            return false

        return true
    }


    /*
     * Configure Keycloak OIDC and initiate the Authorization Code flow with PKCE.
     */
    private initAuthorizationCodeFlow(redirectUri?: string) {
        const configuration = this.appConfigService.getConfig();
        const oauthService = this.injector.get(OAuthService);
        const authConfig = {
            oidc: true,
            issuer: configuration?.keycloak.issuerUri,
            loginUrl: configuration?.keycloak.authorizationEndpoint,
            redirectUri: redirectUri ? redirectUri : window.location.href,
            postLogoutRedirectUri: configuration?.application.baseUrl,
            logoutUrl: configuration?.keycloak.logoutUrl,
            tokenEndpoint: configuration?.keycloak.tokenEndpoint,
            clientId: configuration?.keycloak.clientId,
            scope: configuration?.keycloak.authScopes,
            responseType: 'code',
            disablePKCE: false,
            strictDiscoveryDocumentValidation: false,
            requireHttps: 'remoteOnly' as const,
            sessionChecksEnabled: false
        };

        oauthService.configure(authConfig);
        oauthService.loadDiscoveryDocumentAndTryLogin().then(loginSucceeded => {
            if (loginSucceeded && oauthService.getAccessToken()) {
                this.initAuthFromOAuthService(oauthService);
            } else if (!oauthService.hasValidAccessToken()) {
                oauthService.initCodeFlow();
            }
        }).catch(error => {
            console.error('Keycloak discovery or login failed', error);
            oauthService.initCodeFlow();
        });
    }

    private initAuthFromOAuthService(oauthService: OAuthService) {
        this.oauth = {
            access_token: oauthService.getAccessToken(),
            expires_in: oauthService.getAccessTokenExpiration() > 0
                ? Math.max(0, Math.floor((oauthService.getAccessTokenExpiration() - Date.now()) / 1000))
                : undefined,
            scope: oauthService.getGrantedScopes()
        };
        this.initAuth(this.oauth);
    }

    /*
     * Set authentication configuration and initiate refresh token implicit flow
     */
    public initRefreshTokenImplicitFlow( url: string, storageKey: string, errorCallback: any): Promise<any> {
        const oauthService = this.injector.get(OAuthService);
        return oauthService.refreshToken().then(response => {
            const token = {
                access_token: response.access_token,
                expires_in: response.expires_in,
                scope: response.scope
            };
            this.updateToken(token);
            return token;
        }).catch(error => {
            errorCallback('Session Expired. Unable to refresh Keycloak session.');
            throw error;
        });
    }

    /*
     * initialize authentication response in application, emit to subscribers
     */
    public initAuth(response: any) {
        if (response) {
            try {
                this.oauth = response;
                this.initAndEmit();
            } catch (err) {
                console.log('Failed to handle token payload', this.oauth);
                this.handleError(err, 'Failed to handle token');
            }
        }
    }

    /*
     * Initialize all token service attributes and emit
     */

    private initAndEmit() {
        // console.log('init and emit', this.appConfigService.getConfig());
        if (this.appConfigService.getConfig()?.keycloak.enableCheckToken) {
            let baseUrl = this.appConfigService.getConfig()?.application.baseUrl;
            if (baseUrl && !baseUrl.endsWith('/')) {
                baseUrl = baseUrl.concat('/');
            }
            
            let checkTokenUrl = `${this.appConfigService.getConfig()?.keycloak.checkTokenUrl}`;

            const headers = new HttpHeaders({
                'Authorization': `Bearer ${this.oauth.access_token}`,
            });

            setTimeout(() => {
                let http = new HttpClient(this.injector.get(HttpHandler));

                http.get(checkTokenUrl, { headers }).toPromise()
                    .then(
                        (response: any) => {
                            this.tokenDetails = response;
                            // console.log('access_token', this.oauth.access_token);
                            this.authToken.next(this.oauth.access_token);
                            this.authToken.complete();
                            // console.log('tokenDetails', this.tokenDetails);
                            this.credentials.next(this.tokenDetails);
                            this.credentials.complete();
                        })
                    , catchError(error => {
                        console.log(error);
                        alert(`App initialization Failed ${error.status}. Status(Check token failed)`);
                        return error;
                    }
                    );
            });
        } else {

            //Split for JWT
            const oauthInfo: string[] = this.oauth.access_token.split('.');
            // console.log('oauthInfo', oauthInfo);

            if (oauthInfo.length > 1) {
                const payload = oauthInfo[1].replace(/-/g, '+').replace(/_/g, '/');
                this.tokenDetails = JSON.parse(atob(payload));
            }
            this.tokenDetails = this.tokenDetails || {};
            this.normalizePermissions();

            // console.log('access_token2', this.oauth.access_token);
            this.authToken.next(this.oauth.access_token);
            this.authToken.complete();
            // console.log('tokenDetails2', this.tokenDetails);
            this.credentials.next(this.tokenDetails);
            this.credentials.complete();
        }
    }

    updateToken(oauthToken: any) {
        this.oauth = oauthToken;
        this.initAndEmit();
    }

    public getOauthToken() {
        return (this.oauth) ? this.oauth.access_token : null;
    }

    public getTokenDetails() {
        return (this.tokenDetails) ? this.tokenDetails : null;
    }

    public doesUserHaveApplicationPermissions(scopes?: string[]): boolean {
        if (this.tokenDetails && this.tokenDetails.scope && this.tokenDetails.scope.length > 0) {
            if (scopes) {
                for (let i = 0; i < scopes.length; i++) {
                    if (this.tokenDetails.scope.indexOf(scopes[i]) == -1) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private normalizePermissions() {
        const permissions = new Set<string>();

        if (typeof this.tokenDetails.scope === 'string') {
            this.tokenDetails.scope.split(/\s+/).forEach((scope: string) => scope && permissions.add(scope));
        } else if (Array.isArray(this.tokenDetails.scope)) {
            this.tokenDetails.scope.forEach((scope: string) => scope && permissions.add(scope));
        }

        (this.tokenDetails.realm_access?.roles || []).forEach((role: string) => permissions.add(role));
        Object.values(this.tokenDetails.resource_access || {}).forEach((resource: any) => {
            (resource?.roles || []).forEach((role: string) => permissions.add(role));
        });
        (this.tokenDetails.authorities || []).forEach((authority: string) => permissions.add(authority));

        this.tokenDetails.scope = Array.from(permissions);
    }

    private handleError(err: any, message?: any) {
        console.error('Unexpected error', err);
        alert(message ? message + ' ' + err : '' + err);
        throw err;
    }

}