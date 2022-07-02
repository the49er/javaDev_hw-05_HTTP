package ua.goit.http.server.service;

//import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.util.Base64;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class ImageTest {
    public static void main(String[] args) {
        String filePath = "images/cat_1.jpg";
        String str1 = uploadImg(filePath);
        System.out.println(str1.length());

    }

    private static String uploadImg(String filePath) {
        File img = new File(filePath);
        String imgStr = "";
        try {
            InputStream bis = new FileInputStream(img);
            byte[] allBytes = bis.readAllBytes();
            imgStr = Base64.getEncoder().encodeToString(allBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return imgStr;
    }

//    private static String imgLoader(String path){
//        String result;
//        try {
//            BufferedImage sourceImage = ImageIO.read(new File(path));
//            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//            ImageIO.write(sourceImage, "jpg", bytes);
//            result = Base64.encode(bytes.toByteArray());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return result;
//    }
}
