package com.github.salilvnair.ccf.email.core.template.context;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class MessageTemplateContext {
    private Integer templateId;
    private Map<String, Object> templateVariables;
    private String message;
    private boolean active;
}
