package chat.bean;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.Charset;

import chat.utils.Configuration;

/**
 * 通信消息
 * 
 * 说明： u1 一个字节(byte)， u2两个字节(byte)， u4四个字节(byte) u8八个字节(byte)<br/>
 * 消息格式：<br/>
 * u4 唯一标识的长度<br/>
 * u1 所代表长度个字节数，唯一标识的值<br/>
 * u4 指令<br/>
 * u8 时间<br/>
 * u4 代表消息的类型<br/>
 * u4 消息的长度<br/>
 * u4代表具体长度的的字节，消息的内容<br/>
 */
public abstract class Message {

	// 文本消息
	public static final int TEXT = 0x01;

	public static final int MAGIC = 0x55;
	
	// 消息固定部分的长度
	public static final int FIX_LEN = 32;
	
	// 一个字节多少位
	public static final int BIT_SIZE = 8;
	
	// 整数字节数
	public static final int INT_SIZE = Integer.SIZE / BIT_SIZE;
	
	// long 字节数
	public static final int LONG_SIZE = Long.SIZE / BIT_SIZE;

	// 魔数，主要代表是不是可以处理的消息
	private int magic = MAGIC;

	// 消息的类型
	private int type;

	// 标识的长度
	private int idLength;
	// 唯一标识的值
	private String id;

	// 指令
	private int command;
	// 时间
	private long time;

	public int getIdLength() {
		return (int) (id == null ? 0
				: id.getBytes(Configuration.getCharset()).length);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}

	public long getTime() {
		return System.currentTimeMillis();
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getMagic() {
		return magic;
	}

	public void setMagic(int magic) {
		this.magic = magic;
	}
	
	/**
	 *  编码
	 */
	public abstract ByteBuf encode(ChannelHandlerContext ctx, Message msg);
	
	/**
	 * 解码
	 */
	public abstract void decode(ByteBuf buf);
}
