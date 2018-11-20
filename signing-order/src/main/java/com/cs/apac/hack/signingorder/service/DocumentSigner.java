package com.cs.apac.hack.signingorder.service;

public interface DocumentSigner {

    DocumentService.Document sign(byte[] signature, String signer, byte[] pdfDoc);

}
