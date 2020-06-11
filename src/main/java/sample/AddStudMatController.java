package sample;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.time.ZoneId;
import java.util.ResourceBundle;

import database.DataBaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import struc.StudMat;
import struc.Subject;

public class AddStudMatController {
    private File selectedFile;
    private DataBaseHandler dataBaseHandler = new DataBaseHandler();


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button chooseFile;

    @FXML
    private DatePicker datePlatnost;

    @FXML
    private TextArea popis;

    @FXML
    private ChoiceBox<Subject> cbPredmety;

    @FXML
    private ChoiceBox<?> cbKategorie;

    @FXML
    private TextField pocetStran;

    @FXML
    private AnchorPane pane;

    @FXML
    void addNewStudMat(ActionEvent event) throws FileNotFoundException {
        String nazev = selectedFile.getName();
        String[] nazevTyp = nazev.split("[.]", 2);
        String vytvorenUziv = Controller.logedInUser.getName() +" "+ Controller.logedInUser.getSurname();
        int pStran = Integer.parseInt(pocetStran.getText().trim());
        int predmetID = cbPredmety.getSelectionModel().getSelectedItem().getSubjetId();
        FileInputStream fos = new FileInputStream(selectedFile);
        java.util.Date date = java.util.Date.from(datePlatnost.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());


        String query = "INSERT INTO STUDIJNI_MATERIALY(NAZEV, SOUBOR, TYP_SOUBORU, POPIS, PLATNOST_DO, VYTVOREN_UZIV," +
                "PREDMETY_PREDMET_ID, POCET_STRAN) VALUES(?,?,?,?,?,?,?,?)";

        try {
            final PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(query);
            preparedStatement.setString(1, nazevTyp[0]);
            preparedStatement.setBlob(2, fos, selectedFile.length());
            preparedStatement.setString(3, nazevTyp[1]);
            preparedStatement.setString(4, popis.getText().trim());
            preparedStatement.setDate(5, sqlDate);
            preparedStatement.setString(6, vytvorenUziv);
            preparedStatement.setInt(7, predmetID);
            preparedStatement.setInt(8, pStran);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        AlterStudMatController.fillStuMats();
        AlterStudMatController.filterTVList(AlterStudMatController.cbID);
        ((Node)event.getSource()).getScene().getWindow().hide();

    }

    @FXML
    void initialize() {
        cbPredmety.setItems(AlterStudMatController.subjects);
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text, PDF Files", "*.txt","*.pdf")
        );

        chooseFile.setOnAction(e -> {
            selectedFile = fileChooser.showOpenDialog(null);
        });

    }
}
