package struc;

public class Category {
    private String nazev, popis;
    private int categoryID;

    public Category(String nazev, String popis, int categoryID) {
        this.nazev = nazev;
        this.popis = popis;
        this.categoryID = categoryID;
    }

    public Category(String nazev, String popis) {
        this.nazev = nazev;
        this.popis = popis;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public String getPopis() {
        return popis;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    @Override
    public String toString() {
        return nazev;
    }
}
