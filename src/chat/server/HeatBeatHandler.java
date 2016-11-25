package chat.server;

import java.util.logging.Logger;

import chat.bean.PingMessage;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 处理心跳
 */
public class HeatBeatHandler extends ChannelDuplexHandler {
	
	private final static Logger LOGGER = Logger.getLogger("HeatBeatHandler");

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if (evt instanceof IdleStateEvent) {		
			IdleStateEvent e = (IdleStateEvent) evt;
			// 如果长时间没有读写则关闭
			if (e.state() == IdleState.ALL_IDLE) {
				ctx.close();
			} else {
				ctx.writeAndFlush(PingMessage.getPingSend());
			}		
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

}
