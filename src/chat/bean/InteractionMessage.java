package chat.bean;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import chat.annotation.Data;
import chat.utils.Configuration;

/**
 * 交互消息
 *
 */
public abstract class InteractionMessage extends Message {
	
	// 唯一标识的值
	@Data(type=DataType.STRING)
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

	
	public String toString() {
		return "[ from " + getId() + " to  " + toId + "]";
	}

}
