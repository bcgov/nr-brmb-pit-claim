import { HttpClient, HttpHandler, HttpHeaders } from "@angular/common/http";
import { Injectable, Injector } from "@angular/core";
import { OAuthService } from "angular-oauth2-oidc";
import moment from "moment";
import { AsyncSubject, Observable } from "rxjs";
import { catchError } from "rxjs/operators";
import { AppConfigService } from "./app-config.service";

const OAUTH_LOCAL_STORAGE_KEY = 'oauth';

@Injectable({
    providedIn: 'root',
})
export class TokenService {

    private LOCAL_STORAGE_KEY = OAUTH_LOCAL_STORAGE_KEY;
    private useLocalStore: boolean = false;
    private oauth: any;
    private tokenDetails: any;

    private credentials = new AsyncSubject<any>();
    private authToken = new AsyncSubject<string>();
    public credentialsEmitter: Observable<any> = this.credentials.asObservable();
    public authTokenEmitter: Observable<string> = this.authToken.asObservable();
    constructor(private injector: Injector, protected appConfigService: AppConfigService) {

        
        //console.log("initing token service", appConfigService.getConfig());
        console.log("TokenService.constructor")
        
        // const lazyAuthenticate = appConfigService.getConfig()?.application.lazyAuthenticate;
        // const enableLocalStorageToken = appConfigService.getConfig()?.application.enableLocalStorageToken;
        // const localStorageTokenKey = appConfigService.getConfig()?.application.localStorageTokenKey;
        // const allowLocalExpiredToken = appConfigService.getConfig()?.application.allowLocalExpiredToken;

        // console.log("From appConfig -> lazyAuthenticate: " + lazyAuthenticate + "; enableLocalStorageToken: " + enableLocalStorageToken 
        //     + "; localStorageTokenKey: " + localStorageTokenKey + "; allowLocalExpiredToken: " + allowLocalExpiredToken)

        // if (localStorageTokenKey) {
        //     this.LOCAL_STORAGE_KEY = localStorageTokenKey;
        // }

        // if (enableLocalStorageToken) {
        //     this.useLocalStore = true;
        // }

        this.checkForToken(undefined);
    }

    /*
     * Check window location hash fragment or local storage session for access token.
     * Parse and set the token if the access token is present,
     * otherwise initiate implicit flow.
     *
     * @param {string} redirectUri The redirect URI after login is complete
     * @param {boolean} lazyAuth When true, allows application to handle when to login ( by default: false which will require login as soon as the application initializes)
     * @param {boolean} allowLocalExpiredToken When true, expired tokens are not removed and does not invoke login (allows token to be used even when expired for offline mode and service workers).
     */
    public checkForToken(redirectUri?: string) {
        // console.log('checkForToken>>> redirect uri', redirectUri);
        console.log("TokenService.checkForToken")

        let hash = window.location.hash;

        // Check if URL has token (redirected back from oauth)
        if (hash && hash.indexOf('access_token') > -1) { // ok
            console.log("TokenService.checkForToken >>> if (hash && hash.indexOf(access_token) > -1)")
            // We have a token in the URL, parse it
            this.parseToken(hash);

        } else if (this.useLocalStore && !navigator.onLine) {
            console.log("TokenService.checkForToken >>> else if (this.useLocalStore && !navigator.onLine)")
            // Only use local storage if application is offline
            // this is to refresh expired tokens before check token is enabled, when there is connectivity

            // Check if local storage has a token
            let tokenStore: any = localStorage.getItem(this.LOCAL_STORAGE_KEY);

            // Parse the token
            if (tokenStore) {
                console.log("TokenService.checkForToken >>> if (tokenStore) ")
                try {
                    console.log('use local storage if application is offline');
                    tokenStore = JSON.parse(tokenStore);
                    this.initAuthFromSession();
                } catch (err) {

                    // Failed to parse the token, remove the old token and get a new token by logging in again
                    console.log('Failed to read session token - reinitializing');
                    this.tokenDetails = undefined;
                    localStorage.removeItem(this.LOCAL_STORAGE_KEY);
                    this.initImplicitFlow(redirectUri);
                }

            } else {
                console.log("TokenService.checkForToken >>> else -- no tokenStore ")
                // no token was found initiate login
                this.initImplicitFlow(redirectUri)
            }

            // Check if token is expired if it is not allowed
            //if (!allowLocalExpiredToken && this.isTokenExpired(this.tokenDetails)) {
            if (this.isTokenExpired(this.tokenDetails)) {
                console.log("TokenService.checkForToken >>> if (this.isTokenExpired(this.tokenDetails))")

                localStorage.removeItem(this.LOCAL_STORAGE_KEY);

                this.initImplicitFlow(redirectUri);
            }

        } else if (hash && hash.indexOf('error') > -1) { // ok
            console.log("else if (hash && hash.indexOf('error') > -1) ")

            alert('Error occurred during authentication.');
            return;

        } else {

            // login if lazy auth not enabled as we need a token
            // if (!lazyAuth) {
                console.log("TokenService.checkForToken >>> final else ")
                this.initImplicitFlow(redirectUri); // this.initLogin(redirectUri);
            //}

        }
    }

