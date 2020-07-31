package com.jiangfucheng.im.client.chat;

import com.jiangfucheng.im.client.command.CommandParser;
import com.jiangfucheng.im.client.command.executor.CommandExecutor;
import com.jiangfucheng.im.client.context.ChatClientContext;
import com.jiangfucheng.im.client.exception.CommandParseException;
import com.jiangfucheng.im.client.handler.ChatClientHandler;
import com.jiangfucheng.im.protobuf.Base;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/21
 * Time: 21:29
 *
 * @author jiangfucheng
 */
@Slf4j
public class ChatClient {
	private EventLoopGroup eventLoopGroup;
	private ChatClientHandler chatClientHandler;
	Channel channel;

	private ChatClientContext context;

	public ChatClient(ChatClientHandler chatClientHandler, ChatClientContext context) {
		this.context = context;
		this.eventLoopGroup = new NioEventLoopGroup();
		this.chatClientHandler = chatClientHandler;

	}

	public ChannelFuture start() {
		Bootstrap bootstrap = new Bootstrap();
		ChannelFuture channelFuture = null;
		bootstrap.group(eventLoopGroup)
				.channel(NioSocketChannel.class)
				.handler(new LoggingHandler(LogLevel.DEBUG))
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel socketChannel) throws Exception {
						ChannelPipeline pipeline = socketChannel.pipeline();
						pipeline.addLast(new ProtobufVarint32FrameDecoder());
						pipeline.addLast(new ProtobufDecoder(Base.Message.getDefaultInstance()));
						pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
						pipeline.addLast(new ProtobufEncoder());
						pipeline.addLast(chatClientHandler);
					}
				});
		resolveCommand();
		//等待用户登陆
		while (context.getChatServerUrl() == null || "".equals(context.getChatServerUrl())) {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String[] chatServerAddress = context.getChatServerUrl().split(":");
		channelFuture = bootstrap.connect(chatServerAddress[0], Integer.valueOf(chatServerAddress[1]))
				.addListener((ChannelFutureListener) future -> {
					if (future.isSuccess()) {
						log.info("client connect to server success");
					} else {
						log.error("client connect to server failed");
					}
				});
		channel = channelFuture.channel();
		context.setChannel(channel);
		return channelFuture;
	}

	@Autowired
	private CommandParser commandParser;

	private void resolveCommand() {
		Thread thread = new Thread(() -> {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				try {
					System.out.print(">");
					String command = reader.readLine();
					CommandExecutor executor = commandParser.parse(command);
					executor.execute();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (CommandParseException e) {
					System.out.println(e.getMessage());
				}
			}
		});
		thread.setDaemon(true);
		thread.start();
	}

	public void stop() {
		channel.close();
		this.eventLoopGroup.shutdownGracefully();
		log.info("chat client stopped");
	}
}
