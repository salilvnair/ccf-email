package com.github.salilvnair.ccf.email.core.template.resolver.provider;


import com.github.salilvnair.ccf.email.core.entity.EmailTemplate;
import com.github.salilvnair.ccf.email.core.repository.EmailTemplateRepo;
import com.github.salilvnair.ccf.email.core.template.context.EmailTemplateContext;
import com.github.salilvnair.ccf.email.core.template.resolver.core.BaseEmailTemplateResolver;
import com.github.salilvnair.ccf.email.core.template.type.TemplateResolverType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import java.util.Map;

@Service(TemplateResolverType.Value.EMAIL)
@RequiredArgsConstructor
public class EmailTemplateResolver extends BaseEmailTemplateResolver<EmailTemplateContext> {

    private final EmailTemplateRepo emailTemplateRepo;

    
    @Override
    public EmailTemplateContext resolve(EmailTemplateContext templateContext, Map<String, Object> inputParams)  throws Exception  {
        EmailTemplate emailTemplate = emailTemplateRepo.findById(templateContext.getTemplateId()).orElse(null);
        if(emailTemplate == null) {
            throw new Exception("EmailTemplate not found.");
        }
        templateContext.setActive(emailTemplate.active());
        Context context = new Context();
        context.setVariables(templateContext.getTemplateVariables());
        resolveSenders(emailTemplate.getFrom(), context, templateContext, inputParams);
        resolveRecipients(emailTemplate.getTo(), context, templateContext, inputParams);
        resolveCopiedRecipients(emailTemplate.getCc(), context, templateContext, inputParams);
        resolveBlindCarbonCopy(emailTemplate.getBcc(), context, templateContext, inputParams);
        resolveSubject(emailTemplate.getSubject(), context, templateContext, inputParams);
        resolveBody(emailTemplate.getBody(), context, templateContext, inputParams);
        return templateContext;
    }
}
