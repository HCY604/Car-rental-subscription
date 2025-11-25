package com.demo.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 預留給未來會員API+JWT功能（目前不啟動）
 * 要啟動時會加到Spring Security Filter Chain
 */
@Component
public class JwtFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        // 目前先不驗證 Token，只是 pass-through
        // System.out.println("JWT Filter 預留："+ req.getRequestURI());

        chain.doFilter(request, response);
    }
}
