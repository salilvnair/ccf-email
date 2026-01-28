package com.github.salilvnair.ccf.email.core.template.resolver.core;

import java.util.Map;

public interface TemplateResolver<T> {

    T resolve(T context, Map<String, Object> inputParams)  throws Exception ;

}
