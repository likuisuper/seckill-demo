package tech.aistar.mapper;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * 本类功能:
 *
 * @author cxylk
 * @date 2020/10/22 14:46
 */
public interface SeckillMapper {

    /**
     * 测试环境用的 - 和秒杀业务无关...
     * key - 列的名称
     * value - 列的值
     */
    @Select("select * from web_seckill")
    List<Map<String,Object>> findAll();

    /**
     * 加载到redis数据库中.
     * 秒杀尚未开始
     * @return
     */
    @Select("select * from web_seckill where startTime>now() and status = '0' ")
    List<Map<String,Object>> unStartKill();

    /**
     * 正在秒杀中
     * 将状态{0}->{1}
     */
    @Update("update web_seckill set status='1' where startTime<now() and endTime>now() and status='0'")
    int updateStarting();

    /**
     * 把正在秒杀活动中的状态设置成秒杀已经结束
     * 状态{1}->{2}
     * 秒杀结束
     * @return
     */
    @Update("update web_seckill set status='2' where endTime<now() and status='1'")
    int endKill();

    /**
     * 查秒杀表的状态
     * @return
     */
    @Select("select status from web_seckill where id=#{value}")
    String findStatus(Long seckillId);

    /**
     * 查询所有正在秒杀的商品id
     * @return 商品的id
     */
    @Select("select id from web_seckill where startTime<now() and endTime>now() and status='1'")
    List<Long> findKillStarting();

    /**
     * 获取所有秒杀活动之后的商品的id
     * @return
     */
    @Select("select id from web_seckill where endTime<now() and status='2'")
    List<Long> findKillFinished();

}
