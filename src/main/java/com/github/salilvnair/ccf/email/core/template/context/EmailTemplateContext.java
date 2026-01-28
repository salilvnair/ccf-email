package com.github.salilvnair.ccf.email.core.template.context;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class EmailTemplateContext {
    private Integer templateId;
    private Map<String, Object> templateVariables;
    private String resolvedSenders;
    private String resolvedRecipients;
    private String resolvedCarbonCopy;
    private String resolvedBlindCarbonCopy;
    private String resolvedSubject;
    private String resolvedBody;
    private boolean active;
}
