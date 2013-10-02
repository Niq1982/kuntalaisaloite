package fi.om.municipalityinitiative.util;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ImageModifierTest {

    private static final int MAX_WIDTH = 1000;
    private static final int MAX_HEIGHT = 500;

    public static void main(String[] params) throws IOException {
        FileInputStream inputStream = new FileInputStream("/Users/paulika/Desktop/jees.png");

        try (FileOutputStream outputStream = new FileOutputStream("/Users/paulika/Desktop/aa.png")) {
            ImageModifier.modify(inputStream, outputStream, "png", MAX_WIDTH, MAX_HEIGHT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            inputStream.close();
        }
    }


    @Test
    public void resizes_by_50_prosent_if_over_width() {
        ImageModifier.Size size = new ImageModifier.Size(2000, 100, MAX_WIDTH, MAX_HEIGHT);
        assertThat(size.getWidth(), is(1000));
        assertThat(size.getHeight(), is(50));
    }

    @Test
    public void resizes_by_50_prosent_if_over_width_more_than_over_height() {
        ImageModifier.Size size = new ImageModifier.Size(2000, 700, MAX_WIDTH, MAX_HEIGHT);
        assertThat(size.getWidth(), is(1000));
        assertThat(size.getHeight(), is(350));
    }

    @Test
    public void resizes_by_50_prosent_if_over_height() {
        ImageModifier.Size size = new ImageModifier.Size(1000, 1000, MAX_WIDTH, MAX_HEIGHT);
        assertThat(size.getWidth(), is(500));
        assertThat(size.getHeight(), is(500));
    }

    @Test
    public void resizes_by_50_prosent_if_over_height_more_than_over_width() {
        ImageModifier.Size size = new ImageModifier.Size(1500, 1000, MAX_WIDTH, MAX_HEIGHT);
        assertThat(size.getWidth(), is(750));
        assertThat(size.getHeight(), is(500));
    }

    @Test
    public void does_not_resize() {
        ImageModifier.Size size = new ImageModifier.Size(MAX_WIDTH, MAX_HEIGHT, MAX_WIDTH, MAX_HEIGHT);
        assertThat(size.getWidth(), is(MAX_WIDTH));
        assertThat(size.getHeight(), is(MAX_HEIGHT));
    }


}
