package com.cs.apac.hack.signingorder;

import com.cs.apac.hack.signingorder.service.DocumentService;
import com.cs.apac.hack.signingorder.service.DocumentServiceImpl;
import com.cs.apac.hack.signingorder.service.DocumentSigner;
import com.cs.apac.hack.signingorder.service.PdfSigner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AppConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    public DocumentSigner getPdfSigner() {
        return new PdfSigner();
    }

    @Bean
    public DocumentService getDocumentService() {
        return new DocumentServiceImpl(environment.getProperty("document-service.url"));
    }
}
