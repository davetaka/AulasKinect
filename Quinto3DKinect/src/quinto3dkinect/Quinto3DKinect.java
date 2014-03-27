package quinto3dkinect;

import processing.core.PApplet;
import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;

public class Quinto3DKinect extends PApplet {

	SimpleOpenNI context;
	float zoomF = 0.5f;
	float rotX = radians(180);
	float rotY = radians(0);

	PVector bodyCenter = new PVector();
	PVector bodyDir = new PVector();

	public void setup() {
		size(1024, 768, P3D);
		context = new SimpleOpenNI(this);
		context.enableDepth();
		context.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);

		stroke(255, 255, 255);
		smooth();
		perspective(radians(45), width / height, 10, 150000);
	}

	public void draw() {
		context.update();

		background(0, 0, 0);

		translate(width / 2, height / 2, 0);
		rotateX(rotX);
		rotateY(rotY);
		scale(zoomF);

		int[] depthMap = context.depthMap();
		int steps = 3;
		int index;
		PVector realWorldPoint;

		translate(0, 0, -1000); 

		for (int y = 0; y < context.depthHeight(); y += steps) {
			for (int x = 0; x < context.depthWidth(); x += steps) {
				index = x + y * context.depthWidth();
				if (depthMap[index] > 0) {
					realWorldPoint = context.depthMapRealWorld()[index];
					point(realWorldPoint.x, realWorldPoint.y, realWorldPoint.z);
				}
			}
		}
	}

	public void keyPressed() {
		switch (key) {
		case ' ':
			context.setMirror(!context.mirror());
			break;
		}

		switch (keyCode) {
		case LEFT:
			rotY += 0.1f;
			break;
		case RIGHT:
			rotY -= 0.1f;
			break;
		case UP:
			if (keyEvent.isShiftDown())
				zoomF += 0.01f;
			else
				rotX += 0.1f;
			break;
		case DOWN:
			if (keyEvent.isShiftDown()) {
				zoomF -= 0.01f;
				if (zoomF < 0.01)
					zoomF = 0.01f;
			} else
				rotX -= 0.1f;
			break;
		}
	}
}
