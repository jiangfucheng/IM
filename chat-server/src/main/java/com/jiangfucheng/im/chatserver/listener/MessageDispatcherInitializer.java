package com.jiangfucheng.im.chatserver.listener;

import com.jiangfucheng.im.common.chat.ChatMessageController;
import com.jiangfucheng.im.common.chat.ChatMessageDispatcher;
import com.jiangfucheng.im.common.chat.ChatMessageMapping;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/21
 * Time: 23:44
 *
 * @author jiangfucheng
 */
@Configuration
public class MessageDispatcherInitializer implements ApplicationListener<ApplicationStartedEvent> {

	private ChatMessageDispatcher messageDispatcher;

	public MessageDispatcherInitializer(ChatMessageDispatcher messageDispatcher) {
		this.messageDispatcher = messageDispatcher;
	}

	@Override
	public void onApplicationEvent(ApplicationStartedEvent  event) {
		ConfigurableApplicationContext context = event.getApplicationContext();
		Map<String, Object> controllers = context.getBeansWithAnnotation(ChatMessageController.class);
		controllers.values().forEach(controller -> {
			Method[] methods = controller.getClass().getDeclaredMethods();
			for (Method method : methods) {
				ChatMessageMapping messageMapping = method.getAnnotation(ChatMessageMapping.class);
				if (messageMapping != null) {
					messageDispatcher.registerMethod(messageMapping.messageType(), method);
					messageDispatcher.registerMethodControllerMap(method, controller);
				}
			}
		});
	}
}
