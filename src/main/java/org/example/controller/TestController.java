package org.example.controller;

import cn.dev33.satoken.util.SaResult;
import lombok.RequiredArgsConstructor;
import org.example.util.FilesUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final FilesUtils filesUtils;
    @PostMapping("/upload")
    public SaResult testFileUpload(@RequestParam("file") MultipartFile file) {
        Map<String, Object> upload = filesUtils.upload(file);
         return SaResult.ok(upload.get("fileName").toString());
    }
}
