package com.github.salilvnair.ccf.email.core.repository;

import com.github.salilvnair.ccf.email.core.entity.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EmailTemplateRepo extends JpaRepository<EmailTemplate, Integer> {
    Optional<EmailTemplate> findById(Integer templateId);
}
