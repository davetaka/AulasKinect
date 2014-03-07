package primeirokinect;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;


public class PrimeiroKinect extends PApplet {

	SimpleOpenNI kinect;
	List<Integer> userIds;
	int menorDistancia = 9999;
	PImage gorila;
	
	public void setup() {
		size(1640,780);
		frameRate(30);
		kinect = new SimpleOpenNI(this);
		kinect.enableDepth();
		//kinect.enableRGB();
		kinect.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);
		
		kinect.setMirror(true);
		userIds = new ArrayList<Integer>();
		
		gorila = loadImage("gorilla.png");
	}

	public void draw() {
		kinect.update();
		
		image(kinect.depthImage(),0,0);
		//image(kinect.rgbImage(),0,0);
		
		for(Integer userId: userIds){
			if(kinect.isTrackingSkeleton(userId)){
				desenharEsqueleto(userId);
				
				int[] arrayPixels = kinect.depthMap();
				
				for(int i = 0; i < arrayPixels.length; i++){
					if(arrayPixels[i] < menorDistancia){
						menorDistancia = arrayPixels[i];
					}
				}
				
				PVector maoDireita = new PVector();
				kinect.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_RIGHT_HAND, maoDireita);
				
				PVector maoDireitaConvertida = new PVector();
				kinect.convertRealWorldToProjective(maoDireita, maoDireitaConvertida);
				
				PVector cabeca = new PVector();
				kinect.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_HEAD, cabeca);
				
				PVector cabecaConvertida = new PVector();
				kinect.convertRealWorldToProjective(cabeca, cabecaConvertida);
				
				image(gorila, cabecaConvertida.x -35, cabecaConvertida.y - 50);
				
				fill(255,0,0);
				ellipse(maoDireitaConvertida.x, maoDireitaConvertida.y, 20, 20);
			}
		}
		
	}
	
	public void desenharEsqueleto(int id){
		kinect.drawLimb(id, SimpleOpenNI.SKEL_HEAD, SimpleOpenNI.SKEL_NECK);
	}
	
	public void mousePressed() {
		int[] arrayPixels = kinect.depthMap();
		int cliquei = mouseX + mouseY * 640;
		int mm = arrayPixels[cliquei];
		
		println("Distancia = " + mm + " mm");

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
}
