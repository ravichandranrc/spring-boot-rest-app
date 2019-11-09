package com.ts;

import com.ts.service.NodeHolderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    @Value("${data.filePath}")
    String dataFilePath;

    @Bean(name = "nodeHolderService")
    NodeHolderService nodeHolderService() throws Exception {
        return new NodeHolderService(dataFilePath);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
