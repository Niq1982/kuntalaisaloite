package fi.om.municipalityinitiative.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.im4java.core.*;
import org.im4java.process.Pipe;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;


public class ImageModifier {

    public void modify(InputStream inputStream, String resultFilePath, String fileType, int maxWidth, int maxHeight) throws IOException, IM4JavaException, InterruptedException {


        File tempFile = File.createTempFile(RandomHashGenerator.shortHash(), "." + fileType);
        try (FileOutputStream output = new FileOutputStream(tempFile)) {
            IOUtils.write(IOUtils.toByteArray(inputStream), output);
        }
        finally {
            inputStream.close();
        }

        Info info = new Info(tempFile.getAbsolutePath(), true);

        Size size = new Size(info.getImageWidth(), info.getImageHeight(), maxWidth, maxHeight);
        ConvertCmd cmd = new ConvertCmd();
        cmd.setSearchPath("/opt/ImageMagick/");

        IMOperation op = new IMOperation();
        op.addImage(tempFile.getAbsolutePath());
        op.resize(size.getWidth(), size.getHeight());
        op.addImage(resultFilePath);

        cmd.run(op);

    }

//    private static BufferedImage resizeImage(BufferedImage originalImage, int type, int width, int height){
//        BufferedImage resizedImage = new BufferedImage(width, height, type);
//        Graphics2D g = resizedImage.createGraphics();
//        g.drawImage(originalImage, 0, 0, width, height, null);
//        g.dispose();
//
//        return resizedImage;
//    }

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
