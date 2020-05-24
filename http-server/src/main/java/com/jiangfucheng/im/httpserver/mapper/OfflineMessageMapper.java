package com.jiangfucheng.im.httpserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiangfucheng.im.model.po.OfflineMessagePo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 22:24
 *
 * @author jiangfucheng
 */
@Mapper
public interface OfflineMessageMapper extends BaseMapper<OfflineMessagePo> {

	@Select("select id, from_id, to_id, msg_type, content, create_time " +
			"from im_offline_msg " +
			"where from_id = #{friendId} and to_id = #{id} " +
			"order by create_time desc limit 1")
	OfflineMessagePo selectLastOfflineMessage(@Param("id") Long userId, @Param("friendId") Long friendId);

}
