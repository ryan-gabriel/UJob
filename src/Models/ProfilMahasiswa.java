package Models;

public class ProfilMahasiswa {
    private int userId;
    private String nama;
    private String pendidikan;
    private String ringkasan;
    private String pengalaman;
    private String skill;
    
    // Konstruktor yang baru
    public ProfilMahasiswa(int userId, String nama, String pendidikan, 
                           String ringkasan, String pengalaman, String skill) {
        this.userId = userId;
        this.nama = nama;
        this.pendidikan = pendidikan;
        this.ringkasan = ringkasan;
        this.pengalaman = pengalaman;
        this.skill = skill;
    }

    // Tambahkan getter untuk nama
    public String getNama() {
        return nama;
    }

    // Getter lainnya tetap sama
    public int getUserId() {
        return userId;
    }

    public String getPendidikan() {
        return pendidikan;
    }

    public String getRingkasan() {
        return ringkasan;
    }

    public String getPengalaman() {
        return pengalaman;
    }

    public String getSkill() {
        return skill;
    }
}
