package com.github.salilvnair.ccf.email.letter.context;

import com.github.salilvnair.ccf.email.core.entity.EmailTemplate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailLetterContext {
    private Integer templateId;
    private EmailTemplate emailTemplate;
}
