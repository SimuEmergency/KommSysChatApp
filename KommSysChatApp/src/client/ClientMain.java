package client;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientMain {
	private static String username = " ";

	public static void main(String[] args) {
		try (Socket socket = new Socket("localhost", 2022)) {
			//Nachrichten an Server
			PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

			//Einlesen der Konsole von Nutzer
			Scanner scanner = new Scanner(System.in);
			String userInput;

			ClientMessageListener listener = new ClientMessageListener(socket);

			new Thread(listener).start();

			do {

				if (username.equals(" ")) {
					System.out.println("Login:");
					userInput = scanner.nextLine();

					if (userInput.equals("exit")) {
						//Client beenden
						break;
					}
					username = userInput;
					//Registrieren beim Server
					output.println("R|" + username);

				} else {
					userInput = scanner.nextLine();
					if (userInput.equals("exit")) {
						//Client beenden
						//Abmelden beim Server
						output.println("L|" + username);
						break;
					}
					String messageHeader = ("M|" + username + "|");
					//System.out.println("[C] Header: " + messageHeader);
					output.println(messageHeader + userInput);
				}

			} while (!userInput.equals("exit"));

		} catch (Exception e) {
			System.out.println("[C] Exception: " + e.getStackTrace());
		}
	}
}
