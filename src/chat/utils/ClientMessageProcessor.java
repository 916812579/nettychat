package chat.utils;


import java.util.logging.Logger;

import chat.bean.Message;
import chat.bean.PingMessage;
import chat.bean.TextMessage;
import io.netty.channel.Channel;

/**
 * 消息处理器
 */
public class ClientMessageProcessor {

	private final static Logger LOGGER = Logger.getLogger("ServerMain");
	/**
	 * 对消息进行处理
	 */
	public static void process(Channel ch, Message msg) {
		int type = msg.getType();
		switch (type) {
		case Message.TEXT:
			processText(ch, msg);
			break;
		case Message.PING:
			processPing(ch, msg);
			break;
		}

	}

	/**
	 *  处理心跳消息
	 */
	private static void processPing(Channel ch, Message msg) {
		ch.writeAndFlush(PingMessage.getPingReceived());
	}

	/**
	 * 处理文本类消息
	 */
	public static void processText(Channel ch, Message msg) {
		TextMessage message = (TextMessage) msg;
		LOGGER.info(message.toString());
	}

}
