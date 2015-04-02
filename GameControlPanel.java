import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class to hold all of the control panel components such as the score labels
 * and add tower buttons.
 *
 * @author Zachary Peterson
 * @version 1.0
 */
public class GameControlPanel extends JPanel implements Runnable {
    private JLabel selectLevel, score, health, money, currentWave, monstersLeft;
    private JButton level1, level2, addBasicTower, addFastTower, addMegaTower,
        spawnWave;

    /**
     * Action listener for the Basic Tower add button.
     *
     * @author Zachary Peterson
     * @version 1.0
     */
    private class BasicTowerListener implements ActionListener {
        /**
         * Queues a message for the game panel to start placing of a basic
         * tower.
         *
         * @param e The action event triggered for this action listener.
         */
        public void actionPerformed(ActionEvent e) {
            MessageQueue.getInstance().push(
                new Message<String>(MessageRecipient.GAME_PANEL,
                    MessageType.PLACE_BASIC_TOWER, "Place Basic Tower"));
        }
    }

    /**
     * Action listener for the Fast Tower add button.
     *
     * @author Zachary Peterson
     * @version 1.0
     */
    private class FastTowerListener implements ActionListener {
        /**
         * Queues a message for the game panel to start placing of a fast
         * tower.
         *
         * @param e The action event triggered for this action listener.
         */
        public void actionPerformed(ActionEvent e) {
            MessageQueue.getInstance().push(
                new Message<String>(MessageRecipient.GAME_PANEL,
                    MessageType.PLACE_FAST_TOWER, "Place Fast Tower"));
        }
    }

    /**
     * Action listener for the Mega Tower add button.
     *
     * @author Zachary Peterson
     * @version 1.0
     */
    private class MegaTowerListener implements ActionListener {
        /**
         * Queues a message for the game panel to start placing of a mega
         * tower.
         *
         * @param e The action event triggered for this action listener.
         */
        public void actionPerformed(ActionEvent e) {
            MessageQueue.getInstance().push(
                new Message<String>(MessageRecipient.GAME_PANEL,
                    MessageType.PLACE_MEGA_TOWER, "Place Mega Tower"));
        }
    }

    /**
     * Action listener for the Spawn Wave button.
     *
     * @author Zachary Peterson
     * @version 1.0
     */
    private class SpawnWaveListener implements ActionListener {
        /**
         * Queues a message for the game panel to spawn a wave of monsters.
         *
         * @param e The action event triggered for this action listener.
         */
        public void actionPerformed(ActionEvent e) {
            MessageQueue.getInstance().push(
                new Message<String>(MessageRecipient.GAME_PANEL,
                    MessageType.SPAWN_WAVE, "Spawn Wave"));
        }
    }

    /**
     * Action listener for the Spawn Wave button.
     *
     * @author Zachary Peterson
     * @version 1.0
     */
    private class Level1Listener implements ActionListener {
        /**
         * Queues a message for the game panel to spawn a wave of monsters.
         *
         * @param e The action event triggered for this action listener.
         */
        public void actionPerformed(ActionEvent e) {
            MessageQueue.getInstance().push(
                new Message<Integer>(MessageRecipient.GAME_PANEL,
                    MessageType.CHANGE_LEVEL, 1));
        }
    }

    /**
     * Action listener for the Spawn Wave button.
     *
     * @author Zachary Peterson
     * @version 1.0
     */
    private class Level2Listener implements ActionListener {
        /**
         * Queues a message for the game panel to spawn a wave of monsters.
         *
         * @param e The action event triggered for this action listener.
         */
        public void actionPerformed(ActionEvent e) {
            MessageQueue.getInstance().push(
                new Message<Integer>(MessageRecipient.GAME_PANEL,
                    MessageType.CHANGE_LEVEL, 2));
        }
    }

    /**
     * Constructs the GameControlPanel and sets up all of the labels and
     * buttons to use for it.
     */
    public GameControlPanel() {
        setPreferredSize(new Dimension(150, 600));

        setBorder(BorderFactory.createLineBorder(Color.black));

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		// Create the panel items for when the game is playing
        score = new JLabel("Score: ");
        score.setPreferredSize(new Dimension(150, 20));
        score.setAlignmentX(Component.CENTER_ALIGNMENT);
        health = new JLabel("Health: ");
        health.setPreferredSize(new Dimension(150, 20));
        health.setAlignmentX(Component.CENTER_ALIGNMENT);
        money = new JLabel("Money: ");
        money.setPreferredSize(new Dimension(150, 20));
        money.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentWave = new JLabel("Current Wave: ");
        currentWave.setPreferredSize(new Dimension(150, 20));
        currentWave.setAlignmentX(Component.CENTER_ALIGNMENT);
        monstersLeft = new JLabel("Monsters Left: ");
        monstersLeft.setPreferredSize(new Dimension(150, 20));
        monstersLeft.setAlignmentX(Component.CENTER_ALIGNMENT);

        addBasicTower = new JButton("<html><center>Add Basic Tower<br>"
            + "Cost: 400</center></html");
        addBasicTower.setPreferredSize(new Dimension(150, 50));
        addBasicTower.setAlignmentX(Component.CENTER_ALIGNMENT);
        addBasicTower.addActionListener(new BasicTowerListener());

        addFastTower = new JButton("<html><center>Add Fast Tower<br>"
                + "Cost: 500</center></html>");
        addFastTower.setPreferredSize(new Dimension(150, 50));
        addFastTower.setAlignmentX(Component.CENTER_ALIGNMENT);
        addFastTower.addActionListener(new FastTowerListener());

        addMegaTower = new JButton("<html><center>Add Mega Tower<br>"
                + "Cost: 2,000</center></html>");
        addMegaTower.setPreferredSize(new Dimension(150, 50));
        addMegaTower.setAlignmentX(Component.CENTER_ALIGNMENT);
        addMegaTower.addActionListener(new MegaTowerListener());

        spawnWave = new JButton("Spawn Wave");
        spawnWave.setPreferredSize(new Dimension(150, 50));
        spawnWave.setAlignmentX(Component.CENTER_ALIGNMENT);
        spawnWave.addActionListener(new SpawnWaveListener());

		// Create the panel items for selecting a level from the main menu
        selectLevel = new JLabel("Select level to play");
        selectLevel.setPreferredSize(new Dimension(150, 20));
        selectLevel.setAlignmentX(Component.CENTER_ALIGNMENT);

        level1 = new JButton("Level 1");
        level1.setPreferredSize(new Dimension(150, 50));
        level1.setAlignmentX(Component.CENTER_ALIGNMENT);
        level1.addActionListener(new Level1Listener());
        level2 = new JButton("Level 2");
        level2.setPreferredSize(new Dimension(150, 50));
        level2.setAlignmentX(Component.CENTER_ALIGNMENT);
        level2.addActionListener(new Level2Listener());

		// Start up the game on the menu level
        MessageQueue.getInstance().push(new Message<Integer>(
            MessageRecipient.GAME_PANEL, MessageType.CHANGE_LEVEL, 0));
    }

