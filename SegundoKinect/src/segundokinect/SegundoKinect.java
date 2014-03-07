package segundokinect;

import processing.core.PApplet;
import processing.core.PImage;
import SimpleOpenNI.SimpleOpenNI;


public class SegundoKinect extends PApplet {
	
	SimpleOpenNI kinect;
	
	PImage usuario;
	int id;
	int[] pixelsUsuario;
	
	
	public void setup() {
		size(640, 480, OPENGL);
		kinect = new SimpleOpenNI(this);
		kinect.enableDepth();
		
		kinect.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);
		
	}

	public void draw() {
		background(0);
		kinect.update();
		
		if(kinect.getNumberOfUsers() > 0){
			pixelsUsuario = kinect.getUsersPixels(SimpleOpenNI.USERS_ALL);
			loadPixels();
			for(int i=0; i < pixelsUsuario.length; i++){
				
				if(pixelsUsuario[i] != 0){
					pixels[i] = color(0,0,255);
				}
			}
			updatePixels();
		}
		
	}
	
	
	public void onNewUser(int userId){
		id = userId;
		println("OK, rastreando ...");
		
	}
}

