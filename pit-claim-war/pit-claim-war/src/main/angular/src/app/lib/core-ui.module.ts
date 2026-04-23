import { HttpHandler } from "@angular/common/http";
import { APP_INITIALIZER, Injector, ModuleWithProviders, NgModule } from "@angular/core";
import { OAuthModule } from "angular-oauth2-oidc";
// import { LibraryConfig } from "./config/library-config";
// import { AppConfigService } from "./services/app-config.service";
// import { appInitializerFn } from "./utils";
import { LibraryConfig } from "../config/library-config";
import { AppConfigService } from "../services/app-config.service";
import { appInitializerFn } from "../utils/index1";

@NgModule({
    imports: [
        OAuthModule.forRoot(),
    ],
    providers: [],
    declarations: [],
    exports: []
})
export class CoreUIModule {
    static forRoot(config: LibraryConfig): ModuleWithProviders<CoreUIModule> {
        return {
            ngModule: CoreUIModule,
            providers: [
                // Initializes the AppConfigService on Angular app init, so it is ready before components are loaded.
                // Technically, we're adding an additional dependency for the Angular APP_INITIALIZER token (Thus: 'multi:true').
                // That dependency fires a factory function 'appInitializerFn' that depends on the AppConfigService singleton,
                // and which call the service's loadAppConfig() function on Angular init.
                // See for reference: https://www.intertech.com/Blog/angular-4-tutorial-run-code-during-app-initialization/
                {
                    provide: APP_INITIALIZER,
                    useFactory: appInitializerFn,
                    multi: true,
                    deps: [Injector]
                },
                {
                    provide: LibraryConfig,
                    useValue: config
                },
                {
                    provide: AppConfigService,
                    useClass: AppConfigService,
                    deps: [HttpHandler, LibraryConfig]
                }
            ]
        }
    }
}