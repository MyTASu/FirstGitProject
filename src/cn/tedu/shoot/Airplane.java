package cn.tedu.shoot;

import java.awt.image.BufferedImage;

public class Airplane extends FlyingObject implements Enemy{
	private static BufferedImage[] images;
	static {
		
		images = new BufferedImage[5];
		for(int i=0;i<images.length;i++) {
			images[i] = readImage("airplane"+i+".png");
		}
	}
	
	
	private int speed;
	
	public Airplane(){
		super(49,36);
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
	/*
	 * 10ms img =img[1] index=2          img[1]
	 * 20ms img=img[2] index=3  					img[2]
	 * 30ms img=img[3] index=4						img[3]
	 * 40ms img=img[4] index=5 state =remove img[4]
	 * 50ms  return null
	 * 
	 * 
	 */
	
	public int getScore() {
		return 1;
	}
	
	
}
