package com.github.salilvnair.ccf.email.core.service;

import com.github.salilvnair.ccf.email.core.type.EmailScenarioType;
import java.util.List;

public interface EmailScenario {
    EmailScenarioType type();
    int templateId();
    Integer sectionId();
    Integer containerId();
    default List<Integer> containerIds() {
        return List.of(containerId());
    }
    String scenarioDescription();
}
