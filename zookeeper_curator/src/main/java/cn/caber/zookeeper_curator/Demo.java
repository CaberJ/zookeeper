package cn.caber.zookeeper_curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.List;

public class Demo {
    public static void main(String[] args) throws Exception {

        String path = "/brokers/topics/Hello-Kafka/partitions/0/state";

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client =
                CuratorFrameworkFactory.builder()
                        .connectString("192.168.1.141:2181")
                        .sessionTimeoutMs(5000)
                        .connectionTimeoutMs(5000)
                        .retryPolicy(retryPolicy)
                        .build();

        client.start();
        List<String> list = client.getChildren().forPath(path);
        System.out.println("-----------------------------------");

        /*for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }*/


        byte[] bytes = client.getData().forPath(path);
        String data = new String(bytes);
        System.out.println("data="+data);



    }
}
