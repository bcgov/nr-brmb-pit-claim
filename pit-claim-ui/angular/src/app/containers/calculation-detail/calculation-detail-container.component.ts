import {Component} from "@angular/core";
import { Location, LocationStrategy, PathLocationStrategy, AsyncPipe, NgComponentOutlet } from "@angular/common";
import {BaseContainer} from "../base/base-container.component";
import {switchMap, from, Observable} from "rxjs";
import {vmCalculation} from "../../conversion/models";
import {select} from "@ngrx/store";
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
import { COVERAGE_TYPE, INSURANCE_PLAN, MEASUREMENT_TYPE } from "src/app/utils";

@Component({
    selector: "cirras-claims-calculation-detail-container",
    template: `
        <ng-container *ngComponentOutlet="dynamicComponent$ | async; inputs: {
            calculationDetail: calculationDetail$ | async,
            loadState: loadState$ | async,
            errorState: errorState$ | async,
            isUnsaved: isUnsaved$ | async
        }"></ng-container>
        `,
    providers: [Location, { provide: LocationStrategy, useClass: PathLocationStrategy }],
    imports: [NgComponentOutlet, AsyncPipe]
})
export class CalculationDetailContainer extends BaseContainer  {
    displayLabel = "Calculation Detail";
    calculationDetail$: Observable<vmCalculation> = this.store.pipe(select(selectCalculationDetail()));

    // 1. Listen to data changes
    // 2. Trigger a lazy runtime chunk import based on the insurance plan and coverage
    // 3. Resolve the underlying class reference dynamically
    dynamicComponent$: Observable<any> = this.calculationDetail$.pipe(
        switchMap(detail => {

            if (detail?.insurancePlanId === INSURANCE_PLAN.GRAPES) {
                return from(
                    import('../../components/calculation-detail/grapes/grapes.component')
                        .then(m => m.CalculationDetailGrapesComponent)
                );
            } 

            if (detail?.insurancePlanId === INSURANCE_PLAN.BERRIES) {
                
                if ((detail.commodityCoverageCode).toUpperCase() === COVERAGE_TYPE.BERRIES_QUANTITY) {
                    // Dynamically fetch the Berries file chunk from the network
                    return from(
                        import('../../components/calculation-detail/berries/berries.component')
                            .then(m => m.CalculationDetailBerriesComponent)
                    );
                }

                if ((detail.commodityCoverageCode).toUpperCase() === COVERAGE_TYPE.BERRIES_PLANT && 
                    detail.insuredByMeasurementType && detail.insuredByMeasurementType.toUpperCase() === MEASUREMENT_TYPE.UNITS) {
                    return from(
                        import('../../components/calculation-detail/blueberries-plant/blueberries-plant.component')
                            .then(m => m.CalculationDetailBlueberriesPlantComponent)
                    );
                }
                
                if ((detail.commodityCoverageCode).toUpperCase() === COVERAGE_TYPE.BERRIES_PLANT && 
                    detail.insuredByMeasurementType && detail.insuredByMeasurementType.toUpperCase() === MEASUREMENT_TYPE.ACRES) {
                    return from(
                        import('../../components/calculation-detail/strawberries-plant/strawberries-plant.component')
                            .then(m => m.CalculationDetailStrawberriesPlantComponent)
                    );
                }
            }

            if (detail?.insurancePlanId === INSURANCE_PLAN.GRAIN) {
                if ((detail.commodityCoverageCode).toUpperCase() === COVERAGE_TYPE.GRAIN_BASKET) {
                    return from(
                        import('../../components/calculation-detail/grain-basket/grain-basket.component')
                            .then(m => m.CalculationDetailGrainBasketComponent)
                    );
                }

                if ((detail.commodityCoverageCode).toUpperCase() === COVERAGE_TYPE.GRAIN_QUANTITY) {
                    return from(
                        import('../../components/calculation-detail/grain-quantity/grain-quantity.component')
                            .then(m => m.CalculationDetailGrainQuantityComponent)
                    );
                }

                if ((detail.commodityCoverageCode).toUpperCase() === COVERAGE_TYPE.GRAIN_SPOT_LOSS) {
                    return from(
                        import('../../components/calculation-detail/grain-spot-loss/grain-spot-loss.component')
                            .then(m => m.CalculationDetailGrainSpotLossComponent)
                    );
                }

                if ((detail.commodityCoverageCode).toUpperCase() === COVERAGE_TYPE.GRAIN_UNSEEDED) {
                    return from(
                        import('../../components/calculation-detail/grain-unseeded/grain-unseeded.component')
                            .then(m => m.CalculationDetailGrainUnseededComponent)
                    );
                }
            } 

            // Default: go to the new calculation component
            return from(
                import('../../components/calculation-detail/new-calculation/new-calculation.component').then(m => m.NewCalculationComponent)
            );

        })
    );

    loadState$: Observable<LoadState> = this.store.pipe(select(selectCalculationDetailMetadataLoadState()));
    errorState$: Observable<ErrorState[]> = this.store.pipe(select(selectCalculationDetailMetadataErrorState()));
    isUnsaved$: Observable<boolean> = this.store.pipe(select(selectFormStateUnsaved(CALCULATION_DETAIL_COMPONENT_ID)));

    getAssociatedComponentIds(): string[] {
        return [
          CALCULATION_DETAIL_COMPONENT_ID
        ];
    }
}
