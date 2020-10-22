package tech.aistar.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.aistar.mapper.OrderMapper;
import tech.aistar.service.IOrderService;

/**
 * 本类功能:
 *
 * @author cxylk
 * @date 2020/10/22 15:00
 */
@Service
@Transactional
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Override
    public int insert(String orderNo, String uName, String orderStatus, Long secId) {
        return orderMapper.insert(orderNo,uName,orderStatus,secId);
    }
}
