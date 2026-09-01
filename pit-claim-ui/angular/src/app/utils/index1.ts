import { AppConfigService } from "../services/app-config.service";
import { inject, Injector } from "@angular/core";
import { TokenService } from "../services/token.service";

/**
 * Function needed to run the application config service at app initialization time.
 */

export function appInitializerFn(injector: Injector) {
    const appConfig = injector.get(AppConfigService);
    appConfig.configEmitter.subscribe(() => {
        injector.get(TokenService);
    });
    return () => appConfig.loadAppConfig();
}
