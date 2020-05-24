package com.jiangfucheng.im.common.chat;

import com.jiangfucheng.im.protobuf.Base;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/20
 * Time: 23:42
 *
 * @author jiangfucheng
 */
@Slf4j
public class ChatMessageDispatcher {
	private Map<Base.DataType, Method> allHandlers;
	private Map<Method, Object> methodObjectMap;


	public ChatMessageDispatcher() {
		allHandlers = new HashMap<>();
		methodObjectMap = new HashMap<>();
		log.info("ChatMessageDispatcher加载成功");
	}


	public void registerMethod(Base.DataType dataType, Method method) {
		allHandlers.put(dataType, method);
	}

	public void registerMethodControllerMap(Method method, Object controller) {
		this.methodObjectMap.put(method, controller);
	}


	public void dispatch(ChannelHandlerContext ctx, Base.Message message) {
		Base.DataType dataType = message.getDataType();
		Method handler = allHandlers.get(dataType);
		Object obj = methodObjectMap.get(handler);
		try {
			handler.invoke(obj, ctx, message);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
