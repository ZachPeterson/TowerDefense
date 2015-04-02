import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Class to represent the main game panel in the application.
 * The game panel handels all update logic and determines what to update on the
 * control panel. Receives messages from the game panel on spawning towers and
 * monsters.
 *
 * @author Zachary Peterson
 * @version 1.0
 */
public class GamePanel extends JPanel implements Runnable {
    private Vector<Monster> myMonsters;
    private Vector<Tower> myTowers;
    private int score, health, money;

    private boolean isInPlaceMode;
    private TowerType towerToPlace;

    private BufferedImage backgroundImage;
    private BufferedImage pathImage;

    private BufferedImage basicTowerPlacer;
    private BufferedImage fastTowerPlacer;
    private BufferedImage megaTowerPlacer;

    private int currentWave;
    private int timeBetweenMonsterSpawn;
    private int monstersToSpawn;

    private int monstersToKill;

    private Timer waveSpawnTimer;

    private boolean dead;

    private BasicTower basic;
    private FastTower fast;
    private MegaTower mega;

    /**
     * Constructs the game panel.
     */
    public GamePanel() {
        setPreferredSize(new Dimension(600, 600));
        setBorder(BorderFactory.createLineBorder(Color.black));
        addMouseListener(new GameMouseListener());

        try {
            backgroundImage
                = LevelHelper.loadBackgroundFromLevelFile(
                    new File("levels.cfg"), 1);
            pathImage
                = LevelHelper.loadPathImageFromLevelFile(
                    new File("levels.cfg"), 1);

            basicTowerPlacer = ImageIO.read(new File("basicTower.png"));
            fastTowerPlacer = ImageIO.read(new File("fastTower.png"));
            megaTowerPlacer = ImageIO.read(new File("megaTower.png"));
        } catch (IOException e) {
            System.out.println("Could not load image");
            System.out.println(e.getMessage());
        }

        basic = new BasicTower(0, 0);
        fast = new FastTower(0, 0);
        mega = new MegaTower(0, 0);

        initialize();
    }

    /**
     * Initializes all of the game data to defaults so that the game can be
     * easily reset by calling this function.
     */
    public void initialize() {
        myMonsters = new Vector<Monster>();

        myTowers = new Vector<Tower>();

        currentWave = 0;
        timeBetweenMonsterSpawn = 5000;
        monstersToSpawn = currentWave * currentWave;

        monstersToKill = 0;

        waveSpawnTimer = new Timer(timeBetweenMonsterSpawn,
            new WaveSpawnTimeListener());

        score = 0;
        health = 100;
        money = 1000;

        isInPlaceMode = false;

        MessageQueue.getInstance().push(
            new Message<Integer>(MessageRecipient.CONTROL_PANEL,
                MessageType.UPDATE_SCORE, score));
        MessageQueue.getInstance().push(
            new Message<Integer>(MessageRecipient.CONTROL_PANEL,
                MessageType.UPDATE_HEALTH, health));
        MessageQueue.getInstance().push(
            new Message<Integer>(MessageRecipient.CONTROL_PANEL,
                MessageType.UPDATE_MONEY, money));
        MessageQueue.getInstance().push(
            new Message<Integer>(MessageRecipient.CONTROL_PANEL,
                MessageType.UPDATE_CURRENT_WAVE, currentWave));
        MessageQueue.getInstance().push(
            new Message<Integer>(MessageRecipient.CONTROL_PANEL,
                MessageType.UPDATE_MONSTER_COUNT, monstersToKill));
        MessageQueue.getInstance().push(
            new Message<String>(MessageRecipient.CONTROL_PANEL,
                MessageType.START_GAME, "Start Game"));

        dead = false;
    }

    /**
     * Runs the game.
     * Called when the new game panel thread is created and enters the infinite
     * game loop. Also calculates the time differential so that time can be
     * used to scale all update calls.
     */
    public void run() {
        long currentTime = 0;
        long previousTime = 0;
        double dt = 0.0;

        previousTime = System.nanoTime();

        while (true) {
            currentTime = System.nanoTime();
            dt = (currentTime - previousTime) / 1000000000.0;

            update(dt);

            previousTime = currentTime;
        }
    }

