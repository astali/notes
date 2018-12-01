package com.jumpw.pt.common.junit;


import java.util.concurrent.*;

/**
 * @TODO 描述
 * <p>
 * 创建者 Administrator on 2018/8/31 9:50
 */
public class CyclicBarrierTest {
    public static void main(String[] args) {
       
        int size = 4;
        ExecutorService executorService = Executors.newFixedThreadPool(size);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(size, new BarrierAction(false));
        String[] name = {"狙击手","突击手","救护车","医生"};
        for (int i = 1; i <= size; i++) {
            executorService.execute(new Thread(new DoWork(cyclicBarrier,name[i-1])));
        }
        executorService.shutdown();
    }

    static class DoWork implements Runnable{
        CyclicBarrier cyclicBarrier;
        String gemeUserName;
        DoWork(CyclicBarrier cyclicBarrier,String gemeUserName) {
            this.cyclicBarrier = cyclicBarrier;
            this.gemeUserName = gemeUserName;
        }
        @Override
        public void run() {
            System.out.println("[ "+ gemeUserName + "]正在就位中...");
            try {
                Thread.sleep(1000);
                cyclicBarrier.await();
                //加载游戏
                loadGame();
                cyclicBarrier.await();
                kill();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("InterruptedException");
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
                System.out.println("BrokenBarrierException");
            }
        }

        void loadGame() throws InterruptedException {
            System.out.println("[" +  gemeUserName + "]听最高长官号令中！");
            Thread.sleep(2000);
        }

        void kill() throws InterruptedException {
            System.out.println(gemeUserName  +"制服抢匪！");
            Thread.sleep(2000);
        }
    }

   static class BarrierAction implements Runnable{

        boolean flag;

        BarrierAction(boolean flag){
            this.flag = flag;
        }
        @Override
        public void run() {
            if (flag){
                System.out.println("制服抢匪完毕！");
            }else {
                System.out.println("全部就位完毕！听从最高长官号令中...");
                flag = true; //继续下一步操作
            }
        }
    }


}

