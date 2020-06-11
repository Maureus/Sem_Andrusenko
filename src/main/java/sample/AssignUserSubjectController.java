package sample;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import database.DataBaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import struc.Subject;
import struc.User;

public class AssignUserSubjectController {
    private ObservableList<Subject> subjectsNotAssigned = FXCollections.observableArrayList();
    private ObservableList<Subject> filteredSubjects = FXCollections.observableArrayList();
    private ObservableList<User> users = FXCollections.observableArrayList();
    private DataBaseHandler dataBaseHandler = new DataBaseHandler();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<Subject> lvSubjects;

    @FXML
    private ComboBox<Subject> cbSubject;

    @FXML
    private ComboBox<User> cbUser;

    @FXML
    void assignSubject(ActionEvent event) {
        int selectedUserID = cbUser.getSelectionModel().getSelectedItem().getUserID();
        int selectedSubjID = cbSubject.getSelectionModel().getSelectedItem().getSubjetId();
        String insertQuery = "insert into PREDMETY_UZIVATELE values(?, ?)";

        try {
            final PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(insertQuery);
            preparedStatement.setInt(1,selectedUserID);
            preparedStatement.setInt(2,selectedSubjID);
            preparedStatement.executeQuery();
            filteredSubjects.add(cbSubject.getSelectionModel().getSelectedItem());
            subjectsNotAssigned.remove(cbSubject.getSelectionModel().getSelectedItem());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void withdrawSubject(ActionEvent event) {
        if (lvSubjects.getSelectionModel().getSelectedItem() != null){
            int subID = lvSubjects.getSelectionModel().getSelectedItem().getSubjetId();
            String deleteQuery = "DELETE FROM PREDMETY_UZIVATELE WHERE PREDMETY_PREDMET_ID = ?";
            try {
                PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(deleteQuery);
                preparedStatement.setInt(1, subID);
                preparedStatement.execute();
                filteredSubjects.remove(lvSubjects.getSelectionModel().getSelectedItem());
                fillSubjectsNotAssigned();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning!");
            alert.setHeaderText(null);
            alert.setContentText("Please select subject from the list!");
            alert.showAndWait();
        }

    }

    @FXML
    void initialize() {
        fillUsers();
        cbUser.setItems(users);
        cbUser.getSelectionModel().selectFirst();
        fillSubjectsNotAssigned();
        fillSubjectsAssigned();
        cbSubject.setItems(subjectsNotAssigned);
        lvSubjects.setItems(filteredSubjects);
        cbUser.setOnAction(event -> {
            fillSubjectsAssigned();
        });
    }

    private void fillSubjectsNotAssigned() {
        subjectsNotAssigned.clear();
        String selectNotQuery = "select * from PREDMETY where PREDMET_ID not in(select PREDMETY_PREDMET_ID from PREDMETY_UZIVATELE" +
                " where UZIVATELE_UZIVATEL_ID = ?)";

        try {
            final PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(selectNotQuery);
            preparedStatement.setInt(1,cbUser.getSelectionModel().getSelectedItem().getUserID());
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final int subID = resultSet.getInt("PREDMET_ID");
                final String nazevSub = resultSet.getString("NAZEV");
                final String zkratkaSub = resultSet.getString("ZKRATKA");
                final String semestrSub = resultSet.getString("SEMESTR");
                final String rocnikSub = resultSet.getString("ROCNIK");
                final String bodySub = resultSet.getString("BODY");

                final Subject newSubj = new Subject(subID, nazevSub, zkratkaSub, semestrSub, rocnikSub, bodySub);
                subjectsNotAssigned.add(newSubj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillSubjectsAssigned() {
        filteredSubjects.clear();
        String selectQuery = "select * from PREDMETY where PREDMET_ID in(select PREDMETY_PREDMET_ID from PREDMETY_UZIVATELE" +
                " where UZIVATELE_UZIVATEL_ID = ?)";

        try {
            final PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(selectQuery);
            preparedStatement.setInt(1,cbUser.getSelectionModel().getSelectedItem().getUserID());
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final int subID = resultSet.getInt("PREDMET_ID");
                final String nazevSub = resultSet.getString("NAZEV");
                final String zkratkaSub = resultSet.getString("ZKRATKA");
                final String semestrSub = resultSet.getString("SEMESTR");
                final String rocnikSub = resultSet.getString("ROCNIK");
                final String bodySub = resultSet.getString("BODY");

                final Subject newSubj = new Subject(subID, nazevSub, zkratkaSub, semestrSub, rocnikSub, bodySub);
                filteredSubjects.add(newSubj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillUsers() {
        users.clear();
        String selectQueryUsers = "SELECT * FROM ST58211.UZIVATELE";
        try {
            final PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(selectQueryUsers);
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final int userID = resultSet.getInt("UZIVATEL_ID");
                final String name = resultSet.getString("JMENO");
                final String surname = resultSet.getString("PRIJMENI");
                final String password = resultSet.getString("HESLO");
                final String email = resultSet.getString("EMAIL");
                final String telephone = resultSet.getString("PHONE");
                final String login = resultSet.getString("LOGIN");
                final int role = resultSet.getInt("ROLE_ROLE_ID");
                final User newUs = new User(userID, name, surname, login, password, email, telephone, role);
                users.add(newUs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}