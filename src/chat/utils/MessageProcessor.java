package chat.utils;

import java.util.concurrent.ConcurrentHashMap;

import chat.bean.Message;
import chat.bean.TextMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * 消息处理器
 *
 */
public class MessageProcessor {

	/**
	 * 消息编码
	 */
	public static ByteBuf encode(ChannelHandlerContext ctx, Message msg)
			throws Exception {
		if (msg == null) {
			return null;
		}
		ByteBuf buf = msg.encode(ctx, msg);
		return buf;
	}

	/**
	 * 消息解码
	 */
	public static Message decode(ByteBuf buf) throws Exception {
		if (buf == null || buf.readableBytes() <= 0) {
			return null;
		}
		int type = buf.getInt(4);
		Message msg = null;
		switch (type) {
		case Message.TEXT:
			msg = new TextMessage();
			break;

		default:
			break;
		}
		if (msg != null) {
			msg.decode(buf);
		}
		return msg;
	}

	/**
	 * 对消息进行处理
	 */
	public static void process(Channel ch, Message msg,
			ConcurrentHashMap<String, Channel> map) {		
		if (map.get(msg.getId()) == null) {
			map.put(msg.getId(), ch);
		}
		int type = msg.getType();
		switch (type) {
		case Message.TEXT:
			processText(ch, msg, map);
			break;
		}
		
	}
	
	/**
	 * 处理文本类消息
	 */
	public static void processText(Channel ch, Message msg,
			ConcurrentHashMap<String, Channel> map) {
		TextMessage message = (TextMessage)msg;
		String toId = message.getToId();
		boolean isFriendOnline = false;
		if (toId != null) {
			Channel toCh = map.get(toId);
			if (toCh != null && toCh.isWritable()) {
				isFriendOnline = true;
				toCh.writeAndFlush(message);
			}
		} 
		if (toId != null && !isFriendOnline) {
			message = new TextMessage();
			message.setContent("好友不在线");
			message.setToId(msg.getId());
			message.setId("系统");
			ch.writeAndFlush(message);
		}
	}

}
