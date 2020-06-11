package sample;

import database.DataBaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import struc.OpenWindow;
import struc.Role;
import struc.User;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller {
    public static User logedInUser;


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button loginSignUpButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField loginField;


    @FXML
    void initialize() {
        logedInUser = new User();
        loginField.setText("admin");
        passwordField.setText("1234");
        loginSignUpButton.setOnAction(event -> {
            OpenWindow.openWindow(event, "/fxmlfiles/signUp.fxml", getClass(), false, false);
        });
    }


    @FXML
    private void logIn(ActionEvent event) {
        if (checkLogEmpty() || checkPassEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning!");
            alert.setHeaderText(null);
            alert.setContentText("Please enter login and password!");
            alert.showAndWait();
        } else {
            final String login = loginField.getText().trim();
            final String password = passwordField.getText().trim();
            final String query = "select * from ST58211.UZIVATELE where LOGIN like ? and HESLO like ?";
            try {
                DataBaseHandler dbHandler = new DataBaseHandler();
                PreparedStatement preparedStatement = dbHandler.getConnection().prepareStatement(query);
                preparedStatement.setString(1, login);
                preparedStatement.setString(2, password);
                final ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    int userID = resultSet.getInt("UZIVATEL_ID");
                    logedInUser.setUserID(userID);
                    logedInUser.setLogin(resultSet.getString("LOGIN"));
                    logedInUser.setPassword(resultSet.getString("HESLO"));
                    logedInUser.setName(resultSet.getString("JMENO"));
                    logedInUser.setSurname(resultSet.getString("PRIJMENI"));
                    logedInUser.setEmail(resultSet.getString("EMAIL"));
                    logedInUser.setTelephone(resultSet.getString("PHONE"));
                    logedInUser.setRoleId(resultSet.getInt("ROLE_ROLE_ID"));


                    switch (Role.getRole(logedInUser.getRoleId())) {
                        case GUEST:
                            break;
                        default:
                            OpenWindow.openWindow(event, "/fxmlfiles/AdminWindow.fxml", getClass(), true, false);
                            break;
                    }
                }else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Warning!");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong login or password! Please try again!");
                    alert.showAndWait();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    private boolean checkLogEmpty() {
        return loginField.getText().trim().isEmpty();
    }

    private boolean checkPassEmpty() {
        return passwordField.getText().trim().isEmpty();
    }

}

