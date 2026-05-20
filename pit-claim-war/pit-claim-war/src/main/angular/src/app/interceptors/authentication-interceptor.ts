import { Injectable, Injector } from "@angular/core";
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Observable } from "rxjs";
import { AppConfigService } from "../services/app-config.service";
import { TokenService } from "../services/token.service";

/** Pass untouched request through to the next request handler. */
@Injectable()
export class AuthenticationInterceptor implements HttpInterceptor {
    private token: any;
    private authToken: any;

    constructor(protected injector: Injector) {
    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        // console.log(this.appConfig.getConfig());
        let processedRequest = req;
        if (this.isUrlSecured(req.url)) {
            this.token = this.injector.get(TokenService);
            this.authToken = this.token.getOauthToken();

            if (this.authToken) {
                processedRequest = req.clone({
                    headers: req.headers.set('Authorization', 'Bearer ' + this.authToken)
                });
            }
        }
        return next.handle(processedRequest);
    }

    isUrlSecured(url: string): boolean {
        let isSecured = false;
        const config = this.injector.get(AppConfigService).getConfig();
        if (config && config.rest) {
            for (let endpoint in config.rest) {
                if (url.startsWith(config.rest[endpoint])) {
                    isSecured = true;
                    break;
                }
            }
        }
        return isSecured;
    }
}