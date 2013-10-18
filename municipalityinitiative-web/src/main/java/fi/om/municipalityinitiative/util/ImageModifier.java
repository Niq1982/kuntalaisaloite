package fi.om.municipalityinitiative.util;

import org.apache.commons.io.IOUtils;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.Info;

import java.io.*;


public class ImageModifier {

    public void modify(File tempFile, String resultFilePath, int maxWidth, int maxHeight) throws IOException, IM4JavaException, InterruptedException {

        Info info = new Info(tempFile.getAbsolutePath(), true);

        Size size = new Size(info.getImageWidth(), info.getImageHeight(), maxWidth, maxHeight);
        ConvertCmd cmd = new ConvertCmd();

        IMOperation op = new IMOperation();
        op.addImage(tempFile.getAbsolutePath());
        op.resize(size.getWidth(), size.getHeight());
        op.addImage(resultFilePath);

        cmd.run(op);

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
