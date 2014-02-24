package fruitninja;

import processing.core.PApplet;
import processing.core.PImage;

/**
class Fruit
@author Group 23 (Paulius Domarkas).

This class provides functionality to create fruits and their interaction with the walls.
*/

public class Fruit
{

 // INSTANCE VARIABLES
 private float x = 0;
 public float y = 0;
 private float speedX = 0;
 private float speedY = 0;
 //public 
 private float r = 0;
 private PImage apple;
 private float deg;
 private String appleString;

 private PApplet applet;
 
 // CONSTRUCTOR
 Fruit(float _x, float _y, PApplet applet)
 {
   x = _x;
   y = _y;
   speedX = applet.random(-2, 2);
   speedY = applet.random(-15, -10);
   r = 50;
   appleString = "apple.png";
   apple = applet.loadImage("apple.png");
   
   this.applet = applet;
 }

 // METHODS

 private void gravity()
 {
   speedY += 0.2;
 }

 public void setAppleString(String appleString) {
   this.appleString = appleString;
 }
 
 public String getAppleString() {
   return this.appleString;
 }
 
 public void setApple(PImage apple) {
   this.apple = apple;
 }
 
 public PImage getApple() {
   return this.apple;
 }
 
 public float getX() {
   return this.x;
 }
 
 public float getY() {
   return this.y;
 }
 
 public float getRadius() {
   return this.r;
 }

 public void run()
 {
   display();
   move();
   bounce();
   gravity();
 }


 private void bounce()
 {
   if ((x >= applet.width - r && speedX > 0) || (x < r && speedX < 0))
   {
     speedX = speedX * (-1);
   }
   if (y <  0)
   {
     speedY = speedY * (-1);
   }
 }


 private void move()
 {
   x += speedX;
   y += speedY;
 }




 public void display()
 {
	 applet.image(apple, x, y);
 }
}

