package com.github.salilvnair.ccf.email.core.template.resolver.provider;

import com.github.salilvnair.ccf.email.core.entity.MessageTemplate;
import com.github.salilvnair.ccf.email.core.repository.MessageTemplateInfoRepo;
import com.github.salilvnair.ccf.email.core.template.context.MessageTemplateContext;
import com.github.salilvnair.ccf.email.core.template.resolver.core.BaseEmailTemplateResolver;
import com.github.salilvnair.ccf.email.core.template.type.TemplateResolverType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import java.util.Map;

@Service(TemplateResolverType.Value.MESSAGE)
@RequiredArgsConstructor
public class MessageTemplateResolver extends BaseEmailTemplateResolver<MessageTemplateContext> {

    private final MessageTemplateInfoRepo messageTemplateInfoRepo;

    @Override
    public MessageTemplateContext resolve(MessageTemplateContext templateContext, Map<String, Object> inputParams)  throws Exception  {
        MessageTemplate messageTemplateInfo = messageTemplateInfoRepo.findById(templateContext.getTemplateId()).orElse(null);
        if(messageTemplateInfo == null) {
            throw new Exception("MessageTemplateInfo not found.");
        }
        templateContext.setActive(messageTemplateInfo.active());
        Context context = new Context();
        context.setVariables(templateContext.getTemplateVariables());
        String messageTemplate = messageTemplateInfo.getMessage();
        if(messageTemplate != null) {
            String resolvedMessage =  templateEngine(TemplateMode.HTML).process(messageTemplate, context);
            templateContext.setMessage(resolvedMessage);
        }
        return templateContext;
    }
}
