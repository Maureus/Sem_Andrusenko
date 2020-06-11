package sample;

import database.DataBaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import struc.User;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignUpController {
    private final DataBaseHandler dbConnection = new DataBaseHandler();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField signUpName;

    @FXML
    private PasswordField signUpPassword;

    @FXML
    private TextField signUpLogin;

    @FXML
    private TextField signUpSurname;

    @FXML
    void signUp(ActionEvent event) {
        String insertQueryUsers = "insert into ST58211.UZIVATELE (ST58211.UZIVATELE.LOGIN, ST58211.UZIVATELE.HESLO," +
                " ST58211.UZIVATELE.JMENO, ST58211.UZIVATELE.PRIJMENI, ST58211.UZIVATELE.ROLE_ROLE_ID)" +
                " values (?, ?, ?, ?, ?)";
        if (!checkLogEmpty() || !checkPassEmpty()){
            User newUser = new User(signUpLogin.getText().trim(), signUpPassword.getText().trim(),
                    signUpName.getText().trim(), signUpSurname.getText().trim(), 1);
            try {
                PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(insertQueryUsers);
                preparedStatement.setString(1, newUser.getLogin());
                preparedStatement.setString(2, newUser.getPassword());
                preparedStatement.setString(3, newUser.getName());
                preparedStatement.setString(4, newUser.getSurname());
                preparedStatement.setInt(5, newUser.getRoleId());
                preparedStatement.execute();
                signUpSurname.getScene().getWindow().hide();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Congratulation!");
                alert.setHeaderText(null);
                alert.setContentText("You have been registered! Please wait for role!");
                alert.showAndWait();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning!");
            alert.setHeaderText(null);
            alert.setContentText("Please enter correct login and password!");
            alert.showAndWait();
        }
    }

    private boolean checkLogEmpty() {
        return signUpLogin.getText().trim().isEmpty();
    }

    private boolean checkPassEmpty() {
        return signUpPassword.getText().trim().isEmpty();
    }

    @FXML
    void initialize() {


    }
}
