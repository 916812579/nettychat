package chat.bean;

import chat.utils.Command;

public class PingMessage extends Message {
	
	public PingMessage () {
		this.setId("sys");
		setType(PING);
	}

	/**
	 * @return ping  消息
	 */
	public static PingMessage getPingSend() {
		PingMessage message = new PingMessage();
		message.setCommand(Command.PING_SEND);
		return message;
	}
	
	/**
	 * @return ping 接收消息
	 */
	public static PingMessage getPingReceived() {
		PingMessage message = new PingMessage();
		message.setCommand(Command.PING_RECEIVED);
		return message;
	}

	@Override
	public String toString() {
		return this.getCommand() == Command.PING_SEND ? "ping send" : "ping received";
	}
}
