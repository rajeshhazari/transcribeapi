package com.c3transcribe.transribeapi.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class AppInfoController {

    /*@Autowired
    private BuildProperties buildProperties;*/

    @RequestMapping("/version")
    public @ResponseBody
    ResponseEntity<Map<String,String>> greeting(HttpServletRequest request, HttpServletResponse response) {
        Map<String,String> respMap = new ConcurrentHashMap<>();
       /* respMap.put("appVersion",buildProperties.getVersion());
        respMap.put("appName",buildProperties.getName());
        respMap.put("appBuildTime", buildProperties.getTime().toString());*/
        return ResponseEntity.ok(respMap);
    }
    
}
