package Models;

import java.sql.Date;
import java.sql.Timestamp;

public class Lowongan {
    private int lowonganId;
    private int userId; // Diubah dari perusahaanId
    private String judulPekerjaan;
    private String deskripsi;
    private String kualifikasi;
    private Date tanggalPosting;
    private String status;
    private Date tanggalDeadline;
    private String jenisLowongan;
    private String gaji;
    private String lokasiKerja;
    private String durasi;
    private int jumlahLamaran;
    
    // Field tambahan dari relasi
    private String namaPerusahaan; // Dari JOIN dengan tabel user
    
    // Default constructor
    public Lowongan() {}
    
    // Constructor dengan semua parameter (termasuk namaPerusahaan)
    public Lowongan(int lowonganId, int userId, String judulPekerjaan, 
                         String deskripsi, String kualifikasi, Date tanggalPosting, 
                         String status, Date tanggalDeadline, String jenisLowongan, 
                         String gaji, String lokasiKerja, String durasi, int jumlahLamaran,
                         String namaPerusahaan) {
        this.lowonganId = lowonganId;
        this.userId = userId;
        this.judulPekerjaan = judulPekerjaan;
        this.deskripsi = deskripsi;
        this.kualifikasi = kualifikasi;
        this.tanggalPosting = tanggalPosting;
        this.status = status;
        this.tanggalDeadline = tanggalDeadline;
        this.jenisLowongan = jenisLowongan;
        this.gaji = gaji;
        this.lokasiKerja = lokasiKerja;
        this.durasi = durasi;
        this.jumlahLamaran = jumlahLamaran;
        this.namaPerusahaan = namaPerusahaan;
    }
    
    // Constructor untuk insert (tanpa lowongan_id, jumlah_lamaran, dan namaPerusahaan)
    public Lowongan(int userId, String judulPekerjaan, String deskripsi, 
                         String kualifikasi, String status, Date tanggalDeadline, 
                         String jenisLowongan, String gaji, String lokasiKerja, String durasi) {
        this.userId = userId;
        this.judulPekerjaan = judulPekerjaan;
        this.deskripsi = deskripsi;
        this.kualifikasi = kualifikasi;
        this.status = status;
        this.tanggalDeadline = tanggalDeadline;
        this.jenisLowongan = jenisLowongan;
        this.gaji = gaji;
        this.lokasiKerja = lokasiKerja;
        this.durasi = durasi;
        this.jumlahLamaran = 0; // Default 0 untuk lowongan baru
    }
    
    // Constructor untuk display (dengan namaPerusahaan tapi tanpa ID)
    public Lowongan(int userId, String judulPekerjaan, String deskripsi, 
                         String kualifikasi, String status, Date tanggalDeadline, 
                         String jenisLowongan, String gaji, String lokasiKerja, 
                         String durasi, String namaPerusahaan) {
        this.userId = userId;
        this.judulPekerjaan = judulPekerjaan;
        this.deskripsi = deskripsi;
        this.kualifikasi = kualifikasi;
        this.status = status;
        this.tanggalDeadline = tanggalDeadline;
        this.jenisLowongan = jenisLowongan;
        this.gaji = gaji;
        this.lokasiKerja = lokasiKerja;
        this.durasi = durasi;
        this.namaPerusahaan = namaPerusahaan;
        this.jumlahLamaran = 0;
    }
    
    // Getters and Setters
    public int getLowonganId() {
        return lowonganId;
    }
    
    public void setLowonganId(int lowonganId) {
        this.lowonganId = lowonganId;
    }
    
    public int getUserId() { // Diubah dari getPerusahaanId()
        return userId;
    }
    
    public void setUserId(int userId) { // Diubah dari setPerusahaanId()
        this.userId = userId;
    }
    
    // Backward compatibility - deprecated methods
    @Deprecated
    public int getPerusahaanId() {
        return userId;
    }
    
    @Deprecated
    public void setPerusahaanId(int perusahaanId) {
        this.userId = perusahaanId;
    }
    
    public String getJudulPekerjaan() {
        return judulPekerjaan;
    }
    
    public void setJudulPekerjaan(String judulPekerjaan) {
        this.judulPekerjaan = judulPekerjaan;
    }
    
    public String getDeskripsi() {
        return deskripsi;
    }
    
    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
    
    public String getKualifikasi() {
        return kualifikasi;
    }
    
    public void setKualifikasi(String kualifikasi) {
        this.kualifikasi = kualifikasi;
    }
    
    public Date getTanggalPosting() {
        return tanggalPosting;
    }
    
    public void setTanggalPosting(Date tanggalPosting) {
        this.tanggalPosting = tanggalPosting;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Date getTanggalDeadline() {
        return tanggalDeadline;
    }
    
    public void setTanggalDeadline(Date tanggalDeadline) {
        this.tanggalDeadline = tanggalDeadline;
    }
    
    public String getJenisLowongan() {
        return jenisLowongan;
    }
    
    public void setJenisLowongan(String jenisLowongan) {
        this.jenisLowongan = jenisLowongan;
    }
    
    public String getGaji() {
        return gaji;
    }
    
    public void setGaji(String gaji) {
        this.gaji = gaji;
    }
    
    public String getLokasiKerja() {
        return lokasiKerja;
    }
    
    public void setLokasiKerja(String lokasiKerja) {
        this.lokasiKerja = lokasiKerja;
    }
    
    public String getDurasi() {
        return durasi;
    }
    
    public void setDurasi(String durasi) {
        this.durasi = durasi;
    }
    
    public int getJumlahLamaran() {
        return jumlahLamaran;
    }
    
    public void setJumlahLamaran(int jumlahLamaran) {
        this.jumlahLamaran = jumlahLamaran;
    }
    
    // Getter dan Setter untuk namaPerusahaan
    public String getNamaPerusahaan() {
        return namaPerusahaan;
    }
    
    public void setNamaPerusahaan(String namaPerusahaan) {
        this.namaPerusahaan = namaPerusahaan;
    }
    
    // Method helper untuk mengecek apakah lowongan masih aktif
    public boolean isAktif() {
        return "aktif".equalsIgnoreCase(this.status);
    }
    
    // Method helper untuk mengecek apakah deadline sudah lewat
    public boolean isDeadlineLewat() {
        if (tanggalDeadline == null) return false;
        return tanggalDeadline.before(new Date(System.currentTimeMillis()));
    }
    
    // Method helper untuk mendapatkan informasi lengkap lowongan
    public String getInfoLengkap() {
        return judulPekerjaan + " - " + namaPerusahaan + " (" + lokasiKerja + ")";
    }
    
    @Override
    public String toString() {
        return "Lowongan{" +
                "lowonganId=" + lowonganId +
                ", userId=" + userId +
                ", judulPekerjaan='" + judulPekerjaan + '\'' +
                ", deskripsi='" + deskripsi + '\'' +
                ", kualifikasi='" + kualifikasi + '\'' +
                ", tanggalPosting=" + tanggalPosting +
                ", status='" + status + '\'' +
                ", tanggalDeadline=" + tanggalDeadline +
                ", jenisLowongan='" + jenisLowongan + '\'' +
                ", gaji='" + gaji + '\'' +
                ", lokasiKerja='" + lokasiKerja + '\'' +
                ", durasi='" + durasi + '\'' +
                ", jumlahLamaran=" + jumlahLamaran +
                ", namaPerusahaan='" + namaPerusahaan + '\'' +
                '}';
    }
}
