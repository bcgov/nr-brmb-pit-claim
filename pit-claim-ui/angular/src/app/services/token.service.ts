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
     * Check window location hash fragment or local storage session for access token.
     * Parse and set the token if the access token is present,
     * otherwise initiate implicit flow.
     */
    public checkForToken(redirectUri?: string, lazyAuth?: boolean, allowLocalExpiredToken?: boolean) {
        // console.log('checkForToken >> redirect uri: ', redirectUri);
        let hash = window.location.hash;

        // Check if URL has token (redirected back from oauth)
        if (hash && hash.indexOf('access_token') > -1) {

            // We have a token in the URL, parse it
            this.parseToken(hash);

        } else if (hash && hash.indexOf('error') > -1) {
            alert('Error occurred during authentication.');
            return;

        } else {
            this.initImplicitFlow(redirectUri);
        }
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
     * Parse token from a hash fragment
     * Example:
     *    #access_token=ABC&token_type=bearer&state=&expires_in=43199&scope=WFIM.GET_WILDFIRE_INCIDENT%20WFORG.GET_ORG_UNITS%&jti=3a642b53-d90e-4ee3-a00c-5cd780155225
     */
    private parseToken(hash: any) {
        if (hash.startsWith('#')) {
            hash = hash.substr(1);
        }

        let responseParameters = (hash).split("&");
        let parameterMap: { [key: string]: any } = {};
        for (let i = 0; i < responseParameters.length; i++) {
            parameterMap[responseParameters[i].split("=")[0]] = responseParameters[i].split("=")[1];
        }

        if (parameterMap['access_token'] !== undefined && parameterMap['access_token'] !== null) {
            location.hash = '';
            this.initAuth(parameterMap);
        }
    }

    /*
     * Set authentication configuration and initiate implicit flow
     */
    private initImplicitFlow(redirectUri?: string) {
        const configuration = this.appConfigService.getConfig();
        let authConfig = {
            oidc: false,
            issuer: configuration?.application.baseUrl,
            loginUrl: configuration?.webade.oauth2Url,
            redirectUri: redirectUri ? redirectUri : window.location.href,
            clientId: configuration?.webade.clientId,
            scope: configuration?.webade.authScopes
        };

        const oauthService = this.injector.get(OAuthService);
        oauthService.configure(authConfig);
        oauthService.initImplicitFlow();
    }

    /*
     * Set authentication configuration and initiate refresh token implicit flow
     */
    public initRefreshTokenImplicitFlow( url: string, storageKey: string, errorCallback: any): Promise<any> {
        return new Promise( ( res, rej ) => {
        
            const options = 'resizable=yes,scrollbars=yes,statusbar=yes,status=yes';

            let windowObj,
                retries = 0

            let refreshInterval = setInterval( () => {
                
                if ( !windowObj )
                    windowObj =  window.open( url, 'authorize', options )
    
                if ( windowObj ) {   

                    let newToken = window.localStorage.getItem( storageKey )

                    if ( !newToken ) {                        
                        retries += 1
                        return
                    }

                    clearInterval(refreshInterval)

                    window.localStorage.removeItem( storageKey )
    
                    try {
                        let parsedToken = JSON.parse( newToken )
                        this.updateToken( parsedToken )
                        res( parsedToken )
                    }
                    catch ( e ) {
                        console.warn( 'failed to parse', newToken, e )
                        rej()
                    }
                }
                else {
                    errorCallback('Session Expired. Unable to open refresh window. Please allow pop-ups.')
                    retries += 1
                }
            }, 500 )    
        } )
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
        if (this.appConfigService.getConfig()?.webade.enableCheckToken) {
            let baseUrl = this.appConfigService.getConfig()?.application.baseUrl;
            if (baseUrl && !baseUrl.endsWith('/')) {
                baseUrl = baseUrl.concat('/');
            }
            
            // let checkTokenUrl = `${baseUrl}${this.appConfigService.getConfig()?.webade.checkTokenUrl}`;
            let checkTokenUrl = `${this.appConfigService.getConfig()?.webade.checkTokenUrl}`;

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
                this.tokenDetails = JSON.parse(atob(oauthInfo[1]));
            }

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

    private handleError(err: any, message?: any) {
        console.error('Unexpected error', err);
        alert(message ? message + ' ' + err : '' + err);
        throw err;
    }

}