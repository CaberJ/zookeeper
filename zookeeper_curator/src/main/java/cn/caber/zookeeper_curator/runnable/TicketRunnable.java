package cn.caber.zookeeper_curator.runnable;

import lombok.SneakyThrows;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

public class TicketRunnable implements Runnable {

    private int ticketNum;
    private InterProcessMutex lock;

/*    private ReentrantLock reentrantLock;

    public TicketRunnable(int ticketNum, ReentrantLock reentrantLock) {
        this.ticketNum = ticketNum;
        this.reentrantLock = reentrantLock;
    }*/

    public TicketRunnable(int ticketNum, InterProcessMutex lock) {
        this.ticketNum = ticketNum;
        this.lock = lock;
    }



    @SneakyThrows
    @Override
    public void run() {
        lock.acquire();
        while (ticketNum > 0) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ticketNum--;
            System.out.println(Thread.currentThread().getName() + "还有" + ticketNum + "张票");
        }
        lock.release();
    }


}
