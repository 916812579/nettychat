package chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import chat.bean.TextMessage;
import chat.utils.Configuration;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ClientMain {
	private String host;
	private int port;
	private boolean stop = false;

	public ClientMain(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public static void main(String[] args) throws IOException {
		new ClientMain("192.168.1.124", Configuration.getPort()).run();
	}

	public void run() throws IOException {
		EventLoopGroup worker = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(worker);
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.handler(new ClientIniterHandler());

		try {
			Channel channel = bootstrap.connect(host, port).sync().channel();
			while (true) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(System.in));								
				String input = reader.readLine();
				if (input != null) {
					if ("quit".equals(input)) {
						System.exit(1);
					}
					String[] inputs = input.split(" ");
					if (inputs.length >= 3) {
						TextMessage message = new TextMessage();
						message.setId(inputs[0]);						
						message.setToId(inputs[1]);						
						message.setContent(inputs[2]);
						channel.writeAndFlush(message);
					}					
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

}
