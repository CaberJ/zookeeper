package cn.caber.zookeeper_curator;

import com.google.common.base.Strings;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;

public class CuratorService {

    @Autowired
    private CuratorFramework client;

    private String path ="";

    public String getPath() throws Exception {
        client.start();
        byte[] bytes = client.getData().forPath(path);
        String data = new String(bytes);
        return data;
    }



}
