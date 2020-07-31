package com.jiangfucheng.im.chatserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiangfucheng.im.model.bo.FriendRemarksBo;
import com.jiangfucheng.im.model.po.RelationPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 22:25
 *
 * @author jiangfucheng
 */
@Mapper
public interface RelationMapper extends BaseMapper<RelationPo> {

	@Select("select user_id,friend_id,remarks,create_time from im_relation where user_id = #{userId} and friend_id = #{friendId}")
	RelationPo getRelationByUserIdAndFriendId(Long userId, Long friendId);

	@Update("update im_relation set remarks = #{remarks} where user_id = #{userId} and friend_id = #{friendId}")
	void updateFriendRemarks(FriendRemarksBo remarksBo);
}
