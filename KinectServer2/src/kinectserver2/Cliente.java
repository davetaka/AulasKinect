package kinectserver2;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class Cliente {

	private static List<String> ips = new ArrayList<String>();
	
	public static void main(String[] args)  {
		
		
		ips.add("192.168.0.245");
		//ips.add("192.168.0.240");
		//ips.add("192.168.0.246");
		//ips.add("192.168.0.251");
		//ips.add("192.168.0.108");
		//ips.add("192.168.0.244");
		//ips.add("127.0.0.1");
		
		for(String ip : ips){
			Socket s;
			try {
				s = new Socket(ip, 33333);
				
				String msg = "60;10;40;200;100;255;0;David";
				s.getOutputStream().write(msg.getBytes());
				
				s.close();
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		
			

		}

	}

}
