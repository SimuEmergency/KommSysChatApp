package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerMain {
	public static void main(String[] args) {
		//using serversocket as argument to automatically close the socket

		//Alle geöffneten Client Threads
		ArrayList<ServerMessageListener> threadList = new ArrayList<ServerMessageListener>();

		try (ServerSocket serversocket = new ServerSocket(2022)) {
			while (true) {
				Socket socket = serversocket.accept();
				ServerMessageListener serverThread = new ServerMessageListener(socket, threadList);
				//Startet Thread für neuen Client
				threadList.add(serverThread);
				serverThread.start();

			}
		} catch (Exception e) {
			System.out.println("[S] Error: " + e.getStackTrace());
		}
	}
}
