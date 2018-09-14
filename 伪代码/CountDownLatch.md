```java

        try {
            CountDownLatch latch = new CountDownLatch(4);
            for (int i = 1; i <= 4; i++) {
                new Thread(new MyThread(latch,i)).start(); //启用四个线程
            }
            latch.await(); //主线程执行完毕
            MyThread.sum(); //最后统计总数
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

class MyThread implements Runnable{
    int threadNumber ;
    static int tab1 = 0,tab2 = 0,tab3 = 0,tab4 = 0;
    CountDownLatch countDownLatch;
    MyThread(CountDownLatch countDownLatch ,int threadName){
        this.countDownLatch = countDownLatch;
        this.threadNumber = threadName;
    }
    @Override
    public void run(){
        try{
            switch (threadNumber) {
                case 1: tab1 = new Random().nextInt(100);
                    System.out.println("当前线程" + Thread.currentThread().getName()  + ", 表1数量：" +tab1); break;
                case 2: tab2 = new Random().nextInt(100);
                    System.out.println("当前线程" + Thread.currentThread().getName()  + ", 表2数量：" +tab2);break;
                case 3: tab3 = new Random().nextInt(100);
                    System.out.println("当前线程" + Thread.currentThread().getName()  + ", 表3数量："+tab3);break;
                case 4: tab4 = new Random().nextInt(100);
                    System.out.println("当前线程" + Thread.currentThread().getName()  + ", 表4数量：" + tab4);break;
                default:
                    System.out.println("异常了");break;
            }
            System.out.println("kkkk" +countDownLatch.getCount());
        }finally {
            if (countDownLatch != null) {
                countDownLatch.countDown();//避免阻塞  计数器减1
            }
        }
    }

    public static void sum(){
        System.out.println(tab1 + tab2 + tab3 +tab4);
    }
}
```