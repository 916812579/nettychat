package chat.server;

import chat.utils.MessageDecoder;
import chat.utils.MessageEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class ServerIniterHandler extends  ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		// 解码
		pipeline.addLast("docode",new MessageDecoder());
		// 编码
		pipeline.addLast("encode",new MessageEncoder());
		pipeline.addLast("chat",new ChatServerHandler());
		
	}

}
