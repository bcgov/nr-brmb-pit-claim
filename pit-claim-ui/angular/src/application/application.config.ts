export type PitDevice = 'desktop'|'mobile'

export interface PitApplicationConfiguration {
    title: string
    device: PitDevice
    userName: string
    actingOnBehalfOf?: string
    version: {
        long: string
        short: string
    }
    environment: string
}

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

export type PitMenuState = 'hidden'|'collapsed'|'expanded'

export interface PitApplicationState {
    menu: PitMenuState
}
