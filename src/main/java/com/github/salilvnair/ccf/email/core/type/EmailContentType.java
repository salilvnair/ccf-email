package com.github.salilvnair.ccf.email.core.type;

import java.util.Arrays;
import java.util.Optional;

public enum EmailContentType {
    TEXT("txt", "application/text"),
    XLSX("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");


    private final String extension;
    private final String contentType;

    EmailContentType(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public String contentType() {
        return contentType;
    }


    public String extension() {
        return extension;
    }

    public static EmailContentType findByExtension(String extension) {
        Optional<EmailContentType> typeEnum = Arrays.stream(EmailContentType.values())
                .filter(comp -> comp.extension().equals(extension)).findFirst();
        return typeEnum.orElse(TEXT);
    }
}
