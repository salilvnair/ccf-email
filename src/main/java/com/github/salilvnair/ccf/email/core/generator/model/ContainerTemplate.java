package com.github.salilvnair.ccf.email.core.generator.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class ContainerTemplate {
    private Integer containerId;
    private String containerType;
    private String containerDisplayName;
    private Set<String> headers;
    private Map<String, Object> data;
    private List<Map<String, Object>> tableData;
    private boolean containsData;
}
