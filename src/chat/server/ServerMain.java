package chat.server;

import java.util.logging.Logger;

import chat.utils.Configuration;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 *  服务器主类
 */
public class ServerMain {
	
	private final static Logger LOGGER = Logger.getLogger("ServerMain");

	// 服务器监听的端口
	private int port;

	public ServerMain(int port) {
		this.port = port;
	}

	public static void main(String[] args) {
		new ServerMain(Configuration.getPort()).run();
	}

	public void run() {
		// 用来接收进来的连接
		EventLoopGroup acceptor = new NioEventLoopGroup();
		// 用来处理已经被接收的连接
		EventLoopGroup worker = new NioEventLoopGroup();
		// 启动 NIO 服务的辅助启动类
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
		bootstrap.group(acceptor, worker);
		bootstrap.channel(ChatNioServerSocketChannel.class);
		bootstrap.childHandler(new ServerIniterHandler());
		try {
			Channel channel = bootstrap.bind(port).sync().channel();
			LOGGER.info("server strart running in port :" + port);
			channel.closeFuture().sync();
		} catch (InterruptedException e) {
			LOGGER.info("server strart failed, reason is " + e.getMessage());
		} finally {
			acceptor.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
}
