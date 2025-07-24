package com.store.book.configs;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;
import java.util.Locale;

@Configuration
public class LocalizationConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setBasenames(
                "classpath:messages_validations",
                "classpath:messages_errors"
        );
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver() {
            @Override
            public Locale resolveLocale(HttpServletRequest request) {
                String headerLang = request.getHeader("Accept-Language");
                if (headerLang == null || headerLang.isEmpty()) {
                    return Locale.getDefault();
                }
                List<Locale> supportedLocales = List.of(
                        Locale.ENGLISH,
                        new Locale("ru"),
                        new Locale("az")
                );
                Locale locale = Locale.lookup(Locale.LanguageRange.parse(headerLang), supportedLocales);
                return locale != null ? locale : Locale.getDefault();
            }
        };
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }
}