    /**
     * Sets up all of the menu components for the game.
     */
    public void setupMenuPanel() {
        removeAll();

        add(Box.createRigidArea(new Dimension(0, 100)));
        add(selectLevel);
        add(Box.createRigidArea(new Dimension(0, 50)));
        add(level1);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(level2);

        revalidate();
        repaint();
    }

    /**
     * Sets up all of the game control elements such as tower adding.
     */
    public void setupGameControlPanel() {
        removeAll();

        add(Box.createRigidArea(new Dimension(0, 50)));
        add(score);
        add(Box.createRigidArea(new Dimension(0, 25)));
        add(health);
        add(Box.createRigidArea(new Dimension(0, 25)));
        add(money);
        add(Box.createRigidArea(new Dimension(0, 40)));
        add(addBasicTower);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(addFastTower);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(addMegaTower);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(spawnWave);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(currentWave);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(monstersLeft);

        revalidate();
        repaint();
    }

    /**
     * Main thread for the GameControlPanel.
     * Handles all messages and updates control panel information accordingly.
     */
    public void run() {
        while (true) {
            Message currentMessage;
            while (MessageQueue.getInstance().peek(
                MessageRecipient.CONTROL_PANEL) != null) {
                currentMessage = MessageQueue.getInstance().pop(
                    MessageRecipient.CONTROL_PANEL);

                if (currentMessage.getMessageType()
                    == MessageType.UPDATE_SCORE) {
                    score.setText("Score: " + currentMessage.getMessageData());
                } else if (currentMessage.getMessageType()
                    == MessageType.UPDATE_HEALTH) {
                    health.setText("Health: "
                        + currentMessage.getMessageData());
                } else if (currentMessage.getMessageType()
                    == MessageType.UPDATE_MONEY) {
                    money.setText("Money: " + currentMessage.getMessageData());
                } else if (currentMessage.getMessageType()
                    == MessageType.ENABLE_SPAWN_BUTTON) {
                    spawnWave.setEnabled(true);
                } else if (currentMessage.getMessageType()
                    == MessageType.DISABLE_SPAWN_BUTTON) {
                    spawnWave.setEnabled(false);
                } else if (currentMessage.getMessageType()
                    == MessageType.UPDATE_CURRENT_WAVE) {
                    currentWave.setText("Current Wave: "
                        + currentMessage.getMessageData());
                } else if (currentMessage.getMessageType()
                    == MessageType.UPDATE_MONSTER_COUNT) {
                    monstersLeft.setText("Monsters Left: "
                        + currentMessage.getMessageData());
                } else if (currentMessage.getMessageType()
                    == MessageType.END_GAME) {
                    addBasicTower.setEnabled(false);
                    addFastTower.setEnabled(false);
                    addMegaTower.setEnabled(false);
                    spawnWave.setEnabled(false);
                } else if (currentMessage.getMessageType()
                    == MessageType.START_GAME) {
                    addBasicTower.setEnabled(true);
                    addFastTower.setEnabled(true);
                    addMegaTower.setEnabled(true);
                    spawnWave.setEnabled(true);
                } else if (currentMessage.getMessageType()
                    == MessageType.OPEN_MENU) {
                    setupMenuPanel();
                } else if (currentMessage.getMessageType()
                    == MessageType.OPEN_GAME) {
                    setupGameControlPanel();
                } else {
                    System.out.println("Unrecognised message processed in game"
                        + " control panel");
                    System.out.println("Message contents are as follows:");
                    System.out.println("Message Recipient: "
                        + currentMessage.getMessageRecipient());
                    System.out.println("Message Type: "
                        + currentMessage.getMessageType());
                    System.out.println("Message Data: "
                        + currentMessage.getMessageData());
                }
            }

			// Sleep the thread for a while so we don't process things too quickly
            try {
                Thread.sleep(50);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
