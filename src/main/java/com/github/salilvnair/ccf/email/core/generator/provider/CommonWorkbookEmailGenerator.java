package com.github.salilvnair.ccf.email.core.generator.provider;


import com.github.salilvnair.ccf.core.model.ContainerData;
import com.github.salilvnair.ccf.email.core.context.EmailDataContext;
import com.github.salilvnair.ccf.email.core.type.EmailGeneratorType;
import com.github.salilvnair.ccf.email.core.template.factory.TemplateResolverFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component(value = EmailGeneratorType.Value.COMMON_WORKBOOK_EMAIL)
@RequiredArgsConstructor
public class CommonWorkbookEmailGenerator extends BaseEmailGenerator {

    private final TemplateResolverFactory templateResolverFactory;

    @Override
    public EmailDataContext generate(EmailDataContext emailDataContext, Map<String, Object> inputParams) throws Exception {
        Integer templateId = emailDataContext.getTemplateId();
        resolveEmailFromTemplate(templateResolverFactory, emailDataContext, inputParams, templateId);
        return emailDataContext;
    }

    protected Map<String, Object> generateTemplateVariables(EmailDataContext emailDataContext, Map<String, Object> inputParams) {
        if(CollectionUtils.isEmpty(emailDataContext.getContainerData())) {
            return super.generateTemplateVariables(emailDataContext, inputParams);
        }
        List<Integer> containerIds = CollectionUtils.isEmpty(emailDataContext.getContainerIds()) ? emailDataContext.getContainerData().stream().map(ContainerData::getContainerId).collect(Collectors.toList()) : emailDataContext.getContainerIds();
        emailDataContext.setContainerIds(containerIds);
        return generateContainerTemplateVariables(emailDataContext, inputParams);
    }


}
