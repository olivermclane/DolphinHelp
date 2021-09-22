package dolphin;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

class Dolphin {
    enum Direction {
        UP, DOWN, LEFT, RIGHT;
    }

    protected final JFrame dolphinJFrame;
    protected final JLabel dolphinJLabel;

    protected final int pushesRequired;
    protected final int directionChangeProbability;

    protected Map<Direction, String> dolphinImageNames = new HashMap<>(Direction.values().length);
    protected Map<Direction, ImageIcon> dolphinImages = new HashMap<>(Direction.values().length);

    protected int horizontalMovement;
    protected int verticalMovement;
    protected int pushesTaken;
    protected Direction dolphinDirection;

    // top left corner of Image
    protected int xPosition = 0;
    protected int yPosition = 0;

    public Dolphin(JFrame dolphinJFrame, int hitsRequired, int directionChangeProbability) {
        this.dolphinJFrame = dolphinJFrame;
        this.pushesRequired = hitsRequired;
        this.directionChangeProbability = directionChangeProbability;

        dolphinJLabel = new JLabel();
        dolphinJLabel.setBounds(10, 10, 10, 10); // arbitrary, will change later
        dolphinJFrame.getContentPane().add(dolphinJLabel);
        dolphinJLabel.setVisible(true);

        horizontalMovement = verticalMovement = 0;
        pushesTaken = 0;
        dolphinDirection = Direction.RIGHT;

        // arbitrary starting point
        xPosition = 20;
        yPosition = 20;
    }

    protected void drawDolphin() {
        final ImageIcon icon = dolphinImages.get(dolphinDirection);
        dolphinJLabel.setIcon(icon);
        dolphinJLabel.setBounds(xPosition, yPosition, icon.getIconWidth(), icon.getIconHeight());

        dolphinJLabel.setVisible(true);
    }

    protected void eraseDolphin() {
        dolphinJLabel.setVisible(false);

    }

    protected boolean dolphinVisable(){
       return dolphinJLabel.isVisible();
    }

    protected boolean atRightEdge() {
        return (xPosition + dolphinJLabel.getWidth() + horizontalMovement) > dolphinJFrame.getContentPane().getWidth();
    }

    protected boolean atLeftEdge() {
        // horizontalMovement variable is alway positive
        return (xPosition - horizontalMovement) < 0;
    }

    protected boolean atTopEdge() {
        // verticalMovement variable is always positive
        return (yPosition - verticalMovement) < 0;
    }

    protected boolean atBottomEdge() {
        return (yPosition + dolphinJLabel.getHeight() + verticalMovement) > dolphinJFrame.getContentPane().getHeight();
    }

    public boolean isDolphinPushed(int xMousePosition, int yMousePosition) {
        final ImageIcon icon = dolphinImages.get(dolphinDirection);
        return (xPosition <= xMousePosition && xMousePosition <= (xPosition + icon.getIconWidth())) &&
               (yPosition <= yMousePosition && yMousePosition <= (yPosition + icon.getIconHeight()));
    }

    public void gotPushed() {
        pushesTaken++;
    }

    public boolean isGonnaLive() {
        return pushesTaken >= pushesRequired;
    }

    public void putInGame() {
        pushesTaken = 0;
        drawDolphin();
    }

    public void erase() {
        eraseDolphin();
    }

    public void move() {

    }
}
