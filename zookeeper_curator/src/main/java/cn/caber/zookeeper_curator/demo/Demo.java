package cn.caber.zookeeper_curator.demo;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.List;

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


// 测试
        String path = "/zookeeper";
        List<String> list = client.getChildren().forPath(path);
        System.out.println("-----------------------------------");
        System.out.println(list.toString());


        byte[] bytes = client.getData().forPath(path);
        String data = new String(bytes);
        System.out.println("data="+data);

    }
}
