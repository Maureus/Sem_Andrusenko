package sample;

import database.DataBaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import struc.ImageManager;
import struc.OpenWindow;

import java.io.*;
import java.net.URL;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static sample.Controller.logedInUser;

public class AdminWindowController {
    private final DataBaseHandler dbConnection = new DataBaseHandler();
    private int[] size = {150, 125, 387, 146, 80};

    @FXML
    private ResourceBundle resources;

    @FXML
    private AnchorPane anchorTV;

    @FXML
    private URL location;

    @FXML
    private Label userLogin;

    @FXML
    private Button studMatBtn;

    @FXML
    private Button kategorieBtn;

    @FXML
    private Button kbizyBtn;

    @FXML
    private Button otzkyBtn;

    @FXML
    private ImageView avatar;

    @FXML
    private Button subjectsBtn;

    @FXML
    private Button usersBtn;

    @FXML
    void initialize() {
        userLogin.setText(logedInUser.getLogin());
        int userID = logedInUser.getRoleId();
        if (userID == 2) {
            usersBtn.setDisable(true);
            subjectsBtn.setDisable(true);
        } else if (userID == 3) {
            usersBtn.setDisable(true);
            subjectsBtn.setDisable(true);
        }
        updateAvatar();
    }


    @FXML
    void logOut(ActionEvent event) {
        OpenWindow.openWindow(event, "/fxmlfiles/LogInWindow.fxml", getClass(), true, false);
    }

    @FXML
    void profile(ActionEvent event) {
        OpenWindow.openWindow(event, "/fxmlfiles/EditProfile.fxml", getClass(), false, false);
    }

    @FXML
    void users(ActionEvent event) {
        OpenWindow.openWindow(event, "/fxmlfiles/AlterUsers.fxml", getClass(), false, true);
    }

    @FXML
    void subjects(ActionEvent event) {
        OpenWindow.openWindow(event, "/fxmlfiles/AlterSubjectWindow.fxml", getClass(), false, true);
    }

    @FXML
    void studMat(ActionEvent event) {
        OpenWindow.openWindow(event, "/fxmlfiles/AlterStudMat.fxml", getClass(), false, true);
    }

    @FXML
    void kategorie(ActionEvent event) {
        OpenWindow.openWindow(event, "/fxmlfiles/AlterCategoryWindow.fxml", getClass(), false, true);
    }

    @FXML
    void comments(ActionEvent event) {
        OpenWindow.openWindow(event, "/fxmlfiles/OpenCommentsWindow.fxml", getClass(), false, true);
    }

    @FXML
    void kvizy(ActionEvent event) {

    }

    @FXML
    void otazky(ActionEvent event) {

    }

    @FXML
    void assignUserSubject(ActionEvent event) {
        OpenWindow.openWindow(event, "/fxmlfiles/AssignUserSubject.fxml", getClass(), false, true);
    }

    @FXML
    void assignCatSM(ActionEvent event) {
        OpenWindow.openWindow(event, "/fxmlfiles/AssignCatSM.fxml", getClass(), false, true);
    }


    private void updateAvatar() {
        String avatarQuery = "select AVATAR from UZIVATELE where UZIVATEL_ID like ?";

        try {
            final PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(avatarQuery);
            preparedStatement.setInt(1, Controller.logedInUser.getUserID());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                final Blob blob = resultSet.getBlob("AVATAR");
                if (blob != null) {
                    BufferedInputStream is = new BufferedInputStream(blob.getBinaryStream());
                    File file = new File("avatar.png");
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
                    ImageManager.scaleImage(avatar, file, size);
                    file.delete();
                } else {
                    Image image = new Image("/assets/empty_avatar1.jpg");
                    avatar.setImage(image);
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}
