package cn.tedu.shoot;

import java.awt.image.BufferedImage;

public class Bullet extends FlyingObject{
	private static BufferedImage image;
	static {
		
		image = readImage("bullet.png");
	}
	
	private int speed;
	
	public Bullet(int x,int y){
		super(8,14,x,y);
		speed = 3;
	}
	public void step() {
		y -= speed;//y-（向上）；
	}
	
	public BufferedImage getImage() {
		if(isLife()) {    //方法中遇到return就会结束方法
			return image;
		}else if(isDead()) {
			state = REMOVE;
		}
		return null;
	}
	
	public boolean outOfBounds() {
		return this.y<=-this.height;
	}
	
}
