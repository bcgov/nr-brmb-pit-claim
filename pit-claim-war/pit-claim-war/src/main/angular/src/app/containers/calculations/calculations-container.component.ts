import {BaseContainer} from "../base/base-container.component";
import {select} from "@ngrx/store";
import {Observable} from "rxjs";
import {selectCalculations} from '../../store/calculations/calculations.selectors';
import {ErrorState, LoadState} from "../../store/application/application.state";
import {
    selectCalculationsErrorState,
    selectCalculationsLoadState,
    selectSearchState
} from "../../store/application/application.selectors";
import {SEARCH_CALCULATIONS_COMPONENT_ID} from "../../store/calculations/calculations.state";
import {SearchState} from "@wf1/wfcc-core-lib";
import {Component} from "@angular/core";
import {Location, LocationStrategy, PathLocationStrategy} from "@angular/common";

@Component({
    selector: "cirras-claims-calculations-container",
    template: `
        <cirras-claims-calculations
                [collection]="collection$ | async"
                [searchState]="searchState$ | async"
                [loadState]="loadState$ | async"
                [errorState]="errorState$ | async"
        ></cirras-claims-calculations>`,
    providers: [Location, {provide: LocationStrategy, useClass: PathLocationStrategy}]
})
export class CalculationsContainer extends BaseContainer {
    collection$: Observable<any> = this.store.pipe(select(selectCalculations()));
    searchState$: Observable<SearchState> = this.store.pipe(select(selectSearchState(SEARCH_CALCULATIONS_COMPONENT_ID)));
    loadState$: Observable<LoadState> = this.store.pipe(select(selectCalculationsLoadState()));
    errorState$: Observable<ErrorState[]> = this.store.pipe(select(selectCalculationsErrorState()));
}
