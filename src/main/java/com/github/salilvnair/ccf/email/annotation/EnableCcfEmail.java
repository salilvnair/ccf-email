package com.github.salilvnair.ccf.email.annotation;

import com.github.salilvnair.ccf.email.config.CcfEmailAutoConfiguration;
import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(CcfEmailAutoConfiguration.class)
public @interface EnableCcfEmail {
}
