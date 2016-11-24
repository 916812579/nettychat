package chat.bean;

import chat.utils.Configuration;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 *  处理登录和退出操作
 */
public class AuthMessage extends InteractionMessage {
	// 密码长度
	private int passLen;
	// 密码
	private String password;

	public int getPassLen() {
		return (int) (password == null ? 0 : password.getBytes(Configuration.getCharset()).length);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public ByteBuf encode(ChannelHandlerContext ctx, Message msg) {
		AuthMessage message = (AuthMessage) msg;
		int len = FIX_LEN + INT_SIZE + message.getIdLength() + message.getToIdLength()
				+ message.getPassLen();
		ByteBuf buf = ctx.alloc().buffer(len);
		buf.writeBytes(super.encode(ctx, msg));		
		buf.writeInt(message.getPassLen());
		buf.writeCharSequence(message.getPassword(),
				Configuration.getCharset());
		return buf;
	}

	@Override
	public void decode(ByteBuf buf) {
		super.decode(buf);
		int idLen = buf.getInt(2 * INT_SIZE);
		int toIdLen = buf.getInt(idLen + 3 * INT_SIZE);
		int passLen = buf.getInt(idLen + toIdLen + 5 * INT_SIZE + LONG_SIZE);
        CharSequence password = buf.getCharSequence(idLen + toIdLen + 6 * INT_SIZE + LONG_SIZE, passLen, Configuration.getCharset());       
        if (password != null && password.length() > 0) {
        	 setPassword(password.toString());
        }
	}

}
