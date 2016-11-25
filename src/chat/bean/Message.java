package chat.bean;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import chat.annotation.Data;
import chat.utils.ClassUtils;
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

	// ping消息
	public static final int PING = 0x0;

	public static final int MAGIC = 0x55;

	// 魔数，主要代表是不是可以处理的消息
	@Data(type = DataType.INT)
	private int magic = MAGIC;

	// 消息的类型
	@Data(type = DataType.INT)
	private int type;

	// 唯一标识的值
	@Data(type = DataType.STRING)
	private String id;

	// 指令
	@Data(type = DataType.INT)
	private int command;
	// 时间
	@Data(type = DataType.LONG)
	private long time;

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
	 * 编码
	 * 
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public ByteBuf encode(ChannelHandlerContext ctx, Message msg)
			throws IllegalArgumentException, IllegalAccessException {
		Class<? extends Message> clazz = msg.getClass();
		List<Field> fields = ClassUtils.getAllDeclaredFields(clazz);
		List<Field> datas = new ArrayList<Field>();
		int len = 0;
		for (Field filed : fields) {
			Data an = filed.getAnnotation(Data.class);
			if (an != null) {
				filed.setAccessible(true);
				datas.add(filed);
				DataType type = an.type();
				int size = type.getLength();
				if (size > 0) {
					len += size;
				} else {
					switch (type) {
					case STRING:
						String value = (String) filed.get(this);
						len += DataType.getLength(DataType.INT);
						if (value != null) {
							len += value.getBytes(Configuration.getCharset()).length;
						}
						break;
					}
				}
			}
		}
		ByteBuf buf = ctx.alloc().buffer(len);
		for (Field filed : datas) {
			Data an = filed.getAnnotation(Data.class);
			DataType type = an.type();
			switch (type) {
			case INT:
				buf.writeInt(filed.getInt(this));
				break;
			case LONG:
				buf.writeLong(filed.getLong(this));
				break;
			case STRING:
				String value = (String) filed.get(this);
				if (value != null) {
					byte[] datats = value.getBytes(Configuration.getCharset());
					buf.writeInt(datats.length);
					buf.writeBytes(datats);
				} else {
					buf.writeInt(0);
				}
				break;
			}
		}
		return buf;
	}

	/**
	 * 解码
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public void decode(ByteBuf buf) throws IllegalArgumentException, IllegalAccessException {
		Class<? extends Message> clazz = this.getClass();
		List<Field> fields = ClassUtils.getAllDeclaredFields(clazz);
		int pos = 0;
		for (Field field : fields) {
			Data an = field.getAnnotation(Data.class);
			if (an != null) {
				field.setAccessible(true);
				DataType type = an.type();
				switch (type) {
				case INT:
					field.set(this, buf.getInt(pos));
					pos += type.getLength();
					break;
				case LONG:
					field.set(this, buf.getLong(pos));
					pos += type.getLength();
					break;
				case STRING:
					int sL = buf.getInt(pos);
					pos += DataType.getLength(DataType.INT);
					CharSequence seq = buf.getCharSequence(pos, sL, Configuration.getCharset());
					pos += sL;
					if (seq != null) {
						field.set(this, seq.toString());
					}			
					break;
				}
			}
		}
	}

	public static void main(String[] args) {
		Class<? extends Message> clazz = Message.class;
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			System.out.println(field.getName());
		}

	}
}
