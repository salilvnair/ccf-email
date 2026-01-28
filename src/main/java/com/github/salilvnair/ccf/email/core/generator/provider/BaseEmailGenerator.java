package com.github.salilvnair.ccf.email.core.generator.provider;

import com.github.salilvnair.ccf.core.data.type.ContainerType;
import com.github.salilvnair.ccf.core.model.ContainerData;
import com.github.salilvnair.ccf.core.model.SectionField;
import com.github.salilvnair.ccf.email.core.context.EmailDataContext;
import com.github.salilvnair.ccf.email.core.generator.EmailGenerator;
import com.github.salilvnair.ccf.email.core.generator.model.ContainerTemplate;
import com.github.salilvnair.ccf.email.core.template.context.EmailTemplateContext;
import com.github.salilvnair.ccf.email.core.template.factory.TemplateResolverFactory;
import com.github.salilvnair.ccf.email.core.template.resolver.core.TemplateResolver;
import com.github.salilvnair.ccf.email.core.template.type.TemplateResolverType;
import org.springframework.util.CollectionUtils;
import java.util.*;
import java.util.stream.Collectors;

public abstract class BaseEmailGenerator implements EmailGenerator {
    protected Map<String, Object> generateTemplateVariables(EmailDataContext emailDataContext, Map<String, Object> inputParams) {
        return inputParams;
    }

    protected void resolveEmailFromTemplate(TemplateResolverFactory templateResolverFactory, EmailDataContext emailDataContext, Map<String, Object> inputParams, Integer templateId) throws Exception {
        EmailTemplateContext templateContext = EmailTemplateContext
                .builder()
                .templateId(templateId)
                .templateVariables(generateTemplateVariables(emailDataContext, inputParams))
                .build();

        TemplateResolver<EmailTemplateContext> templateResolver = templateResolverFactory.generate(EmailTemplateContext.class, TemplateResolverType.EMAIL, inputParams);
        templateContext = templateResolver.resolve(templateContext, inputParams);

        emailDataContext.setFrom(templateContext.getResolvedSenders());
        emailDataContext.setTo(templateContext.getResolvedRecipients());
        emailDataContext.setCc(templateContext.getResolvedCarbonCopy());
        emailDataContext.setSubject(templateContext.getResolvedSubject());
        emailDataContext.setBody(templateContext.getResolvedBody());
        emailDataContext.setActive(templateContext.isActive());
    }


    protected Map<String, Object> addMultiInfoContainerTemplateVariables(EmailDataContext emailDataContext, Map<String, Object> inputParams) {
        Map<String, Object> templateVariables = new HashMap<>(inputParams);

        List<ContainerData> containerData = emailDataContext.getContainerData();
        if(CollectionUtils.isEmpty(containerData)) {
            return templateVariables;
        }
        Integer containerId = emailDataContext.getContainerId();

        addMultiInfoContainerTemplateVariables(inputParams, containerData, containerId, templateVariables);
        templateVariables.put("hasAttachments", emailDataContext.isHasAttachments());
        return templateVariables;
    }

    protected Map<String, Object> addDataTableContainerTemplateVariables(EmailDataContext emailDataContext, Map<String, Object> inputParams) {
        Map<String, Object> templateVariables = new HashMap<>(inputParams);

        List<ContainerData> containerData = emailDataContext.getContainerData();
        if(CollectionUtils.isEmpty(containerData)) {
            return templateVariables;
        }
        Integer containerId = emailDataContext.getContainerId();

        addDataTableContainerTemplateVariables(inputParams, containerData, containerId, templateVariables);
        templateVariables.put("hasAttachments", emailDataContext.isHasAttachments());
        return templateVariables;
    }


    protected static void addMultiInfoContainerTemplateVariables(Map<String, Object> inputParams, List<ContainerData> containerData, Integer containerId, Map<String, Object> templateVariables) {
        Map<String, Object> headerKeyedValues = new LinkedHashMap<>();
        ContainerData eligibleContainer = containerData.stream().filter(cd -> Objects.equals(cd.getContainerId(), containerId)).findFirst().orElse(null);
        if(eligibleContainer != null) {
            eligibleContainer.getData().forEach(sectionField -> {
                if(sectionField.isVisible()) {
                    headerKeyedValues.put(sectionField.getFieldDisplayName(), sectionField.getFieldValue());
                }
                templateVariables.put(sectionField.getFieldDisplayName(), sectionField.getFieldValue());
            });
            templateVariables.putAll(inputParams);
            templateVariables.put("headers", headerKeyedValues.keySet());
            templateVariables.put("data", headerKeyedValues);
        }
    }

