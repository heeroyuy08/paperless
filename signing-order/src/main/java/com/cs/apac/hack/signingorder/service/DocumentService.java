package com.cs.apac.hack.signingorder.service;

import com.google.common.base.MoreObjects;

public interface DocumentService {

    Document getDocument(String docId);

    String storeDocument(byte[] doc);

    class Document {
        private String docId;
        private byte[] doc;

        public String getDocId() {
            return docId;
        }

        public void setDocId(String docId) {
            this.docId = docId;
        }

        public byte[] getDoc() {
            return doc;
        }

        public void setDoc(byte[] doc) {
            this.doc = doc;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                .add("docId", docId)
                .toString();
        }
    }
}
