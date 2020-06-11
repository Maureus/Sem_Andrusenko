package struc;

import database.DataBaseHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

public class StudMat {
    private String nazev, popis, typSouboru, vytvorenUziv, zmenenUziv;
    private Blob soubor;
    private int pocetStran, predmetID, studMatID;
    private Date platnostDo, datumVytvareni, datumZmeny;
    private Button download, comments;

    public StudMat() {
    }

    public StudMat(String nazev, String popis, String vytvorenUziv, String zmenenUziv, Blob soubor,
                   int pocetStran, int predmetID, int studMatID, Date platnostDo, Date datumVytvareni,
                   Date datumZmeny, Button download, Button comments) {
        this.nazev = nazev;
        this.popis = popis;
        this.vytvorenUziv = vytvorenUziv;
        this.zmenenUziv = zmenenUziv;
        this.soubor = soubor;
        this.pocetStran = pocetStran;
        this.predmetID = predmetID;
        this.studMatID = studMatID;
        this.platnostDo = platnostDo;
        this.datumVytvareni = datumVytvareni;
        this.datumZmeny = datumZmeny;
        this.download = download;
        this.comments = comments;

        download.setOnAction(event -> {
            String querry = "select SOUBOR, TYP_SOUBORU, NAZEV from STUDIJNI_MATERIALY where STUD_MAT_ID like ?";
            try {
                DataBaseHandler dbConnection = new DataBaseHandler();
                final PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(querry);
                preparedStatement.setInt(1, this.studMatID);
                final ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {

                    final Blob blob = resultSet.getBlob("SOUBOR");
                    final String typ = resultSet.getString("TYP_SOUBORU");
                    final String fName = resultSet.getString("NAZEV");
                    BufferedInputStream is = new BufferedInputStream(blob.getBinaryStream());
                    String home = System.getProperty("user.home");
                    String fileName = fName.trim() + "." + typ.trim();
                    File fileDir = new File(home + "/Downloads/");
                    if (fileDir.exists()) {
                        File file = new File(home + "/Downloads/", fileName);
                        FileOutputStream fos = new FileOutputStream(file);
                        byte[] buffer = new byte[2048];
                        int r = 0;
                        while ((r = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, r);
                        }
                        fos.flush();
                        fos.close();
                        is.close();
                        blob.free();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success!");
                        alert.setHeaderText(null);
                        alert.setContentText("File was saved to dir: " + home + "/Downloads/");
                        alert.showAndWait();
                    } else {
                        File file = new File(home, fileName);
                        FileOutputStream fos = new FileOutputStream(file);
                        byte[] buffer = new byte[2048];
                        int r = 0;
                        while ((r = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, r);
                        }
                        fos.flush();
                        fos.close();
                        is.close();
                        blob.free();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success!");
                        alert.setHeaderText(null);
                        alert.setContentText("File was saved to dir: " + home);
                        alert.showAndWait();
                    }

                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }

        });

        comments.setOnAction(event -> {
            OpenWindow.openWindow(event, "/fxmlfiles/OpenCommentsWindow.fxml", getClass(), false, true);
        });
    }

    public StudMat(String nazev, String popis, String typSouboru, String vytvorenUziv, String zmenenUziv,
                   int pocetStran, int studMatID, Date platnostDo, Date datumVytvareni, Date datumZmeny) {
        this.nazev = nazev;
        this.popis = popis;
        this.typSouboru = typSouboru;
        this.vytvorenUziv = vytvorenUziv;
        this.zmenenUziv = zmenenUziv;
        this.pocetStran = pocetStran;
        this.studMatID = studMatID;
        this.platnostDo = platnostDo;
        this.datumVytvareni = datumVytvareni;
        this.datumZmeny = datumZmeny;
    }

    public Button getComments() {
        return comments;
    }

    public void setComments(Button comments) {
        this.comments = comments;
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

    public String getTypSouboru() {
        return typSouboru;
    }

    public void setTypSouboru(String typSouboru) {
        this.typSouboru = typSouboru;
    }

    public String getVytvorenUziv() {
        return vytvorenUziv;
    }

    public void setVytvorenUziv(String vytvorenUziv) {
        this.vytvorenUziv = vytvorenUziv;
    }

    public String getZmenenUziv() {
        return zmenenUziv;
    }

    public void setZmenenUziv(String zmenenUziv) {
        this.zmenenUziv = zmenenUziv;
    }

    public Blob getSoubor() {
        return soubor;
    }

    public void setSoubor(Blob soubor) {
        this.soubor = soubor;
    }

    public int getPocetStran() {
        return pocetStran;
    }

    public void setPocetStran(int pocetStran) {
        this.pocetStran = pocetStran;
    }

    public int getPredmetID() {
        return predmetID;
    }

    public void setPredmetID(int predmetID) {
        this.predmetID = predmetID;
    }

    public int getStudMatID() {
        return studMatID;
    }

    public void setStudMatID(int studMatID) {
        this.studMatID = studMatID;
    }

    public Date getPlatnostDo() {
        return platnostDo;
    }

    public void setPlatnostDo(Date platnostDo) {
        this.platnostDo = platnostDo;
    }

    public Date getDatumVytvareni() {
        return datumVytvareni;
    }

    public void setDatumVytvareni(Date datumVytvareni) {
        this.datumVytvareni = datumVytvareni;
    }

    public Date getDatumZmeny() {
        return datumZmeny;
    }

    public void setDatumZmeny(Date datumZmeny) {
        this.datumZmeny = datumZmeny;
    }

    public Button getDownload() {
        return download;
    }

    public void setDownload(Button download) {
        this.download = download;
    }

    @Override
    public String toString() {
        return "StudMat - " +studMatID+
                " - nazev: '" + nazev +"."+ typSouboru +
                ", pocetStran: " + pocetStran +
                ", popis: '" + popis + '\'' +
                ", vytvorenUziv: '" + vytvorenUziv + " " +datumVytvareni+
                ", zmenenUziv: '" + zmenenUziv + " " +datumZmeny+
                ", platnostDo: " + platnostDo+";";
    }
}
