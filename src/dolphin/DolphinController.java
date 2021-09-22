package dolphin;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.*;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.text.Utilities;

class DolphinController extends TimerTask implements MouseListener  {
    public static final int TIME_TO_MOVE_DOLPHINS_IN_MILLISECONDS = 700;

    private static final ImageIcon aliveDolphin = new ImageIcon("aliveDolphin.jpg");
    private static final ImageIcon deadDolphin = new ImageIcon("deadDolphin.jpg");

    public static void main(String args[]) {

        new DolphinController("Push the Baby Dolphin up to the surface so it can breathe!",
                              50, 50, 800, 600);
    }

    private final JFrame gameJFrame;
    private final Timer gameTimer = new Timer();

    // 3 types of Dolphins currently, Slow and Fast and very fast
    //private final Dolphin[] gameDolphin = new Dolphin[NUMBER_OF_DOLPHIN_TYPES];
    private int currentLevel;

    private boolean gameIsRunning = false;
    private int xMouseOffsetToContentPaneFromJFrame;
    private int yMouseOffsetToContentPaneFromJFrame;

    //counter for misses, flagger for a miss and a counter for misses every 6 seconds
    private boolean missFlag;
    private int timesMissed = 0;
    private long lastPressed;


    //currentDolphins hold the list for current level, dolphin levels is the levels and dolphins.
    private Dolphin[][] DolphinsLevel = new Dolphin[4][4];
    private List<Dolphin> currentDolphins;
    private List<Dolphin> erasedDolphins = new ArrayList<>();


    public DolphinController(String passedInWindowTitle, int gameWindowX, int gameWindowY, int gameWindowWidth, int gameWindowHeight) {
        lastPressed = System.currentTimeMillis();
        gameJFrame = new JFrame(passedInWindowTitle);
        gameJFrame.setSize(gameWindowWidth, gameWindowHeight);
        gameJFrame.setLocation(gameWindowX, gameWindowY);
        gameJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //changing crosshair
        gameJFrame.setCursor(Frame.CROSSHAIR_CURSOR);

        // no layout, will use absolute system
        Container gameContentPane = gameJFrame.getContentPane();
        gameContentPane.setLayout(null);
        gameContentPane.setBackground(Color.blue);
        gameJFrame.setVisible(true);

        // Event mouse position is given relative to JFrame, where
        // dolphin's image in JLabel is given relative to ContentPane,
        // so adjust for the border ( / 2  since border is on either side)
        int borderWidth = (gameWindowWidth - gameContentPane.getWidth()) / 2;
        // assume side border = bottom border; ignore title bar
        xMouseOffsetToContentPaneFromJFrame = borderWidth;
        yMouseOffsetToContentPaneFromJFrame = gameWindowHeight - gameContentPane.getHeight() - borderWidth;

        // create the dolphin versions, now that JFrame has been initialized
        for(int i = 0; i < 4; i++){
                DolphinsLevel[i][0] = new SlowDolphin(gameJFrame, 3, 10);
                DolphinsLevel[i][1] =new FastDolphin(gameJFrame, 4, 25);
                DolphinsLevel[i][2] = new VeryFastDolphin(gameJFrame, 6, 40);
                DolphinsLevel[i][3] = new VeryFastDolphin(gameJFrame, 10, 20);
        }

        //init currentDolphins
        currentDolphins = gameDolphins();
        resetGame();

        // start the timer
        gameTimer.schedule(this, 0, TIME_TO_MOVE_DOLPHINS_IN_MILLISECONDS);

        // register this class as a mouse event listener for the JFrame
        gameJFrame.addMouseListener(this);
    }

    // run() overrides the same method in java.util.TimerTask
    // this executes everytime the timer expires
    public void run() {
        if (gameIsRunning) {
            for(Dolphin d: erasedDolphins){
                currentDolphins.remove(d);
            }

            for(Dolphin d: currentDolphins){
                d.move();
            }

        }
    }

    public void mousePressed(MouseEvent event) {
        // make sure game is in progress

        if (!gameIsRunning)
            return;

        if(System.currentTimeMillis() - lastPressed >= 6000){
            timesMissed++;
        }
        // have to create some sort of grab for a single dolphin
        for(Dolphin d: currentDolphins) {
            if (currentLevel < gameDolphins().size() &&
                    d.isDolphinPushed(event.getX() - xMouseOffsetToContentPaneFromJFrame,
                            event.getY() - yMouseOffsetToContentPaneFromJFrame)) {
                dolphinGotPushed(d);
                missFlag = true;
                break;
             }
         }

        // did they win?
        if(!missFlag){
            timesMissed++;
        }

        if (didIWin()) {
            gameIsRunning = false;
            JOptionPane.showMessageDialog(gameJFrame, "You saved the baby dolphin!", "Winner",
                     JOptionPane.PLAIN_MESSAGE, aliveDolphin);
            JOptionPane.showMessageDialog(gameJFrame, "Let's save another baby dolphin now!");
            resetGame();
         }

        if(timesMissed == 5) {
            gameIsRunning = false;
            JOptionPane.showMessageDialog(gameJFrame, "You missed and killed the baby dolphin! (What kind of person are you?)", "Loser",
                    JOptionPane.ERROR_MESSAGE, deadDolphin);
            for(Dolphin d: currentDolphins){
                d.erase();
            }
            resetGame();
        }

        lastPressed = System.currentTimeMillis();
        missFlag = false;

    }

    public void mouseEntered(MouseEvent event) {
    }

    public void mouseExited(MouseEvent event) {
    }

    public void mouseClicked(MouseEvent event) {

    }

    public void mouseReleased(MouseEvent event) {
    }

    //tried Arrays.asList(ex.) the method returns a final array so the Operation is unexpected
    private List<Dolphin> gameDolphins() {
        List<Dolphin> temp = new ArrayList<>();
        for(Dolphin d: DolphinsLevel[currentLevel]){
            temp.add(d);
        }
        return temp;
    }

    private boolean didIWin() {
        return currentLevel >= DolphinsLevel.length;
    }

    private void dolphinGotPushed(Dolphin d) {
            d.gotPushed();

            if (d.isGonnaLive()) {
                d.erase();
                erasedDolphins.add(d);

                // if not done, go to next level of dolphin
                if(currentDolphins.size() == 1){
                    currentLevel++;
                    erasedDolphins.clear();
                    for(Dolphin b : currentDolphins){
                        b.erase();
                    }
                    if(currentLevel == 4){
                        erasedDolphins.clear();
                    }else{
                        currentDolphins = gameDolphins();
                    }

                }

            }

    }

    private void resetGame() {
        gameIsRunning = false;
        currentLevel = 0;
        timesMissed = 0;
        for(int i = 0; i < 4; i++){
            DolphinsLevel[i][0] = new SlowDolphin(gameJFrame, 3, 10);
            DolphinsLevel[i][1] =new FastDolphin(gameJFrame, 4, 25);
            DolphinsLevel[i][2] = new VeryFastDolphin(gameJFrame, 6, 40);
            DolphinsLevel[i][3] = new VeryFastDolphin(gameJFrame, 10, 60);
        }
        currentDolphins = gameDolphins();
        for(Dolphin d: currentDolphins){
           d.putInGame();
        }
        gameIsRunning = true;
    }
}
