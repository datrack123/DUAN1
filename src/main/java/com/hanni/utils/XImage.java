package com.hanni.utils;

import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.ImageIcon;

public class XImage {
//    public static Image getAppIcon()
//    {
//        URL url = XImage.class.getResource("src\\main\\java\\Icon\\hanni_img_1.jpg");//src\\main\\java\\Icon\\2b0edf3964c4b29aebd5.jpg
//        //C:\Users\anctWin10\Documents\NetBeansProjects\app-Du An 1\DuAn1_BackUp2\src\main\resources\Icon
//        return new ImageIcon(url).getImage();             
//    }
    public static boolean save(File src)
    {
        File dst = new File("src\\main\\java\\Icon",src.getName());
        if(!dst.getParentFile().exists())
        {
            dst.getParentFile().mkdirs();//tạo thư mục
        }
        try {
            Path from = Paths.get(src.getAbsolutePath());
            Path to = Paths.get(dst.getAbsolutePath());
            Files.copy(from,to,StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static ImageIcon read(String fileName)
    {
        File path = new File("src\\main\\java\\Icon",fileName);
        return new ImageIcon(path.getAbsolutePath());
    }
    //C:\Users\anctWin10\Documents\NetBeansProjects\DuAn1\src\main\resources\Icon
    //C:\Users\anctWin10\Documents\NetBeansProjects\DuAn1\src\main\java\Icon
}
