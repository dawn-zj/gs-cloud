package demo;

import org.junit.Test;

import java.util.HashMap;

public class StringTest {

    /**
     * 问：为什么判断一个对象是否相等，需要equals和hashCode一起判断？
     * 答：
     * 如果两个对象的hashCode 值相等，那这两个对象不一定相等（哈希碰撞）
     * 如果两个对象的hashCode 值相等并且equals()方法返回 true，我们才认为这两个对象相等
     *
     *
     * 问：重写equals为什么一定要重写hashcode?
     * 答：
     * object的equals()，默认比较的事对象地址，
     * Object 的 hashCode() 方法是本地方法, 是用 C 语言或 C++ 实现的，该方法通常用来将对象的内存地址转换为整数之后返回
     * 对象相等，则其hash值也得相等。
     * 若不重写hashCode，会导致equals 方法判断是相等的两个对象，hashCode 值却不相等。
     *
     * 问：两个对象的 hashCode() 相同，则 equals() 也一定为 true，对吗？
     * 答：不对。
     */
    @Test
    public void hashCodeTest() {
        String str1 = "通话";
        String str2 = "重地";
        System.out.println(String.format("str1：%d | str2：%d", str1.hashCode(), str2.hashCode())); //str1：1179395 | str2：1179395
        System.out.println(str1.equals(str2)); //false
    }

    /**
     * HashMap 基于 Hash 算法实现的，我们通过 put(key,value)存储，get(key)来获取。
     * 当传入 key 时，HashMap 会根据 key.hashCode() 计算出 hash 值，根据 hash 值将 value 保存在 bucket 里。
     * 当计算出的 hash 值相同时，我们称之为 hash 冲突，HashMap 的做法是用链表和红黑树存储相同 hash 值的 value。
     * 当 hash 冲突的个数比较少时，使用链表否则使用红黑树。
     */
    @Test
    public void hashMapTest() {
        // 存入两个hashCode一样的key值，测试get(key)
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("通话", "通话");
        hashMap.put("重地", "重地");

        System.out.println(hashMap.get("通话"));
    }

    /**
     * 问：如何将字符串反转？
     * 答：使用 StringBuilder 或者 stringBuffer 的 reverse() 方法。
     * StringBuilder：非线程安全，性能高，适合于单线程场景
     * StringBuffer: 线程安全，性能低，适合于多线程场景
     */
    @Test
    public void reverseTest() {
        // StringBuffer reverse
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.hashCode();
        stringBuffer.append("abcdefg");
        System.out.println(stringBuffer.reverse()); // gfedcba

        // StringBuilder reverse
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder. append("abcdefg");
        System. out. println(stringBuilder. reverse()); // gfedcba
    }

}
