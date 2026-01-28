package com.github.salilvnair.ccf.email.core.generator.factory;

import com.github.salilvnair.ccf.email.core.generator.EmailGenerator;
import com.github.salilvnair.ccf.email.core.type.EmailGeneratorType;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class EmailGeneratorFactory {
    private final ApplicationContext applicationContext;
    public EmailGeneratorFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public EmailGenerator generate(EmailGeneratorType emailGeneratorType, Map<String, Object> inputParams ) {
        return  applicationContext.containsBean(emailGeneratorType.name()) ? (EmailGenerator) applicationContext.getBean(emailGeneratorType.name()) : null;
    }
}
