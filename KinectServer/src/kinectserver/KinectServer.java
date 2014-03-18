package kinectserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import processing.core.PApplet;


public class KinectServer extends PApplet {

	ServerSocket server;
	
	//X;Y;largura;altura
	String msg = "200;300;40;40";
	private List<Forma> formas = new ArrayList<Forma>();
	
	public void setup() {
		size(640, 480);
		
		
		try {
			server = new ServerSocket(3333);
			System.out.println("servidor no ar");
			
			//while(true)
				receber(server.accept());
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public void draw() {
		background(0);
		fill(255,0,0);
		
		for(Forma f : formas){
			rect(f.getX(), f.getY(), f.getAlt(), f.getLar());
		}
	}
	
	private void receber(Socket socket){
		Scanner scan;
		try {
			scan = new Scanner(socket.getInputStream());
			while(scan.hasNextLine()){
				String linha = scan.nextLine();
				System.out.println("Recebi a mensagem " + linha);
				String[] n = linha.split(";");
				int x = Integer.parseInt(n[0]);
				int y = Integer.parseInt(n[1]);
				int l = Integer.parseInt(n[2]);
				int a = Integer.parseInt(n[3]);
				
				Forma forma = new Forma(x,y,l,a);
				formas.add(forma);
			}
		
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	
}
