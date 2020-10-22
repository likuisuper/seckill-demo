package tech.aistar.service;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 本类功能:
 *
 * @author cxylk
 * @date 2020/10/22 14:59
 */
public interface ISecKillService {
    //获取秒杀未开始的所有的商品
    List<Map<String,Object>> unStartKill();

    /**
     * 正在秒杀中
     * 将状态{0}->{1}
     */
    //@Update("update web_seckill set status='1' where startTime<now() and endTime>now() and status='0'")
    int updateStarting();

    /**
     * 状态{1}->{2}
     * 秒杀结束
     * @return
     */
    // @Update("update web_seckill set status='2' where endTime<now() and status='1'")
    int endKill();

    /**
     * 查秒杀表的状态
     * @return
     */
    @Select("select status from web_seckill where id=#{value}")
    String findStatus(Long seckillId);

    //查询所有正在秒杀的商品id
    List<Long> findKillStarting();

    //查询秒杀活动之后秒杀剩余的商品id
    List<Long> findKillFinished();

    /**
     * 从redis数据库中去获取产品的状态.
     * @param seckillId
     * @return
     */
    String findStatusRedis(Long seckillId);

    /**
     * 将秒杀的关键的信息放入到rabbitmq中去...
     * @param secId
     * @param uname
     */
    void save2Rbm(Long secId,String uname);
}
