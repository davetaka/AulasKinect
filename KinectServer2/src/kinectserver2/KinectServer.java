package kinectserver2;

import java.util.ArrayList;
import java.util.List;

import pbox2d.PBox2D;
import processing.core.PApplet;
import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;


public class KinectServer extends PApplet {
	
	private List<Forma> formas = new ArrayList<Forma>();
	private PBox2D mundo;
	List<Integer> userIds;
	SimpleOpenNI kinect;
	
	
	public void setup() {
		size(640, 480);
		background(255);
		
		mundo = new PBox2D(this);
		mundo.createWorld();
		mundo.setGravity(0, -10);
		mundo.listenForCollisions();
		kinect = new SimpleOpenNI(this);
		kinect.enableDepth();
		kinect.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);
		kinect.setMirror(true);
		
		userIds = new ArrayList<Integer>();
		
		MinhaThread paralelo = new MinhaThread(this);
		paralelo.start();
	}

	public void draw() {
		mundo.step();
		background(0);
		kinect.update();
		image(kinect.depthImage(), 0, 0);
		
		for(Integer id : userIds){
			if(kinect.isTrackingSkeleton(id)){
				PVector maoDireita = new PVector();
				kinect.getJointPositionSkeleton(id, SimpleOpenNI.SKEL_RIGHT_HAND, maoDireita);
				kinect.convertRealWorldToProjective(maoDireita, maoDireita);
				
				Forma bloco= new Forma((int)maoDireita.x, (int)maoDireita.y, 16, 16, mundo, true, this);
				bloco.setR(255);
				bloco.display();
				
			}
		}
		
		for(Forma f : formas){
			fill(f.getR(), f.getG(), f.getB());
			//rect(f.getX(), f.getY(), f.getAlt(), f.getLar());
			f.display();
		}
	}
	
	public void onNewUser(int id){
		println("Detectei alguém");
		
		kinect.startPoseDetection("Psi", id);
		userIds.add(id);
	}
	
	public void onStartPose(String pose, int id){
		println("fazendo a pose do ladrão " + pose);
		kinect.stopPoseDetection(id);
		
		kinect.requestCalibrationSkeleton(id, true);
		
	}
	
	public void onStartCalibration(int id){
		println("Iniciando calibracao do esqueleto... fique parado");
	}
	
	public void onEndCalibration(int id, boolean sucesso){
		println("Finalizando calibração");
		
		if(sucesso){
			println("sucesso!!!");
			kinect.startTrackingSkeleton(id);
		}else{
			println("Tente novamente...");
		}
	}
	
	public void adicionar(Forma forma){
		synchronized (formas) {
			formas.add(forma);	
		}
	}
	
	public PBox2D getMundo(){
		return mundo;
		
	}
	
	
	
	
}

class MinhaThread extends Thread{
	private KinectServer tela;
	private boolean rodando = false;
	private String id;
	private int contador;
	ServidorSocket meuServer;
	
	public MinhaThread(KinectServer _tela) {
		tela = _tela;
		
	}
	
	@Override
	public void start() {
		rodando = true;
		System.out.println("Minha thread rodando ...");
		meuServer = new ServidorSocket(tela);
		super.start();
	}
	
	@Override
	public void run() {
		meuServer.iniciar();
	}
	
}


