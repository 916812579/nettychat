package chat.utils;

 
import java.util.List;

import chat.bean.Message;
import chat.bean.TextMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 *  消息编码器
 */
public class MessageEncoder extends MessageToMessageEncoder<Message> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg,
			List<Object> out) throws Exception {
		if (msg == null) {
			return;
		}
		ByteBuf buf = MessageProcessor.encode(ctx, msg);
		if (buf != null) {
			out.add(buf);
		}	
	}

}
