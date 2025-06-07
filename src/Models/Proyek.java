package Models;

public class Proyek {
    public String proyekId;
    public String userId;
    public String namaPemilik;
    public String judul;
    public String deskripsi;
    public String bidang;
    public String tanggalDibuat;

    public Proyek(String proyekId, String userId, String namaPemilik, String judul, String deskripsi, String bidang, String tanggalDibuat) {
        this.proyekId = proyekId;
        this.userId = userId;
        this.namaPemilik = namaPemilik;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.bidang = bidang;
        this.tanggalDibuat = tanggalDibuat;
    }
}