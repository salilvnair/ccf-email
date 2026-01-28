package com.github.salilvnair.ccf.email.core.entity;

import com.github.salilvnair.ccf.core.constant.StringConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CCF_EMAIL_TEMPLATE")
public class EmailTemplate {
    @Id
    @Column(name = "TEMPLATE_ID")
    private Integer id;

    @Column(name = "TEMPLATE_NAME")
    private String templateName;

    @Column(name = "TEMPLATE_DESC")
    private String templateDescription;

    @Column(name = "SENDER")
    private String from;

    @Column(name = "RECIPIENT")
    private String to;

    @Column(name = "CC")
    private String cc;

    @Column(name = "BCC")
    private String bcc;

    @Column(name = "SUBJECT")
    private String subject;

    @Column(name = "EMAIL_BODY", columnDefinition = "text")
    private String body;

    @Column(name = "ACTIVE")
    private String active;


    public boolean active() {
        return StringConstant.Y.equalsIgnoreCase(active);
    }

}
