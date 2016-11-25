package chat.bean;

import chat.utils.Configuration;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 *  处理登录和退出操作
 */
public class AuthMessage extends InteractionMessage {
	// 密码
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


}
