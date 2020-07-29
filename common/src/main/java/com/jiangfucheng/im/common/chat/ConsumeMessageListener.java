package com.jiangfucheng.im.common.chat;

import com.jiangfucheng.im.protobuf.Base;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/23
 * Time: 23:14
 *
 * @author jiangfucheng
 */
public interface ConsumeMessageListener {
	void consume(Base.Message msg);
}
