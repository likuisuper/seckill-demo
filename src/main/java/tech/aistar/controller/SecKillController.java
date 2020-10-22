package tech.aistar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.aistar.service.ISecKillService;

import java.util.HashMap;
import java.util.Map;

/**
 * 本类功能:秒杀控制器
 *
 * @author cxylk
 * @date 2020/10/22 18:40
 */
@RestController
@RequestMapping("kill")
public class SecKillController {
    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private ISecKillService secKillService;

    @GetMapping("/{seckillId}/{uname}")
    public Map<String,Object> kill(@PathVariable("seckillId")Long seckillId, @PathVariable("uname")String uname) {
        //作为json->返回出去...
        Map<String, Object> resultMaps = new HashMap<>();

        //1. 模拟的判断用户是否登录.
        if (null == uname || "".equals(uname.trim())) {
            //没有登录.
            resultMaps.put("code", "0");//0 抢购失败.
            resultMaps.put("msg", "抢购失败!请你先登录!");
            return resultMaps;
        }

        //优化之前的操作,还是从mysql中进行状态的查看的...
        //String status = secKillService.findStatus(seckillId);

        String status = secKillService.findStatusRedis(seckillId);

        if ("0".equals(status)) {
            resultMaps.put("code", "0");//0 抢购失败.
            resultMaps.put("msg", "秒杀活动尚未开始!");
            return resultMaps;
        }

        if ("2".equals(status)) {
            resultMaps.put("code", "0");//0 抢购失败.
            resultMaps.put("msg", "秒杀活动已经结束!明天再来!");
            return resultMaps;
        }

        //根据key来删除
        ListOperations lops = redisTemplate.opsForList();

        //返回的就是你删除的那个
        Object o = lops.leftPop("seckill_product_" + seckillId);

        //判断是否秒杀商品已经是否被抢购一空,来晚了
        if (o == null) {
            resultMaps.put("code", "0");//0 抢购失败.
            resultMaps.put("msg", "抢购名额已经抢购一空了!");
            return resultMaps;
        }

        //业务 - 保证每个人只能抢购某个商品1次,不能重复抢购.
        //redis中set集合...
        //key->user_product_1   value->tom,jack,james,lilei
        //key->user_product_2   value->jack

        //判断用户有无成功抢购信息
        SetOperations sets = redisTemplate.opsForSet();

        Boolean flag = sets.isMember("user_product_" + seckillId, uname);

        if (flag) {//true
            resultMaps.put("code", "0");//0 抢购失败.
            resultMaps.put("msg", "秒杀名额只有1个!不能重复抢购!");

            //要抢到的这个重复的名额,再还给list集合中去
            lops.leftPush("seckill_product_" + seckillId, seckillId);

            return resultMaps;
        }

        //终于正常秒杀以及抢购了...
        sets.add("user_product_" + seckillId, uname);

        resultMaps.put("code", "1");//0 抢购成功
        resultMaps.put("msg", "恭喜你!抢购成功!");

        //放入到消息队列中...
        secKillService.save2Rbm(seckillId, uname);

        return resultMaps;
    }
}
