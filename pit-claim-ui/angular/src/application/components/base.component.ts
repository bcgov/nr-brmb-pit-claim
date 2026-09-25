import { Directive, EventEmitter, Input, Output } from "@angular/core";
import { WfApplicationConfiguration, WfApplicationState, WfMenuState } from "../application.config";

@Directive()
export abstract class BaseComponent {

    @Input() config: WfApplicationConfiguration
    
    get title() { return this.config.title }
    get environment() { return this.config.environment }
    get userName() { return this.config.userName }
    get actingOnBehalfOf() { return this.config.actingOnBehalfOf }
    get version() { return this.config.version }

    @Input() set state(s: WfApplicationState) {
        this._state = s;
        this.menuState = s.menu
    }
    @Output() stateChange = new EventEmitter<WfApplicationState>()

    _state: WfApplicationState = {
        menu: null
    }
    get menuState() { return this._state.menu }
    set menuState(m: WfMenuState) {
        if (m == this.menuState) return
        this._state.menu = m

        // copying the state object is important to ensure that the change is detected
        this.stateChange.emit({ ...this._state })
    }
}
