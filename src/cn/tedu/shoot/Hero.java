package cn.tedu.shoot;

import java.awt.image.BufferedImage;

public class Hero extends FlyingObject{
	private static BufferedImage[] images;
	static {
		
		images = new BufferedImage[2];
		images[0] = readImage("hero0.png");
		images[1] = readImage("hero1.png");
	}
	
	private int life;
	private int doubleFire;
	
	public Hero(){
		super(97,124,140,400);
		life = 3;
		doubleFire = 0;
	}
	
	//鼠标坐标
	public void moveTo(int x,int y) {
		this.x = x-this.width/2;
		this.y = y-this.height/2;
	}
	public void step() {}
	
	int index = 0;
	public BufferedImage getImage() {
		if(isLife()) {
			return images[index++%images.length];
		}
		return null;
	
	}
	
	public Bullet[] shoot() {
		int xStep = this.width/4;
		int yStep = 20;
		if(doubleFire>0) {
			Bullet[] bs = new Bullet[2];
			bs[0] = new Bullet(this.x+1*xStep,this.y-yStep);
			bs[1] = new Bullet(this.x+3*xStep,this.y-yStep);
			doubleFire -=2;
			return bs;
		}else {
			Bullet[] bs = new Bullet[1];
			bs[0] = new Bullet(this.x+2*xStep,this.y-yStep);
			return bs;
		}

	}
	
	public void addLife() {
		life++;
	}
	
	
	public void addDoubleFire() {
		doubleFire += 40;
	}
	
	public int getLife() {
		return life;          //想使用私有变量就要公开方法返回私有变量的值
	}
	
	public void subtractLife() {
		life--;		
	}
	
	public void clearDoubleFire() {
		doubleFire = 0;
	}
}
