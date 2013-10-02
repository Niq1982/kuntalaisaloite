package fi.om.municipalityinitiative.util;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ImageModifierTest {

    public static void main(String[] params) throws IOException {
        FileInputStream inputStream = new FileInputStream("/Users/paulika/Desktop/jees.png");

        try (FileOutputStream outputStream = new FileOutputStream("/Users/paulika/Desktop/aa.png")) {
            ImageModifier.modify(inputStream, outputStream, "png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            inputStream.close();
        }
    }


    @Test
    public void resizes_by_50_prosent_if_over_width() {
        ImageModifier.Size size = new ImageModifier.Size(2000, 100);
        assertThat(size.getWidth(), is(1000));
        assertThat(size.getHeight(), is(50));
    }

    @Test
    public void resizes_by_50_prosent_if_over_width_more_than_over_height() {
        ImageModifier.Size size = new ImageModifier.Size(2000, 700);
        assertThat(size.getWidth(), is(1000));
        assertThat(size.getHeight(), is(350));
    }

    @Test
    public void resizes_by_50_prosent_if_over_height() {
        ImageModifier.Size size = new ImageModifier.Size(1000, 1000);
        assertThat(size.getWidth(), is(500));
        assertThat(size.getHeight(), is(500));
    }

    @Test
    public void resizes_by_50_prosent_if_over_height_more_than_over_width() {
        ImageModifier.Size size = new ImageModifier.Size(1500, 1000);
        assertThat(size.getWidth(), is(750));
        assertThat(size.getHeight(), is(500));
    }

    @Test
    public void does_not_resize() {
        ImageModifier.Size size = new ImageModifier.Size(ImageModifier.MAX_WIDTH, ImageModifier.MAX_HEIGHT);
        assertThat(size.getWidth(), is(ImageModifier.MAX_WIDTH));
        assertThat(size.getHeight(), is(ImageModifier.MAX_HEIGHT));
    }


}
