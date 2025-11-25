package com.demo.util;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String path = req.getRequestURI();

        // 第 1 步：先設定 CORS 回應 Header（每個 response 都要有）
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        //  第 2 步：OPTIONS 預檢請求一定要放行（不然前端會報 CORS）⭐⭐⭐
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }

//        // 第 3 步：不需要 token 的 API 
        if (path.equals("/login") 
        	    || path.equals("/register") 
        	    || path.startsWith("/api/cars")
        	) {
        	    chain.doFilter(req, res);
        	    return;
        	}
//        if (path.equals("/login") 
//                || path.equals("/register") 
//                || path.startsWith("/api/cars")
//                || path.startsWith("/api/orders")  // 若訂單要不用 token，可以在這裡放行
//        ) {
//            chain.doFilter(req, res);
//            return;
//        }

        // 第 4 步：檢查 Authorization: Bearer token
        String header = req.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (jwtUtil.validateToken(token)) {
                chain.doFilter(req, res);
                return;
            }
        }

        // 第 5 步：token 無效（回傳 401
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.getWriter().write("Unauthorized: Invalid or missing token");
    }
}
