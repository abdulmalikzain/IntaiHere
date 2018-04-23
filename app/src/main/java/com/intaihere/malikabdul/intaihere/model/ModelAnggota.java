package com.intaihere.malikabdul.intaihere.model;

public class ModelAnggota {
    String id, username, email, telephone, alamat, latitude, longitude, image;

    public ModelAnggota(){}

    public ModelAnggota(String id, String username, String email, String telephone, String alamat, String latitude, String longitude, String image) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.telephone = telephone;
        this.alamat = alamat;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() { return longitude;}

    public void setLongitude(String longitude) { this.longitude = longitude; }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat= alamat;
    }

    public String getGambar() {
        return image;
    }

    public void setGambar(String image) {
        this.image = image;
    }
}
