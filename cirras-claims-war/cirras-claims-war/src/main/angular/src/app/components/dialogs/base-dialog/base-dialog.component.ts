import {
    AfterViewInit,
    ChangeDetectionStrategy,
    ChangeDetectorRef,
    Component,
    Input,
    OnChanges,
    OnDestroy,
    SimpleChanges
} from "@angular/core";

import {DomSanitizer} from "@angular/platform-browser";
import {UntypedFormArray, UntypedFormBuilder, UntypedFormGroup} from "@angular/forms";
import {Store} from "@ngrx/store";
import {ApplicationStateService} from "../../../services/application-state.service";
import {RootState} from "../../../store";
import {BaseComponentModel} from "../../common/base/base.component.model";
import {ErrorMessages, getDisplayErrorMessage} from "../../../utils/error-messages";
import {MatDialogRef} from "@angular/material/dialog";
import {addRemoveCdkOverlayClass, CONSTANTS, hasValues} from "../../../utils";
import {setFormStateUnsaved} from "../../../store/application/application.actions";
import {ErrorState, LoadState} from "../../../store/application/application.state";

export enum DIALOG_TYPE {
    INFO,
    ERROR
}

@Component({
    selector: "cirras-claims-base-dialog",
    templateUrl: "./base-dialog.component.html",
    styleUrls: ["../../common/base/base.component.scss", "./base-dialog.component.scss"],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class BaseDialogComponent implements AfterViewInit, OnChanges, OnDestroy {
    DIALOG_TYPE_OBJ = DIALOG_TYPE;
    mobile: boolean;
    protected model: BaseComponentModel;
    public viewModel: BaseComponentModel;
    isLocalSaving: boolean = false;
    isLoading: boolean = false;
    DISPLAY_ERROR_MESSAGE = getDisplayErrorMessage;
    ERROR_MESSAGES = ErrorMessages;
    isUnsaved = false;
    componentId = null;
    @Input() disableDrag?: boolean = false;
    @Input() loadState?: LoadState;
    @Input() errorState?: ErrorState[];
    @Input() titleLabel: string;
    @Input() type?: DIALOG_TYPE = DIALOG_TYPE.INFO;
    CONSTANTS = CONSTANTS;
    unsavedChangesMessage = "Unsaved Changes";

    constructor(
        public dialogRef: MatDialogRef<BaseDialogComponent>,
        protected applicationStateService: ApplicationStateService,
        protected sanitizer: DomSanitizer,
        protected fb: UntypedFormBuilder,
        public cdr: ChangeDetectorRef,
        protected store: Store<RootState>
    ) {
        dialogRef.disableClose = true;
        this.mobile = applicationStateService.getIsMobileResolution();
        this.initModels();
    }

    initModels() {

    }

    handleAccessibiltyOnLoad() {
        let mainEl = document.getElementById("cirrasClaimsMain");
        if (mainEl) {
            mainEl.removeAttribute("role");
            mainEl.setAttribute("inert", "");
        }

        document.querySelector(`.cdk-overlay-container`)?.querySelectorAll(`[aria-hidden="true"]`).forEach(el => {
            el.setAttribute("inert", "");
        });
    }

    handleAccessibiltyOonDestroy() {
        let mainEl = document.getElementById("cirrasClaimsMain");
        if (mainEl) {
            mainEl.setAttribute("role", "main");
            mainEl.removeAttribute("inert");
        }

        document.querySelector(`.cdk-overlay-container`)?.querySelectorAll(`[aria-hidden="true"]`).forEach(el => {
            el.removeAttribute("inert");
        });
    }

    ngAfterViewInit() {
        addRemoveCdkOverlayClass(this.applicationStateService.getIsMobileResolution());
        this.handleAccessibiltyOnLoad();
    }

    ngOnChanges(changes: SimpleChanges) {
        if (changes.loadState) {
            this.isLoading = changes.loadState.currentValue ? changes.loadState.currentValue.isLoading : false;
        }
        if (changes.errorState) {
            this.errorState = changes.errorState.currentValue as ErrorState[];
        }
    }

    ngOnDestroy() {
        this.handleAccessibiltyOonDestroy();
    }

    protected updateView(): void {
        this.viewModel = this.model.clone();
    }

    protected convertFromForm() {
        return this.viewModel.formGroup.value;
    }

    ok(ret?: any): void {
        this.dialogRef.close(ret);
    }

    cancel() {
        this.dialogRef.close();
    }

    setLocalSaving(value: boolean) {
        this.isLocalSaving = value;
    }

    setLoading(value: boolean) {
        this.isLoading = value;
    }

    disableSaveForm(form?: UntypedFormGroup): boolean {
        let fg = form ? form : this.viewModel.formGroup;
        //console.log(fg);
        return !fg.dirty || !fg.valid;
    }

    unsavedForm(form?: UntypedFormGroup, arrayProperty?: string): boolean {
        //console.log("unsaved", this.componentId);
        let fg = form ? form : this.viewModel.formGroup;
        if (arrayProperty) {
            this.unsavedBatchForm(arrayProperty);
        } else {
            this.doUnsavedStateUpdateIfNeeded(this.componentId, fg.dirty);
        }
        return fg.dirty;
    }

    unsavedBatchForm(arrayProperty: string): boolean {
        let fg = this.viewModel.formGroup;
        //Check form array for dirty flag
        let fgArray: UntypedFormGroup[] = fg?.controls[arrayProperty]['controls'];
        let arrayHasDirtyFlag = fgArray.some(contactFg => contactFg.dirty);
        let hasAddedUnsavedItem = this.hasAddedUnsavedItemNotBlank(fg, arrayProperty);
        //console.log("arrayHasDirtyFlag", arrayHasDirtyFlag, "fgDirty", fg.dirty, "hasAddedUnsavedItem", hasAddedUnsavedItem);
        this.doUnsavedStateUpdateIfNeeded(this.componentId, arrayHasDirtyFlag || fg.dirty || hasAddedUnsavedItem);
        return this.isUnsaved;
    }

    doUnsavedStateUpdateIfNeeded(componentId: string, newUnsavedState: boolean) {
        let prevUnsaved = this.isUnsaved; //save old value for comparison
        //console.log(componentId, "prev", prevUnsaved, "new", newUnsavedState);
        this.isUnsaved = newUnsavedState;
        if (componentId && prevUnsaved != this.isUnsaved) { //check if first time set to unsaved
            this.store.dispatch(setFormStateUnsaved(componentId, this.isUnsaved));
        }
    }

    hasAddedUnsavedItemNotBlank(fgMain: UntypedFormGroup, arrayProperty: string) {
        let controls = fgMain?.controls[arrayProperty]['controls'];
        let ret = controls.some(ac => {
                let fg: UntypedFormGroup = <UntypedFormGroup>ac;
                if (!fg.get("id").value && controls.length > 1) { //not a default empty entry
                    //console.log("not default entry");
                    return true;
                } else if (!fg.get("id").value && controls.length == 1) { //check if empty entry
                    let item = fg.getRawValue();
                    if (!hasValues(item)) {
                        //console.log("is default empty entry");
                        return false;
                    } else {
                        //console.log("default entry with info");
                        return true;
                    }
                } else {
                    //console.log("existing entry");
                    return false;
                }
            }
        );
        return ret;
    }

    protected touchAndValidateAllControls() {
        if (this.viewModel) {
            Object.keys(this.viewModel.formGroup.controls).forEach(key => {
                let control = this.viewModel.formGroup.get(key);
                if (control instanceof UntypedFormArray) {
                    if (control.controls.length > 0) {
                        control["controls"].forEach(fg => {
                            let formGroup = fg as UntypedFormGroup;
                            Object.keys(formGroup.controls).forEach(key2 => {
                                let arrayControlItem = formGroup.get(key2);
                                arrayControlItem.markAsTouched();
                                arrayControlItem.updateValueAndValidity();
                            });

                        });
                    }
                }
                control.markAsTouched();
                control.updateValueAndValidity();
            });
        }
    }
}
