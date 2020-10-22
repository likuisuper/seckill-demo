package tech.aistar.controller;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import tech.aistar.service.IOrderService;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * 本类功能:
 *
 * @author cxylk
 * @date 2020/10/22 18:42
 */
@Component
public class OrderRbmController {
    @Autowired
    private IOrderService orderService;

//    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "test01"),
//            exchange = @Exchange(value = "j02_exch",type = "fanout")))
//    @RabbitHandler
//    public void handlerOrder(Map<String,Object> maps){
//        //处理订单信息
//        System.out.println("正在安全环境检测...");
//
//        Long secId = (Long) maps.get("secId");
//        String uname = (String) maps.get("uname");
//
//        //订单编号如何生成... md5
//        orderService.insert(UUID.randomUUID().toString(),uname,"0",secId);
//
//        System.out.println("订单生成成功...");
//    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "test01"),
            exchange = @Exchange(value = "hello_exch",type = "fanout")))
    @RabbitHandler
    public void handlerOrder(@Payload Map<String,Object> maps, @Headers Map<String,Object> headers, Channel channel){
        //处理订单信息
        System.out.println("正在安全环境检测...");

        Long secId = (Long) maps.get("secId");
        String uname = (String) maps.get("uname");

        //订单编号如何生成... md5
        orderService.insert(UUID.randomUUID().toString(),uname,"0",secId);

        System.out.println("订单生成成功...");

        //手动删除...
        long tag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);

        try {
            channel.basicAck(tag,false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
