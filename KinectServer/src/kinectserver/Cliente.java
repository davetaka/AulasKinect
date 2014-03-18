package kinectserver;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class Cliente {

	public static void main(String[] args)  {
		Socket s;
		try {
			s = new Socket("127.0.0.1", 3333);
			String msg = "100;100;40;20";
			s.getOutputStream().write(msg.getBytes());
			
			s.close();
		
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		

	}

}
