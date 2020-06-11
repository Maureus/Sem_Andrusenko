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
import javafx.scene.control.TextArea;
import struc.Comment;
import struc.StudMatCB;


public class OpenCommentsWindowController {
    private ObservableList<Comment> comments = FXCollections.observableArrayList();
    private ObservableList<StudMatCB> studMatCBS = FXCollections.observableArrayList();
    private DataBaseHandler dataBaseHandler = new DataBaseHandler();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea taComment;

    @FXML
    private ComboBox<StudMatCB> cbSM;

    @FXML
    private ListView<Comment> lvComments;

    @FXML
    void post(ActionEvent event) {
        if (!taComment.getText().isEmpty()){
            String insertQuery = "insert into KOMENTAR(OBSAH, S_M_STUD_MAT_ID,USER_LOGIN) values(?,?,?)";
            try {
                final PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(insertQuery);
                preparedStatement.setString(1,taComment.getText().trim());
                preparedStatement.setInt(2,cbSM.getSelectionModel().getSelectedItem().getStudMatID());
                preparedStatement.setString(3, Controller.logedInUser.getLogin());
                preparedStatement.executeQuery();
                fillComments(cbSM.getSelectionModel().getSelectedItem().getStudMatID());
                taComment.clear();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning!");
            alert.setHeaderText(null);
            alert.setContentText("Please enter comment!");
            alert.showAndWait();
        }
    }

    @FXML
    void deleteSelected(ActionEvent event) {
        if (lvComments.getSelectionModel().getSelectedItem() != null){
            int commentID = lvComments.getSelectionModel().getSelectedItem().getCommentID();
            String deleteQuery = "DELETE FROM KOMENTAR WHERE KOMENTAR_ID = ?";
            try {
                PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(deleteQuery);
                preparedStatement.setInt(1, commentID);
                preparedStatement.execute();
                fillComments(cbSM.getSelectionModel().getSelectedItem().getStudMatID());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning!");
            alert.setHeaderText(null);
            alert.setContentText("Please select comment from the list!");
            alert.showAndWait();
        }

    }

    @FXML
    void initialize() {
        fillStudMat();
        cbSM.setItems(studMatCBS);
        cbSM.getSelectionModel().selectFirst();
        fillComments(cbSM.getSelectionModel().getSelectedItem().getStudMatID());
        lvComments.setItems(comments);
        cbSM.setOnAction(event -> {
            fillComments(cbSM.getSelectionModel().getSelectedItem().getStudMatID());
        });
    }

    private void fillComments(int smID) {
        comments.clear();
        try {
            if (smID == 0){
                String selectQuery = "select * from KOMENTAR";
                final PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(selectQuery);
                final ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    final int commentID = resultSet.getInt("KOMENTAR_ID");
                    final int studMatID = resultSet.getInt("S_M_STUD_MAT_ID");
                    final String obsah = resultSet.getString("OBSAH");
                    final String userLogin = resultSet.getString("USER_LOGIN");

                    final Comment newComment = new Comment(commentID, studMatID, obsah, userLogin);
                    comments.add(newComment);
                }
            }else{
                String selectQuery = "select * from KOMENTAR where S_M_STUD_MAT_ID = ?";
                final PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(selectQuery);
                preparedStatement.setInt(1, smID);
                final ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    final int commentID = resultSet.getInt("KOMENTAR_ID");
                    final int studMatID = resultSet.getInt("S_M_STUD_MAT_ID");
                    final String obsah = resultSet.getString("OBSAH");
                    final String userLogin = resultSet.getString("USER_LOGIN");

                    final Comment newComment = new Comment(commentID, studMatID, obsah, userLogin);
                    comments.add(newComment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillStudMat() {
        studMatCBS.clear();
        String selectQuery = "select STUD_MAT_ID,NAZEV,TYP_SOUBORU from STUDIJNI_MATERIALY";
        try {
            final PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(selectQuery);
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final int studMatID = resultSet.getInt("STUD_MAT_ID");
                final String nazev = resultSet.getString("NAZEV");
                final String typSouboru = resultSet.getString("TYP_SOUBORU");

                final StudMatCB newSM = new StudMatCB(studMatID, nazev, typSouboru);
                studMatCBS.add(newSM);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}