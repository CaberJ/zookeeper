package cn.caber.zookeeper_curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GetClient {

    @Value("${zk.connectionInfo}")
    private String connectionInfo;
   /* RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    CuratorFramework client = CuratorFrameworkFactory.newClient(
            connectionInfo,
            5000,
            3000,
            retryPolicy);*/

   @Bean
   public CuratorFramework getClient(){
       RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
       CuratorFramework client =
               CuratorFrameworkFactory.builder()
                       .connectString(connectionInfo)
                       .sessionTimeoutMs(5000)
                       .connectionTimeoutMs(5000)
                       .retryPolicy(retryPolicy)
                       .build();
       return client;
   }

}
