package chat.client;

import chat.bean.Message;
import chat.utils.ClientMessageProcessor;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ChatClientHandler extends SimpleChannelInboundHandler<Message> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg)
			throws Exception {
		Channel ch = ctx.channel();
		ClientMessageProcessor.process(ch, msg);
	}

}