    /**
     * Updates all of the game's stuff.
     * Uses a calculated time differential to scale all updates.
     *
     * @param dt The time differential to use.
     */
    public void update(double dt) {
        processMessages();

        if (health <= 0 && !dead) {
            dead = true;
            MessageQueue.getInstance().push(
                new Message<String>(MessageRecipient.CONTROL_PANEL,
                    MessageType.END_GAME, "End Game"));
        }

        if (!dead) {
            for (Tower t : myTowers) {
                t.update(dt, myMonsters);
            }

            for (Monster m : myMonsters) {
                m.update(dt);
            }

            for (int i = 0; i < myMonsters.size(); i++) {
                if (!myMonsters.get(i).isAlive()) {
                    myMonsters.remove(i);
                    if (monstersToKill == 1) {
                        MessageQueue.getInstance().push(
                            new Message<String>(MessageRecipient.CONTROL_PANEL,
                                MessageType.ENABLE_SPAWN_BUTTON,
                                "Enable Spawn Button"));
                    }
                    monstersToKill--;
                    MessageQueue.getInstance().push(
                        new Message<Integer>(MessageRecipient.CONTROL_PANEL,
                            MessageType.UPDATE_MONSTER_COUNT, monstersToKill));
                }
            }
        }

        repaint();
    }

    /**
     * Changes the level to the one specified by levelIndex.
     *
     * @param levelIndex The level to change to.
     */
    public void changeLevel(int levelIndex) {
        backgroundImage
            = LevelHelper.loadBackgroundFromLevelFile(
                new File("levels.cfg"), levelIndex);
        pathImage
            = LevelHelper.loadPathImageFromLevelFile(
                new File("levels.cfg"), levelIndex);
        PathNodeList.getInstance().setNodeList(
                LevelHelper.loadPathNodeListFromLevelFile(
                    new File("levels.cfg"), levelIndex));
    }

