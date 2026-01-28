package com.github.salilvnair.ccf.email.core.context;


import com.github.salilvnair.ccf.core.model.ContainerData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.mail.internet.MimeBodyPart;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailDataContext {
    private Integer containerId;
    private List<Integer> containerIds;
    private Integer templateId;
    private List<ContainerData> containerData;
    private String from;
    private String to;
    private String cc;
    private String bcc;
    private String subject;
    private String body;
    private boolean hasAttachments;
    private Set<MimeBodyPart> attachments;
    private boolean active;
}