    protected Map<String, Object> addMultiInfoMultiContainerTemplateVariables(EmailDataContext emailDataContext, Map<String, Object> inputParams) {
        return addMultiContainerTemplateVariables(emailDataContext, inputParams, false);
    }

    protected Map<String, Object> addDataTableMultiContainerTemplateVariables(EmailDataContext emailDataContext, Map<String, Object> inputParams) {
        return addMultiContainerTemplateVariables(emailDataContext, inputParams, true);
    }

    protected Map<String, Object> addMultiContainerTemplateVariables(EmailDataContext emailDataContext, Map<String, Object> inputParams, boolean isDataTable) {
        Map<String, Object> templateVariables = new HashMap<>(inputParams);
        List<ContainerData> containerData = emailDataContext.getContainerData();
        if(CollectionUtils.isEmpty(containerData)) {
            return templateVariables;
        }
        List<Integer> containerIds = emailDataContext.getContainerIds();
        if(isDataTable) {
            addDataTableMultiContainerTemplateVariables(inputParams, containerData, containerIds, templateVariables);
        }
        else {
            addMultiInfoMultiContainerTemplateVariables(inputParams, containerData, containerIds, templateVariables);
        }
        templateVariables.put("hasAttachments", emailDataContext.isHasAttachments());
        return templateVariables;

    }

    protected Map<String, Object> generateContainerTemplateVariables(EmailDataContext emailDataContext, Map<String, Object> inputParams) {
        Map<String, Object> containerIdKeyedContainerTemplateData = new LinkedHashMap<>();
        Map<String, Object> templateVariables = new HashMap<>(inputParams);
        List<ContainerData> containerData = emailDataContext.getContainerData();
        if(CollectionUtils.isEmpty(containerData)) {
            return templateVariables;
        }
        for (ContainerData eachContainerData : containerData) {
            ContainerTemplate containerTemplateData;
            if(ContainerType.type(eachContainerData.getContainerType()).isTable()) {
                containerTemplateData = generateDataTableContainerTemplate(eachContainerData);
            }
            else {
                containerTemplateData = generateMultiInfoContainerTemplate(eachContainerData);
            }
            containerIdKeyedContainerTemplateData.put(eachContainerData.getContainerId() + "", containerTemplateData);
        }
        templateVariables.putAll(inputParams);
        templateVariables.put("containers", containerIdKeyedContainerTemplateData);
        templateVariables.put("containerIds", containerIdKeyedContainerTemplateData.keySet());
        return templateVariables;
    }

    protected static void addMultiInfoMultiContainerTemplateVariables(Map<String, Object> inputParams, List<ContainerData> containerData, List<Integer> containerIds, Map<String, Object> templateVariables) {
        Map<String, Object> containerIdKeyedContainerTemplateData = generateMultiInfoMultiContainerTemplateVariables(inputParams, containerData, containerIds, templateVariables);
        templateVariables.putAll(inputParams);
        templateVariables.put("containers", containerIdKeyedContainerTemplateData);
        templateVariables.put("containerIds", containerIdKeyedContainerTemplateData.keySet());
    }

    protected static Map<String, Object> generateMultiInfoMultiContainerTemplateVariables(Map<String, Object> inputParams, List<ContainerData> containerData, List<Integer> containerIds, Map<String, Object> templateVariables) {
        Map<String, Object> containerIdKeyedContainerTemplateData = new LinkedHashMap<>();
        containerIds.forEach(containerId -> {
            ContainerData eligibleContainer = containerData.stream().filter(cd -> Objects.equals(cd.getContainerId(), containerId)).findFirst().orElse(null);
            ContainerTemplate containerTemplate = generateMultiInfoContainerTemplate(eligibleContainer);
            containerIdKeyedContainerTemplateData.put(containerId + "", containerTemplate);
        });
        return containerIdKeyedContainerTemplateData;
    }

