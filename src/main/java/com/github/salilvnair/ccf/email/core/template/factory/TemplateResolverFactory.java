package com.github.salilvnair.ccf.email.core.template.factory;

import com.github.salilvnair.ccf.email.core.template.resolver.core.TemplateResolver;
import com.github.salilvnair.ccf.email.core.template.type.TemplateResolverType;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class TemplateResolverFactory {

    private final ApplicationContext applicationContext;
    public TemplateResolverFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @SuppressWarnings({"unchecked"})
    public  <T> TemplateResolver<T> generate(Class<T> tClass, TemplateResolverType resolverType, Map<String, Object> inputParams ) {
        return  applicationContext.containsBean(resolverType.value()) ? (TemplateResolver<T>) applicationContext.getBean(resolverType.value()) : null;
    }
}