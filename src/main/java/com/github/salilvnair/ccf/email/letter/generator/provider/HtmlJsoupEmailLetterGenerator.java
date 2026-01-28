package com.github.salilvnair.ccf.email.letter.generator.provider;

import com.github.salilvnair.ccf.email.core.entity.EmailTemplate;
import com.github.salilvnair.ccf.email.core.repository.EmailTemplateRepo;
import com.github.salilvnair.ccf.email.letter.context.EmailLetterContext;
import com.github.salilvnair.ccf.email.letter.generator.EmailLetterGenerator;
import com.github.salilvnair.ccf.email.letter.helper.HtmlToJsonCompiler;
import com.github.salilvnair.ccf.email.letter.helper.VariableBindingEngine;
import com.github.salilvnair.ccf.email.letter.model.EmailLetterResponse;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class HtmlJsoupEmailLetterGenerator implements EmailLetterGenerator {
    private final VariableBindingEngine bindingEngine;
    private final HtmlToJsonCompiler htmlCompiler;
    private final EmailTemplateRepo emailTemplateRepo;

    @Override
    public EmailLetterResponse generate(EmailLetterContext context, Map<String, Object> inputParams) throws Exception {
        EmailTemplate emailTemplate = context.getEmailTemplate();
        if(emailTemplate == null) {
            emailTemplate = emailTemplateRepo.findById(context.getTemplateId()).orElse(null);
        }
        if(emailTemplate == null) {
            throw new Exception("EmailTemplate not found.");
        }
        Map<String, Object> bindings = bindingEngine.buildBindings(emailTemplate.getId(), inputParams);
        String bodyHtml = "";
        String themeCss = "";
        if(htmlCompiler.isHtml(emailTemplate.getBody())) {
            Document html = Jsoup.parse(emailTemplate.getBody());
            bodyHtml = html.body().html();
            themeCss = html.head().select("style").html();
        }
        else {
            bodyHtml = "<span>" + emailTemplate.getBody() + "</span>";
        }

        Map<String, Object> bodyDoc = htmlCompiler.compile(bodyHtml);
        String subjectHtml = "<span>" + emailTemplate.getSubject() + "</span>";

        Map<String, Object> subjectDoc = htmlCompiler.compile(subjectHtml);

        Map<String, Object> fromDoc = htmlCompiler.compile("<span>" + emailTemplate.getFrom() + "</span>");

        Map<String, Object> toDoc = htmlCompiler.compile("<span>" + emailTemplate.getTo() + "</span>");

        Map<String, Object> ccDoc =
                emailTemplate.getCc() == null
                        ? null
                        : htmlCompiler.compile("<span>" + emailTemplate.getCc() + "</span>");

        return EmailLetterResponse
                .builder()
                .templateId(emailTemplate.getId().toString())
                .themeCss(themeCss)
                .bindings(bindings)
                .fromDoc(fromDoc)
                .from(emailTemplate.getFrom())
                .toDoc(toDoc)
                .to(emailTemplate.getTo())
                .ccDoc(ccDoc)
                .cc(emailTemplate.getCc())
                .subject(emailTemplate.getSubject())
                .subjectDoc(subjectDoc)
                .bodyDoc(bodyDoc)
                .build();
    }
}
