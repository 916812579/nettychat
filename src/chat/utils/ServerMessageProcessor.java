package chat.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import chat.bean.Message;
import chat.bean.TextMessage;
import io.netty.channel.Channel;

/**
 * 消息处理器
 */
public class ServerMessageProcessor {
	
	private final static Logger LOGGER = Logger.getLogger("ServerMain");

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
		case Message.PING:
			processPing(msg);
			break;
		}
		
	}
	
	
	/**
	 *  处理客户端响应ping的消息
	 */
	private static void processPing(Message msg) {
		LOGGER.info(msg.toString());
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
