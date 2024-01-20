package com.example.final_deneme;

import java.util.List;

public class Fotograf {
    private String fotografId;
    private String fotografUrl;
    private List<String> seciliBasliklar;

    public Fotograf() {
    }

    public Fotograf(String fotografId, String fotografUrl, List<String> seciliBasliklar) {
        this.fotografId = fotografId;
        this.fotografUrl = fotografUrl;
        this.seciliBasliklar = seciliBasliklar;
    }

    public String getFotografId() {
        return fotografId;
    }

    public void setFotografId(String fotografId) {
        this.fotografId = fotografId;
    }

    public String getFotografUrl() {
        return fotografUrl;
    }

    public void setFotografUrl(String fotografUrl) {
        this.fotografUrl = fotografUrl;
    }

    public List<String> getSeciliBasliklar() {
        return seciliBasliklar;
    }

    public void setSeciliBasliklar(List<String> seciliBasliklar) {
        this.seciliBasliklar = seciliBasliklar;
    }
}

