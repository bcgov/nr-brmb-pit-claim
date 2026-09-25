import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component, HostBinding, Input } from '@angular/core';
import { applicationMetrics } from '../../application.metrics';
import { BaseComponent } from '../base.component';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'wf-application',
    templateUrl: './wf-application.component.html',
    styleUrls: ['./wf-application.component.scss',
        './wf-basic-tab.scss',
        './wf-file-tab.scss'],
    animations: [
        trigger('menu-collapsed-expanded', [
            state('collapsed', style({
                'width': applicationMetrics[0].variables['--wf-menu-collapsed-width']
            })),
            state('expanded', style({
                'width': applicationMetrics[0].variables['--wf-menu-expanded-width']
            })),
            transition('collapsed => expanded', [
                animate('0.25s')
            ]),
            transition('expanded => collapsed', [
                animate('0.25s')
            ])
        ])
    ],
    imports: [
        CommonModule
    ]
})
export class WfApplicationComponent extends BaseComponent {
    @Input() isInitializing: boolean = false

    @HostBinding('class') get deviceClass() {
        return `device-${this.config.device} menu-${this.menuState}`
    }

    onBackdropClick() {
        this.menuState = 'hidden'
    }
}
