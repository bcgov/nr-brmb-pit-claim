import {Component} from "@angular/core";
import {Location, LocationStrategy, PathLocationStrategy} from "@angular/common";
import {BaseContainer} from "../base/base-container.component";
import {Observable} from "rxjs";
import {vmCalculation} from "../../conversion/models";
import {select, Store} from "@ngrx/store";
import {selectCalculationDetail} from "../../store/calculation-detail/calculation-detail.selectors";
import {
    CALCULATION_DETAIL_COMPONENT_ID
} from "../../store/calculation-detail/calculation-detail.state";

import {
    selectCalculationDetailMetadataErrorState,
    selectCalculationDetailMetadataLoadState,
    selectFormStateUnsaved
} from "../../store/application/application.selectors";
import {ErrorState, LoadState} from "../../store/application/application.state";

@Component({
    selector: "cirras-claims-calculation-detail-container",
    template: `
        <cirras-claims-calculation-detail  
            [calculationDetail]="calculationDetail$ | async"
            [loadState]="loadState$ | async"
            [errorState]="errorState$ | async"
            [isUnsaved]="isUnsaved$ | async"
        ></cirras-claims-calculation-detail>`,
    providers: [Location, { provide: LocationStrategy, useClass: PathLocationStrategy }],
    standalone: false
})
export class CalculationDetailContainer extends BaseContainer  {
    displayLabel = "Calculation Detail";
    calculationDetail$: Observable<vmCalculation> = this.store.pipe(select(selectCalculationDetail()));

    loadState$: Observable<LoadState> = this.store.pipe(select(selectCalculationDetailMetadataLoadState()));
    errorState$: Observable<ErrorState[]> = this.store.pipe(select(selectCalculationDetailMetadataErrorState()));
    isUnsaved$: Observable<boolean> = this.store.pipe(select(selectFormStateUnsaved(CALCULATION_DETAIL_COMPONENT_ID)));

    getAssociatedComponentIds(): string[] {
        return [
          CALCULATION_DETAIL_COMPONENT_ID
        ];
    }
}
