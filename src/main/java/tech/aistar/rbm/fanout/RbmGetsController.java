package tech.aistar.rbm.fanout;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 本类功能:分裂模式
 *
 * @author cxylk
 * @date 2020/10/22 16:36
 */
@RestController
@RequestMapping("rbms")
public class RbmGetsController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping
    public String test(){
        rabbitTemplate.convertAndSend("hello_exch",null,"tom");

        return "OK rbm send";
    }
}
