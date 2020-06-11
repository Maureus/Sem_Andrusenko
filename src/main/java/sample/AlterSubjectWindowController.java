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
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.jetbrains.annotations.NotNull;
import struc.Subject;


public class AlterSubjectWindowController {
    private ObservableList<Subject> subjects = FXCollections.observableArrayList();
    private ObservableList<String> semesters = FXCollections.observableArrayList();
    private DataBaseHandler dataBaseHandler = new DataBaseHandler();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> cbSemestr;

    @FXML
    private TableColumn<Subject, String> col_zkratka;

    @FXML
    private TextField rocnikSubject;

    @FXML
    private TableColumn<Subject, String> col_semestr;

    @FXML
    private TextField bodySubject;

    @FXML
    private TableColumn<Subject, String> col_nazev;

    @FXML
    private TableColumn<Subject, String> col_rocnik;

    @FXML
    private TextField zkratkaSubject;

    @FXML
    private TableColumn<Subject, String> col_body;

    @FXML
    private TextField nazevSubject;

    @FXML
    private TableView<Subject> tvCategory;

    @FXML
    void addSubject(ActionEvent event) {
        if (!nazevSubject.getText().isEmpty() && !zkratkaSubject.getText().isEmpty()) {
            String insertQueryUsers = "insert into PREDMETY (NAZEV, ZKRATKA, SEMESTR, ROCNIK, BODY) values (?, ?, ?, ?, ?)";
            try {
                PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(insertQueryUsers);
                preparedStatement.setString(1, nazevSubject.getText().trim());
                preparedStatement.setString(2, zkratkaSubject.getText().trim());
                preparedStatement.setString(3, cbSemestr.getSelectionModel().getSelectedItem());
                preparedStatement.setString(4, rocnikSubject.getText().trim());
                preparedStatement.setString(5, bodySubject.getText().trim());
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            fillSubjects();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning!");
            alert.setHeaderText(null);
            alert.setContentText("Please enter name and zkratka of subject!");
            alert.showAndWait();
        }

    }

    @FXML
    void deleteSelected(ActionEvent event) {
        if (tvCategory.getSelectionModel().getSelectedItem() != null) {
            int subID = tvCategory.getSelectionModel().getSelectedItem().getSubjetId();
            String deleteQuery = "DELETE FROM PREDMETY WHERE PREDMET_ID = ?";
            try {
                PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(deleteQuery);
                preparedStatement.setInt(1, subID);
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            subjects.remove(tvCategory.getSelectionModel().getSelectedItem());
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning!");
            alert.setHeaderText(null);
            alert.setContentText("Please select subject from the list!");
            alert.showAndWait();
        }
    }

    @FXML
    void changeNazevCellEvent(TableColumn.@NotNull CellEditEvent edditedCell) {
        Subject selectedSub = tvCategory.getSelectionModel().getSelectedItem();
        selectedSub.setNazev(edditedCell.getNewValue().toString());
        String alterName = "UPDATE PREDMETY SET NAZEV = ? WHERE PREDMET_ID = ?";
        try {
            PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(alterName);
            preparedStatement.setString(1, selectedSub.getNazev());
            preparedStatement.setInt(2, selectedSub.getSubjetId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void changeZkratkaCellEvent(TableColumn.@NotNull CellEditEvent edditedCell) {
        Subject selectedSub = tvCategory.getSelectionModel().getSelectedItem();
        selectedSub.setSkratka(edditedCell.getNewValue().toString());
        String alterName = "UPDATE PREDMETY SET ZKRATKA = ? WHERE PREDMET_ID = ?";
        try {
            PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(alterName);
            preparedStatement.setString(1, selectedSub.getSkratka());
            preparedStatement.setInt(2, selectedSub.getSubjetId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void changeRocnikCellEvent(TableColumn.@NotNull CellEditEvent edditedCell) {
        Subject selectedSub = tvCategory.getSelectionModel().getSelectedItem();
        selectedSub.setRocnik(edditedCell.getNewValue().toString());
        String alterName = "UPDATE PREDMETY SET ROCNIK = ? WHERE PREDMET_ID = ?";
        try {
            PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(alterName);
            preparedStatement.setString(1, selectedSub.getRocnik());
            preparedStatement.setInt(2, selectedSub.getSubjetId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void changeBodyCellEvent(TableColumn.@NotNull CellEditEvent edditedCell) {
        Subject selectedSub = tvCategory.getSelectionModel().getSelectedItem();
        selectedSub.setBody(edditedCell.getNewValue().toString());
        String alterName = "UPDATE PREDMETY SET BODY = ? WHERE PREDMET_ID = ?";
        try {
            PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(alterName);
            preparedStatement.setString(1, selectedSub.getBody());
            preparedStatement.setInt(2, selectedSub.getSubjetId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void changeSemestrCellEvent(TableColumn.@NotNull CellEditEvent<Subject, String> edditedCell) {
        Subject selectedSub = tvCategory.getSelectionModel().getSelectedItem();
        selectedSub.setSemestr(edditedCell.getNewValue());
        String alterRole = "UPDATE PREDMETY SET SEMESTR = ? WHERE PREDMET_ID = ?";
        try {
            PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(alterRole);
            preparedStatement.setString(1, selectedSub.getSemestr());
            preparedStatement.setInt(2, selectedSub.getSubjetId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        semesters.addAll("ZS", "LS");
        cbSemestr.setItems(semesters);
        fillSubjects();
        tvCategory.setEditable(true);
        tvCategory.setItems(subjects);

        col_nazev.setCellValueFactory(new PropertyValueFactory<>("nazev"));
        col_nazev.setCellFactory(TextFieldTableCell.forTableColumn());

        col_body.setCellValueFactory(new PropertyValueFactory<>("body"));
        col_body.setCellFactory(TextFieldTableCell.forTableColumn());

        col_rocnik.setCellValueFactory(new PropertyValueFactory<>("rocnik"));
        col_rocnik.setCellFactory(TextFieldTableCell.forTableColumn());

        col_zkratka.setCellValueFactory(new PropertyValueFactory<>("skratka"));
        col_zkratka.setCellFactory(TextFieldTableCell.forTableColumn());

        col_semestr.setCellValueFactory(new PropertyValueFactory<>("semestr"));
        col_semestr.setCellFactory(ComboBoxTableCell.forTableColumn("ZS", "LS"));

    }

    private void fillSubjects() {
        subjects.clear();
        String selectQuery = "select * from PREDMETY";

        try {
            final PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(selectQuery);
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final int subID = resultSet.getInt("PREDMET_ID");
                final String nazevSub = resultSet.getString("NAZEV");
                final String zkratkaSub = resultSet.getString("ZKRATKA");
                final String semestrSub = resultSet.getString("SEMESTR");
                final String rocnikSub = resultSet.getString("ROCNIK");
                final String bodySub = resultSet.getString("BODY");

                final Subject newSubj = new Subject(subID, nazevSub, zkratkaSub, semestrSub, rocnikSub, bodySub);
                subjects.add(newSubj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
