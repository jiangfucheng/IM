package com.jiangfucheng.im.chatserver.chat;

import com.jiangfucheng.im.chatserver.config.properties.HeartBeatProperties;
import com.jiangfucheng.im.chatserver.handler.ChatServerHandler;
import com.jiangfucheng.im.chatserver.utils.CommonUtils;
import com.jiangfucheng.im.common.constants.ZookeeperConstants;
import com.jiangfucheng.im.protobuf.Base;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/20
 * Time: 21:39
 *
 * @author jiangfucheng
 */
@Slf4j
public class ChatServer {

	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	private ChatServerHandler chatServerHandler;
	private Channel channel;
	private String localIp;
	private int port;

	@Autowired
	private ZkClient zkClient;
	@Autowired
	private HeartBeatProperties heartBeatProperties;

	public ChatServer(ChatServerHandler chatServerHandler, int port, ZkClient zkClient) {
		this.zkClient = zkClient;
		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup();
		this.port = port;
		this.chatServerHandler = chatServerHandler;
		try {
			this.localIp = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}


	public ChannelFuture start() {
		ChannelFuture channelFuture = null;
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.DEBUG));

			serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();
					pipeline.addLast(new ProtobufVarint32FrameDecoder());
					pipeline.addLast(new ProtobufDecoder(Base.Message.getDefaultInstance()));
					pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
					pipeline.addLast(new ProtobufEncoder());
					pipeline.addLast(new IdleStateHandler(heartBeatProperties.getPeriod(), heartBeatProperties.getPeriod() + 10000, heartBeatProperties.getPeriod() + 10000));
					pipeline.addLast(chatServerHandler);
				}
			});
			channelFuture = serverBootstrap.bind(port).sync();
			channel = channelFuture.channel();
			registerHostToZookeeper();
			log.info("chat server " + this.localIp + ":" + port + " 启动成功");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return channelFuture;
	}

	public void destroy() {
		if (channel != null) {
			channel.close();
		}
		this.bossGroup.shutdownGracefully();
		this.workerGroup.shutdownGracefully();
		cancelRegisteredZookeeper();
		log.info("chat server " + this.localIp + ":" + this.port + " 关闭");

	}

	private void registerHostToZookeeper() {
		if (!zkClient.exists(ZookeeperConstants.CHAT_SERVER_HOST_PATH)) {
			zkClient.createPersistent(ZookeeperConstants.CHAT_SERVER_HOST_PATH);
		}
		String urlPath = CommonUtils.generateZookeeperUrlPath(this.localIp, this.port);
		zkClient.createEphemeral(urlPath);
	}

	private void cancelRegisteredZookeeper() {
		String urlPath = CommonUtils.generateZookeeperUrlPath(this.localIp, this.port);
		if (zkClient.exists(urlPath)) {
			zkClient.delete(urlPath);
			log.info("chat server connection utils {} removed", urlPath);
		}
	}


}
