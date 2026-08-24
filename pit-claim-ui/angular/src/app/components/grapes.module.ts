import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CommonModule } from '@angular/common';
import { CalculationDetailGrapesContainer } from '../containers/calculation-detail/calculation-detail-grapes-container.component';
import { CalculationDetailGrapesComponent } from './calculation-detail/grapes/grapes.component';
import { ResourcesAuthGuard } from '../services/util/ResourcesAuthGuard';
import { DeactivateGuard } from '../services/util/DeactivateGuard';
import { CALCULATION_DETAIL_SCOPES } from '../app-routing.module';

import { CalculationPrintoutComponent } from './calculation-printout/grapes/grapes.component';

const routes: Routes = [
 { 
    path: ':policyNumber/:claimNumber', 
    component: CalculationDetailGrapesContainer, 
    data: { scopes: CALCULATION_DETAIL_SCOPES },
    canActivate: [ResourcesAuthGuard],
    canDeactivate: [DeactivateGuard]
  },
  { 
    path: ':policyNumber/:claimNumber/:claimCalculationGuid', 
    component: CalculationDetailGrapesContainer, 
    data: { scopes: CALCULATION_DETAIL_SCOPES },
    canActivate: [ResourcesAuthGuard],
    canDeactivate: [DeactivateGuard]
  }
];

@NgModule({
    imports: [
      CommonModule,
      RouterModule.forChild(routes),
      CalculationDetailGrapesContainer,
      CalculationDetailGrapesComponent,
      CalculationPrintoutComponent
    ]
})

export class GrapesModule {}