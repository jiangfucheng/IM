package com.jiangfucheng.im.client;

import com.jiangfucheng.im.client.chat.ChatClient;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/21
 * Time: 21:25
 *
 * @author jiangfucheng
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.jiangfucheng.im.client.feign"})
public class ChatClientApplication implements CommandLineRunner {

	private final ChatClient chatClient;

	@Autowired
	public ChatClientApplication(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	public static void main(String[] args) {
		SpringApplication.run(ChatClientApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ChannelFuture channelFuture = chatClient.start();
		Runtime.getRuntime().addShutdownHook(new Thread(chatClient::stop));
		channelFuture.channel().closeFuture().sync();
	}
}
