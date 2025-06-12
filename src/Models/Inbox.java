package Models;

public class Inbox {
    private String isi;
    private String tanggal;

    // Default constructor
    public Inbox() {}

    // Constructor dengan parameter
    public Inbox(String isi, String tanggal) {
        this.isi = isi;
        this.tanggal = tanggal;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}