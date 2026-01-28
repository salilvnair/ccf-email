package com.github.salilvnair.ccf.email.core.service;

import com.github.salilvnair.ccf.email.core.context.EmailDataContext;
import com.github.salilvnair.ccf.email.core.generator.EmailGenerator;
import com.github.salilvnair.ccf.email.core.generator.factory.EmailGeneratorFactory;
import com.github.salilvnair.ccf.email.core.type.EmailGeneratorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${mail.smtp.host}")
    private String mailSmtpHost;

    @Value("${mail.smtp.port}")
    private String mailSmtpPort;

    @Value("${mail.smtp.username}")
    private String mailSmtpUsername;

    @Value("${mail.smtp.password}")
    private String mailSmtpPassword;

    @Autowired
    private EmailGeneratorFactory emailGeneratorFactory;

    @Override
    public String smtpHost() {
        return mailSmtpHost;
    }

    @Override
    public String smtpPort() {
        return mailSmtpPort;
    }

    @Override
    public String smtpUsername() {
        return mailSmtpUsername;
    }

    @Override
    public String smtpPassword() {
        return mailSmtpPassword;
    }

    @Override
    public EmailDataContext execute(EmailGeneratorType generatorType, EmailDataContext emailDataContext, Map<String, Object> inputParams) throws Exception {
        EmailGenerator emailGenerator = emailGeneratorFactory.generate(generatorType, inputParams);
        return emailGenerator.generate(emailDataContext, inputParams);
    }
}
