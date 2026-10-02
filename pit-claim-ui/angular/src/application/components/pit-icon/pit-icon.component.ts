import { NgIf } from '@angular/common';
import { Component, Input } from '@angular/core';
import { MatIcon } from '@angular/material/icon';

@Component({
    selector: 'pit-icon',
    templateUrl: './pit-icon.component.html',
    styleUrls: ['./pit-icon.component.scss'],
    host: { 'class': 'mat-typography' },
    imports: [ NgIf, MatIcon]
})
export class PitIconComponent {
    @Input() iconName: string
    @Input() iconSize: 'small' | 'medium' | 'large' = 'medium'

    constructor() {}

    get actualIconName() {
        return this.iconName
    }

    get sizeSmall() {
        return this.iconSize == 'small'
    }

    get sizeMedium() {
        return this.iconSize == 'medium'
    }

    get sizeLarge() {
        return this.iconSize == 'large'
    }

}

enum IconSize {
    DEFAULT = 'toolbar',
    TOOLBAR = 'toolbar',
    LIST_SMALL = 'list-small',
    LIST_LARGE = 'list-large',
}
