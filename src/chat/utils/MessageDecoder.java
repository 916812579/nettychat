package chat.utils;

import java.util.List;

import chat.bean.Message;
import chat.bean.TextMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.ReferenceCountUtil;

/**
 * 解码消息
 */
public class MessageDecoder extends MessageToMessageDecoder<ByteBuf> {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf,
			List<Object> out) throws Exception {
		Message message = MessageProcessor.decode(buf);		
		if (message != null) {
			out.add(message);
		}
	} 
}
