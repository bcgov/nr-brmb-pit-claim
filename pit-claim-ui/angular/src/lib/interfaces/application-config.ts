export interface Application {
    acronym: string;
    version: string;
    baseUrl: string;
    environment: string;
}

export interface RestConfig {
    [prop: string]: string;
}

export interface Keycloak {
    issuerUri: string;
    authorizationEndpoint: string;
    tokenEndpoint: string;
    logoutUrl: string;
    clientId: string;
    authScopes: string;
}

export interface ApplicationConfig {
    application: Application;
    rest: RestConfig;
    keycloak: Keycloak;
}