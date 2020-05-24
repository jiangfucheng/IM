package com.jiangfucheng.im.httpserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiangfucheng.im.model.po.NotifyPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 22:24
 *
 * @author jiangfucheng
 */
@Mapper
public interface NotifyMapper extends BaseMapper<NotifyPo> {

	@Select("select im_notify.id, type, from_id, to_id, content, im_notify.create_time " +
			"from im_notify,im_recently_notify " +
			"where im_notify.id = im_recently_notify.notify_id " +
			"and user_id = #{id}")
	List<NotifyPo> selectRecentlyNotifies(@Param("id") Long userId);

}
