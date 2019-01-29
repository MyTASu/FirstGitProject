package cn.tedu.shoot;

import java.awt.image.BufferedImage;

public class BigAirplane extends FlyingObject implements Enemy{
	private static BufferedImage[] images;
	static {
		
		images = new BufferedImage[5];
		for(int i=0;i<images.length;i++) {
			images[i] = readImage("bigplane"+i+".png");
		}
	}
	
	private int speed;
	
	public BigAirplane(){
		super(69,99);
		speed = 2;
	}
	public void step() {
		y += speed;//y+（向下）；
	}
	
	int index =1;
	public BufferedImage getImage() { //每10个毫秒走一次
		if(isLife()) {
			return images[0];
		}else if(isDead()) {
			BufferedImage img = images[index++];
			if(index==images.length) {
				state = REMOVE;
			}
			return img;
		}
		return null;
	}
	
	public int getScore() {
		return 3;
	}
	
	
	
}
