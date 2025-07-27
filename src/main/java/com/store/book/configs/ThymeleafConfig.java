package com.store.book.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;

@Configuration
public class ThymeleafConfig {

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("XHTML");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false);
        return resolver;
    }

    @Bean(name = "customTemplateEngine")
    public TemplateEngine templateEngine(SpringResourceTemplateResolver templateResolver) {
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(templateResolver);
        return engine;
    }
}
