package Models;

public class ProfilMahasiswa {
    private int profil_id;
    private int userId;
    private String nama;
    private String ringkasan;
    private String pendidikan;
    private String pengalaman;
    private String skill;

    public ProfilMahasiswa() {
    }

    // Getters
    public int getProfil_id() { return profil_id; }
    public int getUserId() { return userId; }
    public String getNama() { return nama; }
    public String getRingkasan() { return ringkasan; }
    public String getPendidikan() { return pendidikan; }
    public String getPengalaman() { return pengalaman; }
    public String getSkill() { return skill; }

    // Setters
    public void setProfil_id(int profil_id) { this.profil_id = profil_id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setNama(String nama) { this.nama = nama; }
    public void setRingkasan(String ringkasan) { this.ringkasan = ringkasan; }
    public void setPendidikan(String pendidikan) { this.pendidikan = pendidikan; }
    public void setPengalaman(String pengalaman) { this.pengalaman = pengalaman; }
    public void setSkill(String skill) { this.skill = skill; }
}