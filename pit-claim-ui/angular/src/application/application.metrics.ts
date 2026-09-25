export const applicationMetrics = [
    {
        selector: 'wf-application, .wf-dialog',
        variables: {
            '--wf-colour-blue': '#003366',
            '--wf-colour-white': '#ffffff',
            '--wf-colour-light-grey': '#c6c8cb',
            '--wf-colour-lighter-grey': '#f2f2f2',
            '--wf-colour-disabled-background': 'rgba(0, 0, 0, 0.06)',

            '--wf-header-background-color': '#003366',
            '--wf-header-color': 'white',
            '--wf-header-border-color': '#FCBA19',
            '--wf-header-environment-color': '#fcba19',

            '--wf-menu-expanded-width': '250px',
            '--wf-menu-collapsed-width': '50px',
            '--wf-menu-icon-size': '24px',
            '--wf-menu-icon-color': 'rgba(0, 0, 0, 0.9)',
            '--wf-menu-item-row-height': '48px',

            '--wf-menu-color': '#454545',
            '--wf-menu-background-color': '#f2f2f2',

            '--wf-menu-highlight-background-color': '#ddd',

            '--wf-menu-active-color': '#003366',
            '--wf-menu-active-font-weight': '600',
            '--wf-menu-active-background-color': '#ddd',

            '--wf-icon-size-small': '24px',
            '--wf-icon-size-medium': '32px',
            '--wf-gutter': '16px',

            '--wf-font-family-main': '"BCSans", "Noto Sans", Verdana, Arial, sans-serif',

            '--wf-font-size': '15px',          
            '--wf-font-size-emphasis': '17px',
            '--wf-font-weight-emphasis': '400',
            '--wf-font-weight-emphasis-more': '600',
            '--wf-border-radius': '5px',

            '--wf-colour-active-tab': '#ffffff',
            '--wf-colour-inactive-tab': '#f2f2f2',

            '--wf-menu-bar-background-color': '#355992',
            '--wf-menu-bar-font-weight': '16',
            '--wf-menu-bar-item-row-height': '20px',
            '--wf-menu-bar-divider-color': '#9b9b9b',

            '--wf-footer-background-color': '#003366',
            '--wf-footer-item-row-width': '150px',           
        },
    },
    {
        selector: 'wf-application.device-desktop, .wf-dialog .desktop',
        variables: {
            '--wf-header-height': '72px',
            '--wf-header-bcwfservice-logo-height': '40px',
            '--wf-header-wildfire1-logo-height': '40px',
        }
    },
    {
        selector: 'wf-application.device-mobile, .wf-dialog .mobile',
        variables: {
            '--wf-header-height': '48px',
            '--wf-header-bcwfservice-logo-height': '30px',
            '--wf-header-wildfire1-logo-height': '30px',
            '--wf-menu-expanded-width': '220px',
            '--wf-gutter': '8px',
        },
    }
]

