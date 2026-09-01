// export interface PagingConfig {
//     [prop: string]: number;
// }

export interface Application {
    acronym: string;
    version: string;
    baseUrl: string;
    environment: string;
    // buildNumber?: string;
    // maxListPageSize: PagingConfig;
    // lazyAuthenticate?: boolean;
    // enableLocalStorageToken?: boolean;
    // allowLocalExpiredToken?: boolean;
    // localStorageTokenKey?: string;
}

export interface RestConfig {
    [prop: string]: string;
}

export interface WebADE {
    oauth2Url: string;
    clientId: string;
    authScopes: string;
    enableCheckToken?: boolean;
    checkTokenUrl?: string;
}

export interface ApplicationConfig {
    application: Application;
    rest: RestConfig;
    webade: WebADE;
}