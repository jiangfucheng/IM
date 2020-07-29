package com.jiangfucheng.im.httpserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiangfucheng.im.model.po.MessagePo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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
			"where ((from_id = #{userId} and to_id = #{friendId}) or  (from_id = #{friendId} and to_id = #{userId}))" +
			"order by create_time desc limit 1")
	MessagePo selectLastMessage(@Param("userId") Long userId, @Param("friendId") Long friendId);

	@Select("select id, from_id, to_id, msg_type, content, delivered, create_time " +
			"from im_msg " +
			"where ((from_id = #{userId} and to_id = #{friendId}) or  (from_id = #{friendId} and to_id = #{userId}))" +
			"and id < #{mastMsgId} " +
			"order by create_time limit #{number}")
	List<MessagePo> selectHistoryMessages(@Param("userId") Long userId,
										  @Param("friendId") Long friendId,
										  @Param("lastMsgId") Long lastMsgId,
										  @Param("number") Integer number);

}
