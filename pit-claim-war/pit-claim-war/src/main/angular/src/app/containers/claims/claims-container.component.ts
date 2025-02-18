import {BaseContainer} from "../base/base-container.component";
import {select} from "@ngrx/store";
import {Observable} from "rxjs";
import {selectClaims} from './../../store/claims/claims.selectors';
import {ErrorState, LoadState} from "../../store/application/application.state";
import {
  selectClaimsErrorState,
  selectClaimsLoadState,
  selectSearchState
} from "../../store/application/application.selectors";
import {SEARCH_CLAIMS_COMPONENT_ID} from "../../store/claims/claims.state";
import {SearchState} from "@wf1/wfcc-core-lib";
import {Component} from "@angular/core";
import { LocationStrategy, PathLocationStrategy } from "@angular/common";

@Component({
  selector: "cirras-claims-container",
  template: `
      <cirras-claims-desktop
              [collection]="collection$ | async"
              [searchState]="searchState$ | async"
              [loadState]="loadState$ | async"
              [errorState]="errorState$ | async"
      ></cirras-claims-desktop>`,
  providers: [Location, {provide: LocationStrategy, useClass: PathLocationStrategy}]
})
export class ClaimsContainer extends BaseContainer {
    collection$: Observable<any> = this.store.pipe(select(selectClaims()));
    searchState$: Observable<SearchState> = this.store.pipe(select(selectSearchState(SEARCH_CLAIMS_COMPONENT_ID)));
    loadState$: Observable<LoadState> = this.store.pipe(select(selectClaimsLoadState()));
    errorState$: Observable<ErrorState[]> = this.store.pipe(select(selectClaimsErrorState()));
}
