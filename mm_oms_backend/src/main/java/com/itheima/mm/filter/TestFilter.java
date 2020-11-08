package com.itheima.mm.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class TestFilter implements Filter {
    public void destroy() {
        log.info("###destroyFilter###");
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        log.info("###doFilter###");
        log.info("###servletPath###:{}",request.getServletPath());
        log.info("###contexttPath###:{}",request.getContextPath());
        chain.doFilter(request,response);
    }

    public void init(FilterConfig config) throws ServletException {
        log.info("###initFilter###");
    }

}
