package tech.aistar.rbm;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 本类功能:
 *
 * @author cxylk
 * @date 2020/10/22 18:16
 */
@RestController
@RequestMapping("/rbm")
public class RbsSendController {
    @Autowired
    private RabbitTemplate rabbitTemplate;


    @GetMapping
    public String test(){
        rabbitTemplate.convertAndSend("hello_test01","tom");

        return "OK rbm send";
    }
}
