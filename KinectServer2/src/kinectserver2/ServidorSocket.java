package kinectserver2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServidorSocket {
	private KinectServer tela;
	private ServerSocket server;
	
	public ServidorSocket(KinectServer _tela) {
		tela = _tela;
	}
	
	public void iniciar(){
		try {
			server = new ServerSocket(33333);
			System.out.println("Servidor no ar ...");
			
			while(true){
				receber(server.accept());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void receber(Socket socket){
		Scanner scan;
		try {
			scan = new Scanner(socket.getInputStream());
			while(scan.hasNextLine()){
				String linha = scan.nextLine();
				
				String[] n = linha.split(";");
				int x = Integer.parseInt(n[0]);
				int y = Integer.parseInt(n[1]);
				int l = Integer.parseInt(n[2]);
				int a = Integer.parseInt(n[3]);
				
				int r, g, b;
				
				r = Integer.parseInt(n[4]);
				g = Integer.parseInt(n[5]);
				b = Integer.parseInt(n[6]);
				
				String nome = n[7];
				String ip = socket.getInetAddress().getHostName();
				
				
				try {
					Forma f = new Forma(x,y,a,l, tela.getMundo(), false, tela);
					f.setR(r);
					f.setG(g);
					f.setB(b);
					
					f.setNome(nome);
					f.setIp(ip);
					
					tela.adicionar(f);
					System.out.println("Recebi a mensagem " + linha + " de " + nome);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		} catch (IOException e) {
	
			e.printStackTrace();
		} finally{
			if(socket != null){
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
}
