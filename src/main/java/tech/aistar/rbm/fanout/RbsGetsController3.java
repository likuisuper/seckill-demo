package tech.aistar.rbm.fanout;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Controller;

/**
 * 本类功能:
 *
 * @author cxylk
 * @date 2020/10/22 18:03
 */
@Controller
@RabbitListener(queues = "test02")
public class RbsGetsController3 {
    @RabbitHandler
    public void test(String msg){
        System.out.println("test02_msg:"+msg);
    }
}
