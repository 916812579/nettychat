package chat.client;

import chat.utils.MessageDecoder;
import chat.utils.MessageEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class ClientIniterHandler extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel arg0) throws Exception {
		ChannelPipeline pipeline = arg0.pipeline();
		// 解码
		pipeline.addLast("docode", new MessageDecoder());
		// 编码
		pipeline.addLast("encode", new MessageEncoder());
		
		pipeline.addLast("chat", new ChatClientHandler());
	}

}
