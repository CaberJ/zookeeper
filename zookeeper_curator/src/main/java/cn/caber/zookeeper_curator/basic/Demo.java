package cn.caber.zookeeper_curator.basic;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Demo {
    public static void main(String[] args) throws Exception {

        // 连接准备
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client =
                CuratorFrameworkFactory.builder()
                        .connectString("192.168.1.145:2181")
                        .sessionTimeoutMs(5000)
                        .connectionTimeoutMs(5000)
                        .retryPolicy(retryPolicy)
                        .build();

        client.start();

        /**
         * 节点相关 创建
         */
        String path = "/zookeeper/caber";

        // 创建子节点（默认为持久化节点）,内容为空
        client.create().forPath(path + "1");
        // 创建子节点（默认为持久化节点）,带内容
        client.create().forPath(path + "2", "默认持久化节点".getBytes());
        // 创建子临时节点
        client.create().withMode(CreateMode.EPHEMERAL).forPath(path + "3");
        // 创建子临时节点，带内容
        client.create().withMode(CreateMode.EPHEMERAL).forPath(path + "4", "临时节点".getBytes());
        // 创建子临时节点，带内容，并自动递归创建父节点
        client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path + "5", "临时节点，自动创建父节点".getBytes());

        /**
         * 节点相关 删除
         */
        // 删除一个节点
        client.delete().forPath(path + "1"); //只能删除叶子节点（没有子节点的节点）
        // 删除一个节点，并递归删除所有的子节点
        client.delete().deletingChildrenIfNeeded().forPath(path + "5");
        //删除一个节点，强制保证删除
        client.delete().guaranteed().forPath(path + "2");//guaranteed()接口是一个保障措施，只要客户端会话有效，那么Curator会在后台持续进行删除操作，直到删除节点成功。

        /**
         * 节点相关 数据
         */
        // 读取一个节点的数据
        client.getData().forPath(path);
        // 读取一个节点的数据内容，同时获取到该节点的stat
        Stat stat = new Stat();
        client.getData().storingStatIn(stat).forPath(path);
        System.out.println("stat:" + stat.toString());
        // 更新一个节点的数据
        client.setData().forPath(path, "新数据".getBytes());
        /**
         * 节点相关 其他
         */
        // 查看节点是否存在
        Stat exist = client.checkExists().forPath(path);
        // 获取子节点列表
        List<String> list = client.getChildren().forPath(path);
        // 事务提交
        client.inTransaction().check().forPath(path)
                .and()
                .create().withMode(CreateMode.EPHEMERAL).forPath(path, "data".getBytes())
                .and()
                .setData().withVersion(10086).forPath(path, "data2".getBytes())
                .and()
                .commit();
        // 异步操作
        Executor executor = Executors.newFixedThreadPool(2);
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .inBackground((curatorFramework, curatorEvent) -> {
                    System.out.println(String.format("eventType:%s,resultCode:%s", curatorEvent.getType(), curatorEvent.getResultCode()));
                }, executor)
                .forPath(path);
        //注意：如果#inBackground()方法不指定executor，那么会默认使用Curator的EventThread去进行异步处理。
    }
}
