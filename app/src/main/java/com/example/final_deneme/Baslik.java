package com.example.final_deneme;

public class Baslik {
    private String baslik;
    private String aciklama;

    // Boş yapıcı metot Firebase tarafından kullanılmak üzere gereklidir.
    public Baslik() {
    }

    public Baslik(String baslik, String aciklama) {
        this.baslik = baslik;
        this.aciklama = aciklama;
    }

    public String getBaslik() {
        return baslik;
    }

    public String getAciklama() {
        return aciklama;
    }
}

