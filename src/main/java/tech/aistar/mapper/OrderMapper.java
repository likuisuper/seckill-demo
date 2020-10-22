package tech.aistar.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * 本类功能:
 *
 * @author cxylk
 * @date 2020/10/22 14:46
 */
public interface OrderMapper {
    @Insert("insert into web_order values(null,#{orderNo},#{uName},#{orderStatus},#{secId})")
    int insert(@Param("orderNo") String orderNo,@Param("uName") String uName, @Param("orderStatus") String orderStatus,@Param("secId") Long secId);
}
