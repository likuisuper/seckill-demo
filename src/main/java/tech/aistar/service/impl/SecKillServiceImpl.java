package tech.aistar.service.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.aistar.mapper.SeckillMapper;
import tech.aistar.service.ISecKillService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 本类功能:
 *
 * @author cxylk
 * @date 2020/10/22 15:02
 */
@Service
@Transactional
public class SecKillServiceImpl implements ISecKillService {
    @Autowired
    private SeckillMapper seckillMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public List<Map<String, Object>> unStartKill() {
        return seckillMapper.unStartKill();
    }

    @Override
    public int updateStarting() {
        return seckillMapper.updateStarting();
    }

    @Override
    public int endKill() {
        return seckillMapper.endKill();
    }

    @Override
    public String findStatus(Long seckillId) {
        return seckillMapper.findStatus(seckillId);
    }

    @Override
    public List<Long> findKillStarting() {
        return seckillMapper.findKillStarting();
    }

    @Override
    public List<Long> findKillFinished() {
        return seckillMapper.findKillFinished();
    }

    @Override
    public String findStatusRedis(Long seckillId) {
        ValueOperations vop = redisTemplate.opsForValue();

        String status = (String) vop.get("seckill_product_status_"+seckillId);

        if(null==status){
            return "0";
        }

        return status;
    }

    @Override
    public void save2Rbm(Long secId, String uname) {
        Map<String,Object> dataMaps = new HashMap<>();

        dataMaps.put("secId",secId);
        dataMaps.put("uname",uname);


        //放入到rabbitmq中
        //分裂模式 - j02_exch

        rabbitTemplate.convertAndSend("hello_exch",null,dataMaps);
    }
}
