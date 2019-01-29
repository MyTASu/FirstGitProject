
package cn.tedu.shoot;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.awt.Graphics;

import javax.imageio.ImageIO;

public abstract class FlyingObject {
	public static final int LIFE = 0;
	public static final int DEAD = 1;
	public static final int REMOVE = 2;
	protected int state = LIFE;
	
	
	protected int width;
	protected int height;
	protected int x;
	protected int y;
	
	/**专门给英雄机、天空、子弹提供的 */
	public FlyingObject(int width,int height,int x,int y){
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
	}
	/** 专门提供给小敌机、大敌机、小蜜蜂*/
	public FlyingObject(int width,int height){
		this.width = width;
		this.height = height;
		Random rand = new Random();
		x = rand.nextInt(World.WIDTH-this.width);
		y = -this.height;
	}
	
	public abstract void step();
	
	
	public static BufferedImage readImage(String fileName) {
		try{
			BufferedImage img = ImageIO.read(FlyingObject.class.getResource("img/"+fileName)); //
																				//将FlyingObject.class所在的目录设置为前目录
			return img;
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException();
		}
		
	}
	public abstract BufferedImage getImage();
	
	public boolean isLife() {
		return state==LIFE;
	}
	
	public boolean isDead() {
		return state==DEAD;
	}
	
	public boolean isRemove() {
		return state==REMOVE;
	}
	
	public void paintObject(Graphics g) {
		g.drawImage(this.getImage(),this.x,this.y,null);
	}
	
	public boolean outofBounds() {
		return this.y>=World.HEIGHT;
	}
	
	//other(英雄机/子弹)碰撞this（小敌机/大敌机/小蜜蜂）
	public boolean hit(FlyingObject other) {
		int x1 = this.x-other.width;
		int x2 = this.x+this.width;
		int y1 = this.y-other.height ;
		int y2 = this.y+this.height;
		int x = other.x;
		int y = other.y;
		return x>=x1 && x<=x2 && y>=y1 && y<=y2;
	}
	
	public void goDead() {
		state = DEAD;
	}
	
	
}
