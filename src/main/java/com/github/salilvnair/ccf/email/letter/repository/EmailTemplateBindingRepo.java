package com.github.salilvnair.ccf.email.letter.repository;

import com.github.salilvnair.ccf.email.letter.entity.EmailTemplateBinding;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmailTemplateBindingRepo extends JpaRepository<EmailTemplateBinding, Long> {
    List<EmailTemplateBinding> findByTemplateIdOrderByDisplayOrderAsc(Integer templateId);
}