package com.gs.common.util.ofd;

import com.gs.common.util.FileUtil;
import org.ofdrw.converter.ConvertHelper;
import org.ofdrw.converter.GeneralConvertException;
import org.ofdrw.converter.ImageMaker;
import org.ofdrw.converter.SVGMaker;
import org.ofdrw.reader.OFDReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Zhang Juan
 * @Date 2024/3/28 11:05
 */
public class OfdUtil {
    private static int DEFAULT_DPI = 96;
    /**
     * 将ofd转换为图片， 默认DPI=96
     * @param originPath 源文件路径
     * @param imgPath 目标文件路径
     */
    public static List<byte[]> ofdToImg(byte[] ofdData) throws IOException {
        ArrayList<byte[]> list = new ArrayList<>();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(ofdData);

        // 创建转换转换对象
        try(OFDReader reader = new OFDReader(inputStream)) {
            // 设置 每毫米像素数量(Pixels per millimeter)
            double ppm = 1 / 25.4f * DEFAULT_DPI;
            ImageMaker imageMaker = new ImageMaker(reader, ppm);
            for (int i = 0; i < imageMaker.pageSize(); i++) {
                // 4. 指定页码转换图片
                BufferedImage image = imageMaker.makePage(i);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "png", baos);
                list.add(baos.toByteArray());
                baos.flush();
            }
        }
        return list;
    }

    public static void ofdToSvg(String originPath, String svgPath) {
        // 如果想使用此功能，先去修改pom，引入jar
        // 1. 文件输入路径
        Path src = Paths.get(originPath);
        // 2. 加载指定目录字体(非必须)
        // FontLoader.getInstance().scanFontDir(new File("src/test/resources/fonts"));
        // 3. 创建转换转换对象，设置 每毫米像素数量(Pixels per millimeter)
        try(OFDReader reader = new OFDReader(src)) {
            SVGMaker svgMaker = new SVGMaker(reader, 20d);
            for (int i = 0; i < svgMaker.pageSize(); i++) {
                // 4. 指定页码转换SVG，得到SVG(XML)
                String svg = svgMaker.makePage(i);
                Path dist = Paths.get("", svgPath + "_" + i + ".svg");
                // 5. 存储到文件。
                Files.write(dist, svg.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 6. Close OFDReader 删除工作过程中的临时文件 try close 语法
    }

    public static void ofdToHtml(String originPath, String htmlPath) {
        // 如果想使用此功能，先去修改pom，引入jar
        try {
            // 1. 提供文档
            Path ofdIn = Paths.get(originPath);
            Path htmlOut = Paths.get(htmlPath);
            // 2. [可选]配置字体，别名，扫描目录等
            // FontLoader.getInstance().addAliasMapping(null, "小标宋体", "方正小标宋简体", "方正小标宋简体")
            // FontLoader.getInstance().scanFontDir(new File("src/test/resources/fonts"));
            // 3. 配置参数（HTML页面宽度(px)），转换并存储HTML到文件。
            ConvertHelper.toHtml(ofdIn, htmlOut, 1000);
        } catch (GeneralConvertException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
//        OfdUtil.ofdToSvg("F:/1.ofd", "F:/temp/ofdParse/");
//        OfdUtil.ofdToHtml("F:/1.ofd", "F:/temp/ofdParse/1.html");
        List<byte[]> list = OfdUtil.ofdToImg(FileUtil.getFile("F:/1.ofd"));
        for (int i = 0; i < list.size(); i++) {
            FileUtil.storeFile("F:/temp/ofdParse/" + i + ".png", list.get(i));
        }
    }
}
