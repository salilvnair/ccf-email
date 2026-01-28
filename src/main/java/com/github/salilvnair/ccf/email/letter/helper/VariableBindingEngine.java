package com.github.salilvnair.ccf.email.letter.helper;


import com.github.salilvnair.ccf.core.constant.InputParamsKey;
import com.github.salilvnair.ccf.core.model.ContainerComponentResponse;
import com.github.salilvnair.ccf.core.model.ContainerData;
import com.github.salilvnair.ccf.email.letter.entity.EmailTemplateBinding;
import com.github.salilvnair.ccf.email.letter.repository.EmailTemplateBindingRepo;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
@RequiredArgsConstructor
public class VariableBindingEngine {

    private final EmailTemplateBindingRepo repo;
    private final ContainerDataUtil containerDataUtil;

    public Map<String, Object> buildBindings(Integer templateId, Map<String, Object> inputParams) {

        List<EmailTemplateBinding> vars = repo.findByTemplateIdOrderByDisplayOrderAsc(templateId);

        Map<String, Object> bindings = new LinkedHashMap<>();
        Map<String, ContainerData> cache = new HashMap<>();

        for (EmailTemplateBinding v : vars) {
            List<Long> fieldIds = v.getContainerFieldIdList();
            String cacheKey = v.getPageId() + ":" + v.getSectionId() + ":" + v.getContainerId();

            ContainerData container = cache.computeIfAbsent(cacheKey, k -> {
                ContainerComponentResponse resp =
                        containerDataUtil.execute(
                                (String) inputParams.get(InputParamsKey.USER_ID),
                                (String) inputParams.get(InputParamsKey.LOGGED_IN_USER_ID),
                                v.getPageId(),
                                v.getSectionId(),
                                v.getContainerId(),
                                inputParams
                        );
                return containerDataUtil.findContainer(
                        resp,
                        v.getPageId(),
                        v.getSectionId(),
                        v.getContainerId()
                );
            });

            Map<String, Object> meta = new LinkedHashMap<>();
            meta.put("type", v.getInputType());
            meta.put("required", v.isRequired());
            meta.put("editable", v.isEditable());


            if ("TEXT".equalsIgnoreCase(v.getInputType())) {

                String value = "";

                if (!container.getTableData().isEmpty()) {
                    for (var cell : container.getTableData().get(0)) {
                        if (Objects.equals(cell.getFieldId(), fieldIds.get(0))) {
                            value = cell.getFieldValue() == null
                                    ? ""
                                    : String.valueOf(cell.getFieldValue());
                            break;
                        }
                    }
                }

                meta.put("value", value);
            }

            if ("DROPDOWN".equalsIgnoreCase(v.getInputType())) {
                List<Map<String, String>> options = extractOptions(fieldIds, container);
                meta.put("options", options);
                meta.put("value", options.isEmpty() ? null : options.get(0).get("value"));
            }

            bindings.put(v.getBindingKey(), meta);
        }

        return bindings;
    }

    private static @NonNull List<Map<String, String>> extractOptions(List<Long> fieldIds, ContainerData container) {
        long valueFieldId = fieldIds.get(0);
        long labelFieldId = fieldIds.size() > 1 ? fieldIds.get(1) : valueFieldId;

        List<Map<String, String>> options = new ArrayList<>();

        for (var row : container.getTableData()) {

            String value = null;
            String label = null;

            for (var cell : row) {

                if (Objects.equals(cell.getFieldId(), valueFieldId)) {
                    value = cell.getFieldValue() == null
                            ? null
                            : String.valueOf(cell.getFieldValue());
                }

                if (Objects.equals(cell.getFieldId(), labelFieldId)) {
                    label = cell.getFieldValue() == null
                            ? null
                            : String.valueOf(cell.getFieldValue());
                }
            }

            if (value != null) {
                Map<String, String> opt = new LinkedHashMap<>();
                opt.put("value", value);
                opt.put("label", label != null ? label : value);
                options.add(opt);
            }
        }
        return options;
    }

}
