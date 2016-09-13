import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class InfoScreen {

	public static void main(String[] args) throws UnknownHostException,
			SocketException {

		Screen screen = new Screen();
		screen.setLocation(10, 10);
		screen.setSize(1024, 768);
		screen.setVisible(true);

		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(7777);
			serverSocket.setSoTimeout(5000);
		} catch (IOException e) {
			System.err.println("Could not listen on port: 7777.");
			System.exit(1);
		}

		Socket clientSocket = null;

		while (true) {// Lyssna efter anslutningar
			screen.display("[Väntar på anslutning]");
			try {
				clientSocket = serverSocket.accept();
				clientSocket.setSoTimeout(5000);
			} catch (SocketTimeoutException e) {
				System.err.println("Accept timed out.");
				continue;
			} catch (IOException e) {
				System.err.println("Accept failed.");
				continue;
			}
			System.out.println("Accepted connection.");
			BufferedReader in = null;
			PrintWriter out = null;

			try {
				in = new BufferedReader(new InputStreamReader(clientSocket
						.getInputStream()));
				out = new PrintWriter(clientSocket.getOutputStream(), true);
			} catch (IOException e) {
				System.err.println("Input error.");
				continue;
			}
			String inputLine = null;

			screen.hide();
			while (true) {// Loop som visar inkommande paket på skärmen
				try {
					if ((inputLine = in.readLine()) != null) {
						inputLine = inputLine.replace("\\n", "\n");
						if (inputLine.indexOf('/') == 0) {// Specialkommando
							if (inputLine.equals("/quit")) {
								break;
							} else if (inputLine.equals("/hide")) {
								screen.hide();
							} else if (inputLine.equals("/shutdown")) {
								System.exit(0);
							} else if (inputLine.equals("/timestamp")) {
								out.println(System.currentTimeMillis());
							} else {
								out.println("UNKNOWN_COMMAND");
							}
						} else if (!inputLine.equals("")) {
							screen.display(inputLine);
							out.println("OK");
						}
					}

				} catch (SocketTimeoutException e) {
					System.err.println("Read operation timed out.");
					continue;
				} catch (IOException e) {
					System.err.println("Read operation failed.");
					continue;
				}
			}
			try {// Stäng anslutningen
				in.close();
				clientSocket.close();
			} catch (IOException e) {
			}
		}// Slut anslutningsloop
	}
}
