import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ResourcesAuthGuard } from '../../../services/util/ResourcesAuthGuard';
import { DeactivateGuard } from '../../../services/util/DeactivateGuard';
import { CALCULATION_DETAIL_SCOPES } from '../../../app-routing.module';
import { CalculationDetailContainer } from '../../../containers/calculation-detail/calculation-detail-container.component';
import { CalculationDetailGrainSpotLossComponent } from './grain-spot-loss.component';
import { CalculationPrintoutGrainSpotLossComponent } from '../../calculation-printout/grain-spot-loss/grain-spot-loss.component';

const routes: Routes = [
 { 
    path: ':policyNumber/:claimNumber', 
    component: CalculationDetailContainer, 
    data: { scopes: CALCULATION_DETAIL_SCOPES },
    canActivate: [ResourcesAuthGuard],
    canDeactivate: [DeactivateGuard]
  },
  { 
    path: ':policyNumber/:claimNumber/:claimCalculationGuid', 
    component: CalculationDetailContainer, 
    data: { scopes: CALCULATION_DETAIL_SCOPES },
    canActivate: [ResourcesAuthGuard],
    canDeactivate: [DeactivateGuard]
  }
];

@NgModule({
    imports: [
      CommonModule,
      RouterModule.forChild(routes),
      CalculationDetailContainer,
      CalculationDetailGrainSpotLossComponent,
      CalculationPrintoutGrainSpotLossComponent
    ]
})

export class GrainSpotLossRoutingModule {}