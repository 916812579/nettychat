package chat.bean;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import chat.utils.Configuration;

/**
 * 交互消息
 *
 */
public abstract class InteractionMessage extends Message {
	// 标识的长度
	private int toIdLength;
	// 唯一标识的值
	private String toId;
	
	public int getToIdLength() {
		return (int) (toId == null ? 0 : toId.getBytes(Configuration
				.getCharset()).length);
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}
	
	
	public ByteBuf encode(ChannelHandlerContext ctx, Message msg) {
		InteractionMessage message = (InteractionMessage) msg;
		int len = FIX_LEN + message.getIdLength() + message.getToIdLength();
		ByteBuf buf = ctx.alloc().buffer(len);
		buf.writeInt(message.getMagic());
		buf.writeInt(message.getType());
		buf.writeInt(message.getIdLength());
		buf.writeCharSequence(message.getId(), Configuration.getCharset());
		buf.writeInt(message.getToIdLength());
		if (message.getToIdLength() > 0) {
			buf.writeCharSequence(message.getToId(), Configuration.getCharset());
		}
		buf.writeInt(message.getCommand());
		buf.writeLong(message.getTime());		
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
	}
	
	public String toString() {
		return "[ from " + getId() + " to  " + toId + "]";
	}

}
