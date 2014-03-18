package kinectserver2;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import pbox2d.PBox2D;
import processing.core.PApplet;

public class Forma {
	private int x, y, alt, lar, r, g, b;
	private String nome;
	private String ip;
	
	boolean estatica;
	PBox2D mundo;
	Body corpo;
	PApplet tela;
	
	public Forma(int _x, int _y, int _alt, int _lar, PBox2D _mundo, boolean _estatica, PApplet _tela){
		x = _x;
		y = _y;
		alt = _alt;
		lar = _lar;
		mundo = _mundo;
		estatica = _estatica;
		tela = _tela;
		
		BodyDef definicao = new BodyDef();
		Vec2 posicao = mundo.coordPixelsToWorld(x,y);
		definicao.position.set(posicao);
		definicao.bullet = true;
		
		if(estatica){
			definicao.type = BodyType.STATIC;
		}else{
			definicao.type = BodyType.DYNAMIC;
		}
		
		corpo = mundo.createBody(definicao);
		
		PolygonShape ret = new PolygonShape();
		
		float aMundo = mundo.scalarPixelsToWorld(alt / 2);
		float lMundo = mundo.scalarPixelsToWorld(lar / 2);
		
		ret.setAsBox(aMundo, lMundo);
		
		FixtureDef material = new FixtureDef();
		material.shape = ret;
		material.density = 1;
		material.friction = 0.3f;
		material.restitution = 2f;
		
		corpo.createFixture(material);
		
		
	}
	
	public void display(){
		tela.fill(r, g, b);
		tela.stroke(0);
		Vec2 pos = mundo.getBodyPixelCoord(corpo);
		
		float angulo = corpo.getAngle();
		
		tela.pushMatrix();
		tela.translate(pos.x, pos.y);
		tela.rotate(-angulo);
		tela.rectMode(tela.CENTER);
		tela.rect(0, 0, alt, lar);
		
		tela.popMatrix();
		
	}
	
	
	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getAlt() {
		return alt;
	}

	public void setAlt(int alt) {
		this.alt = alt;
	}

	public int getLar() {
		return lar;
	}

	public void setLar(int lar) {
		this.lar = lar;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
}
