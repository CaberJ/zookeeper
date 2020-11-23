package cn.caber.zookeeper_curator.service.impl;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CuratorServiceImpl {

    @Autowired
    private CuratorFramework client;

    private String path ="/zookeeper";

    public String getPath() throws Exception {
        client.start();
        byte[] bytes = client.getData().forPath(path);
        String data = new String(bytes);
        return data;
    }



}
