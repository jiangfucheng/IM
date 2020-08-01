package com.jiangfucheng.im.client.chat;

import com.jiangfucheng.im.client.command.CommandParser;
import com.jiangfucheng.im.client.command.executor.CommandExecutor;
import com.jiangfucheng.im.client.context.ChatClientContext;
import com.jiangfucheng.im.client.enums.UserStatus;
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
	private Channel channel;
	private ChatClientContext context;
	@Autowired
	private ChatClientHandler chatClientHandler;

	public ChatClient(ChatClientContext context) {
		this.context = context;
		this.eventLoopGroup = new NioEventLoopGroup();

	}

	public ChannelFuture start() {
		Bootstrap bootstrap = newBootStrap();
		resolveCommand();
		//等待用户登陆
		while (context.getCurrentUser().getStatus() != UserStatus.ONLINE) {

		}
		String[] chatServerAddress = context.getChatServerUrl().split(":");
		ChannelFuture channelFuture = doConnect(bootstrap, chatServerAddress[0], Integer.valueOf(chatServerAddress[1]));
		channel = channelFuture.channel();
		context.setChannel(channel);
		return channelFuture;
	}

	private Bootstrap newBootStrap() {
		Bootstrap bootstrap = new Bootstrap();
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
		return bootstrap;
	}

	private ChannelFuture doConnect(Bootstrap bootstrap, String serverUrl, Integer serverPort) {
		ChannelFuture channelFuture = bootstrap.connect(serverUrl, serverPort)
				.addListener((ChannelFutureListener) future -> {
					if (future.isSuccess()) {
						log.info("client reConnect to server success");
					} else {
						log.error("client reConnect to server failed");
					}
				});
		channel = channelFuture.channel();
		context.setChannel(channel);
		return channelFuture;
	}

	public void reConnect() throws InterruptedException {
		Bootstrap bootstrap = newBootStrap();
		ChannelFuture channelFuture;
		//if (isOrigServer) {
		String[] serverUri = context.getChatServerUrl().split(":");
		channelFuture = doConnect(bootstrap, serverUri[0], Integer.valueOf(serverUri[1]));
		//}
		channelFuture.channel().closeFuture().sync();
	}

	public void startAndSync() throws InterruptedException {
		ChannelFuture channelFuture = start();
		Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
		channelFuture.channel().closeFuture().sync();
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
