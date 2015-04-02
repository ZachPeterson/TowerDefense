import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 * Main class to run the game. Sets up the games control panel and the main
 * display panel and then sets both to visible.
 *
 * @author Zachary Peterson
 * @version 1.0
 */
public class TowerDefenseGame {

    public static void main(String[] args) {
		// Create a JFrame for the main window
        JFrame mainFrame = new JFrame("Tower Defense");
		
		// Exit the frame when the close button is clicked
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create a new game control panel and add it to the main frame
		// Also create a game panel and add it
        GameControlPanel gCPanel = new GameControlPanel();
        GamePanel gPanel = new GamePanel();
        mainFrame.add(gCPanel, BorderLayout.WEST);
        mainFrame.add(gPanel, BorderLayout.CENTER);
        mainFrame.pack();
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);

		// Create the game thread and game control panel threads, then
		// start both of them
        Thread gamePanelThread = new Thread(gPanel);
        gamePanelThread.start();
        Thread gameControlPanelThread = new Thread(gCPanel);
        gameControlPanelThread.start();
    }
}
