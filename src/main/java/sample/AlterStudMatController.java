package sample;

import database.DataBaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import struc.OpenWindow;
import struc.StudMat;
import struc.Subject;

import java.net.URL;
import java.sql.*;
import java.util.Iterator;
import java.util.ResourceBundle;

public class AlterStudMatController {
    public static final DataBaseHandler dbConnection = new DataBaseHandler();
    public static ObservableList<StudMat> studMats = FXCollections.observableArrayList();
    public static ObservableList<Subject> subjects = FXCollections.observableArrayList();
    private static ObservableList<StudMat> filteredList = FXCollections.observableArrayList();
    public static StudMat selectedSM;
    public static int cbID;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button edditBtn;

    @FXML
    private Button addBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button kvizBtn;


    @FXML
    private TableColumn<StudMat, String> col_vu;

    @FXML
    private TableColumn<StudMat, Integer> col_pocet;

    @FXML
    private TableColumn<StudMat, Integer> col_id;

    @FXML
    private TableColumn<StudMat, String> col_zu;

    @FXML
    private TableColumn<StudMat, Integer> col_predmet;

    @FXML
    private TableColumn<StudMat, String> col_popis;

    @FXML
    private TableColumn<StudMat, String> col_name;

    @FXML
    private TableColumn<StudMat, String> col_dz;

    @FXML
    private TableColumn<StudMat, String> col_platnost;

    @FXML
    private TableColumn<StudMat, String> col_dv;

    @FXML
    private TableView<StudMat> tableSM;

    @FXML
    private TableColumn<StudMat, Button> col_download;

    @FXML
    private TableColumn<StudMat, Button> col_comments;

    @FXML
    private ComboBox<Subject> cbPredmet;

    @FXML
    void changeFilter(ActionEvent event) {
        filterTVList(cbPredmet.getSelectionModel().getSelectedItem().getSubjetId());
    }

    public static void filterTVList(int subjectID) {
        filteredList.clear();
        if (subjectID == 0) {
            Iterator<StudMat> it1 = studMats.iterator();
            while (it1.hasNext()){
                StudMat temp = it1.next();
                filteredList.add(temp);
            }
        } else {
            Iterator<StudMat> it = studMats.iterator();
            while (it.hasNext()) {
                StudMat temp = it.next();
                if (subjectID == temp.getPredmetID()) {
                    filteredList.add(temp);
                }
            }
        }
    }


    @FXML
    void createKviz(ActionEvent event) {

    }

    @FXML
    void addSM(ActionEvent event) {
        OpenWindow.openWindow(event, "/fxmlfiles/AddStudMat.fxml", getClass(), false, false);
    }

    @FXML
    void edditSM(ActionEvent event) {
        if (tableSM.getSelectionModel().getSelectedItem() != null) {
            cbID = cbPredmet.getSelectionModel().getSelectedItem().getSubjetId();
            selectedSM = tableSM.getSelectionModel().getSelectedItem();
            OpenWindow.openWindow(event, "/fxmlfiles/EditStudMat.fxml", getClass(), false, false);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning!");
            alert.setHeaderText(null);
            alert.setContentText("Please select Stu Mat from table view!");
            alert.showAndWait();
        }


    }

    @FXML
    void deleteSM(ActionEvent event) {
        int userID = tableSM.getSelectionModel().getSelectedItem().getStudMatID();
        String deleteQuery = "DELETE FROM STUDIJNI_MATERIALY WHERE STUD_MAT_ID = ?";
        try {
            PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(deleteQuery);
            preparedStatement.setInt(1, userID);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        studMats.remove(tableSM.getSelectionModel().getSelectedItem());
        filteredList.remove(tableSM.getSelectionModel().getSelectedItem());

    }

    @FXML
    void initialize() {
        fillStuMats();
        fillSubjects();
        cbPredmet.setItems(subjects);
        cbPredmet.getSelectionModel().selectFirst();

        filterTVList(cbPredmet.getSelectionModel().getSelectedItem().getSubjetId());

        tableSM.setEditable(true);
        tableSM.setItems(filteredList);

        if (Controller.logedInUser.getUserID() == 2){
            addBtn.setDisable(true);
            edditBtn.setDisable(true);
            deleteBtn.setDisable(true);
            kvizBtn.setDisable(true);
        }


        col_id.setCellValueFactory(new PropertyValueFactory<>("studMatID"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("nazev"));
        col_platnost.setCellValueFactory(new PropertyValueFactory<>("platnostDo"));
        col_popis.setCellValueFactory(new PropertyValueFactory<>("popis"));
        col_pocet.setCellValueFactory(new PropertyValueFactory<>("pocetStran"));
        col_dv.setCellValueFactory(new PropertyValueFactory<>("datumVytvareni"));
        col_vu.setCellValueFactory(new PropertyValueFactory<>("vytvorenUziv"));
        col_dz.setCellValueFactory(new PropertyValueFactory<>("datumZmeny"));
        col_zu.setCellValueFactory(new PropertyValueFactory<>("zmenenUziv"));
        col_predmet.setCellValueFactory(new PropertyValueFactory<>("predmetID"));
        col_download.setCellValueFactory(new PropertyValueFactory<>("download"));
        col_comments.setCellValueFactory(new PropertyValueFactory<>("comments"));
    }

    private void fillSubjects() {
        subjects.clear();
        String selectQuery = "select * from PREDMETY";
        try {
            Subject allSubject = new Subject(0, "All Subjects", null, null, null, null);
            subjects.add(allSubject);
            final PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(selectQuery);
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final int subjectID = resultSet.getInt("PREDMET_ID");
                final String nazev = resultSet.getString("NAZEV");
                final String zkratka = resultSet.getString("ZKRATKA");
                final String semestr = resultSet.getString("SEMESTR");
                final String rocnik = resultSet.getString("ROCNIK");
                final String body = resultSet.getString("BODY");

                final Subject newSubj = new Subject(subjectID, nazev, zkratka, semestr, rocnik, body);
                subjects.add(newSubj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void fillStuMats() {
        studMats.clear();
        String selectQuery = "select * from STUDIJNI_MATERIALY";
        try {
            final PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(selectQuery);
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final int studMatID = resultSet.getInt("STUD_MAT_ID");
                final String nazev = resultSet.getString("NAZEV");
                final String popis = resultSet.getString("POPIS");
                final Blob soubor = resultSet.getBlob("SOUBOR");
                final String vytvorenUziv = resultSet.getString("VYTVOREN_UZIV");
                final String zmenenUziv = resultSet.getString("ZMENEN_UZIV");
                final int pocetStran = resultSet.getInt("POCET_STRAN");
                final int predmetID = resultSet.getInt("PREDMETY_PREDMET_ID");
                final Date platnosDo = resultSet.getDate("PLATNOST_DO");
                final Date datumVytvoreni = resultSet.getDate("DATUM_VYTVORENI");
                final Date datumZmeny = resultSet.getDate("DATUM_ZMENY");


                final StudMat newSM = new StudMat(nazev, popis, vytvorenUziv, zmenenUziv, soubor
                        , pocetStran, predmetID, studMatID, platnosDo, datumVytvoreni,
                        datumZmeny, new Button("download"), new Button("comments"));
                studMats.add(newSM);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
