import { Component, EventEmitter, Input, Output } from '@angular/core';
import { BaseComponent } from '../base.component';
import { PitMenuItems } from '../pit-menu/pit-menu.component';
import { CommonModule, NgIf } from '@angular/common';
import { MatMenuModule } from '@angular/material/menu';
import { PitIconComponent } from '../pit-icon/pit-icon.component';

@Component({
    selector: 'pit-header',
    templateUrl: './pit-header.component.html',
    styleUrls: ['./pit-header.component.scss'],
    imports: [
        CommonModule, MatMenuModule, PitIconComponent, NgIf
    ]
})
export class PitHeaderComponent extends BaseComponent {
    @Input() useAppLogo: boolean = true
    @Input() useSystemAndUser: boolean = true
    @Input() useMenu: boolean = true
    @Input() useSupportLink: boolean = false
    @Input() useLogoutButton: boolean = false

    _supportMenu: PitMenuItems
    @Input() set supportMenuItems(m: PitMenuItems) {
        this._supportMenu = m
    }
    get supportMenuItems() { return this._supportMenu }
    get useSupportMenu() { return !!this._supportMenu }

    @Output() bcLogoClick = new EventEmitter<any>()
    @Output() supportLinkClick = new EventEmitter<any>()
    @Output() logoutClick = new EventEmitter<any>()

    onMenuClick() {
        this.menuState = this.menuState == 'expanded' ? 'hidden' : 'expanded'
    }

    onBCLogoClick() {
        this.bcLogoClick.emit();
    }

    onSupportLinkClick() {
        this.supportLinkClick.emit();
    }

    onLogoutClick() {
        this.logoutClick.emit()
    }

    get bcLogoClickEnable() {
        return this.bcLogoClick.observed // True if the parent binds to (bcLogoClick)="someMethod($event)"
    }
}
