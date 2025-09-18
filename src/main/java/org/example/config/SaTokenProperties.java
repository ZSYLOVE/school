package org.example.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

//编写配文件类
@Data
@Configuration
@ConfigurationProperties(prefix = "satoken-config")
@AllArgsConstructor
@NoArgsConstructor
public class SaTokenProperties implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private List<String> whitelist;
    private String verifyKey = "zzz";

}