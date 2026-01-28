package com.github.salilvnair.ccf.email.letter.generator;

import com.github.salilvnair.ccf.email.letter.context.EmailLetterContext;
import com.github.salilvnair.ccf.email.letter.model.EmailLetterResponse;
import java.util.Map;

public interface EmailLetterGenerator {

    EmailLetterResponse generate(EmailLetterContext context, Map<String, Object> inputParams) throws Exception;

}
