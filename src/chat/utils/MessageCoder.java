package chat.utils;

import chat.bean.Message;
import chat.bean.PingMessage;
import chat.bean.TextMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * 消息编码/解码处理器
 */
public class MessageCoder {

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
		case Message.PING:
			msg = new PingMessage();
			break;

		default:
			break;
		}
		if (msg != null) {
			msg.decode(buf);
		}
		return msg;
	}
}
