package fruitninja;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;

public class FruitNinjaGame extends PApplet {

	private ArrayList fruitArray;
	private PImage img;
	private int pointCounter;
	private int hpCounter = 100;
	private long startTime;
	private long counterTime;
	private int appearanceTime = 1000;
	private List<Particle> attackParticles = new ArrayList<Particle>();

	PVector hand = new PVector();
	SimpleOpenNI kinect;

	public void setup() {
		size(640, 480);
		smooth();
		
		
		//**Codigo KINECT
		kinect = new SimpleOpenNI(this);
		kinect.setMirror(true);
		kinect.enableDepth();
		kinect.enableGesture();
		kinect.enableHands();
		kinect.addGesture("RaiseHand");
		//**Codigo KINECT
		
		/*
		 * 30 FPS is a bit too slow, but 60 is way too fast. 40 is perfect.
		 */
		frameRate(40);
		ellipseMode(RADIUS);
		img = loadImage("background.jpg");
		pointCounter = 0;
		startTime = System.currentTimeMillis();
		counterTime = System.currentTimeMillis();
		fruitArray = new ArrayList();
	}

	public void draw() {
		//**Codigo KINECT
		kinect.update();
		mousePressed();
		mouseDragged();
		//**Codigo KINECT
		
		image(img, 0, 0);
		if (System.currentTimeMillis() - startTime > 7500
				&& appearanceTime >= 100) {
			appearanceTime -= 25;
			startTime = System.currentTimeMillis();
		}
		
		printScore();
		printHP();
		if (hpCounter <= 0) {
			noLoop();
			text("You lose!", 100, 100);
		}

		if (System.currentTimeMillis() - counterTime > appearanceTime) {
			Fruit myFruit = new Fruit(random(width), random(height,
					height - 100), this);
			fruitArray.add(myFruit);
			counterTime = System.currentTimeMillis();
		}
		Iterator i = fruitArray.iterator();
		while (i.hasNext()) {
			Fruit mF = (Fruit) i.next();
			mF.run();
			if (mF.y > height && !mF.getAppleString().equals("appleCut.png")) {
				hpCounter -= 5;
				i.remove();
			}
		}

		// draw the particle system for the cut
		strokeWeight(1.0f);
		stroke(255, 255, 255);
		Iterator itr = attackParticles.iterator();
		while (itr.hasNext()) {
			Particle p = (Particle) itr.next();
			p.run();
			if (p.getY() > height) {
				itr.remove();
			}
		}
	}

	public void printScore() {
		int s = pointCounter;
		text("Your score is: " + s, 10, 10, 150, 80);
	}

	public void printHP() {
		text("Your HP is: " + hpCounter, 10, 25, 150, 80);
	}

	// when user drag over the fruit

	public void mousePressed() {

		for (int i = 0; i < fruitArray.size(); i++) {
			Fruit myFruit = (Fruit) fruitArray.get(i);
			if (dist(myFruit.getX(), myFruit.getY(), mouseX, mouseY) < myFruit
					.getRadius()) {
				myFruit.setAppleString("appleCut.png");
				myFruit.setApple(loadImage("appleCut.png"));
				pointCounter++;
			}
		}
	}

	// when user clicks directly on the fruit
	public void mouseDragged() {
		stroke(225, 225, 225);
		strokeWeight(3.0f);
		strokeJoin(ROUND);
		line(pmouseX, pmouseY, mouseX, mouseY);
		ellipse(mouseX, mouseY, 8, 8);
		
		float dx = pmouseX - mouseX; // pmouseX > mouseX :: LEFT
		float randX = random(mouseX - 5, mouseX + 5);
		float randY = random(mouseY - 5, mouseY + 5);
		attackParticles.add(new Particle(randX, randY, dx, this));
		attackParticles.add(new Particle(randX - 1, randY, dx, this));
		attackParticles.add(new Particle(randX - 2, randY, dx, this));

		for (int i = 0; i < fruitArray.size(); i++) {
			Fruit myFruit = (Fruit) fruitArray.get(i);
			if (dist(myFruit.getX(), myFruit.getY(), mouseX, mouseY) < myFruit
					.getRadius()) {
				myFruit.setAppleString("appleCut.png");
				myFruit.setApple(loadImage("appleCut.png"));
				pointCounter++;
			}
		}
	}
	
	//**Codigo KINECT
	public void onCreateHands(int id, PVector position, float time){
		System.out.println("Encontrei sua mão");
		kinect.convertRealWorldToProjective(position, hand);
	}
	public void onUpdateHands(int id, PVector position, float time){
		kinect.convertRealWorldToProjective(position, hand);
		mouseX = (int) hand.x;
		mouseY = (int) hand.y;
		
		System.out.println("Atualizando a mao em " + hand.x + " e " + hand.y);
	}
	
	public void onRecognizeGesture(String gesto, PVector iPos, PVector endPos){
		kinect.startTrackingHands(endPos);
		kinect.removeGesture("RaiseHand");
	}
	//**Codigo KINECT
	

}
