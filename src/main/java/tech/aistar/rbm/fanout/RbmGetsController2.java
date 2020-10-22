package tech.aistar.rbm.fanout;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Controller;

/**
 * 本类功能:将数据发送到rabbitmq中
 *
 * @author cxylk
 * @date 2020/10/22 17:48
 */
@Controller
@RabbitListener(queues = "test01")
public class RbmGetsController2 {
    @RabbitHandler
    public void test(String msg){
        System.out.println("test01_msg:"+msg);
    }
}
