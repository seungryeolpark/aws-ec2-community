package com.example.demo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserLoginFailHandler implements AuthenticationFailureHandler {

    private final MessageSource messageSource;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String errMessage = exception.getMessage();

        if (exception instanceof AuthenticationServiceException) {
            errMessage = messageSource.getMessage("login.error.ase", null, null);
        } else if (exception instanceof BadCredentialsException) {
            errMessage = messageSource.getMessage("login.error.bce", null, null);
        } else if (exception instanceof LockedException) {
            errMessage = messageSource.getMessage("login.error.le", null, null);
        } else if (exception instanceof DisabledException) {
            errMessage = messageSource.getMessage("login.error.de", null, null);
        } else if (exception instanceof AccountExpiredException) {
            errMessage = messageSource.getMessage("login.error.aee", null, null);
        } else if (exception instanceof CredentialsExpiredException) {
            errMessage = messageSource.getMessage("login.error.cee", null, null);
        }

        request.setAttribute("errorMessage", errMessage);

        request.getRequestDispatcher("/login").forward(request, response);
    }
}
