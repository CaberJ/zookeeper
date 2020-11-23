package cn.caber.zookeeper_curator.controller;

import cn.caber.zookeeper_curator.service.LockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private LockService lockService;

    @RequestMapping("/test")
    public String sellTicket(@RequestParam("num")Integer num){
        lockService.sellTicket(num);
        return "success";
    }
}
