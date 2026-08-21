import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CommonModule } from '@angular/common';
import { CalculationDetailGrapesContainer } from '../containers/calculation-detail/calculation-detail-grapes-container.component';
import { CalculationDetailGrapesComponent } from './calculation-detail/grapes/grapes.component';
import { ResourcesAuthGuard } from '../services/util/ResourcesAuthGuard';
import { DeactivateGuard } from '../services/util/DeactivateGuard';
import { CALCULATION_DETAIL_SCOPES } from '../app-routing.module';
import { AppSharedModule } from '../app-shared.module';
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
  declarations: [
    CalculationDetailGrapesContainer,
    CalculationDetailGrapesComponent,
    CalculationPrintoutComponent
  ],
  imports: [
    CommonModule,
    AppSharedModule,
    RouterModule.forChild(routes)
  ] 
})

export class GrapesModule {}