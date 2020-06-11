package sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Iterator;
import java.util.ResourceBundle;

import database.DataBaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import struc.Subject;

public class EditStudMatController {
    private ObservableList<Subject> editSubjects = FXCollections.observableArrayList();
    private DataBaseHandler dataBaseHandler = new DataBaseHandler();
    private File selectedFile;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private DatePicker datePlatnost;

    @FXML
    private Button chooseFile;

    @FXML
    private TextField nazev;

    @FXML
    private TextArea popis;

    @FXML
    private ChoiceBox<Subject> cbPredmety;

    @FXML
    private TextField pocetStran;

    @FXML
    private AnchorPane pane;

    @FXML
    private ChoiceBox<?> cbKategorie;

    @FXML
    void editStudMat(ActionEvent event) throws FileNotFoundException {
        int pStran = Integer.parseInt(pocetStran.getText().trim());
        int predmetID = cbPredmety.getSelectionModel().getSelectedItem().getSubjetId();
        java.util.Date date = java.util.Date.from(datePlatnost.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        String zmenil = Controller.logedInUser.getName()+" "+Controller.logedInUser.getSurname();

        if ( selectedFile!=null ){
            String nazevF = selectedFile.getName();
            String[] nazevTyp = nazevF.split("[.]", 2);
            FileInputStream fos = new FileInputStream(selectedFile);


            String editQuery = "UPDATE STUDIJNI_MATERIALY SET NAZEV = ?, SOUBOR = ?, TYP_SOUBORU = ?, POPIS  = ?," +
                    "PLATNOST_DO = ?, PREDMETY_PREDMET_ID = ?, POCET_STRAN = ?, ZMENEN_UZIV = ?," +
                    "DATUM_ZMENY = SYSDATE WHERE STUD_MAT_ID = ?";

            try {
                final PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(editQuery);
                preparedStatement.setString(1, nazev.getText().trim());
                preparedStatement.setBlob(2, fos, selectedFile.length());
                preparedStatement.setString(3, nazevTyp[1]);
                preparedStatement.setString(4, popis.getText().trim());
                preparedStatement.setDate(5, sqlDate);
                preparedStatement.setInt(6, predmetID);
                preparedStatement.setInt(7, pStran);
                preparedStatement.setString(8, zmenil);
                preparedStatement.setInt(9, AlterStudMatController.selectedSM.getStudMatID());
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ((Node)event.getSource()).getScene().getWindow().hide();
        }else{
            String editQuery = "UPDATE STUDIJNI_MATERIALY SET NAZEV = ?, POPIS  = ?," +
                    "PLATNOST_DO = ?,PREDMETY_PREDMET_ID = ?, POCET_STRAN = ?, ZMENEN_UZIV = ?," +
                    "DATUM_ZMENY = SYSDATE WHERE STUD_MAT_ID = ?";

            try {
                final PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(editQuery);
                preparedStatement.setString(1, nazev.getText().trim());
                preparedStatement.setString(2, popis.getText().trim());
                preparedStatement.setDate(3, sqlDate);
                preparedStatement.setInt(4, predmetID);
                preparedStatement.setInt(5, pStran);
                preparedStatement.setString(6, zmenil);
                preparedStatement.setInt(7, AlterStudMatController.selectedSM.getStudMatID());
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ((Node)event.getSource()).getScene().getWindow().hide();
        }


        AlterStudMatController.fillStuMats();
        AlterStudMatController.filterTVList(AlterStudMatController.cbID);
    }

    @FXML
    void initialize() {
        fillSubjects();
        int indexStudMat = AlterStudMatController.studMats.indexOf(AlterStudMatController.selectedSM);
        cbPredmety.setItems(editSubjects);
        Iterator<Subject> it  = editSubjects.iterator();
        while (it.hasNext()){
            Subject temp = it.next();
            if (temp.getSubjetId() == AlterStudMatController.selectedSM.getPredmetID()){
                cbPredmety.getSelectionModel().select(editSubjects.indexOf(temp));
            }
        }
        popis.setText(AlterStudMatController.studMats.get(indexStudMat).getPopis());
        nazev.setText(AlterStudMatController.studMats.get(indexStudMat).getNazev());
        pocetStran.setText(String.valueOf(AlterStudMatController.studMats.get(indexStudMat).getPocetStran()));
        LocalDate date = AlterStudMatController.studMats.get(indexStudMat).getPlatnostDo().toLocalDate();
        datePlatnost.setValue(date);

        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text, PDF Files", "*.txt","*.pdf")
        );

        chooseFile.setOnAction(e -> {
            selectedFile = fileChooser.showOpenDialog(null);
        });
    }

    private void fillSubjects() {
        editSubjects.clear();
        String selectQuery = "select * from PREDMETY";
        try {
            final PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(selectQuery);
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final int subjectID = resultSet.getInt("PREDMET_ID");
                final String nazev = resultSet.getString("NAZEV");
                final String zkratka = resultSet.getString("ZKRATKA");
                final String semestr = resultSet.getString("SEMESTR");
                final String rocnik = resultSet.getString("ROCNIK");
                final String body = resultSet.getString("BODY");

                final Subject newSubj = new Subject(subjectID, nazev, zkratka, semestr, rocnik, body);
                editSubjects.add(newSubj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
