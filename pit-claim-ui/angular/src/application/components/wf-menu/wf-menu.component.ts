import { Component, EventEmitter, HostBinding, Input, Output } from "@angular/core";
import { NavigationEnd, Router, RouterModule, UrlTree } from "@angular/router";
import { WfMenuState } from "../../application.config";
import { BaseComponent } from "../base.component";
import { animate, state, style, transition, trigger } from '@angular/animations';
import { applicationMetrics } from '../../application.metrics';
import { WfIconComponent } from "../wf-icon/wf-icon.component";
import { MatMenuModule } from "@angular/material/menu";
import { NgFor, NgForOf, NgIf } from "@angular/common";
import { MatTooltip } from "@angular/material/tooltip";

@Component({
    selector: 'wf-menu',
    templateUrl: './wf-menu.component.html',
    styleUrls: ['./wf-menu.component.scss'],
    animations: [
        trigger( 'menu-collapsed-expanded', [
            state( 'collapsed', style( {
                'width': applicationMetrics[ 0 ].variables[ '--wf-menu-collapsed-width' ]
            } ) ),
            state( 'expanded', style( {
                'width': applicationMetrics[ 0 ].variables[ '--wf-menu-expanded-width' ]
            } ) ),
            transition( 'collapsed => expanded', [
                animate( '0.25s' )
            ] ),
            transition( 'expanded => collapsed', [
                animate( '0.25s' )
            ] )
        ] ),
        trigger( 'label-collapsed-expanded', [
            state( 'collapsed', style( {
                'opacity': '0'
            } ) ),
            state( 'expanded', style( {
                'opacity': '1'
            } ) ),
            transition( 'collapsed => expanded', [
                animate( '0.25s' )
            ] ),
            transition( 'expanded => collapsed', [
                animate( '0.25s' )
            ] )
        ] )

    ],
    imports: [
        WfIconComponent, MatMenuModule, NgForOf, NgIf, RouterModule, MatTooltip
    ]
})
export class WfMenuComponent extends BaseComponent {
    _menu: WfMenuItems
    @Input() set menuItems( m: WfMenuItems ) {
        this._menu = m
        this._menu.forEach( i => i.OnActiveChanged( () => {
            this.activeItemChanged.emit( i )
        } ) )
    }
    get menuItems() { return this._menu }

    @Output() activeItemChanged = new EventEmitter<WfMenuItem>();

    @HostBinding( '@menu-collapsed-expanded' ) get animation() {
        return this.menuState
    }

    tooltipShowDelay = 300

    constructor() {
        super()
        this.activeItemChanged.subscribe( ( item ) => {
            if ( item.menuStateAfterActive && this.config && this.config.device != "desktop"){
                this.menuState = item.menuStateAfterActive
            }
        } )
    }

    get isHidden() { return this.menuState == 'hidden' }
    get isCollapsed() { return this.menuState == 'collapsed' }
    get isExpanded() { return this.menuState == 'expanded' }

    get toggleTitle() {
        if ( this.config.device == 'desktop' ) {
            if ( this.menuState == 'expanded' ) return 'Collapse'
            if ( this.menuState == 'collapsed' ) return 'Expand'
        }
        else if ( this.config.device == 'mobile' ) {
            if ( this.menuState == 'expanded' ) return 'Hide'
        }
    }

    onToggleClick() {
        if ( this.config.device == 'desktop' ) {
            this.menuState = this.menuState == 'expanded' ? 'collapsed' : 'expanded'
        }
        else if ( this.config.device == 'mobile' ) {
            this.menuState = this.menuState == 'expanded' ? 'hidden' : 'expanded'
        }
    }

    onItemClick( item: WfMenuItem ) {
        this.activeItemChanged.emit( item )
    }
}

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

export type WfMenuItem = RouterLink|ActionTrigger
export type WfMenuItems = Array<WfMenuItem>

export class RouterLink{
    readonly type = 'router-link'

    private active: boolean = false
    private activeChangedCallback: () => void = () => {}

    constructor(
        public title: string,
        public route: string|UrlTree,
        public iconName: string,
        public menuStateAfterActive: WfMenuState = 'collapsed',
        private router: Router,
        public routerLinkExactMatch:boolean =  true,
        public indented = false
    ) {
        this.setActive( this.router.isActive( this.route, this.routerLinkExactMatch ) )

        router.events.subscribe( event => {
            if ( event instanceof NavigationEnd ) {
                this.setActive( this.router.isActive( this.route, this.routerLinkExactMatch ) )
            }
        } )
    }

    get isActive(): boolean {
        return this.active
    }

    setActive( active: boolean ) {
        if ( active == this.active ) return
        this.active = active
        if ( this.active )
            this.activeChangedCallback()
    }

    OnActiveChanged( cb: () => void ) {
        this.activeChangedCallback = cb
    }
}

export class ActionTrigger {
    readonly type = 'action-trigger'

    actionTriggered = new EventEmitter<ActionTrigger>();

    constructor(
        public title: string,
        public iconName: string = null,
        public menuStateAfterActive: WfMenuState = 'collapsed',
        public indented = false
    ) {
    }

    OnActiveChanged( cb: () => void ) {}

    onClick() {
        this.actionTriggered.emit(this)
    }
}

