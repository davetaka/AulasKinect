package kinectblobdetection;

import processing.core.PApplet;
import processing.core.PImage;
import SimpleOpenNI.SimpleOpenNI;
import blobDetection.Blob;
import blobDetection.BlobDetection;
import blobDetection.EdgeVertex;


public class KinectBlobDetection extends PApplet {

	PImage imagem;
	BlobDetection bd;
	int alt = 640, lar = 480;
	SimpleOpenNI kinect;
	
	public void setup() {
		size(alt, lar);
		smooth();
		
		bd = new BlobDetection(alt, lar);
		bd.setThreshold(0.6f);

		kinect = new SimpleOpenNI(this);
		kinect.enableDepth();
	}
	
	public void draw() {
		kinect.update();
		kinect.setMirror(true);
		
		background(0);
		imagem = kinect.depthImage();
		
		bd.computeBlobs(imagem.pixels);
		contornar();
	}
	
	private void contornar(){
		Blob contorno;
		EdgeVertex a,b;
		
		for(int i = 0; i < bd.getBlobNb(); i++){
			contorno = bd.getBlob(i);
			
			if(null != contorno){
				stroke(255);
				
				for(int y = 0; y < contorno.getEdgeNb(); y++){
					a = contorno.getEdgeVertexA(y);
					b = contorno.getEdgeVertexB(y);
					
					if(a != null && b != null){
						line(a.x * lar, a.y * alt, b.x * lar, b.y * alt);
					}
				}
			}
		}
	}

}
