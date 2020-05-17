package com.jiangfucheng.im.httpserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiangfucheng.im.httpserver.bo.UserInfoBo;
import com.jiangfucheng.im.httpserver.po.UserPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 22:19
 *
 * @author jiangfucheng
 */
@Mapper
public interface UserMapper extends BaseMapper<UserPo> {

	@Select("select password from im_user where id = #{userId}")
	String getPasswordById(Long userId);

	@Select("update im_user set password = #{password} where id = #{userId}")
	int updatePassword(Long userId, String password);

	@Select("select id from im_user where account = #{account} and password = #{password}")
	Long hasUser(String account, String password);

	@Select("select id,nick_name,account from im_user where account = #{account}")
	UserInfoBo getUserInfoByAccount(String account);

	@Select("select id,nick_name,account from im_user where id = #{id}")
	UserInfoBo getUserInfoById(Long id);
}
