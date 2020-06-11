package struc;

public class StudMatCB {
    private int studMatID;
    private String nazev, typSouboru;

    public StudMatCB(int studMatID, String nazev, String typSouboru) {
        this.studMatID = studMatID;
        this.nazev = nazev;
        this.typSouboru = typSouboru;
    }

    public int getStudMatID() {
        return studMatID;
    }

    public void setStudMatID(int studMatID) {
        this.studMatID = studMatID;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public String getTypSouboru() {
        return typSouboru;
    }

    public void setTypSouboru(String typSouboru) {
        this.typSouboru = typSouboru;
    }

    @Override
    public String toString() {
        return nazev+"."+ typSouboru;
    }
}
