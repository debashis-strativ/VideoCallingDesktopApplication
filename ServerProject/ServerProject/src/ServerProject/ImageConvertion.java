package ServerProject;




import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageConvertion {
    private static final boolean LOSSY_COMPRESSION = true;

    public static byte[] bufferedImageToByteArray(BufferedImage image) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            ImageIO.write(image,"jpg", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return outputStream.toByteArray();
    }

    public  BufferedImage byteArrayToBufferedImage(byte[] array) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(array);

        BufferedImage image = null;

        try {
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return image;
    }
}
