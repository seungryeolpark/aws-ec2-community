package com.example.demo.service.etc;

import com.example.demo.repository.EmailCertificationDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailCertificationService {

    private final EmailCertificationDao emailCertificationDao;

    public void save(String email, String certificationNumber) {
        if (emailCertificationDao.hasKey(email)) emailCertificationDao.removeEmailCertification(email);

        emailCertificationDao.createEmailCertification(email, certificationNumber);
    }

    public String findByEmail(String email) {
        if (email.isEmpty()) return null;
        if (emailCertificationDao.hasKey(email)) return emailCertificationDao.getEmailCertification(email);

        return null;
    }

    public void remove(String email) {
        emailCertificationDao.removeEmailCertification(email);
    }
}
