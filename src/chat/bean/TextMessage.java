package chat.bean;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import chat.utils.Configuration;

/**
 * 文本类型的消息
 */
public class TextMessage extends InteractionMessage {
	
	public TextMessage() {
		setType(TEXT);
	}
	
	// 消息的长度
	private int contentLength;
	// 消息的内容
	protected String content;
	
	public int getContentLength() {
		return (int) (content == null ? 0 : content.getBytes(Configuration.getCharset()).length);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ByteBuf encode(ChannelHandlerContext ctx, Message msg) {
		TextMessage message = (TextMessage) msg;
		int len = FIX_LEN + message.getIdLength() + message.getToIdLength()
				+ message.getContentLength();
		ByteBuf buf = ctx.alloc().buffer(len);
		buf.writeBytes(super.encode(ctx, msg));		
		buf.writeInt(message.getContentLength());
		buf.writeCharSequence(message.getContent(),
				Configuration.getCharset());
		return buf;
	}

	public void decode(ByteBuf buf) {		
		super.decode(buf);
		int idLen = buf.getInt(2 * INT_SIZE);
		int toIdLen = buf.getInt(idLen + 3 * INT_SIZE);
		int contentLength = buf.getInt(idLen + toIdLen + 5 * INT_SIZE + LONG_SIZE);
        CharSequence content = buf.getCharSequence(idLen + toIdLen + 6 * INT_SIZE + LONG_SIZE, contentLength, Configuration.getCharset());
        
        if (content != null && content.length() > 0) {
        	 setContent(content.toString());
        }
	}
	
	@Override
	public String toString() {
		return super.toString() + " message is " + content;
	}
	
}
