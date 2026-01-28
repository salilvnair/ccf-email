package com.github.salilvnair.ccf.email.core.template.type;

import java.util.Arrays;
import java.util.Optional;

public enum TemplateResolverType {
    EMAIL(Value.EMAIL),
    MESSAGE(Value.MESSAGE),

    ;

    public static class Value {
        public static final String EMAIL = "EMAIL";
        public static final String MESSAGE = "MESSAGE";
    }

    private final String type;

    TemplateResolverType(String type) {
        this.type = type;
    }

    public String value() {
        return type;
    }

    public static TemplateResolverType findByName(String name) {
        Optional<TemplateResolverType> typeEnum = Arrays.stream(TemplateResolverType.values())
                .filter(comp -> comp.value() != null && comp.value().equals(name)).findFirst();
        return typeEnum.orElse(EMAIL);
    }
}