    public isTokenExpired(token: any): boolean {
        let expiryDate;
        let now = moment()
        if (token && token.exp) {
            expiryDate = moment.unix(token.exp);
            if (now.isBefore(expiryDate)) {
                return false;
            }
        }
        return true;
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
    public initRefreshTokenImplicitFlow(authorizeURL: string, storageKey: string, errorCallback: any): Observable<any> {
        const options = 'resizable=yes,scrollbars=yes,statusbar=yes,status=yes';
        let refreshWindow = window.open(authorizeURL, undefined, options);
        let refreshAsync = new AsyncSubject();

        let refreshInterval = setInterval(() => {
            if (!refreshWindow) {
                errorCallback('Session Expired. Unable to open refresh window. Please allow pop-ups.');
                refreshWindow = window.open(authorizeURL, undefined, options);
            }

            if (refreshWindow && refreshWindow.closed) {
                clearInterval(refreshInterval);
                let newToken = window.localStorage.getItem(`${storageKey}`);
                newToken = JSON.parse(newToken ?? '""');
                window.localStorage.removeItem(`${storageKey}`);
                this.updateToken(newToken);
                refreshAsync.next(newToken);
                refreshAsync.complete();
            }
        }, 500);

        return refreshAsync.asObservable();
    }

    /*
     * initialize authentication from session in application, emit to subscribers
     */
    private initAuthFromSession() {
        try {
            let localOauth = localStorage.getItem(this.LOCAL_STORAGE_KEY);
            localOauth = JSON.parse(localOauth ?? '""');
            this.oauth = localOauth;
            this.initAndEmit();
        } catch (err) {
            localStorage.removeItem(this.LOCAL_STORAGE_KEY);
            console.log('Failed to handle token payload', this.oauth);
            this.handleError(err, 'Failed to handle token');
        }
    }

    /*
     * initialize authentication response in application, emit to subscribers
     */
    public initAuth(response: any) {
        if (response) {
            try {
                if (this.useLocalStore) {
                    let tokenStore = {
                        access_token: response.access_token,
                        expires_in: response.expires_in
                    };
                    localStorage.setItem(this.LOCAL_STORAGE_KEY, JSON.stringify(tokenStore));
                }
                this.oauth = response;
                this.initAndEmit();
            } catch (err) {
                if (this.useLocalStore) {
                    localStorage.removeItem(this.LOCAL_STORAGE_KEY);
                }
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

    // public clearLocalStorageToken() {
    //     localStorage.removeItem(this.LOCAL_STORAGE_KEY);
    // }

    private handleError(err: any, message?: any) {
        console.error('Unexpected error', err);
        alert(message ? message + ' ' + err : '' + err);
        throw err;
    }

}