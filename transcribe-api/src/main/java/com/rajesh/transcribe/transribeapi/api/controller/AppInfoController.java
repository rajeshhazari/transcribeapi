package com.rajesh.transcribe.transribeapi.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class AppInfoController {
    
    @Autowired
    private BuildProperties buildProperties;
    
    @PreAuthorize("hasAnyRole({'ROLE_ADMIN','ROLE_DEVOPS'})")
    @RequestMapping("/version")
    public @ResponseBody
    ResponseEntity<Map<String,String>> greeting(HttpServletRequest request, HttpServletResponse response) {
        Map<String,String> respMap = new ConcurrentHashMap<>();
        respMap.put("appVersion",buildProperties.getVersion());
        respMap.put("appName",buildProperties.getName());
        respMap.put("appBuildTime", buildProperties.getTime().toString());
        return ResponseEntity.ok(respMap);
    }
    
}
