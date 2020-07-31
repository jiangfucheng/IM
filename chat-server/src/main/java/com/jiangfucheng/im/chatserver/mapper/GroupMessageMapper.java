package com.jiangfucheng.im.chatserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiangfucheng.im.model.po.GroupMessagePo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 22:22
 *
 * @author jiangfucheng
 */
@Mapper
public interface GroupMessageMapper extends BaseMapper<GroupMessagePo> {

	@Select("select id, group_id, from_id, msg_type, content, create_time " +
			"from im_group_msg " +
			"where group_id = #{groupId} " +
			"order by create_time desc limit 1")
	GroupMessagePo selectLastMessage(@Param("groupId") Long groupId);
}
