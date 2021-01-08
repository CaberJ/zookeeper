package cn.caber.zookeeper_curator.watcher;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.Objects;

/**
 * 只能监听当前节点，不能监听子节点的变化
 */
public class NodeCacheDemo {
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
        String path = "/zookeeper/caber/node";
        client.create().creatingParentsIfNeeded().forPath(path);
        NodeCache nodeCache = new NodeCache(client, path);

        NodeCacheListener nodeCacheListener =()->{
            ChildData currentData = nodeCache.getCurrentData();
            if(Objects.nonNull(currentData)){
                System.out.println("节点数据："+ new String(nodeCache.getCurrentData().getData()));
            }else {
                System.out.println("节点被删除!");
            }
        };
        nodeCache.getListenable().addListener(nodeCacheListener);
        nodeCache.start();

        Thread.sleep(1000 * 5*100);
        // 在程序不退出的情况下，手动用客户端修改子节点，会触发监听中定义的操作


        nodeCache.close();
        client.close();
        System.out.println("OK!");
    }
}
