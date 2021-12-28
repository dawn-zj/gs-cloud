package demo;

/**
 * 线程的创建、启动
 */
public class ThreadTest {

    /**
     * 继承 Thread 类 线程测试
     */
    @org.junit.Test
    public void thread1() {
        Thread1 threadTest1 = new Thread1();
        Thread1 threadTest2 = new Thread1();
        threadTest1.start();
        threadTest2.start();
    }

    /**
     * 实现 Runnable 接口 线程测试
     */
    @org.junit.Test
    public void thread2() {
        Thread2 threadTest1 = new Thread2();
        Thread t1 = new Thread(threadTest1);

        Thread2 threadTest2 = new Thread2();
        Thread t2 = new Thread(threadTest2);

        t1.start();
        t2.start();
    }

    /**
     * 匿名内部类方式 线程测试
     */
    @org.junit.Test
    public void thread3() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "---匿名内部类");
                for (int i = 0; i < 10; i++) {
                    System.out.println(Thread.currentThread().getName() + "---" + i);
                }
            }
        };
        thread.start();
        System.out.println(Thread.currentThread().getName() + "---匿名内部类测试结束");
    }
}

/**
 * 创建线程方式1：继承 Thread 重写 run 方法
 * 启动线程的唯一方法就是通过Thread类的start()实例方法
 */
class Thread1 extends Thread {

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + "---" + i);
        }
    }
}

/**
 * 创建线程方式2：实现 Runnable 接口, 重写 run 方法
 * 启动线程的唯一方法就是通过Thread类的start()实例方法
 */
class Thread2 implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + "---" + i);
        }
    }
}
