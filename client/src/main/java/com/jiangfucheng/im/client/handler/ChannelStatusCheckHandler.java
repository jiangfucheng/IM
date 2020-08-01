package com.jiangfucheng.im.client.handler;

import com.jiangfucheng.im.protobuf.Base;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/8/1
 * Time: 15:39
 *
 * @author jiangfucheng
 */
public class ChannelStatusCheckHandler  extends SimpleChannelInboundHandler<Base.Message> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Base.Message msg) throws Exception {

	}

}
