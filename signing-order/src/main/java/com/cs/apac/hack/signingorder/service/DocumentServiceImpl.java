package com.cs.apac.hack.signingorder.service;

import com.google.common.collect.Lists;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class DocumentServiceImpl implements DocumentService {

    private final String documentServiceUrl;

    public DocumentServiceImpl(String documentServiceUrl) {
        this.documentServiceUrl = documentServiceUrl;
    }

    @Override
    public Document getDocument(String docId) {
        String docUri = UriComponentsBuilder.fromHttpUrl(documentServiceUrl).path("/{docId}")
            .buildAndExpand(docId).toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_PDF));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<byte[]> response = new RestTemplate().exchange(docUri, HttpMethod.GET, entity, byte[].class);

        Document doc = new Document();
        doc.setDoc(response.getBody());
        doc.setDocId(docId);

        return doc;
    }

    @Override
    public String storeDocument(byte[] doc) {
        String uploadUri = UriComponentsBuilder.fromHttpUrl(documentServiceUrl).toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Lists.newArrayList(MediaType.MULTIPART_FORM_DATA));

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", doc);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = new RestTemplate().postForEntity(uploadUri, requestEntity, String.class);

        return response.getBody();
    }
}
