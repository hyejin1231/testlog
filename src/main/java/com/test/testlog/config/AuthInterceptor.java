package com.test.testlog.config;

import com.test.testlog.exception.UnAuthorized;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 2024.02.19
 * 섹션6. API 인증 시작 (1)
 * : Interceptor 사용해보기
 * 우리 프로젝트에서는 AuthInterceptor를 사용하지 않을 예정
 */
@Deprecated
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info(">> preHandle");

        String accessToken = request.getParameter("accessToken");
        if (accessToken != null && !accessToken.isEmpty()) {
            request.setAttribute("userName", accessToken);
            return true;
        }

        throw new UnAuthorized();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info(">> postHandle");
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info(">> afterCompletion");
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}