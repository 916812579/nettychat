package chat.utils;

import java.util.List;

import chat.bean.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * 解码消息
 */
public class MessageDecoder extends MessageToMessageDecoder<ByteBuf> {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf,
			List<Object> out) throws Exception {
		try {
			Message message = MessageCoder.decode(buf);		
			if (message != null) {
				out.add(message);
			}
		} catch (Exception e) {			
			e.printStackTrace();
			throw e;
		}
		
	} 
}