    /**
     * Handles all message processing for the game.
     */
    public void processMessages() {
        Message currentMessage;
        while (MessageQueue.getInstance().peek(MessageRecipient.GAME_PANEL)
            != null) {
            currentMessage = MessageQueue.getInstance().pop(
                MessageRecipient.GAME_PANEL);
            if (currentMessage.getMessageType() == MessageType.QUIT_GAME) {
                System.exit(1);
            } else if (currentMessage.getMessageType()
                == MessageType.PLACE_BASIC_TOWER) {
                if (basic.getCost() <= money) {
                    isInPlaceMode = true;
                    towerToPlace = TowerType.BASIC_TOWER;
                }
            } else if (currentMessage.getMessageType()
                == MessageType.PLACE_FAST_TOWER) {
                if (fast.getCost() <= money) {
                    isInPlaceMode = true;
                    towerToPlace = TowerType.FAST_TOWER;
                }
            } else if (currentMessage.getMessageType()
                == MessageType.PLACE_MEGA_TOWER) {
                if (mega.getCost() <= money) {
                    isInPlaceMode = true;
                    towerToPlace = TowerType.MEGA_TOWER;
                }
            } else if (currentMessage.getMessageType()
                == MessageType.SPAWN_BASIC_TOWER) {
                Point spawnPoint = (Point) currentMessage.getMessageData();
                myTowers.add(new BasicTower((int) (spawnPoint.getX()
                    - basicTowerPlacer.getWidth() / 2),
                    (int) (spawnPoint.getY()
                    - basicTowerPlacer.getHeight() / 2)));
            } else if (currentMessage.getMessageType()
                == MessageType.SPAWN_FAST_TOWER) {
                Point spawnPoint = (Point) currentMessage.getMessageData();
                myTowers.add(new FastTower((int) (spawnPoint.getX()
                    - fastTowerPlacer.getHeight() / 2),
                    (int) (spawnPoint.getY()
                    - fastTowerPlacer.getHeight() / 2)));
            } else if (currentMessage.getMessageType()
                == MessageType.SPAWN_MEGA_TOWER) {
                Point spawnPoint = (Point) currentMessage.getMessageData();
                myTowers.add(new MegaTower((int) (spawnPoint.getX()
                    - megaTowerPlacer.getHeight() / 2),
                    (int) (spawnPoint.getY()
                    - megaTowerPlacer.getHeight() / 2)));
            } else if (currentMessage.getMessageType()
                == MessageType.DEAL_DAMAGE) {
                health -= (int) currentMessage.getMessageData();
                MessageQueue.getInstance().push(
                    new Message<Integer>(MessageRecipient.CONTROL_PANEL,
                        MessageType.UPDATE_HEALTH, health));
            } else if (currentMessage.getMessageType()
                == MessageType.ADD_SCORE) {
                score += (int) currentMessage.getMessageData();
                MessageQueue.getInstance().push(
                    new Message<Integer>(MessageRecipient.CONTROL_PANEL,
                        MessageType.UPDATE_SCORE, score));
            } else if (currentMessage.getMessageType()
                == MessageType.ADD_MONEY) {
                money += (int) currentMessage.getMessageData();
                MessageQueue.getInstance().push(
                    new Message<Integer>(MessageRecipient.CONTROL_PANEL,
                        MessageType.UPDATE_MONEY, money));
            } else if (currentMessage.getMessageType()
                == MessageType.SPAWN_WAVE) {
                currentWave++;
                if (currentWave % 5 == 0) {
                    monstersToSpawn = currentWave / 5;
                    monstersToKill = monstersToSpawn;
                    timeBetweenMonsterSpawn = 10000;
                    waveSpawnTimer = new Timer(timeBetweenMonsterSpawn,
                        new WaveSpawnTimeListener());
                    waveSpawnTimer.start();
                    MessageQueue.getInstance().push(
                        new Message<Integer>(MessageRecipient.CONTROL_PANEL,
                            MessageType.UPDATE_CURRENT_WAVE, currentWave));
                    MessageQueue.getInstance().push(
                        new Message<String>(MessageRecipient.CONTROL_PANEL,
                            MessageType.DISABLE_SPAWN_BUTTON,
                            "Disable Spawn Button"));
                    MessageQueue.getInstance().push(
                        new Message<Integer>(MessageRecipient.CONTROL_PANEL,
                            MessageType.UPDATE_MONSTER_COUNT,
                            monstersToKill));
                } else {
                    monstersToSpawn = currentWave * currentWave;
                    monstersToKill = monstersToSpawn;
                    timeBetweenMonsterSpawn = 5000 / currentWave;
                    waveSpawnTimer = new Timer(timeBetweenMonsterSpawn,
                        new WaveSpawnTimeListener());
                    waveSpawnTimer.start();
                    MessageQueue.getInstance().push(
                        new Message<Integer>(MessageRecipient.CONTROL_PANEL,
                            MessageType.UPDATE_CURRENT_WAVE, currentWave));
                    MessageQueue.getInstance().push(
                        new Message<String>(MessageRecipient.CONTROL_PANEL,
                            MessageType.DISABLE_SPAWN_BUTTON,
                            "Disable Spawn Button"));
                    MessageQueue.getInstance().push(
                        new Message<Integer>(MessageRecipient.CONTROL_PANEL,
                            MessageType.UPDATE_MONSTER_COUNT,
                            monstersToKill));
                }
            } else if (currentMessage.getMessageType()
                == MessageType.SPAWN_MONSTER) {
                if (currentWave % 5 == 0) {
                    myMonsters.add(new Tank(currentWave));
                } else {
                    myMonsters.add(new Monster(currentWave));
                }
            } else if (currentMessage.getMessageType()
                == MessageType.CHANGE_LEVEL) {
                int level = (int) currentMessage.getMessageData();
                if (level == 0) {
                    MessageQueue.getInstance().push(new Message<String>(
                        MessageRecipient.CONTROL_PANEL, MessageType.OPEN_MENU,
                        "Open Menu"));
                    dead = false;
                } else {
                    MessageQueue.getInstance().push(new Message<String>(
                        MessageRecipient.CONTROL_PANEL, MessageType.OPEN_GAME,
                        "Open Game"));
                    initialize();
                }

                changeLevel(level);
            } else {
                System.out.println("Unrecognized message processed in game"
                    + " panel!");
                System.out.println("Message contents are as follows:");
                System.out.println("Message Recipient: "
                    + currentMessage.getMessageRecipient());
                System.out.println("Message Type: "
                    + currentMessage.getMessageType());
                System.out.println("Message Data: "
                    + currentMessage.getMessageData());
            }
        }
    }

