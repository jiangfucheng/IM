package com.jiangfucheng.im.chatserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiangfucheng.im.model.dto.LastReceivedMessageBo;
import com.jiangfucheng.im.model.po.SyncGroupMessagePo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 22:26
 *
 * @author jiangfucheng
 */
@Mapper
public interface SyncGroupMessageMapper extends BaseMapper<SyncGroupMessagePo> {
	@Update("update im_sync_group_msg set last_msg_id = #{lastReceivedMsgId}" +
			"where user_id = #{user_id} and group_id = #{group_id} and last_msg_id < #{lastReceivedMsgId}")
	void updateLastReceivedMessage(LastReceivedMessageBo lastReceivedMsg);
}
