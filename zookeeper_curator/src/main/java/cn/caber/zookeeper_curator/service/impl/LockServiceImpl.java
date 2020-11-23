package cn.caber.zookeeper_curator.service.impl;

import cn.caber.zookeeper_curator.runnable.TicketRunnable;
import cn.caber.zookeeper_curator.service.LockService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.ReentrantLock;

@Service
public class LockServiceImpl implements LockService {

    @Autowired
    private CuratorFramework client;

    private String lockPath = "/caber-lock";

    public  void sellTicket(Integer ticketNum) {
//        ReentrantLock reentrantLock = new ReentrantLock();

        InterProcessMutex lock = new InterProcessMutex(client, lockPath);
        TicketRunnable ticketRunnable = new TicketRunnable(ticketNum,lock);
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(ticketRunnable);
            thread.start();
        }
    }
}
