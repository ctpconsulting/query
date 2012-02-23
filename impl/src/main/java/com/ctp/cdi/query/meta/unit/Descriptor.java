package com.ctp.cdi.query.meta.unit;

import java.net.URL;

import org.w3c.dom.Document;

class Descriptor {
    
    private final Document document;
    private final URL url;

    public Descriptor(Document document, URL url) {
        this.document = document;
        this.url = url;
    }

    public Document getDocument() {
        return document;
    }

    public URL getUrl() {
        return url;
    }

}