package cn.tedu.shoot;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
import java.util.Arrays;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

//异常处理：java自带的方法出错不管，跳过 类共有的方法也先跳过不管，看类特有的报错信息然后再检查
//执行过程：用到那个类就先加载那个类的静态方和变量  111，333，222,444，555，666
public class World extends JPanel{
	public static final int START = 0;
	public static final int RUNNING = 1;
	public static final int PAUSE = 2;
	public static final int GAME_OVER = 3;
	public static final int HEIGHT = 700;
	public static final int WIDTH = 400;
	private static BufferedImage start;
	private static BufferedImage pause;
	private static BufferedImage gameover;
	static {
		start = FlyingObject.readImage("start.png");
		pause = FlyingObject.readImage("pause.png");
		gameover = FlyingObject.readImage("gameover.png");
		
	}
	
	private int state = START;
	private Sky sky = new Sky();
	private Hero hero = new Hero();
	private FlyingObject[] enemies = {};  // 等价于FlyingObject[] enemies = new FlyingObject[0]; 这时 enemies不是空指针
	private Bullet[] bullets = {};
	
	public FlyingObject nextOne() {
		Random rand = new Random();
		int type = rand.nextInt(20);
		if(type<5) {
			return new Bee();
		}else if(type<12) {
			return new Airplane();
		}else {
			return new BigAirplane();
		}
	}
	
	int enterIndex =0;
	public void enterAction(){
		enterIndex++;
		if(enterIndex%40==0) {
			FlyingObject obj = nextOne();
			enemies = Arrays.copyOf(enemies, enemies.length+1);
			enemies[enemies.length-1] = obj;
		}
	}
	int shootIndex = 0;
	public void shootAction() {
		shootIndex++;
		if(shootIndex%30==0) {
			Bullet[] bs = hero.shoot();
			bullets = Arrays.copyOf(bullets, bullets.length+bs.length);
			System.arraycopy(bs, 0, bullets, bullets.length-bs.length, bs.length);//数组的追加，连接两个数组
		}
	}
	
	
	public void stepAction() {
		sky.step();
		for(int i=0;i<enemies.length;i++) {
			enemies[i].step();
		}
		for(int i=0;i<bullets.length;i++) {
			bullets[i].step();
		}
	}
	
	public void outOfBoundsAction() {
		FlyingObject[] enemyLives = new FlyingObject[enemies.length];
		int index = 0;
		for(int i=0;i<enemies.length;i++) {
			if(!enemies[i].outofBounds() && !enemies[i].isRemove()) {
				enemyLives[index] = enemies[i];
				index++;
			}
		}
		enemies = Arrays.copyOf(enemyLives, index);
		
		index =0;
		Bullet[] bulletLives = new Bullet[bullets.length];
		for(int i=0;i<bullets.length;i++) {
			if(!bullets[i].outOfBounds() && !bullets[i].isRemove()) {
				bulletLives[index] = bullets[i];
				index++;
				
			}
		}
		bullets = Arrays.copyOf(bulletLives, index);
	}
	
	
	int score = 0;
	public void bulletBangAction() {
		for(int i=0;i<bullets.length;i++) {
			Bullet b = bullets[i];
			for(int k=0;k<enemies.length;k++) {
				FlyingObject f = enemies[k];
				if(b.isLife() && f.isLife() && f.hit(b)) {
					b.goDead();
					f.goDead();
					if(f instanceof Enemy) {
						Enemy e = (Enemy)f;
						score += e.getScore();
					}
					if(f instanceof Award) {
						Award a = (Award)f;
						int type = a.getAwardType();
						switch(type) {
						case Award.DOUBLE_FIRE:
							hero.addDoubleFire();
							break;
						case Award.LIFE:
							hero.addLife();
							break;
						}
					}
				}
			}
		}
	}
	
	public void heroBangAction() {
		for(int i=0;i<enemies.length;i++) {
			FlyingObject f = enemies[i];
			if(hero.isLife() && f.isLife() && f.hit(hero)) {
				f.goDead();
				hero.clearDoubleFire();
				hero.subtractLife();
			}
		}
	}
	
	public void checkGameOverAction() {
		if(hero.getLife()<=0) {
			state = GAME_OVER;
		}
	}
	
	
	
	
	public	void action() {
		MouseAdapter l = new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				if(state==RUNNING) {
					int x = e.getX();
					int y = e.getY();
					hero.moveTo(x, y);
				}
				
			}
			public void mouseClicked(MouseEvent e) {
				switch(state) {
				case START:
					state = RUNNING;
					break;
				case GAME_OVER:
					score = 0;
					sky = new Sky();
					hero = new Hero();
					enemies = new FlyingObject[0];
					bullets = new Bullet[0];
					state = START;
					break;
				}
			}
			public void mouseExited(MouseEvent e) {
				if(state==RUNNING) {
					state = PAUSE;
				}
			}
			public void mouseEntered(MouseEvent e) {
				if(state==PAUSE) {
					state = RUNNING;
				}
			}
		};
		this.addMouseListener(l);    //this  代表world类的对象 而world类是继承jpanel
		this.addMouseMotionListener(l);//就像是在jpanel上装了一个监听器对象l，这个l有两个功能
																		//一个是监听鼠标的单击和鼠标的移动	
		Timer timer = new Timer();
		int interval = 10;//interval 间隔
		timer.schedule(new TimerTask() {
			public void run() {
				if(state==RUNNING) {
					enterAction();   //生成飞行物（敌人）
					shootAction();		//英雄机生成子弹
					stepAction(); //移动
					outOfBoundsAction(); //越境删除
					bulletBangAction(); //子弹碰撞
					heroBangAction();//英雄机碰撞
					checkGameOverAction();//设置状态
				}	
				repaint();
			}
		},interval, interval); //timer.schedule(TimerTask,long,long)
													//timer.schedule(Task,time1,time2) 
												//时间的起点为此刻，time1是此刻到到第一次执行task的时间，time2是执行task的周期；
	
	}
	
	public void paint(Graphics g) {
		sky.paintObject(g);
		hero.paintObject(g);
		for(int i=0;i<enemies.length;i++) {
			enemies[i].paintObject(g);
		}
		for(int i=0;i<bullets.length;i++) {
			bullets[i].paintObject(g);
		}
		g.drawString("SCORE:"+score, 10,25 );
		g.drawString("LIFE:"+hero.getLife(), 10, 45);
		
		switch(state) {
		case START:
			g.drawImage(start,0,0,null);
			break;
		case PAUSE:
			g.drawImage(pause, 0, 0, null);
			break;
		case GAME_OVER:
			g.drawImage(gameover,0,0,null);
			break;
		}
		
		
}


	public static void main(String[] args) {
		World world = new World();
		JFrame jframe = new JFrame();
		jframe.add(world); //将数据JPanel加入JFrame中
		
		
		
		jframe.setSize(WIDTH, HEIGHT); //窗口的大小
		jframe.setAlwaysOnTop(true);//总是使窗口处于最前方，不会被遮挡
		jframe.setVisible(true); //窗口默认为透明的，只有设置才可见
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//这个叫做当窗口关闭时退出程序
		jframe.setLocationRelativeTo(null);   //设置窗口的位置
		world.action();
	}

}
