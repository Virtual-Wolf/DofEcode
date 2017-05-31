
import java.awt.event.*;

import javax.swing.*;


public class Game extends JFrame {
	
	public static int level = 1;
	
	public Game() {
		ImageIcon image = new ImageIcon("src/gfx/Floor.png");
		setIconImage(image.getImage());
		setSize(806, 429);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Turtle RPG");
		setVisible(true);
		add(new Panel());
    }

   public static void main(String[] args) {
	   
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            new Game();
         }
      });

    }

}
