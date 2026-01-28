package com.github.salilvnair.ccf.email.core.generator;

import com.github.salilvnair.ccf.email.core.context.EmailDataContext;
import java.util.Map;

public interface EmailGenerator {
    EmailDataContext generate(EmailDataContext emailDataContext, Map<String, Object> inputParams) throws Exception;
}
