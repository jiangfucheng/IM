package com.jiangfucheng.im.chatserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiangfucheng.im.model.po.MessagePo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 22:23
 *
 * @author jiangfucheng
 */
@Mapper
public interface MessageMapper extends BaseMapper<MessagePo> {

	@Select("select id, from_id, to_id, msg_type, content, delivered, create_time " +
			"from im_msg " +
			"where from_id = #{friendId} and to_id = #{id} " +
			"order by create_time desc limit 1")
	MessagePo selectLastMessage(@Param("id") Long userId, @Param("friendId") Long friendId);

	@Update("update im_msg set delivered = 1 where id = #{msgId}")
	void markMessageDelivered(@Param("msgId") long msgId);

}
