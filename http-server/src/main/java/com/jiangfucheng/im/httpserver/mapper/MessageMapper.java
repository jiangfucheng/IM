package com.jiangfucheng.im.httpserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiangfucheng.im.httpserver.po.MessagePo;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 22:23
 *
 * @author jiangfucheng
 */
@Mapper
public interface MessageMapper extends BaseMapper<MessagePo> {
}
