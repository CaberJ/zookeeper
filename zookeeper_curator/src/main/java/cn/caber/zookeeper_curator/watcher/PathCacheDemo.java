package cn.caber.zookeeper_curator.watcher;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.Objects;

/**
 * 可以监听所有子节点的变化，但不能监听当前节点及子节点的子节点的变化
 */
public class PathCacheDemo {
    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client =
                CuratorFrameworkFactory.builder()
                        .connectString("192.168.1.145:2181")
                        .sessionTimeoutMs(5000)
                        .connectionTimeoutMs(5000)
                        .retryPolicy(retryPolicy)
                        .build();

        client.start();
        String path = "/zookeeper/caber";

        PathChildrenCache pcCache = new PathChildrenCache(client, path, true);
        pcCache.start();

        PathChildrenCacheListener pathChildrenCacheListener = (client1,event)->{
            System.out.println("时间类型："+event.getType());


            if(Objects.nonNull(event.getData())){
                System.out.println("节点数据：" + event.getData().getPath() + " = " + new String(event.getData().getData()));
            }
        };
        pcCache.getListenable().addListener(pathChildrenCacheListener);

        Thread.sleep(1000 * 5*100);
        // 在程序不退出的情况下，手动用客户端修改子节点，会触发监听中定义的操作


        pcCache.close();
        client.close();
        System.out.println("OK!");
    }
}
