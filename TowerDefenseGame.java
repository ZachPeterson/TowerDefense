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
        JFrame mainFrame = new JFrame("Tower Defense");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GameControlPanel gCPanel = new GameControlPanel();
        GamePanel gPanel = new GamePanel();
        mainFrame.add(gCPanel, BorderLayout.WEST);
        mainFrame.add(gPanel, BorderLayout.CENTER);
        mainFrame.pack();
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);

        Thread gamePanelThread = new Thread(gPanel);
        gamePanelThread.start();
        Thread gameControlPanelThread = new Thread(gCPanel);
        gameControlPanelThread.start();
    }
}
