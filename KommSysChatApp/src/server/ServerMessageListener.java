package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ServerMessageListener extends Thread {
	private Socket socket;
	private String username = "ben";
	private ArrayList<ServerMessageListener> threadList;
	private PrintWriter output;

	public ServerMessageListener(Socket socket, ArrayList<ServerMessageListener> threads) {
		this.socket = socket;
		this.threadList = threads;
	}

	@Override
	public void run() {
		try {
			//Nachrichten des Clients
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			//Nachrichten an Client
			output = new PrintWriter(socket.getOutputStream(), true);

			while (true) {
				//Nachricht von Client
				String outputString = input.readLine();

				//Splitten der Parameter
				char type = outputString.charAt(0);
				String[] msg = outputString.split("\\|");

				switch (type) {
				case 'M': // Nachricht
					String sender = msg[1];
					String reciever = msg[2];
					String content = msg[3];
					// System.out.println("[S] Message erhalten");
					System.out.println("[S] " + "Nachricht von " + sender + " an " + reciever + ": " + content);
					sendMessage(reciever, sender, content);
					break;
				case 'R': // Registrieung
					username = msg[1];
					System.out.println("[S] Registriere User " + msg[1]);
					break;
				case 'L': // Abmeldung
					username = "";
					System.out.println("[S] Logout User " + msg[1]);
					return;
				default:
					System.out.println("[S] Sonstige Nachricht erhalten");
					break;
				}
				//sendToAllClients(outputString);
			}

		} catch (Exception e) {
			System.out.println("Error occured " + e.getStackTrace());
		}
	}

	/**
	 * Sendet eine Nachricht an alle aktiven Clients
	 * @param message Inhalt der Nachricht, welche gesendet werden soll
	 */
	private void sendToAllClients(String message) {
		for (ServerMessageListener sT : threadList) {
			sT.output.println(message);
		}

	}

	/**
	 * Sendet die angegebene Nachricht an den Ziel Client
	 * ist dieser offline, wird der Absender informiert
	 * @param reciever Empfänger der Nachricht
	 * @param sender Absender der Nachricht
	 * @param message Inhalt der Nachricht
	 */
	private void sendMessage(String reciever, String sender, String message) {
		ServerMessageListener client = getUser(reciever);
		if (client != null) {
			client.output.println("Nachricht von " + sender + ": " + message);
		} else {
			client = getUser(sender);
			client.output.println(reciever + " nicht online.");
		}

	}

	/**
	 * Gibt Verbindungsthread mit angegebenen User zurück
	 * @param username
	 * @return ServerMessageListener Thread mit aktiver Clientverbindung
	 */
	private ServerMessageListener getUser(String username) {
		for (ServerMessageListener sml : threadList) {
			if (sml.getUsername().equals(username)) {
				return sml;
			}
		}
		return null;
	}

	public String getUsername() {
		return this.username;
	}
}
