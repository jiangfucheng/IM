package com.jiangfucheng.im.chatserver;

import com.jiangfucheng.im.chatserver.chat.ChatServer;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/20
 * Time: 21:22
 *
 * @author jiangfucheng
 */
@SpringBootApplication
public class ChatServerApplication implements CommandLineRunner {

	private final ChatServer chatServer;

	@Autowired
	public ChatServerApplication(ChatServer chatServer) {
		this.chatServer = chatServer;
	}

	public static void main(String[] args) {
		SpringApplication.run(ChatServerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ChannelFuture channelFuture = chatServer.start();
		Runtime.getRuntime().addShutdownHook(new Thread(chatServer::destroy));
		//阻塞直到服务器关闭
		channelFuture.channel().closeFuture().sync();
	}
}
