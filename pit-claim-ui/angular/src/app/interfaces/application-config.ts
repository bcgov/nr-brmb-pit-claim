export interface Application {
    acronym: string;
    version: string;
    baseUrl: string;
    environment: string;
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