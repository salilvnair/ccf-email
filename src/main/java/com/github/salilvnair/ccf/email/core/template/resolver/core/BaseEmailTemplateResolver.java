package com.github.salilvnair.ccf.email.core.template.resolver.core;

import com.github.salilvnair.ccf.email.core.template.context.EmailTemplateContext;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import java.util.Map;

public abstract class BaseEmailTemplateResolver<T> extends BaseTemplateResolver<T> {


    protected void resolveSenders(String recipientTemplate, Context context, EmailTemplateContext templateContext, Map<String, Object> inputParams) {
        if(recipientTemplate != null) {
            String recipients =  templateEngine(TemplateMode.TEXT).process(recipientTemplate, context);
            templateContext.setResolvedSenders(recipients);
        }
    }


    protected void resolveRecipients(String senderTemplate, Context context, EmailTemplateContext templateContext, Map<String, Object> inputParams) {
        if(senderTemplate != null) {
            String senders =  templateEngine(TemplateMode.TEXT).process(senderTemplate, context);
            templateContext.setResolvedRecipients(senders);
        }
    }


    protected void resolveCopiedRecipients(String copiedRecipientTemplate, Context context, EmailTemplateContext templateContext, Map<String, Object> inputParams) {
        if(copiedRecipientTemplate != null) {
            String resolvedText =  templateEngine(TemplateMode.TEXT).process(copiedRecipientTemplate, context);
            templateContext.setResolvedCarbonCopy(resolvedText);
        }
    }

    protected void resolveBlindCarbonCopy(String blindCarbonCopyTemplate, Context context, EmailTemplateContext templateContext, Map<String, Object> inputParams) {
        if(blindCarbonCopyTemplate != null) {
            String resolvedText =  templateEngine(TemplateMode.TEXT).process(blindCarbonCopyTemplate, context);
            templateContext.setResolvedBlindCarbonCopy(resolvedText);
        }
    }


    protected void resolveSubject(String subjectTemplate, Context context, EmailTemplateContext templateContext, Map<String, Object> inputParams) {
        if(subjectTemplate != null) {
            String copiedRecipients =  templateEngine(TemplateMode.TEXT).process(subjectTemplate, context);
            templateContext.setResolvedSubject(copiedRecipients);
        }
    }


    protected void resolveBody(String bodyTemplate, Context context, EmailTemplateContext templateContext, Map<String, Object> inputParams) {
        if(bodyTemplate != null) {
            String emailBody =  templateEngine(TemplateMode.HTML).process(bodyTemplate, context);
            templateContext.setResolvedBody(emailBody);
        }
    }
}
