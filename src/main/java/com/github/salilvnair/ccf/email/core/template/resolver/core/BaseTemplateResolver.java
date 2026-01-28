package com.github.salilvnair.ccf.email.core.template.resolver.core;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

public abstract class BaseTemplateResolver<T> implements TemplateResolver<T> {

    protected TemplateEngine templateEngine(TemplateMode templateMode) {
        TemplateEngine templateEngine = new SpringTemplateEngine();
        StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setTemplateMode(templateMode);
        templateResolver.setCacheable(false);
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }
}
