package org.example.entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serial;
import java.io.Serializable;

@Configuration
@ConfigurationProperties(prefix = "file-config")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileProperties implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String imgAddress;
    private String pdfAddress;
    private String imgHttpAddress;
    private String pdfHttpAddress;
}