package Models;

public class ProfilPerusahaan {
    private int perusahaan_id;
    private int user_id;
    private String nama;
    private String jenis_industri;
    private String alamat;
    private String email_kontak;
    private String website_resmi;
    private int jumlah_koneksi;
    private int jumlah_karyawan;
    private String deskripsi_perusahaan;
    private String kategori_produk_layanan;
    private String informasi_kontak_lainnya;
    private String deskripsi;
    private String website;

    // Constructor kosong
    public ProfilPerusahaan() {
    }

    // Constructor lengkap
    public ProfilPerusahaan(int perusahaan_id, int user_id, String nama, String jenis_industri, 
                           String alamat, String email_kontak, String website_resmi, 
                           int jumlah_koneksi, int jumlah_karyawan, String deskripsi_perusahaan,
                           String kategori_produk_layanan, String informasi_kontak_lainnya, 
                           String deskripsi, String website) {
        this.perusahaan_id = perusahaan_id;
        this.user_id = user_id;
        this.nama = nama;
        this.jenis_industri = jenis_industri;
        this.alamat = alamat;
        this.email_kontak = email_kontak;
        this.website_resmi = website_resmi;
        this.jumlah_koneksi = jumlah_koneksi;
        this.jumlah_karyawan = jumlah_karyawan;
        this.deskripsi_perusahaan = deskripsi_perusahaan;
        this.kategori_produk_layanan = kategori_produk_layanan;
        this.informasi_kontak_lainnya = informasi_kontak_lainnya;
        this.deskripsi = deskripsi;
        this.website = website;
    }

    // Constructor tanpa perusahaan_id (untuk insert baru)
    public ProfilPerusahaan(int user_id, String nama, String jenis_industri, 
                           String alamat, String email_kontak, String website_resmi, 
                           int jumlah_koneksi, int jumlah_karyawan, String deskripsi_perusahaan,
                           String kategori_produk_layanan, String informasi_kontak_lainnya, 
                           String deskripsi, String website) {
        this.user_id = user_id;
        this.nama = nama;
        this.jenis_industri = jenis_industri;
        this.alamat = alamat;
        this.email_kontak = email_kontak;
        this.website_resmi = website_resmi;
        this.jumlah_koneksi = jumlah_koneksi;
        this.jumlah_karyawan = jumlah_karyawan;
        this.deskripsi_perusahaan = deskripsi_perusahaan;
        this.kategori_produk_layanan = kategori_produk_layanan;
        this.informasi_kontak_lainnya = informasi_kontak_lainnya;
        this.deskripsi = deskripsi;
        this.website = website;
    }

    // Getter methods
    public int getPerusahaan_id() {
        return perusahaan_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getNama() {
        return nama;
    }

    public String getJenis_industri() {
        return jenis_industri;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getEmail_kontak() {
        return email_kontak;
    }

    public String getWebsite_resmi() {
        return website_resmi;
    }

    public int getJumlah_koneksi() {
        return jumlah_koneksi;
    }

    public int getJumlah_karyawan() {
        return jumlah_karyawan;
    }

    public String getDeskripsi_perusahaan() {
        return deskripsi_perusahaan;
    }

    public String getKategori_produk_layanan() {
        return kategori_produk_layanan;
    }

    public String getInformasi_kontak_lainnya() {
        return informasi_kontak_lainnya;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getWebsite() {
        return website;
    }

    // Setter methods
    public void setPerusahaan_id(int perusahaan_id) {
        this.perusahaan_id = perusahaan_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setJenis_industri(String jenis_industri) {
        this.jenis_industri = jenis_industri;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setEmail_kontak(String email_kontak) {
        this.email_kontak = email_kontak;
    }

    public void setWebsite_resmi(String website_resmi) {
        this.website_resmi = website_resmi;
    }

    public void setJumlah_koneksi(int jumlah_koneksi) {
        this.jumlah_koneksi = jumlah_koneksi;
    }

    public void setJumlah_karyawan(int jumlah_karyawan) {
        this.jumlah_karyawan = jumlah_karyawan;
    }

    public void setDeskripsi_perusahaan(String deskripsi_perusahaan) {
        this.deskripsi_perusahaan = deskripsi_perusahaan;
    }

    public void setKategori_produk_layanan(String kategori_produk_layanan) {
        this.kategori_produk_layanan = kategori_produk_layanan;
    }

    public void setInformasi_kontak_lainnya(String informasi_kontak_lainnya) {
        this.informasi_kontak_lainnya = informasi_kontak_lainnya;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    // toString method untuk debugging
    @Override
    public String toString() {
        return "ProfilPerusahaan{" +
                "perusahaan_id=" + perusahaan_id +
                ", user_id=" + user_id +
                ", nama='" + nama + '\'' +
                ", jenis_industri='" + jenis_industri + '\'' +
                ", alamat='" + alamat + '\'' +
                ", email_kontak='" + email_kontak + '\'' +
                ", website_resmi='" + website_resmi + '\'' +
                ", jumlah_koneksi=" + jumlah_koneksi +
                ", jumlah_karyawan=" + jumlah_karyawan +
                ", deskripsi_perusahaan='" + deskripsi_perusahaan + '\'' +
                ", kategori_produk_layanan='" + kategori_produk_layanan + '\'' +
                ", informasi_kontak_lainnya='" + informasi_kontak_lainnya + '\'' +
                ", deskripsi='" + deskripsi + '\'' +
                ", website='" + website + '\'' +
                '}';
    }

    // Method untuk validasi data
    public boolean isValid() {
        return nama != null && !nama.trim().isEmpty() &&
               user_id > 0;
    }

    // Method untuk mendapatkan ringkasan profil
    public String getProfileSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(nama != null ? nama : "Nama tidak diisi");
        
        if (jenis_industri != null && !jenis_industri.trim().isEmpty()) {
            summary.append(" - ").append(jenis_industri);
        }
        
        if (alamat != null && !alamat.trim().isEmpty()) {
            summary.append(" (").append(alamat).append(")");
        }
        
        return summary.toString();
    }
}
