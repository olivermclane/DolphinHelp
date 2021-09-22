package dolphin;

import javax.swing.JFrame;
import javax.swing.ImageIcon;

class SlowDolphin extends Dolphin {
    public SlowDolphin(JFrame myJFrame, int hitsNeeded, int directionChangeProbability) {
        super(myJFrame, hitsNeeded, directionChangeProbability);
        // set up images for slow dolphin
        dolphinImageNames.put(Direction.UP, "slowDolphinUp.jpg");
        dolphinImageNames.put(Direction.DOWN, "slowDolphinDown.jpg");
        dolphinImageNames.put(Direction.LEFT, "slowDolphinLeft.jpg");
        dolphinImageNames.put(Direction.RIGHT, "slowDolphinRight.jpg");
        for (Direction d : Direction.values()) {
            dolphinImages.put(d,  new ImageIcon(dolphinImageNames.get(d)));
        }

        // movement is arbitrarily based on size of image (1 / 10th of width)
        horizontalMovement = dolphinImages.get(Direction.RIGHT).getIconWidth() / 10;
        verticalMovement = dolphinImages.get(Direction.UP).getIconHeight() / 10;
    }

    public void move() {
        // change direction? (Math.random gives 0 to .9999)
        final int randomNum = (int)(Math.random() * 100.0);
        if (randomNum < directionChangeProbability) {
            int directionIndex = randomNum % Direction.values().length;
            dolphinDirection = Direction.values()[directionIndex];
        }

        switch (dolphinDirection) {
        case LEFT:
            xPosition -= horizontalMovement;
            break;

        case RIGHT:
            xPosition += horizontalMovement;
            break;

        case UP:
            yPosition -= verticalMovement;
            break;

        case DOWN:
            yPosition += verticalMovement;
            break;
        }
        drawDolphin();

        // hit edge of window and need to turn around?
        if (dolphinDirection == Direction.UP && atTopEdge()) {
            dolphinDirection = Direction.DOWN;
        } else if (dolphinDirection == Direction.DOWN && atBottomEdge()) {
            dolphinDirection = Direction.UP;
        } else if (dolphinDirection == Direction.LEFT && atLeftEdge()) {
            dolphinDirection = Direction.RIGHT;
        } else if (dolphinDirection == Direction.RIGHT && atRightEdge()) {
            dolphinDirection = Direction.LEFT;
        }
    }
}
