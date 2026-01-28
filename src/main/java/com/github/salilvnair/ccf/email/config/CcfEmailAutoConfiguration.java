package com.github.salilvnair.ccf.email.config;

import com.github.salilvnair.ccf.annotation.EnableCcfCore;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration
@AutoConfigurationPackage(basePackages = "com.github.salilvnair.ccf.email")
@ComponentScan(basePackages = "com.github.salilvnair.ccf.email")
@EntityScan(basePackages = {"com.github.salilvnair.ccf.email.core.entity", "com.github.salilvnair.ccf.email.letter.entity"})
@EnableJpaRepositories(basePackages = {"com.github.salilvnair.ccf.email.core.repository", "com.github.salilvnair.ccf.email.letter.repository"})
@EnableCcfCore
public class CcfEmailAutoConfiguration {
}