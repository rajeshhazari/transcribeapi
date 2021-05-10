package com.c3transcribe.transcribeapi.api.controller.sphinx.transribe;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.apache.logging.log4j.LogManager.getLogger;

@Component
public class Sphinx4TranscriptionHandlerInterceptor extends HandlerInterceptorAdapter
{
    private static final Logger logger = getLogger(Sphinx4TranscriptionHandlerInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception
    {
        logger.info("Inside preHandle!!");
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView mav)
            throws Exception
    {
        logger.info("Inside postHandle!!");
    }
}
