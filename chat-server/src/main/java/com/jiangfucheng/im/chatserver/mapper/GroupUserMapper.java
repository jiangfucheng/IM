package com.jiangfucheng.im.chatserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiangfucheng.im.model.po.GroupUserPo;
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
public interface GroupUserMapper extends BaseMapper<GroupUserPo> {

	@Select("select user_id from im_group_user" +
			"where group_id = #{group_id} and role_id in (0,1)")
	List<Long> getGroupOwnerAndManagersByGroupId(@Param("groupId") Long groupId);
}
