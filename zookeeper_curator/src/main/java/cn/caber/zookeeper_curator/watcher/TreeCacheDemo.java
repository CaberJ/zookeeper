package cn.caber.zookeeper_curator.watcher;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.Objects;

/**
 * 当前节点的父节点不会被监听，当前节点，当前节点的子节点，当前节点的子节点的子节点... 都会被监听
 */
public class TreeCacheDemo {
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
        TreeCache treeCache = new TreeCache(client, path);

        TreeCacheListener treeCacheListener = (client1, event) -> {
            System.out.println("事件类型：" + event.getType() + ",路径：" + (Objects.nonNull(event.getData()) ? event.getData().getPath() : null));
        };
        treeCache.getListenable().addListener(treeCacheListener);
        treeCache.start();
        Thread.sleep(1000 * 5*100);
        // 在程序不退出的情况下，手动用客户端修改子节点，会触发监听中定义的操作


        treeCache.close();
        client.close();
        System.out.println("OK!");
    }
}
