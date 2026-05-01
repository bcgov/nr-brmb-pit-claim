import { HttpHandler } from "@angular/common/http";
import { APP_INITIALIZER, Injector, ModuleWithProviders, NgModule } from "@angular/core";
import { OAuthModule } from "angular-oauth2-oidc";
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