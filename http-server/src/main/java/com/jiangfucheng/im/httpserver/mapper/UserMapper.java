package com.jiangfucheng.im.httpserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiangfucheng.im.httpserver.bo.UserInfoBo;
import com.jiangfucheng.im.httpserver.po.UserPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 22:19
 *
 * @author jiangfucheng
 */
@Mapper
public interface UserMapper extends BaseMapper<UserPo> {

	@Select("select password from im_user where id = #{id}")
	String getPasswordById(@Param("id") Long userId);

	@Update("update im_user set password = #{password} where id = #{id}")
	Integer updatePassword(@Param("id") Long userId, @Param("password") String password);

	@Select("select id from im_user where account = #{account} and password = #{password}")
	Long hasUser(@Param("account") String account, @Param("password") String password);

	@Select("select id,nick_name,account from im_user where account = #{account}")
	UserInfoBo getUserInfoByAccount(@Param("account") String account);

	@Select("select id,nick_name,account from im_user where id = #{id}")
	UserInfoBo getUserInfoById(@Param("id") Long id);
}
