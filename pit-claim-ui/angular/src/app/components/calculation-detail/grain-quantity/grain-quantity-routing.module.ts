import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ResourcesAuthGuard } from '../../../services/util/ResourcesAuthGuard';
import { DeactivateGuard } from '../../../services/util/DeactivateGuard';
import { CALCULATION_DETAIL_SCOPES } from '../../../app-routing.module';
import { CalculationDetailContainer } from '../../../containers/calculation-detail/calculation-detail-container.component';
import { CalculationDetailGrainQuantityComponent } from './grain-quantity.component';
import { CalculationPrintoutGrainQuantityComponent } from '../../calculation-printout/grain-quantity/grain-quantity.component';

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
      CalculationDetailGrainQuantityComponent,
      CalculationPrintoutGrainQuantityComponent
    ]
})

export class GrainQuantityRoutingModule {}