package com.github.salilvnair.ccf.email.core.repository;

import com.github.salilvnair.ccf.email.core.entity.MessageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageTemplateInfoRepo extends JpaRepository<MessageTemplate, Integer> {
}
