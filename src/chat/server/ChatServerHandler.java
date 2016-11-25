package chat.server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import chat.bean.Message;
import chat.utils.ServerMessageProcessor;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 处理客户端的消息
 */
public class ChatServerHandler extends SimpleChannelInboundHandler<Message> {

	private final static Logger LOGGER = Logger.getLogger("ServerMain");
	
	public static final ChannelGroup group = new DefaultChannelGroup(
			GlobalEventExecutor.INSTANCE);
	
	private static ConcurrentHashMap<String, Channel> map = new ConcurrentHashMap<String, Channel>();

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg)
			throws Exception {
		Channel channel = ctx.channel();
		ServerMessageProcessor.process(channel, msg, map);
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		for (Channel ch : group) {
			ch.writeAndFlush(
					"[" + channel.remoteAddress() + "] " + "is comming");
		}
		LOGGER.info("[" + channel.remoteAddress() + "] " + "is comming");
		group.add(channel);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		for (Channel ch : group) {
			ch.writeAndFlush(
					"[" + channel.remoteAddress() + "] " + "is removed");
		}
		LOGGER.info("[" + channel.remoteAddress() + "] " + "is removed");
		removeChanel(channel);
		group.remove(channel);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		LOGGER.info("[" + channel.remoteAddress() + "] " + " is online ");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		removeChanel(channel);
		LOGGER.info("[" + channel.remoteAddress() + "] " + " is offline ");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		Channel channel = ctx.channel();
		LOGGER.info("[" + channel.remoteAddress() + "] " + " exit ");
		removeChanel(ctx.channel());
		ctx.close().sync();
	}
	
	/**
	 * 移除没有用的 channel
	 * @param channel
	 */
	private synchronized void  removeChanel(Channel channel) {
		for (String key : map.keySet()) {		 
			if (map.get(key) == channel) {
				map.remove(key);
			}
		}
	}

}
