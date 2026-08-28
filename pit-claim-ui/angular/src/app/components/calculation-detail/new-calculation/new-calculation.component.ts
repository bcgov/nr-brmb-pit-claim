import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Store } from '@ngrx/store';
import { vmCalculation } from 'src/app/conversion/models';
import { RootState } from 'src/app/store';
import { ErrorState, LoadState } from 'src/app/store/application/application.state';
import { loadCalculationDetail } from 'src/app/store/calculation-detail/calculation-detail.actions';

@Component({
  selector: 'pit-new-calculation',
  imports: [],
  templateUrl: './new-calculation.component.html',
  styleUrl: './new-calculation.component.scss'
})
export class NewCalculationComponent implements OnInit {
  @Input() calculationDetail: vmCalculation;
  @Input() isUnsaved: boolean;
  @Input() loadState: LoadState;
  @Input() errorState: ErrorState[];

  claimCalculationGuid: string;
  claimNumber: string;
  policyNumber: string;

  constructor( protected route: ActivatedRoute,
    protected store: Store<RootState>) { }

  ngOnInit(): void {
    this.loadCalculation()
  }

  loadCalculation() {
    this.route.paramMap.subscribe(
        (params: ParamMap) => {
            this.claimCalculationGuid = params.get("claimCalculationGuid") ? params.get("claimCalculationGuid") : null;
            this.claimNumber = params.get("claimNumber") ? params.get("claimNumber") : null;
            this.policyNumber = params.get("policyNumber") ? params.get("policyNumber") : null;

            this.store.dispatch(loadCalculationDetail(this.claimCalculationGuid, "", this.claimNumber,this.policyNumber, "false"));                   
        }
    );
  }

}
