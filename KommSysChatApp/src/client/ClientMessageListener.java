package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientMessageListener implements Runnable {
	private Socket socket;
	private BufferedReader input;

	public ClientMessageListener(Socket socket) throws IOException {
		this.socket = socket;
		this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
	}

	@Override
	public void run() {

		try {
			while (true) {
				String msgInput = input.readLine();
				System.out.println(msgInput);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
