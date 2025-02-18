import {Injectable, Injector} from "@angular/core";
import {TokenService} from "@wf1/wfcc-core-lib";
import {ROLES_UI} from "../utils/scopes";
import {
  vmCalculation
} from "../conversion/models";

@Injectable({
    providedIn: "root"
})
export class SecurityUtilService {
    tokenService: TokenService;

    constructor(private injector: Injector) {
    }

    private getTokenService() {
      const tokenservice = this.tokenService;
      const injToken = this.injector.get(TokenService)      
        return this.tokenService ? this.tokenService : this.injector.get(TokenService);
    }

    public doesUserHaveScopes(scopes: string[]): boolean {
        return this.getTokenService().doesUserHaveApplicationPermissions(scopes);
    }

    public doesUserHaveScope(scope) {
        return this.doesUserHaveScopes([scope]);
    }

    hasScope(scope: string): boolean {
      return this.hasScopes([scope]);
    }

    hasScopes(scopes: string[]): boolean {
        return this.doesUserHaveScopes(scopes);
    }

    //role security---------------------------------------------------------------------------------------------------------------------------------------------------------------------
    isSuperAdmin() {
      return this.hasScope(ROLES_UI.CLAIMS_SUPER_ADMIN);
  }

    isManager() {
        return this.hasScope(ROLES_UI.CLAIMS_MANAGER);
    }

    isMonitor() {
        return this.hasScope(ROLES_UI.CLAIMS_MONITOR);
    }

    isViewer() {
        return this.hasScope(ROLES_UI.CLAIMS_VIEWER);
    }

    //calculation security---------------------------------------------------------------------------------------------------------------------------------------------------------------------  
    canViewCalculationDetail(item: vmCalculation): boolean {
        return true;
        //return this.isSuperAdmin() || this.isManager() || this.isMonitor() || this.isViewer()
    }
}
