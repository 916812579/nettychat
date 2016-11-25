package chat.bean;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import chat.annotation.Data;
import chat.utils.Configuration;

/**
 * 文本类型的消息
 */
public class TextMessage extends InteractionMessage {
	
	public TextMessage() {
		setType(TEXT);
	}
	
	// 消息的内容
	@Data(type=DataType.STRING)
	protected String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return super.toString() + " message is " + content;
	}
	
}
