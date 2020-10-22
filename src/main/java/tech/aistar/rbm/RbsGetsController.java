package tech.aistar.rbm;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Controller;

/**
 * 本类功能:将数据发送到rabbitmq中
 *
 * @author cxylk
 * @date 2020/10/22 18:14
 */
@Controller
@RabbitListener(queues = "hello_test01")
public class RbsGetsController {
    @RabbitHandler
    public void test(String msg){
        System.out.println("test=====");
        System.out.println("msg:"+msg);
    }
}
