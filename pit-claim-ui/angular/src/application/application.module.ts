import { CommonModule } from '@angular/common';
import { ModuleWithProviders, NgModule } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { RouterModule } from '@angular/router';
import { applicationMetrics } from './application.metrics';
import { PitApplicationComponent } from './components/pit-application/pit-application.component';
import { PitHeaderComponent } from './components/pit-header/pit-header.component';
import { PitIconComponent } from './components/pit-icon/pit-icon.component';
import { PitMenuComponent } from './components/pit-menu/pit-menu.component';
import { ScrollingModule } from '@angular/cdk/scrolling';

@NgModule({
    imports: [
        CommonModule,
        MatIconModule,
        MatTooltipModule,
        RouterModule,
        MatButtonModule,
        MatMenuModule,
        ScrollingModule,
        // once the module was ingested
        PitApplicationComponent,
        PitHeaderComponent,
        PitIconComponent,
        PitMenuComponent,

    ],
    declarations: [
        // PitApplicationComponent,
        // PitHeaderComponent,
        // PitIconComponent,
        // PitMenuComponent,
    ],
    exports: [
        PitApplicationComponent,
        PitHeaderComponent,
        PitIconComponent,
        PitMenuComponent
    ]
})
export class WildfireApplicationModule {
    static forRoot(): ModuleWithProviders<WildfireApplicationModule> {
        // console.log('WildfireApplicationModule.forRoot')
        const doc = window[ 'document' ]

        const style = doc.createElement( 'style' )
        style.textContent = applicationMetrics.map( ( m ) => {
            return `
${ m.selector } {
    ${ Object.entries( m.variables ).map( ( [ key, value ] ) => `${ key }: ${ value };` ).join( '\n    ' ) }
}`
        } ).join( '\n' )

        doc.getElementsByTagName( 'head' )[ 0 ].appendChild( style )

        return {
            ngModule: WildfireApplicationModule
        }
    }
}
