import {Injectable} from "@angular/core";
import {HttpBackend, HttpClient} from "@angular/common/http";
import {AsyncSubject, lastValueFrom, Observable} from "rxjs";
import {LibraryConfig} from "../config/library-config";
import {ApplicationConfig} from "../interfaces/application-config";

@Injectable({
  providedIn: 'root',
})
export class AppConfigService {
  private appConfig?: ApplicationConfig;
  private config = new AsyncSubject<ApplicationConfig>();
  public configEmitter: Observable<ApplicationConfig> = this.config.asObservable();

  constructor(private httpHandler: HttpBackend, private libConfig: LibraryConfig) {
   // console.log("initing app config service");
  }

  loadAppConfig() {
    let http = new HttpClient(this.httpHandler);
    if ( !this.libConfig.configurationPath ) return
    return lastValueFrom( http.get(this.libConfig.configurationPath)).then(data => {
        this.appConfig = data as ApplicationConfig; 
        this.config.next(this.appConfig);
        this.config.complete();
    });
  }

  getConfig(): ApplicationConfig {
    if (!this.appConfig) {
      throw new Error('Configuration not loaded. Please call loadAppConfig() first.');
    }
    return this.appConfig;
  }
  
}