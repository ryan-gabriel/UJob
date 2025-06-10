package Models;

import java.sql.Timestamp;

public class Portofolioo {
    private int id;
    private int userId;
    private String judul;
    private String jenis;
    private String deskripsi;
    private String link;
    private Timestamp tanggalUpload;
    
    // Default constructor
    public Portofolioo() {}
    
    // Constructor dengan semua parameter
    Portofolioo(int id, int userId, String judul, String jenis, String deskripsi, String link, Timestamp tanggalUpload) {
        this.id = id;
        this.userId = userId;
        this.judul = judul;
        this.jenis = jenis;
        this.deskripsi = deskripsi;
        this.link = link;
        this.tanggalUpload = tanggalUpload;
    }
    
    // Constructor untuk insert (tanpa id dan tanggal)
    Portofolioo(int userId, String judul, String jenis, String deskripsi, String link) {
        this.userId = userId;
        this.judul = judul;
        this.jenis = jenis;
        this.deskripsi = deskripsi;
        this.link = link;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getJudul() {
        return judul;
    }
    
    public void setJudul(String judul) {
        this.judul = judul;
    }
    
    public String getJenis() {
        return jenis;
    }
    
    public void setJenis(String jenis) {
        this.jenis = jenis;
    }
    
    public String getDeskripsi() {
        return deskripsi;
    }
    
    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
    
    public String getLink() {
        return link;
    }
    
    public void setLink(String link) {
        this.link = link;
    }
    
    public Timestamp getTanggalUpload() {
        return tanggalUpload;
    }
    
    public void setTanggalUpload(Timestamp tanggalUpload) {
        this.tanggalUpload = tanggalUpload;
    }
    
    @Override
    public String toString() {
        return "Portofolioo{" +
                "id=" + id +
                ", userId=" + userId +
                ", judul='" + judul + '\'' +
                ", jenis='" + jenis + '\'' +
                ", deskripsi='" + deskripsi + '\'' +
                ", link='" + link + '\'' +
                ", tanggalUpload=" + tanggalUpload +
                '}';
    }
}