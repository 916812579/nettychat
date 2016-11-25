package chat.server;

import chat.utils.MessageDecoder;
import chat.utils.MessageEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class ServerIniterHandler extends  ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		// 解码
		pipeline.addLast("docode",new MessageDecoder());
		// 编码
		pipeline.addLast("encode",new MessageEncoder());
		pipeline.addLast("idleStateHandler", new IdleStateHandler(20, 10, 60));  
		pipeline.addLast("heatbeat", new HeatBeatHandler());
		pipeline.addLast("chat",new ChatServerHandler());
		
	}

}
