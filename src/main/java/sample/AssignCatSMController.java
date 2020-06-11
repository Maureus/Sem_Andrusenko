package sample;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import database.DataBaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import struc.Category;
import struc.StudMat;

public class AssignCatSMController {
    private ObservableList<StudMat> smNotAssigned = FXCollections.observableArrayList();
    private ObservableList<StudMat> filteredSM = FXCollections.observableArrayList();
    private ObservableList<Category> categories = FXCollections.observableArrayList();
    private DataBaseHandler dataBaseHandler = new DataBaseHandler();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<StudMat> lvSM;

    @FXML
    private ComboBox<StudMat> cbSM;

    @FXML
    private ComboBox<Category> cbCategory;

    @FXML
    void assignSM(ActionEvent event) {
        if (cbSM.getSelectionModel().getSelectedItem()!=null){
            int selectedCatID = cbCategory.getSelectionModel().getSelectedItem().getCategoryID();
            int selectedSMID = cbSM.getSelectionModel().getSelectedItem().getStudMatID();
            String insertQuery = "insert into KAT_ST_MAT values(?, ?)";

            try {
                final PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(insertQuery);
                preparedStatement.setInt(1,selectedSMID);
                preparedStatement.setInt(2,selectedCatID);
                preparedStatement.executeQuery();
                filteredSM.add(cbSM.getSelectionModel().getSelectedItem());
                smNotAssigned.remove(cbSM.getSelectionModel().getSelectedItem());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning!");
            alert.setHeaderText(null);
            alert.setContentText("Please select study material from CB!");
            alert.showAndWait();
        }

    }

    @FXML
    void withdrawSM(ActionEvent event) {
        if (lvSM.getSelectionModel().getSelectedItem() != null){
            int subID = lvSM.getSelectionModel().getSelectedItem().getStudMatID();
            String deleteQuery = "DELETE FROM KAT_ST_MAT WHERE S_M_STUD_MAT_ID = ?";
            try {
                PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(deleteQuery);
                preparedStatement.setInt(1, subID);
                preparedStatement.execute();
                filteredSM.remove(lvSM.getSelectionModel().getSelectedItem());
                fillSMNotAssigned();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning!");
            alert.setHeaderText(null);
            alert.setContentText("Please select study material from the list!");
            alert.showAndWait();
        }
    }

    @FXML
    void initialize() {
        fillCategories();
        cbCategory.setItems(categories);
        cbCategory.getSelectionModel().selectFirst();
        fillSMNotAssigned();
        fillSMAssigned();
        cbSM.setItems(smNotAssigned);
        lvSM.setItems(filteredSM);
        cbCategory.setOnAction(event -> {
            fillSMAssigned();
        });

    }


    private void fillSMNotAssigned() {
        smNotAssigned.clear();
        String selectNotQuery = "select * from STUDIJNI_MATERIALY where STUD_MAT_ID not in(select S_M_STUD_MAT_ID from KAT_ST_MAT" +
                " where K_KAT_STUD_MAT_ID = ?)";
        try {
            final PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(selectNotQuery);
            preparedStatement.setInt(1,cbCategory.getSelectionModel().getSelectedItem().getCategoryID());
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final int studMatID = resultSet.getInt("STUD_MAT_ID");
                final String nazev = resultSet.getString("NAZEV");
                final String popis = resultSet.getString("POPIS");
                final String typSouboru = resultSet.getString("TYP_SOUBORU");
                final String vytvorenUziv = resultSet.getString("VYTVOREN_UZIV");
                final String zmenenUziv = resultSet.getString("ZMENEN_UZIV");
                final int pocetStran = resultSet.getInt("POCET_STRAN");
                final Date platnosDo = resultSet.getDate("PLATNOST_DO");
                final Date datumVytvoreni = resultSet.getDate("DATUM_VYTVORENI");
                final Date datumZmeny = resultSet.getDate("DATUM_ZMENY");


                final StudMat newSM = new StudMat(nazev, popis, typSouboru, vytvorenUziv, zmenenUziv,
                        pocetStran, studMatID, platnosDo, datumVytvoreni, datumZmeny);
                smNotAssigned.add(newSM);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillSMAssigned() {
        filteredSM.clear();
        String selectNotQuery = "select * from STUDIJNI_MATERIALY where STUD_MAT_ID in(select S_M_STUD_MAT_ID from KAT_ST_MAT" +
                " where K_KAT_STUD_MAT_ID = ?)";
        try {
            final PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(selectNotQuery);
            preparedStatement.setInt(1,cbCategory.getSelectionModel().getSelectedItem().getCategoryID());
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final int studMatID = resultSet.getInt("STUD_MAT_ID");
                final String nazev = resultSet.getString("NAZEV");
                final String popis = resultSet.getString("POPIS");
                final String typSouboru = resultSet.getString("TYP_SOUBORU");
                final String vytvorenUziv = resultSet.getString("VYTVOREN_UZIV");
                final String zmenenUziv = resultSet.getString("ZMENEN_UZIV");
                final int pocetStran = resultSet.getInt("POCET_STRAN");
                final Date platnosDo = resultSet.getDate("PLATNOST_DO");
                final Date datumVytvoreni = resultSet.getDate("DATUM_VYTVORENI");
                final Date datumZmeny = resultSet.getDate("DATUM_ZMENY");


                final StudMat newSM = new StudMat(nazev, popis, typSouboru, vytvorenUziv, zmenenUziv,
                        pocetStran, studMatID, platnosDo, datumVytvoreni, datumZmeny);
                filteredSM.add(newSM);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillCategories() {
        categories.clear();
        String selectQuery = "select * from KATEGORIE";

        try {
            final PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(selectQuery);
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final int catID = resultSet.getInt("KATEGORIE_ID");
                final String nazevCat = resultSet.getString("NAZEV");
                final String popisCat = resultSet.getString("POPIS");

                final Category newCat = new Category(nazevCat, popisCat, catID);
                categories.add(newCat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
