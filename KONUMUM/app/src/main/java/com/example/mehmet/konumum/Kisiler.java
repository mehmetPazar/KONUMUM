package com.example.mehmet.konumum;

import android.graphics.Bitmap;
import android.net.Uri;

import java.net.URL;

public class Kisiler {

    private String kullaniciadi;
    private String sifre;
    private String adsoyad;
    private String mail;
    private String resim;
    private String enlem;
    private String boylam;

    public String getTarih() {
        return Tarih;
    }

    public void setTarih(String tarih) {
        Tarih = tarih;
    }

    private String Tarih;

    public String getEnlem() {
        return enlem;
    }

    public void setEnlem(String enlem) {
        this.enlem = enlem;
    }

    public String getBoylam() {
        return boylam;
    }

    public void setBoylam(String boylam) {
        this.boylam = boylam;
    }

    public Kisiler(String kullaniciadi, String sifre, String adsoyad, String mail, String resim, String enlem, String boylam,String tarih) {
        this.kullaniciadi = kullaniciadi;
        this.sifre = sifre;
        this.adsoyad = adsoyad;
        this.mail = mail;
        this.resim = resim;
        this.enlem = enlem;
        this.boylam = boylam;
        this.Tarih=tarih;
    }

    public Kisiler() {
        this.kullaniciadi = "";
        this.sifre = "";
        this.adsoyad = "";
        this.mail = "";
        this.resim = "";
        this.enlem="";
        this.boylam="";
        this.Tarih="";
    }

    public String getKullaniciadi() {
        return kullaniciadi;
    }

    public String getSifre() {
        return sifre;
    }

    public String getAdsoyad() {
        return adsoyad;
    }

    public String getMail() {
        return mail;
    }

    public String getResim() {
        return resim;
    }

    public void setKullaniciadi(String kullaniciadi) {
        this.kullaniciadi = kullaniciadi;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public void setAdsoyad(String adsoyad) {
        this.adsoyad = adsoyad;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }
}
