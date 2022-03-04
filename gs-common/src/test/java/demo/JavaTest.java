package demo;

import org.junit.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JavaTest {
    private final char c1 = '1';// 字符常量
    private final String s = "s";// 字符串常量

    private static String str = "test";// 类变量, 方法区

    @Test
    public void Test() {
        int i = 1;// 栈内存
        UtilTest utilTest = new UtilTest();// 堆内存

        // JDK1.7 之前字符串常量池存放在方法区。JDK1.7 的时候，字符串常量池被从方法区拿到了堆中。
        String aa = "ab"; // 放在字符串常量池中
        String bb = "ab"; // 从字符串常量池中查找
        System.out.println(aa==bb);// true
    }

    @Test
    public void Test1() {
        int i = 0;

        int a = i++;// 分解：a = i; i = i + 1;
        System.out.println(a); // 0
        System.out.println(i); // 1
    }

    @Test
    public void Test2() {
        int i = 0;

        int b = ++i;// 分解：i = i + 1; b = i;
        System.out.println(b); // 1
        System.out.println(i); // 1
    }

    @Test
    public void Test3() {
        boolean flag = false;
        for (int i = 0; i <= 3; i++) {
            if (i == 0) {
                System.out.println("0");
            } else if (i == 1) {
                System.out.println("1");
                continue;
            } else if (i == 2) {
                System.out.println("2");
                flag = true;
            } else if (i == 3) {
                System.out.println("3");
                break;
            } else if (i == 4) {
                System.out.println("4");
            }
            System.out.println("xixi");
        }
        if (flag) {
            System.out.println("haha");
            return;
        }
        System.out.println("heihei");
    }

    @Test
    public void Test4() {
        char c = '0';
        System.out.println(c + c1); // 97
    }

    @Test
    public void Test5() throws Exception {
        List<Integer> list = new ArrayList<>();

        list.add(12);
        // 这里直接添加会报错
//        list.add("k1");

        // 但是通过反射添加是可以的，这就说明在运行期间所有的泛型信息都会被擦掉
        Class<? extends List> clazz = list.getClass();
        Method add = clazz.getDeclaredMethod("add", Object.class);
        add.invoke(list, "kl");

        System.out.println(list);

    }

    @Test
    public void Test6() {
        // float
        float a1 = 1.0f - 0.9f;
        float b1 = 0.9f - 0.8f;
        System.out.println(a1);// 0.100000024
        System.out.println(b1);// 0.099999964
        System.out.println(a1 == b1);// false

        // BigDecimal
        BigDecimal a = new BigDecimal("1.0");
        BigDecimal b = new BigDecimal("0.9");
        BigDecimal c = new BigDecimal("0.8");

        BigDecimal x = a.subtract(b);// a - b
        BigDecimal y = b.subtract(c);// b - c

        // 阿里规约强制规定用compareTo
        System.out.println(x); /* 0.1 */
        System.out.println(y); /* 0.1 */
        System.out.println(Objects.equals(x, y)); /* true */
        System.out.println(x.compareTo(y)); /* 0 */
        System.out.println(x == y); /* false */

        System.out.println(a.add(b));// 1.9
        System.out.println(a.subtract(b));// 0.1
        System.out.println(a.multiply(b));// 0.90
        // System.out.println(a.divide(b));// 无法除尽，抛出 ArithmeticException 异常
        System.out.println(a.divide(b, 2, RoundingMode.HALF_UP));// 1.11


    }
}
