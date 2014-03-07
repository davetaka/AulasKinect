package terceirokinect;

import processing.core.PApplet;
import processing.core.PImage;
import SimpleOpenNI.SimpleOpenNI;

public class TerceiroKinect extends PApplet {
	
	SimpleOpenNI kinect;
	PImage fundo;
	int id;
	int[] pixelsUsuario;
	boolean rastreando;
	
	public void setup() {
		size(640, 480);
		kinect = new SimpleOpenNI(this);
		kinect.enableDepth();
		kinect.enableRGB();
		
		kinect.enableUser(SimpleOpenNI.SKEL_PROFILE_NONE);
		
		kinect.alternativeViewPointDepthToImage();
		fundo = loadImage("paisagem_640_480.png");
	}

	public void draw() {
		image(fundo, 0, 0);
		kinect.update();
		if(rastreando){
			
			PImage imagemUsuario = kinect.rgbImage();
			imagemUsuario.loadPixels();
			loadPixels();
			
			pixelsUsuario = kinect.getUsersPixels(SimpleOpenNI.USERS_ALL);
			
			for(int i = 0; i < pixelsUsuario.length; i++){
				if(pixelsUsuario[i] != 0){
					pixels[i] = imagemUsuario.pixels[i];
				}
			}
			
			updatePixels();
			
			
			
			
		}
		
		
		
	}
	
	
	public void onNewUser(int userId){
		id = userId;
		println("OK, rastreando ...");
		rastreando = true;
		
	}
}