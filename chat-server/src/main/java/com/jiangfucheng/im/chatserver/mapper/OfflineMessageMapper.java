package com.jiangfucheng.im.chatserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiangfucheng.im.model.po.OfflineMessagePo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 22:24
 *
 * @author jiangfucheng
 */
@Mapper
public interface OfflineMessageMapper extends BaseMapper<OfflineMessagePo> {

	List<OfflineMessagePo> selectOfflineMessage(@Param("userId") Long userId,
												@Param("friendId") Long friendId,
												@Param("lastMsgId") Long lastMsgId);

	void removeReceivedOfflineMessage(@Param("userId") Long userId,
									  @Param("friendId") Long friendId,
									  @Param("id") Long id);

}
