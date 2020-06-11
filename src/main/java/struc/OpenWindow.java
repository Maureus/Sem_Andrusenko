package struc;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class OpenWindow {

    private OpenWindow() {
    }

    public static void openWindow(ActionEvent event, String address, Class cl, boolean hide, boolean resizable) {
        FXMLLoader loader = new FXMLLoader();

        if (hide) {
            ((Node) event.getSource()).getScene().getWindow().hide();
        }

        loader.setLocation(cl.getClass().getResource(address));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        if (!resizable) {
            stage.setResizable(false);
        }

        stage.show();
    }
}
