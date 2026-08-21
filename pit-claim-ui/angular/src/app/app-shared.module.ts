import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { BaseWrapperComponent } from "./components/common/base-wrapper/base-wrapper.component";
import { ErrorPanelComponent } from "./components/common/error-panel/error-panel.component";
import { CalculationDetailHeaderComponent } from "./components/calculation-detail/calculation-detail-header/calculation-detail-header.component";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { MatSelectModule } from "@angular/material/select";
import { MatIconModule } from "@angular/material/icon";
import { ReactiveFormsModule } from "@angular/forms";
import { CalculationPrintoutLogoComponent } from "./components/calculation-printout/calculation-printout-logo/calculation-printout-logo.component";
import { CalculationPrintoutHeaderComponent } from "./components/calculation-printout/calculation-printout-header/calculation-printout-header.component";
import { CalculationPrintoutFooterComponent } from "./components/calculation-printout/calculation-printout-footer/calculation-printout-footer.component";

@NgModule({
  declarations: [
    BaseWrapperComponent,
    ErrorPanelComponent,
    CalculationDetailHeaderComponent,
    CalculationPrintoutLogoComponent,
    CalculationPrintoutHeaderComponent,
    CalculationPrintoutFooterComponent
  ],
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatIconModule,
    ReactiveFormsModule
  ],
  exports: [
    BaseWrapperComponent,
    ErrorPanelComponent,
    CalculationDetailHeaderComponent,
    CalculationPrintoutLogoComponent,
    CalculationPrintoutHeaderComponent,
    CalculationPrintoutFooterComponent,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatIconModule,
    ReactiveFormsModule
  ] // Crucial: must be exported
})
export class AppSharedModule { }