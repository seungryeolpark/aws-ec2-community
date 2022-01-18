package com.example.demo.service.etc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class IpService {
    /**
     * Client ip 반환 메서드
     * @param request Client HttpServletRequest
     * @return ip
     */
    public String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        log.info(">>>> X-FORWARDED-FOR : " + ip);

        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
            log.info(">>>> Proxy-Client-IP : " + ip);
        }

        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            log.info(">>>> WL-Proxy-Client-IP : " + ip);
        }

        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            log.info(">>>> HTTP_CLIENT_IP : " + ip);
        }

        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            log.info(">>>> HTTP_X_FORWARDED_FOR : " + ip);
        }

        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        log.info(">>>> Result : IP Address : " + ip);
        String[] temps = ip.split("\\.");
        return temps[0] + "." + temps[temps.length-1];
    }

    public String deleteClientIP(String nickname) {
        int i = 0;
        int pre = 0;

        while (i != -1) {
            pre = i;
            i = nickname.indexOf('(', i+1);
        }

        return nickname.substring(0, pre);
    }
}
