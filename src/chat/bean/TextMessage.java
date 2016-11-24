package chat.bean;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import chat.utils.Configuration;

/**
 * 文本类型的消息
 */
public class TextMessage extends Message {
	
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
		buf.writeInt(message.getMagic());
		buf.writeInt(message.getType());
		buf.writeInt(message.getIdLength());
		buf.writeCharSequence(msg.getId(), Configuration.getCharset());
		buf.writeInt(message.getToIdLength());
		if (message.getToIdLength() > 0) {
			buf.writeCharSequence(msg.getToId(), Configuration.getCharset());
		}
		buf.writeInt(message.getCommand());
		buf.writeLong(message.getTime());		
		buf.writeInt(message.getContentLength());
		buf.writeCharSequence(message.getContent(),
				Configuration.getCharset());
		return buf;
	}

	public void decode(ByteBuf buf) {		
		int magic = buf.getInt(0);
		int type = buf.getInt(INT_SIZE);
		int idLen = buf.getInt(2 * INT_SIZE);		
		CharSequence id = buf.getCharSequence(3 * INT_SIZE, idLen, Configuration.getCharset());
		int toIdLen = buf.getInt(idLen + 3 * INT_SIZE);
		CharSequence toId = buf.getCharSequence(idLen + 4 * INT_SIZE, toIdLen, Configuration.getCharset());			
		int command = buf.getInt(idLen + toIdLen + 4 * INT_SIZE);
		long time = buf.getLong(idLen + toIdLen + 5 * INT_SIZE);
		int contentLength = buf.getInt(idLen + toIdLen + 5 * INT_SIZE + LONG_SIZE);
        CharSequence content = buf.getCharSequence(idLen + toIdLen + 6 * INT_SIZE + LONG_SIZE, contentLength, Configuration.getCharset());

        setMagic(magic);
        setType(type);
        if (id != null && id.length() > 0) {
        	setId(id.toString());
        }
        
        if (toId != null && toId.length() > 0) {
        	setToId(toId.toString());
        }
        
        setCommand(command);
        setTime(time);
        
        if (content != null && content.length() > 0) {
        	 setContent(content.toString());
        }
	}
	
	@Override
	public String toString() {
		return super.toString() + " message is " + content;
	}
	
}
