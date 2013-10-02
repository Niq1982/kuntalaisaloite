package fi.om.municipalityinitiative.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ImageModifier {

    public static void modify(InputStream inputStream, OutputStream outputStream, String formatName, int maxWidth, int maxHeight) throws IOException {

        BufferedImage originalImage = ImageIO.read(inputStream);
        int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
        Size size = new Size(originalImage.getWidth(), originalImage.getHeight(), maxWidth, maxHeight);

        if (size.isScaled()) {

            BufferedImage resizedImage = resizeImage(originalImage, type, size.getWidth(), size.getHeight());
            ImageIO.write(resizedImage, formatName, outputStream);
        }
        else {
            ImageIO.write(originalImage, formatName, outputStream);
        }

    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int type, int width, int height){
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        return resizedImage;
    }

    public static class Size {

        private final int height;
        private final int width;
        private Double scale;

        public Size(int width, int height, int maxWidth, int maxHeight) {
            scale = null;

            if (height > maxHeight) {
                scale = (double) maxHeight / (double) height;
            }
            if (width > maxWidth) {
                double width_scale = (double) maxWidth / (double) width;
                if (scale == null || width_scale < scale) {
                    scale = width_scale;
                }
            }

            if (scale != null) {
                this.width = (int) (scale * width);
                this.height = (int) (scale * height);
            }
            else {
                this.width = width;
                this.height = height;
            }
        }

        public Integer getHeight() {
            return height;
        }

        public Integer getWidth() {
            return width;
        }

        public boolean isScaled() {
            return scale != null;
        }
    }
}
