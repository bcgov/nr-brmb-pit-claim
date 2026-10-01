import { Directive, EventEmitter, Input, Output } from "@angular/core";
import { PitApplicationConfiguration, PitApplicationState, PitMenuState } from "../application.config";

@Directive()
export abstract class BaseComponent {

    @Input() config: PitApplicationConfiguration
    
    get title() { return this.config.title }
    get environment() { return this.config.environment }
    get userName() { return this.config.userName }
    get actingOnBehalfOf() { return this.config.actingOnBehalfOf }
    get version() { return this.config.version }

    @Input() set state(s: PitApplicationState) {
        this._state = s;
        this.menuState = s.menu
    }
    @Output() stateChange = new EventEmitter<PitApplicationState>()

    _state: PitApplicationState = {
        menu: null
    }
    get menuState() { return this._state.menu }
    set menuState(m: PitMenuState) {
        if (m == this.menuState) return
        this._state.menu = m

        // copying the state object is important to ensure that the change is detected
        this.stateChange.emit({ ...this._state })
    }
}
