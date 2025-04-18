package ca.bc.gov.mal.pit.claim.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@ComponentScan("ca.bc.gov.mal.pit.claim.web.controller")
@Import({
        PropertiesSpringConfig.class,
        SecuritySpringConfig.class
})
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Bean
    public UrlBasedViewResolver viewResolver() {
        logger.info("<viewResolver");

        UrlBasedViewResolver resolver
                = new UrlBasedViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);

        logger.info(">viewResolver");
        return resolver;
    }
}
