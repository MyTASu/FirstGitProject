package cn.tedu.shoot;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Bee extends FlyingObject implements Award{
	private static BufferedImage[] images;
	static {
		
		images = new BufferedImage[5];
		for(int i=0;i<images.length;i++) {
			images[i] = readImage("bee"+i+".png");
		}
	}
	
	private int xspeed;
	private int yspeed;
	private int awardType;//奖励
	
	public Bee(){
		super(60,50);
		xspeed = 1;
		yspeed = 2;
		Random rand = new Random();
		awardType = rand.nextInt(2);
	}
	public void step() {
		y += yspeed;
		x += xspeed;
		if(x<=0 || x>=World.WIDTH-this.width) {
			xspeed *= -1;
		}
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
	
	public int getAwardType() {		
		return awardType;
	}
	
	
}
