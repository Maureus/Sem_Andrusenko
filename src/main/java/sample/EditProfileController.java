package sample;

import java.io.*;
import java.net.URL;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import database.DataBaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import struc.ImageManager;


public class EditProfileController {
    private File selectedFile;
    private DataBaseHandler dataBaseHandler = new DataBaseHandler();
    private int[] size = {150, 125, 526, 139, 70};


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField profileSurname;

    @FXML
    private TextField profileName;

    @FXML
    private PasswordField profilePasswordNew;

    @FXML
    private TextField profileEmail;

    @FXML
    private ImageView ivAvatar;

    @FXML
    private Label userName;

    @FXML
    private PasswordField profilePasswordOld;

    @FXML
    private TextField profilePhone;

    @FXML
    private Button chooseFile;

    @FXML
    void saveProfile(ActionEvent event) {
        String updateQuery = "update UZIVATELE set JMENO = ?, PRIJMENI = ?, EMAIL = ?, PHONE = ?" +
                "where UZIVATEL_ID like ?";

        try {
            final PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(updateQuery);
            preparedStatement.setString(1, profileName.getText().trim());
            preparedStatement.setString(2, profileSurname.getText().trim());
            preparedStatement.setString(3, profileEmail.getText().trim());
            preparedStatement.setString(4, profilePhone.getText().trim());
            preparedStatement.setInt(5, Controller.logedInUser.getUserID());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Controller.logedInUser.setName(profileName.getText().trim());
        Controller.logedInUser.setSurname(profileSurname.getText().trim());
        Controller.logedInUser.setEmail(profileEmail.getText().trim());
        Controller.logedInUser.setTelephone(profilePhone.getText().trim());

    }

    @FXML
    void changePassword(ActionEvent event) {
        if (!profilePasswordOld.getText().isEmpty()) {
            if (profilePasswordOld.getText().trim().equals(Controller.logedInUser.getPassword())) {
                if (!profilePasswordNew.getText().isEmpty()) {
                    String updateQuery = "update UZIVATELE set HESLO = ? where UZIVATEL_ID like ?";

                    try {
                        final PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(updateQuery);
                        preparedStatement.setString(1, profilePasswordNew.getText().trim());
                        preparedStatement.setInt(2, Controller.logedInUser.getUserID());
                        preparedStatement.execute();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    Controller.logedInUser.setPassword(profilePasswordNew.getText().trim());

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success!");
                    alert.setHeaderText(null);
                    alert.setContentText("Password has been changed!");
                    alert.showAndWait();

                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Warning!");
                    alert.setHeaderText(null);
                    alert.setContentText("New Password is Empty!");
                    alert.showAndWait();
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Warning!");
                alert.setHeaderText(null);
                alert.setContentText("Please enter correct Old Password!");
                alert.showAndWait();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning!");
            alert.setHeaderText(null);
            alert.setContentText("Please enter correct Old Password!");
            alert.showAndWait();
        }

    }

    @FXML
    void initialize() {
        profileName.setText(Controller.logedInUser.getName());
        profileSurname.setText(Controller.logedInUser.getSurname());
        profileEmail.setText(Controller.logedInUser.getEmail());
        profilePhone.setText(Controller.logedInUser.getTelephone());
        userName.setText(Controller.logedInUser.getLogin());
        updateAvatar();

        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG, PNG Files", "*.jpg", "*.png")
        );

        chooseFile.setOnAction(e -> {
            selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) {
                ImageManager.scaleImage(ivAvatar, selectedFile, size);
                try {
                    FileInputStream fos = new FileInputStream(selectedFile);
                    String editQuery = "UPDATE UZIVATELE SET AVATAR = ? WHERE UZIVATEL_ID = ?";

                    final PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(editQuery);
                    preparedStatement.setBlob(1, fos, selectedFile.length());
                    preparedStatement.setInt(2, Controller.logedInUser.getUserID());
                    preparedStatement.execute();

                } catch (FileNotFoundException | SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void updateAvatar() {
        String avatarQuery = "select AVATAR from UZIVATELE where UZIVATEL_ID like ?";

        try {
            final PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(avatarQuery);
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
                    ImageManager.scaleImage(ivAvatar, file, size);
                    file.delete();
                } else {
                    Image image = new Image("/assets/empty_avatar1.jpg");
                    ivAvatar.setImage(image);
                }
            }

        } catch (SQLException | FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }


    }
}
