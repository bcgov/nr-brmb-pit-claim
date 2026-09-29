export const applicationMetrics = [
    {
        selector: 'pit-application, .wf-dialog',
        variables: {
            '--wf-colour-blue': '#003366',
            '--wf-colour-white': '#ffffff',
            '--wf-colour-light-grey': '#c6c8cb',

            '--wf-header-background-color': '#003366',
            '--wf-header-color': 'white',
            '--wf-header-border-color': '#FCBA19',
            '--wf-header-environment-color': '#fcba19',

            '--pit-menu-expanded-width': '250px',
            '--pit-menu-collapsed-width': '50px',
            '--pit-menu-icon-color': 'rgba(0, 0, 0, 0.9)',
            '--pit-menu-item-row-height': '48px',

            '--pit-menu-color': '#454545',
            '--pit-menu-background-color': '#f2f2f2',

            '--pit-menu-highlight-background-color': '#ddd',

            '--pit-menu-active-color': '#003366',
            '--pit-menu-active-font-weight': '200',
            '--pit-menu-active-background-color': '#ddd',

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
      
        },
    },
    {
        selector: 'pit-application.device-desktop, .wf-dialog .desktop',
        variables: {
            '--wf-header-height': '72px',
            '--wf-header-bcwfservice-logo-height': '40px',
        }
    },
    {
        selector: 'pit-application.device-mobile, .wf-dialog .mobile',
        variables: {
            '--wf-header-height': '48px',
            '--wf-header-bcwfservice-logo-height': '30px',
            '--pit-menu-expanded-width': '220px',
            '--wf-gutter': '8px',
        },
    }
]

