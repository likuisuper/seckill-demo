package tech.aistar.service;

/**
 * 本类功能:
 *
 * @author cxylk
 * @date 2020/10/22 14:59
 */
public interface IOrderService {
    int insert(String orderNo, String uName,  String orderStatus, Long secId);
}
