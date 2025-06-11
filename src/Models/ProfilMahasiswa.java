package Models;

public class ProfilMahasiswa {
    private int userId;
    private String nama;
    private String ringkasan;
    private String pendidikan;
    private String pengalaman;
    private String skill;

    // ================== PERBAIKAN DI AREA INI ==================
    // Constructor yang urutannya sudah diperbaiki.
    // Urutan yang benar adalah: userId, nama, ringkasan, pendidikan, pengalaman, skill.
    public ProfilMahasiswa(int userId, String nama, String ringkasan, String pendidikan, String pengalaman, String skill) {
        this.userId = userId;
        this.nama = nama;
        this.ringkasan = ringkasan;
        this.pendidikan = pendidikan;
        this.pengalaman = pengalaman;
        this.skill = skill;
    }
    // ==========================================================

    public ProfilMahasiswa() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // Getters
    public int getUserId() { return userId; }
    public String getNama() { return nama; }
    public String getRingkasan() { return ringkasan; }
    public String getPendidikan() { return pendidikan; }
    public String getPengalaman() { return pengalaman; }
    public String getSkill() { return skill; }

    // Setters
    public void setRingkasan(String ringkasan) { this.ringkasan = ringkasan; }
    public void setPendidikan(String pendidikan) { this.pendidikan = pendidikan; }
    public void setPengalaman(String pengalaman) { this.pengalaman = pengalaman; }
    public void setSkill(String skill) { this.skill = skill; }

    public void setNama(String john_Doe) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}