    /**
     * Actually renders the game's graphics.
     * Called by paintComponent simply so that all newly created game elements
     * appear in their own functions.
     *
     * @param g The Graphics object to draw with.
     */
    private void render(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, null);

        g.drawImage(pathImage, 0, 0, null);

        for (int i = 0; i < myTowers.size(); i++) {
            myTowers.get(i).render(g);
        }

        for (int j = 0; j < myMonsters.size(); j++) {
            myMonsters.get(j).render(g);
        }

        if (isInPlaceMode) {
            Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
            int mouseX
                = (int) (mouseLocation.getX() - getLocationOnScreen().getX());
            int mouseY
                = (int) (mouseLocation.getY() - getLocationOnScreen().getY());

            if (towerToPlace == TowerType.BASIC_TOWER) {
                Color oldColor = g.getColor();
                boolean towerToTowerCollide = false;
                for (Tower t : myTowers) {
                    if (ImageHelper.checkTowerTowerIntersection(
                        basicTowerPlacer, new Point(mouseX
                            - basicTowerPlacer.getWidth() / 2, mouseY
                            - basicTowerPlacer.getHeight() / 2), t.getImage(),
                            new Point(t.getX(), t.getY()))) {
                        towerToTowerCollide = true;
                    }
                }
                if (ImageHelper.checkTowerPathIntersection(basicTowerPlacer,
                    new Point(mouseX - basicTowerPlacer.getWidth() / 2, mouseY
                        - basicTowerPlacer.getHeight() / 2), pathImage)
                        || towerToTowerCollide) {
                    g.setColor(new Color(255, 0, 0, 40));
                } else {
                    g.setColor(new Color(255, 255, 255, 40));
                }
                g.fillOval(mouseX - basic.getRange(), mouseY
                    - basic.getRange(), basic.getRange() * 2,
                    basic.getRange() * 2);
                g.setColor(oldColor);
                g.drawImage(basicTowerPlacer, mouseX
                    - basicTowerPlacer.getWidth() / 2, mouseY
                    - basicTowerPlacer.getHeight() / 2, null);
            } else if (towerToPlace == TowerType.FAST_TOWER) {
                Color oldColor = g.getColor();
                boolean towerToTowerCollide = false;
                for (Tower t : myTowers) {
                    if (ImageHelper.checkTowerTowerIntersection(
                        fastTowerPlacer, new Point(mouseX
                            - fastTowerPlacer.getWidth() / 2, mouseY
                            - fastTowerPlacer.getHeight() / 2), t.getImage(),
                            new Point(t.getX(), t.getY()))) {
                        towerToTowerCollide = true;
                    }
                }
                if (ImageHelper.checkTowerPathIntersection(fastTowerPlacer,
                    new Point(mouseX - fastTowerPlacer.getWidth() / 2, mouseY
                        - fastTowerPlacer.getHeight() / 2), pathImage)
                        || towerToTowerCollide) {
                    g.setColor(new Color(255, 0, 0, 40));
                } else {
                    g.setColor(new Color(255, 255, 255, 40));
                }
                g.fillOval(mouseX - fast.getRange(), mouseY - fast.getRange(),
                    fast.getRange() * 2, fast.getRange() * 2);
                g.setColor(oldColor);
                g.drawImage(fastTowerPlacer, mouseX
                    - fastTowerPlacer.getWidth() / 2, mouseY
                    - fastTowerPlacer.getHeight() / 2, null);
            } else if (towerToPlace == TowerType.MEGA_TOWER) {
                Color oldColor = g.getColor();
                boolean towerToTowerCollide = false;
                for (Tower t : myTowers) {
                    if (ImageHelper.checkTowerTowerIntersection(
                        megaTowerPlacer, new Point(mouseX
                            - megaTowerPlacer.getWidth() / 2, mouseY
                            - megaTowerPlacer.getHeight() / 2), t.getImage(),
                            new Point(t.getX(), t.getY()))) {
                        towerToTowerCollide = true;
                    }
                }
                if (ImageHelper.checkTowerPathIntersection(megaTowerPlacer,
                    new Point(mouseX - megaTowerPlacer.getWidth() / 2, mouseY
                        - megaTowerPlacer.getHeight() / 2), pathImage)) {
                    g.setColor(new Color(255, 0, 0, 40));
                } else {
                    g.setColor(new Color(255, 255, 255, 40));
                }
                g.fillOval(mouseX - mega.getRange(), mouseY - mega.getRange(),
                    mega.getRange() * 2, mega.getRange() * 2);
                g.setColor(oldColor);
                g.drawImage(megaTowerPlacer, mouseX
                    - megaTowerPlacer.getWidth() / 2, mouseY
                    - megaTowerPlacer.getHeight() / 2, null);
            } else {
                System.out.println("ERROR: Tower type does not match existing"
                        + " tower types");
            }
        }
        if (dead) {
            Font oldFont = g.getFont();
            g.setFont(new Font("Arial", Font.BOLD, 46));
            String line1 = "You have died!";
            int line1Length
                = (int) g.getFontMetrics().getStringBounds(line1, g).getWidth();
            String line2 = "Click to go to menu";
            int line2Length
                = (int) g.getFontMetrics().getStringBounds(line2, g).getWidth();
            g.drawString(line1, 305 - line1Length / 2, 280);
            g.drawString(line2, 305 - line2Length / 2, 320);
            g.setFont(oldFont);
        }
    }

    /**
     * Called by the window each time the graphics need to be updated.
     *
     * @param g The Graphics to draw with.
     */
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        render(g);
    }

    /**
     * Private inner class to represent a wave spawn timer.
     * actionPerformed() is called each time a monster should be spawned. The
     * update function actually handles which monster to spawn, this just ticks
     * the monster count up.
     *
     * @author Zachary Peterson
     * @version 1.0
     */
    private class WaveSpawnTimeListener implements ActionListener {
        private int monstersLeftToSpawn;

        /**
         * Constructs the WaveSpawnTimeListener and sets the number of monsters
         * left to spawn based on the current game value.
         */
        public WaveSpawnTimeListener() {
            monstersLeftToSpawn = monstersToSpawn;
        }

        /**
         * Actually sends a spawn message to the game.
         *
         * @param e The event triggered.
         */
        public void actionPerformed(ActionEvent e) {
            if (monstersLeftToSpawn > 0) {
                MessageQueue.getInstance().push(
                    new Message<String>(MessageRecipient.GAME_PANEL,
                        MessageType.SPAWN_MONSTER, "Spawn Monster"));
                monstersLeftToSpawn--;
            } else {
                Timer t = (Timer) e.getSource();
                if (t != null) {
                    t.stop();
                } else {
                    System.out.println("ERROR: Unable to stop timer, could not"
                            + " get event source");
                }
            }
        }
    }

    /**
     * Private inner class to handle mouse clicks in the main game panel.
     *
     * @author Zachary Peterson
     * @version 1.0
     */
    private class GameMouseListener extends MouseAdapter {

        /**
         * Called whenever the mouse is clicked.
         *
         * @param e A mouse event containing data on what happened.
         */
        public void mousePressed(MouseEvent e) {
            Point p = e.getPoint();
            if (dead) {
                MessageQueue.getInstance().push(new Message<Integer>(
                    MessageRecipient.GAME_PANEL, MessageType.CHANGE_LEVEL, 0));
            } else if (isInPlaceMode) {
                if (towerToPlace == TowerType.BASIC_TOWER) {
                    if (basic.getCost() <= money) {
                        boolean towerToTowerCollide = false;
                        for (Tower t : myTowers) {
                            if (ImageHelper.checkTowerTowerIntersection(
                                basicTowerPlacer, new Point((int) p.getX()
                                - basicTowerPlacer.getWidth() / 2,
                                (int) p.getY()
                                - basicTowerPlacer.getHeight() / 2),
                                t.getImage(), new Point(t.getX(),
                                t.getY()))) {
                                    towerToTowerCollide = true;
                            }
                        }
                        if (!ImageHelper.checkTowerPathIntersection(
                            basicTowerPlacer, new Point((int) (p.getX()
                            - basicTowerPlacer.getWidth() / 2), (int) (p.getY()
                            - basicTowerPlacer.getHeight() / 2)), pathImage)
                            && !towerToTowerCollide) {
                            MessageQueue.getInstance().push(
                                new Message<Point>(
                                    MessageRecipient.GAME_PANEL,
                                    MessageType.SPAWN_BASIC_TOWER, p));
                            money -= basic.getCost();
                            MessageQueue.getInstance().push(
                                new Message<Integer>(
                                    MessageRecipient.CONTROL_PANEL,
                                    MessageType.UPDATE_MONEY, money));
                            isInPlaceMode = false;
                        }
                    }
                } else if (towerToPlace == TowerType.FAST_TOWER) {
                    if (fast.getCost() <= money) {
                        boolean towerToTowerCollide = false;
                        for (Tower t : myTowers) {
                            if (ImageHelper.checkTowerTowerIntersection(
                                fastTowerPlacer, new Point((int) p.getX()
                                - fastTowerPlacer.getWidth() / 2,
                                (int) p.getY()
                                - fastTowerPlacer.getHeight() / 2),
                                t.getImage(), new Point(t.getX(),
                                t.getY()))) {
                                    towerToTowerCollide = true;
                            }
                        }
                        if (!ImageHelper.checkTowerPathIntersection(
                            fastTowerPlacer, new Point((int) (p.getX()
                            - fastTowerPlacer.getWidth() / 2), (int) (p.getY()
                            - fastTowerPlacer.getHeight() / 2)), pathImage)
                            && !towerToTowerCollide) {
                            MessageQueue.getInstance().push(
                                new Message<Point>(
                                    MessageRecipient.GAME_PANEL,
                                    MessageType.SPAWN_FAST_TOWER, p));
                            money -= fast.getCost();
                            MessageQueue.getInstance().push(
                                new Message<Integer>(
                                    MessageRecipient.CONTROL_PANEL,
                                    MessageType.UPDATE_MONEY, money));
                            isInPlaceMode = false;
                        }
                    }
                } else if (towerToPlace == TowerType.MEGA_TOWER) {
                    if (mega.getCost() <= money) {
                        boolean towerToTowerCollide = false;
                        for (Tower t : myTowers) {
                            if (ImageHelper.checkTowerTowerIntersection(
                                megaTowerPlacer, new Point((int) p.getX()
                                - megaTowerPlacer.getWidth() / 2,
                                (int) p.getY()
                                - megaTowerPlacer.getHeight() / 2),
                                t.getImage(), new Point(t.getX(),
                                t.getY()))) {
                                    towerToTowerCollide = true;
                            }
                        }
                        if (!ImageHelper.checkTowerPathIntersection(
                            megaTowerPlacer, new Point((int) (p.getX()
                            - megaTowerPlacer.getWidth() / 2), (int) (p.getY()
                            - megaTowerPlacer.getHeight() / 2)), pathImage)
                            && !towerToTowerCollide) {
                            MessageQueue.getInstance().push(
                                new Message<Point>(
                                    MessageRecipient.GAME_PANEL,
                                    MessageType.SPAWN_MEGA_TOWER, p));
                            money -= mega.getCost();
                            MessageQueue.getInstance().push(
                                new Message<Integer>(
                                    MessageRecipient.CONTROL_PANEL,
                                    MessageType.UPDATE_MONEY, money));
                            isInPlaceMode = false;
                        }
                    }
                }
            }
        }
    }
}
