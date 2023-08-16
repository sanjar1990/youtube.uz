package com.example.service;

import com.example.entity.EmailHistoryEntity;
import com.example.entity.ProfileEntity;
import com.example.repository.EmailHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailHistoryService {
@Autowired
    private EmailHistoryRepository emailHistoryRepository;

    public void sendEmail(String text, String subject, ProfileEntity profile) {
        EmailHistoryEntity entity=new EmailHistoryEntity();
        entity.setMessage(text);
        entity.setToEmail(profile.getEmail());
        entity.setTitle(subject);
        emailHistoryRepository.save(entity);
    }
}
