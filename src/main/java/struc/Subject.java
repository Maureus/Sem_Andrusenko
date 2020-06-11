package struc;

public class Subject {
    private int subID;
    private String nazev;
    private String skratka;
    private String semestr;
    private String rocnik;
    private String body;

    public Subject(int subID, String nazev, String skratka, String semestr, String rocnik, String body) {
        this.subID = subID;
        this.nazev = nazev;
        this.skratka = skratka;
        this.semestr = semestr;
        this.rocnik = rocnik;
        this.body = body;
    }

    public int getSubjetId() {
        return subID;
    }

    public void setSubjetId(int subID) {
        this.subID = subID;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public String getSkratka() {
        return skratka;
    }

    public void setSkratka(String skratka) {
        this.skratka = skratka;
    }

    public String getSemestr() {
        return semestr;
    }

    public void setSemestr(String semestr) {
        this.semestr = semestr;
    }

    public String getRocnik() {
        return rocnik;
    }

    public void setRocnik(String rocnik) {
        this.rocnik = rocnik;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return  nazev;

    }
}
