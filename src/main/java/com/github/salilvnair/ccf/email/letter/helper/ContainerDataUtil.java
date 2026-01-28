package com.github.salilvnair.ccf.email.letter.helper;

import com.github.salilvnair.ccf.core.model.*;
import com.github.salilvnair.ccf.core.model.type.RequestType;
import com.github.salilvnair.ccf.service.CcfCoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ContainerDataUtil {
    private final CcfCoreService ccfCoreService;

    public ContainerComponentResponse execute( String userId, String loggedInUserId, int pageId, int sectionId, int containerId, Map<String, Object> inputParams) {
        PageInfoRequest p = new PageInfoRequest();
        p.setUserId(userId);
        p.setLoggedInUserId(loggedInUserId);
        p.setPageId(pageId);
        p.setSectionId(sectionId);
        p.setContainerId(containerId);
        p.setInputParams(inputParams);

        ContainerComponentRequest req = new ContainerComponentRequest();
        req.setPageInfo(List.of(p));
        req.setRequestTypes(List.of(RequestType.CONTAINER));

        return ccfCoreService.execute(req);
    }

    public ContainerData findContainer( ContainerComponentResponse resp, int pageId, int sectionId, int containerId ) {
        PageDataResponse page = resp.getPages().get(pageId);
        if (page == null) throw new IllegalStateException("Page not found");

        SectionData section = page.getSections().get(sectionId);
        if (section == null) throw new IllegalStateException("Section not found");

        return section.getData().stream()
                .filter(c -> c.getContainerId().equals(containerId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Container not found"));
    }
}
