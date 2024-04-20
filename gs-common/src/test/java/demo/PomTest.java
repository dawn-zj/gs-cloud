package demo;

import com.gs.common.util.FileUtil;
import com.gs.common.util.StringUtil;
import org.junit.jupiter.api.Test;

/**
 * @Author Zhang Juan
 * @Date 2024/4/1 14:27
 */
public class PomTest {
    @Test
    public void test () {
        String path = "E:/Idea/GS/gs-cloud/gs-common/lib";
        String[] strings = FileUtil.listFileName(path);
        for (String jarName : strings ) {
            String name = jarName.substring(0, jarName.lastIndexOf("-"));
            String pom = StringUtil.format("        <dependency>\n" +
                    "            <groupId>com.gs</groupId>\n" +
                    "            <artifactId>{}</artifactId>\n" +
                    "            <version>1.0.0</version>\n" +
                    "            <type>jar</type>\n" +
                    "            <scope>system</scope>\n" +
                    "            <systemPath>${project.basedir}/lib/{}</systemPath>\n" +
                    "        </dependency>", name, jarName);
            System.out.println(pom);
        }
    }
}
