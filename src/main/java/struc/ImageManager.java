package struc;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class ImageManager {

    public ImageManager() {
    }

    public static void scaleImage(@NotNull final ImageView imageView, @NotNull final File file, final int[] size) {
        final Image image = new Image(file.toURI().toString());
        imageView.setImage(new Image(file.toURI().toString()));
        final double rootWidth = size[0];
        final double rootHeight = size[1];
        final double imageWidth = image.getWidth();
        final double imageHeight = image.getHeight();
        final double ratioX = rootWidth / imageWidth;
        final double ratioY = rootHeight / imageHeight;
        final double ratio = Math.min(ratioX, ratioY);
        imageView.setPreserveRatio(false);
        final double width = ratio * imageWidth;
        final double height = ratio * imageHeight;
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setLayoutX(size[2] + (size[4] - imageView.getFitWidth()) / 2);
        imageView.setLayoutY(size[3] - imageView.getFitHeight() - 10);
    }
}
