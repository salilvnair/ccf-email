package com.github.salilvnair.ccf.email.letter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailLetterResponse {
    private String templateId;
    private String themeCss;
    private Object bindings;
    private Object fromDoc;
    private String from;
    private Object toDoc;
    private String to;
    private Object ccDoc;
    private String cc;
    private String subject;
    private Object subjectDoc;
    private Object bodyDoc;
    private List<Map<String, Object>> attachments;
}
