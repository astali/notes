package com.jumpw.pt.common.junit;

import java.util.concurrent.CountDownLatch;

/**
 * @TODO 描述
 * <p>
 * 创建者 Administrator on 2018/8/31 15:22
 */
public class CountDownLatchDemo {
    private static final int N = 10;

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch doneSignal = new CountDownLatch(N);
        CountDownLatch startSignal = new CountDownLatch(1);//开始执行信号

        for (int i = 1; i <= N; i++) {
            new Thread(new Worker(i, doneSignal, startSignal)).start();//线程启动了
        }
        System.out.println("begin------------");
        startSignal.countDown();//开始执行啦
        doneSignal.await();//等待所有的线程执行完毕
        System.out.println("Ok");

    }

    static class Worker implements Runnable {
        private final CountDownLatch doneSignal;
        private final CountDownLatch startSignal;
        private int beginIndex;

        Worker(int beginIndex, CountDownLatch doneSignal,
               CountDownLatch startSignal) {
            this.startSignal = startSignal;
            this.beginIndex = beginIndex;
            this.doneSignal = doneSignal;
        }

        public void run() {
            try {
                System.out.println("阻塞");
                startSignal.await(); //等待开始执行信号的发布

                beginIndex = (beginIndex - 1) * 10 + 1;
                for (int i = beginIndex; i <= beginIndex + 10; i++) {
                    System.out.println(i);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                doneSignal.countDown();
            }
        }
    }
}
