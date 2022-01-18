package com.example.demo.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@RequiredArgsConstructor
@Repository
public class EmailCertificationDao {

    private final int LIMIT_TIME = 1;

    private final StringRedisTemplate stringRedisTemplate;

    public void createEmailCertification(String email, String certificationNumber) {
        stringRedisTemplate.opsForValue()
                .set(email, certificationNumber, Duration.ofHours(LIMIT_TIME));
    }

    public String getEmailCertification(String email) {
        return stringRedisTemplate.opsForValue().get(email);
    }

    public void removeEmailCertification(String email) {
        stringRedisTemplate.delete(email);
    }

    public boolean hasKey(String email) {
        return stringRedisTemplate.hasKey(email);
    }
}
