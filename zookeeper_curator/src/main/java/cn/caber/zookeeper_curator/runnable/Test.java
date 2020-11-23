package cn.caber.zookeeper_curator.runnable;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.locks.ReentrantLock;

public class Test {
    public static void main(String[] args) {
        int ticketNum = 100;
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client =
                CuratorFrameworkFactory.builder()
                        .connectString("192.168.1.145:2181")
                        .sessionTimeoutMs(5000)
                        .connectionTimeoutMs(5000)
                        .retryPolicy(retryPolicy)
                        .build();

        client.start();
        InterProcessMutex lock = new InterProcessMutex(client, "caber-lock");
        TicketRunnable ticketRunnable = new TicketRunnable(ticketNum,lock);
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(ticketRunnable);
            thread.start();
        }
    }
}
