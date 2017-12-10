import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Panel extends JPanel implements Runnable {
	public int map[][] = new int[20][10];
	public int pX = 100, pY = 100;
	public int eX = 700, eY = 300;
	public int turtleSpeed = 4, enemySpeed = 2;
	public boolean doorOpen = false;
	public boolean pickedUpKey = false;
	public boolean gameOver, gameWon = false;
	private Rectangle doorRect, keyRect, winRect = new Rectangle(0,0,0,0);
	private ImageIcon floor, wall, player, enemy, playerL, playerR, playerL2, playerR2, door, openDoor, key, keyFloor, enemyL, enemyR, winFlag;
	private Thread game;
	
	public Panel() {
		loadMap(Game.level);
		floor = new ImageIcon(getClass().getResource("/gfx/Floor.png"));
		wall = new ImageIcon(getClass().getResource("/gfx/Wall.png"));
		playerR = new ImageIcon(getClass().getResource("/gfx/Turtle.png"));
		playerL = new ImageIcon(getClass().getResource("/gfx/LTurtle.png"));
		playerR2 = new ImageIcon(getClass().getResource("/gfx/Turtle2.png"));
		playerL2 = new ImageIcon(getClass().getResource("/gfx/LTurtle2.png"));
		door = new ImageIcon(getClass().getResource("/gfx/Door.png"));
		openDoor = new ImageIcon(getClass().getResource("/gfx/DoorOpen.png"));
		key = new ImageIcon(getClass().getResource("/gfx/Key.png"));
		keyFloor = new ImageIcon(getClass().getResource("/gfx/KeyFloor.png"));
		enemyL = new ImageIcon(getClass().getResource("/gfx/PurpleTurtle.png"));
		enemyR = new ImageIcon(getClass().getResource("/gfx/RPurpleTurtle.png"));
		winFlag = new ImageIcon(getClass().getResource("/gfx/WinFlag.png"));
		enemy = enemyL;
		player = playerR;
		repaint();
		setFocusable(true);
		this.addKeyListener(new KeyEvents());
		game = new Thread(this);
		game.start();
	}
	
	public void loadMap(int level) {
		try(DataInputStream in= new DataInputStream(new BufferedInputStream(getClass().getResourceAsStream("/maps/basic" + Game.level + ".map")))){
			int x = 0, y = 0;
			 for (y=0;y<10;y++) {
				 String s = in.readLine();
				 for(x=0;x<20;x++) {
					 map[x][y] = Integer.parseInt(s.split(" ")[x]); 	
			     }
			 }
		} catch(Exception e) {
			System.out.println("Error loading map");
			e.printStackTrace();
		}	 
	}
	
	public ArrayList<Rectangle> walls = new ArrayList<Rectangle>();
	private boolean setRects = false;
	
	@Override
	public void paint(Graphics g) {		
		int x = 0, y = 0;
		 for (y=0;y<10;y++) {
			 for(x=0;x<20;x++) {
				 if (map[x][y] == 0) {
					 g.drawImage(floor.getImage(), x*40, y*40, null);
				 } else if (map[x][y] == 1) {
					 g.drawImage(wall.getImage(), x*40, y*40, null);
					 if(!setRects)
						 walls.add(new Rectangle(x*40, y*40, 40, 40));
				 } else if (map[x][y] == 2) {
					 if (doorOpen) {
						 g.drawImage(openDoor.getImage(), x*40, y*40, null);
					 } else {
						 g.drawImage(door.getImage(), x*40, y*40, null); 
					 }
					 if(!setRects)
						 doorRect = new Rectangle(x*40+17, y*40, 5, 40);
				 } else if (map[x][y] == 3) {
					 if(!setRects)
						 keyRect = new Rectangle(x*40, y*40, 40, 40);
					 if (!pickedUpKey) {
						 g.drawImage(keyFloor.getImage(),x*40, y*40, null);
					 } else {
						 g.drawImage(floor.getImage(), x*40, y*40, null);
					 }
				 } else if (map[x][y] == 4) {
					 if(!setRects)
						 winRect = new Rectangle(x*40, y*40, 40, 40);
					 g.drawImage(winFlag.getImage(),x*40, y*40, null);
				 }
			 }
		 }
		 g.drawImage(enemy.getImage(), eX, eY, null);
		 g.drawImage(player.getImage(), pX, pY, null);
		 if(gameOver) {
			 g.drawImage(new ImageIcon(getClass().getResource("/gfx/GameOver.png")).getImage(), 0, 0, null);
		 }
		 if (gameWon) {
			 g.drawImage(new ImageIcon(getClass().getResource("/gfx/GameWon.png")).getImage(), 0, 0, null);
		 }
	}
	
	public int tpic = 1;
	public int tdir = 0;
	public int edir = 1;
	
	@Override
	public void run() {
		while (!gameOver && !gameWon) {
			if (doorOpen) {
				if (eX < pX) {
					edir = 0;
					boolean canMove = true;
					for(Rectangle r : walls) {
						 if (r.contains(eX+35+enemySpeed, eY)|| r.contains(eX+35+enemySpeed, eY+17)) {
							 canMove = false;
						 }
					 }
					 if (canMove) {
						 eX+=enemySpeed;
					 }
				} else if (eX > pX) {
					edir = 1;
					boolean canMove = true;
					for(Rectangle r : walls) {
						 if (r.contains(eX-enemySpeed, eY) || r.contains(eX-enemySpeed, eY+17)) {
							 canMove = false;
						 }
					 }
					 if (canMove) {
						 eX -=enemySpeed;
					 }
				}
				if (eY < pY) {
					boolean canMove = true;
					 for(Rectangle r : walls) {
						 if (r.contains(eX, eY+17+enemySpeed) || r.contains(eX+35, eY+17+enemySpeed)) {
							 canMove = false;
						 }
					 }
					 if (canMove) {
						 eY +=enemySpeed;
					 }	
				} else if (eY > pY) {
					boolean canMove = true;
					for(Rectangle r : walls) {
						 if (r.contains(eX, eY-enemySpeed) || r.contains(eX+35, eY-enemySpeed)) {
							 canMove = false;
						 }
					 }
					 if (canMove) {
						 eY -=enemySpeed;
					 }
				}
		}
			if(edir == 0) {
				enemy = enemyR;
			} else {
				enemy = enemyL;
			}
			if(winRect.contains(pX, pY) || winRect.contains(pX+35, pY) || winRect.contains(pX, pY+17) || winRect.contains(pX+35, pY+17)) {
				if(Game.level < 4) {
					Game.level ++;
					pX = 100;
					pY = 100;
					eX = 700;
					eY = 300;
					doorOpen = false;
					pickedUpKey = false;
					loadMap(Game.level);
					walls.clear();
				} else {
					gameWon = true;
					repaint();
				}
			}
			
			Rectangle turtRect = new Rectangle(pX, pY, 35, 17);
			if (turtRect.contains(eX, eY) || turtRect.contains(eX+35, eY) || turtRect.contains(eX, eY + 17) || turtRect.contains(eX + 35, eY + 17)) {
				gameOver = true;
			}
			repaint();
			try {
				game.sleep(40);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		repaint();
	}
	

	public class KeyEvents implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyChar() == 'w') {
				boolean canMove = true;
				 for(Rectangle r : walls) {
					 if (r.contains(pX, pY-4) || r.contains(pX+35, pY-4)) {
						 canMove = false;
					 }
				 }
				 if (doorRect.contains(pX, pY-4) && !doorOpen) {
					 if (pickedUpKey)
						 doorOpen = true;
					 canMove = false;
				 }
				 if (canMove) {
					 pY -=4;
					 tpic ++;
				 }			
			 }
			if (e.getKeyChar() == 's') {
				boolean canMove = true;
				for(Rectangle r : walls) {
					 if (r.contains(pX, pY+17+4) || r.contains(pX+35, pY+17+4)) {
						 canMove = false;
					 }
				 }
				if (doorRect.contains(pX, pY+17+4) && !doorOpen) {
					if (pickedUpKey)
						 doorOpen = true;
					 canMove = false;
				 }
				 if (canMove) {
					 pY +=4;
					 tpic ++;
				 }
			}
			if (e.getKeyChar() == 'a') {
				tdir = 1;
				boolean canMove = true;
				for(Rectangle r : walls) {
					 if (r.contains(pX-4, pY) || r.contains(pX-4, pY+17)) {
						 canMove = false;
					 }
				 }
				if (doorRect.contains(pX-4, pY) && !doorOpen) {
					if (pickedUpKey)
						 doorOpen = true;
					 canMove = false;
				 }
				 if (canMove) {
					 pX -=4;
					 tpic ++;
				 }
			}
			if (e.getKeyChar() == 'd') {
				tdir = 0;
				boolean canMove = true;
				for(Rectangle r : walls) {
					 if (r.contains(pX+35+4, pY)|| r.contains(pX+35+4, pY+17)) {
						 canMove = false;
					 }
				 }
				if (doorRect.contains(pX+35+4, pY) && !doorOpen) {
					if (pickedUpKey)
						 doorOpen = true;
					 canMove = false;
				 }
				 if (canMove) {
					 pX+=4;
					 tpic ++;
				 }
			}
			if (keyRect.contains(pX,pY)) {
				pickedUpKey = true;
			}
			if(tpic < 6) {
				if(tdir == 0) {
					player = playerR;
				} else {
					player = playerL;
				}
			} else {
				if(tdir == 0) {
					player = playerR2;
				} else {
					player = playerL2;
				}
			}
			
			if (tpic > 10) {
				tpic = 1;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
}