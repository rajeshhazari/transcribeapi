package com.c3transcribe.transcribeapi.api.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class ApiMultipartProperties extends MultipartProperties {
    
    //TODO as of now this is hardcode, this can be managed by properties
    private List<String> supportedMimeTypes = Collections.unmodifiableList(Arrays.asList("avi"));
}
