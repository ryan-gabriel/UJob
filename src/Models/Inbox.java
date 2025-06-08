package Models;

public class Inbox {
    private String isi;
    private String tanggal;

    public Inbox(String isi, String tanggal) {
        this.isi = isi;
        this.tanggal = tanggal;
    }

    public String getIsi() {
        return isi;
    }

    public String getTanggal() {
        return tanggal;
    }
}
