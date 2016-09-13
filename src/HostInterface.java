import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class HostInterface {
	Socket hostSocket = null;
	PrintWriter out = null;
	BufferedReader in = null;
	ControlPanel parent;

	public HostInterface(String address, ControlPanel parent) {
		this.parent = parent;
		try {
			hostSocket = new Socket();
			hostSocket.setSoTimeout(1000);
			InetSocketAddress hostAddress = new InetSocketAddress(address, 7777);
			outPutInfo("Ansluter till " + address + ".");
			hostSocket.connect(hostAddress, 2000);
			out = new PrintWriter(hostSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(hostSocket
					.getInputStream()));
			outPutInfo("Ansluten till " + address + ".");

		} catch (SocketTimeoutException e) {
			out = null;
			in = null;
			outPutInfo("Anslutningen till " + address + " gjorde timeout.");
		} catch (Throwable e) {
			out = null;
			in = null;
			outPutInfo("Anslutningen till " + address + " misslyckades.");
		}
	}

	public void close() {
		try {
			out.println("/quit");
			hostSocket.close();
		} catch (Throwable e) {
		}
	}

	public boolean display(String text) {
		try {
			out.println(text);
			String result = in.readLine();
			System.out.println(result);
			if(result.equals("OK")){
				outPutInfo("Skickade frågan till programledaren.");
				return true;
			}
		} catch (Throwable e) {
		}
		outPutInfo("Kunde inte skicka frågan till programledaren.");
		return false;
	}

	public boolean hide() {
		try {
			out.println("/hide");
		} catch (NullPointerException e) {
			return false;
		}
		return true;
	}

	public void outPutInfo(String text) {
		if (parent != null) {
			parent.displayInfo(text);
		}
	}
}
