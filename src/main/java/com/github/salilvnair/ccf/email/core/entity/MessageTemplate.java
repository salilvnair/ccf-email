package com.github.salilvnair.ccf.email.core.entity;

import com.github.salilvnair.ccf.core.constant.StringConstant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CCF_MESSAGE_TEMPLATE")
public class MessageTemplate {
    @Id
    @Column(name = "TEMPLATE_ID")
    private Integer id;

    @Column(name = "TEMPLATE_NAME")
    private String templateName;

    @Column(name = "TEMPLATE_DESC")
    private String templateDescription;

    @Column(name = "MESSAGE_TYPE")
    private String messageType;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "ACTIVE")
    private String active;


    public boolean active() {
        return StringConstant.Y.equalsIgnoreCase(active);
    }
}
