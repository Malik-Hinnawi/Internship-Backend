package com.application.internshipbackend.config;

import org.hibernate.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class LocalResolverConfiguration {

    @Bean
    public LocaleResolver localeResolver(){
        AcceptHeaderLocaleResolver headerLocaleResolver = new AcceptHeaderLocaleResolver();
        headerLocaleResolver.setDefaultLocale(Locale.ENGLISH);
        return headerLocaleResolver;
    }



}
