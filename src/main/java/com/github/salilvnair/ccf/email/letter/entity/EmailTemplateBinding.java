package com.github.salilvnair.ccf.email.letter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "CCF_EMAIL_TEMPLATE_BINDING")
public class EmailTemplateBinding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_id", nullable = false)
    private Integer templateId;

    @Column(name = "binding_key", nullable = false)
    private String bindingKey;

    @Column(name = "input_type", nullable = false)
    private String inputType; // TEXT | DROPDOWN

    @Column(name = "page_id", nullable = false)
    private Integer pageId;

    @Column(name = "section_id", nullable = false)
    private Integer sectionId;

    @Column(name = "container_id", nullable = false)
    private Integer containerId;

    @Column(name = "container_field_ids", nullable = false)
    private String containerFieldIds;

    @Column(name = "required", nullable = false)
    private boolean required;

    @Column(name = "editable", nullable = false)
    private boolean editable;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;


    @Transient
    public List<Long> getContainerFieldIdList() {
        return Arrays.stream(containerFieldIds.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::valueOf)
                .toList();
    }
}
