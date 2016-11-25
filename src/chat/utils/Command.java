package chat.utils;

/**
 * 消息指令
 */
public class Command {

	// 发送ping消息
	public static final int PING_SEND = 0x0;

	// 接收ping消息
	public static final int PING_RECEIVED = 0x1;

	// 登录
	public static final int LOGIN = 0x2;

	// 退出
	public static final int EXIT = 0x3;

	// 发送的消息
	public static final int SEND = 0x4;

	// 接受到的消息
	public static final int RECEIVED = 0x5;

}
