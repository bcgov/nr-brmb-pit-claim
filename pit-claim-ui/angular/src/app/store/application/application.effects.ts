import { HttpClient } from '@angular/common/http';
import {Injectable} from "@angular/core";
import {Actions} from "@ngrx/effects";
import {Store} from "@ngrx/store";
import {RootState} from "../index";
import {ApplicationStateService} from "../../services/application-state.service";
import {MatSnackBar} from "@angular/material/snack-bar";
// import { AppConfigService, TokenService } from '@bcgov/pit-common-core-lib';
import { AppConfigService } from "src/lib/services/app-config.service";
import { TokenService } from "src/lib/services/token.service";

@Injectable()
export class ApplicationEffects {
  constructor(
      private actions: Actions,
      private store: Store<RootState>,
      private snackbarService: MatSnackBar,
      private tokenService: TokenService,
      private applicationStateService: ApplicationStateService,
      private httpClient: HttpClient,
      private appConfigService: AppConfigService) {
  }
}