    protected static ContainerTemplate generateMultiInfoContainerTemplate(ContainerData containerData) {
        ContainerTemplate containerTemplate = new ContainerTemplate();
        Map<String, Object> headerKeyedValues = new LinkedHashMap<>();
        if(containerData != null) {
            containerData.getData().forEach(sectionField -> {
                if(sectionField.isVisible()) {
                    headerKeyedValues.put(sectionField.getFieldDisplayName(), sectionField.getFieldValue());
                }
            });
            containerTemplate.setContainerDisplayName(containerData.getContainerDisplayName());
            containerTemplate.setContainerType(containerData.getContainerType());
            containerTemplate.setHeaders(headerKeyedValues.keySet());
            containerTemplate.setData(headerKeyedValues);
            containerTemplate.setContainsData(!headerKeyedValues.isEmpty());
            containerTemplate.setContainerId(containerData.getContainerId());
        }
        return containerTemplate;
    }

    protected static void addDataTableMultiContainerTemplateVariables(Map<String, Object> inputParams, List<ContainerData> containerData, List<Integer> containerIds, Map<String, Object> templateVariables) {
        Map<String, Object> containerIdKeyedContainerTemplateData = generateDataTableMultiContainerTemplateVariables(inputParams, containerData, containerIds, templateVariables);
        templateVariables.putAll(inputParams);
        templateVariables.put("containers", containerIdKeyedContainerTemplateData);
        templateVariables.put("containerIds", containerIdKeyedContainerTemplateData.keySet());
    }

    protected static Map<String, Object> generateDataTableMultiContainerTemplateVariables(Map<String, Object> inputParams, List<ContainerData> containerData, List<Integer> containerIds, Map<String, Object> templateVariables) {
        Map<String, Object> containerIdKeyedContainerTemplateData = new LinkedHashMap<>();
        containerIds.forEach(containerId -> {
            ContainerTemplate containerTemplate = generateDataTableContainerTemplate(containerData, containerId);
            containerIdKeyedContainerTemplateData.put(containerId + "", containerTemplate);
        });
        return containerIdKeyedContainerTemplateData;
    }

    private static ContainerTemplate generateDataTableContainerTemplate(List<ContainerData> containerData, Integer containerId) {
        ContainerData eligibleContainer = containerData.stream().filter(cd -> Objects.equals(cd.getContainerId(), containerId)).findFirst().orElse(null);
        return generateDataTableContainerTemplate(eligibleContainer);
    }

    private static ContainerTemplate generateDataTableContainerTemplate(ContainerData containerData) {
        ContainerTemplate containerTemplate = new ContainerTemplate();
        if(containerData != null) {
            Set<String> headers = containerData.getTableHeaders().stream().map(SectionField::getFieldDisplayName).collect(Collectors.toCollection(LinkedHashSet::new));
            List<Map<String, Object>> tableData = new ArrayList<>();
            containerData.getTableData().forEach(sectionFields -> {
                Map<String, Object> headerKeyedValues = new LinkedHashMap<>();
                sectionFields.forEach(sectionField -> {
                    if(sectionField.isVisible()) {
                        headerKeyedValues.put(sectionField.getFieldDisplayName(), sectionField.getFieldValue());
                    }
                });
                tableData.add(headerKeyedValues);
            });
            containerTemplate.setTableData(tableData);
            containerTemplate.setContainsData(!CollectionUtils.isEmpty(tableData));
            containerTemplate.setContainerDisplayName(containerData.getContainerDisplayName());
            containerTemplate.setContainerType(containerData.getContainerType());
            containerTemplate.setHeaders(headers);
            containerTemplate.setContainerId(containerData.getContainerId());
        }
        return containerTemplate;
    }

    protected static void addDataTableContainerTemplateVariables(Map<String, Object> inputParams, List<ContainerData> containerData, Integer containerId, Map<String, Object> templateVariables) {
        ContainerTemplate containerTemplate = generateDataTableContainerTemplate(containerData, containerId);;
        templateVariables.putAll(inputParams);
        templateVariables.put("container", containerTemplate);
    }

}
