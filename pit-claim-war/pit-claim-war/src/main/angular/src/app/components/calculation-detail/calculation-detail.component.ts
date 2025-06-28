import {ChangeDetectionStrategy, Component, Input, OnInit,} from "@angular/core";
import {ParamMap} from "@angular/router";
import {vmCalculation} from "../../conversion/models";
import {loadCalculationDetail, clearCalculationDetail} from "../../store/calculation-detail/calculation-detail.actions";
import { ActivatedRoute, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { RootState } from 'src/app/store';
import { ErrorState, LoadState } from "src/app/store/application/application.state";

@Component({
    selector: "cirras-claims-calculation-detail",
    templateUrl: "./calculation-detail.component.html",
    styleUrls: ["../common/base/base.component.scss",
        "../common/base-collection/collection.component.scss",
        "./calculation-detail.component.scss"],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class CalculationDetailComponent implements OnInit {
    @Input() calculationDetail: vmCalculation;
    @Input() loadState: LoadState;
    @Input() errorState: ErrorState[];
    @Input() isUnsaved: boolean;

    claimCalculationGuid: string;
    claimNumber: string;
    policyNumber: string;
    
    displayLabel = "Calculation Detail"

    constructor (protected router: Router,
        protected route: ActivatedRoute,
        protected store: Store<RootState>,) {
      }
    
      ngOnInit() {
        this.loadPage()
      }

    loadPage() {
        this.route.paramMap.subscribe(
            (params: ParamMap) => {
                this.claimCalculationGuid = params.get("claimCalculationGuid") ? params.get("claimCalculationGuid") : null;
                this.claimNumber = params.get("claimNumber") ? params.get("claimNumber") : null;
                this.policyNumber = params.get("policyNumber") ? params.get("policyNumber") : null;

                if (!this.claimCalculationGuid) {
                    this.store.dispatch(clearCalculationDetail());
                }   
                this.store.dispatch(loadCalculationDetail(this.claimCalculationGuid, this.displayLabel, this.claimNumber,this.policyNumber, "false"));                   
            }
        );
    }
 }
