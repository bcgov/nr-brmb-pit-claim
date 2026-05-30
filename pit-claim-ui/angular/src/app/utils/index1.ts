import { AppConfigService } from "../services/app-config.service";
import { Injector } from "@angular/core";
import { TokenService } from "../services/token.service";

/**
 * Function needed to run the application config service at app initialization time.
 * TODO: this is wrong. kill with fire.
 */
export function appInitializerFn(injector: Injector) {
    const appConfig = injector.get(AppConfigService);
    appConfig.configEmitter.subscribe(config => {
        const tokenService = injector.get(TokenService);
    });
    return () => appConfig.loadAppConfig();
}