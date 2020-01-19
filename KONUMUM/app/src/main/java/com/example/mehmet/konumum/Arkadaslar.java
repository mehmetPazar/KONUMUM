package com.example.mehmet.konumum;

public class Arkadaslar {
    public Arkadaslar(String arkadas) {
        this.arkadas = arkadas;
    }
    public Arkadaslar() {
        this.arkadas = "";
    }
    public String getArkadas() {
        return arkadas;
    }

    public void setArkadas(String arkadas) {
        this.arkadas = arkadas;
    }

    String arkadas;
}
