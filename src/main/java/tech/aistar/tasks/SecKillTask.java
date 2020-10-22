package tech.aistar.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tech.aistar.service.ISecKillService;

import java.util.List;
import java.util.Map;

/**
 * 本类功能:SpringBoor任务调度
 *
 * @author cxylk
 * @date 2020/10/22 15:38
 */
@Component
public class SecKillTask {
    @Autowired
    private ISecKillService secKillService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     *  让系统隔一段时间来检测一下数据库中是否有新增的参与秒杀的商品[但是是在秒杀活动之前],
     *  加载到redis数据库中.
     */
    @Scheduled(cron = "0/2 * * * * *")
    public void testHello(){
        List<Map<String,Object>> lists = secKillService.unStartKill();
        if(null!=lists && lists.size()>0){

            System.out.println("秒杀尚未开始...");

//            假设这俩个商品都是属于尚未开始秒杀的.
//                    key -> seckill_product_1  value->[3,3,3,3,3,3,....3]
//            key -> seckill_product_2  value->[1,1,1,1,1]

            //获取redis的集合对象
            ListOperations lops = redisTemplate.opsForList();//有序可重复

            //操作string字符串 - 产品的秒杀状态...
            ValueOperations vop = redisTemplate.opsForValue();

            //遍历lists集合
            for (Map<String, Object> map : lists) {
                //获取秒杀表的主键
                Long id = (Long) map.get("id");

                //获取秒杀的数量num
                Integer num = (Integer) map.get("num");

                //放入到redis数据库中的
                //回忆redis纯操作列表的命令
                //set ids 1
                //set ids 2
                //set ids 3

                //只要加载一次
                Long size = lops.size("seckill_product_"+id);

                if(size==0) {

                    //存放产品的秒杀状态
                    vop.set("seckill_product_status_"+id,"0");

                    for (int i = 0; i < num; i++) {
                        //seckill_product_1,1,1,1,,1,1,1,1,1,1...1
                        //seckill_product_2   2,2,2,2,2
                        lops.leftPush("seckill_product_" + id, id);
                    }
                }
            }
        }
    }

    /**
     * {0}->{1}
     */
    @Scheduled(cron = "0/2 * * * * *")
    public void update021(){


        //获取所有正在秒杀过程中的所有的产品的id
        List<Long> ids = secKillService.findKillStarting();

        if(null!=ids && ids.size()>0){

            System.out.println("秒杀进行中...");

            ValueOperations vop = redisTemplate.opsForValue();

            for (Long id : ids) {
                vop.set("seckill_product_status_"+id,"1");
            }
        }

        int count = secKillService.updateStarting();
    }

    /**
     * {1}->{2}
     */
    @Scheduled(cron = "0/2 * * * * *")
    public void update122(){

        //获取所有正在秒杀过程中的所有的产品的id
        List<Long> ids = secKillService.findKillFinished();
        if(null!=ids && ids.size()>0){

            System.out.println("delete 秒杀活动结束....");

//            ValueOperations vop = redisTemplate.opsForValue();
//
//            for (Long id : ids) {
//                vop.set("seckill_product_status_"+id,"2");
//            }

            for(Long id:ids){
                //直接删除
                redisTemplate.delete("seckill_product_"+id);//删除的是秒杀活动已经结束还没有卖完的商品.

                redisTemplate.delete("seckill_product_status_"+id);//删除秒杀商品状态.
            }

        }

        int count = secKillService.endKill();
    }
}
