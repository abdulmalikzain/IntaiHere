package com.intaihere.malikabdul.intaihere.model;

public class ModelStatus {

    String  username, waktu, image, status, tujuan, foto_status;

    public ModelStatus(){}



    public ModelStatus(String username, String waktu, String image, String status, String tujuan, String foto_status) {

        this.username = username;
        this.waktu = waktu;
        this.image = image;
        this.status = status;
        this.tujuan = tujuan;
        this.foto_status = foto_status;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getGambar() {
        return image;
    }

    public void setGambar(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTujuan() {
        return tujuan;
    }

    public void setTujuan(String tujuan) {
        this.tujuan = tujuan;
    }

    public String getFoto_status() {
        return foto_status;
    }

    public void setFoto_status(String foto_status) {
        this.foto_status = foto_status;
    }
}
