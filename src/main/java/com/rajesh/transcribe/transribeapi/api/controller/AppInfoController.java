package com.rajesh.transcribe.transribeapi.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AppInfoController {
    
    @Autowired
    Environment env;
    
    
    @RequestMapping("/version")
    public @ResponseBody
    ResponseEntity<String> greeting(HttpServletRequest request, HttpServletResponse response) {
        env.getProperty("info.app.version");
        return ResponseEntity.ok(env.getProperty("info.app.version"));
    }
    
}
