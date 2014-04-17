package kinectserver;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Cliente {
	
	
	private static List<String> ips = new ArrayList<String>();
	
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		
//		ips.add("localhost");
		//ips.add("192.168.0.240");
		ips.add("192.168.0.245");
	//	ips.add("192.168.0.251");
		//ips.add("192.168.0.108");
		
		for(String ip : ips){
			try {
				Socket s = new Socket(ip, 33333);
				String msg = "100;60;20;20;0;255;0;Servidor";
				s.getOutputStream().write(msg.getBytes());
				s.close();
				System.out.println("Enviado para " + ip);
			} catch (Exception e) {
				System.out.println("IP " + ip + " fora do ar");
			}
		}
	}
}
