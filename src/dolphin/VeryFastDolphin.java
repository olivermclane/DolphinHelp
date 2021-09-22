package dolphin;

import javax.swing.*;

public class VeryFastDolphin extends Dolphin{
    public VeryFastDolphin(JFrame dolphinJFrame, int hitsRequired, int directionChangeProbability) {
        super(dolphinJFrame, hitsRequired, directionChangeProbability);

        dolphinImageNames.put(Direction.UP, "veryFastDolphinUp.jpg");
        dolphinImageNames.put(Direction.DOWN, "veryFastDolphinDown.jpg");
        dolphinImageNames.put(Direction.LEFT, "veryFastDolphinLeft.jpg");
        dolphinImageNames.put(Direction.RIGHT, "veryFastDolphinRight.jpg");
        for (Direction d : Direction.values()) {
            dolphinImages.put(d,  new ImageIcon(dolphinImageNames.get(d)));
        }

        // movement is arbitrarily based on size of image (1 / 5th of width)
        horizontalMovement = dolphinImages.get(Direction.RIGHT).getIconWidth() / 2;
        verticalMovement = dolphinImages.get(Direction.UP).getIconHeight() / 2;


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

        // if hit edge of window, then wrap around to the other side immediately
        if (dolphinDirection == Direction.UP && atTopEdge()) {
            eraseDolphin();
            yPosition = dolphinJFrame.getContentPane().getHeight() - dolphinImages.get(Direction.UP).getIconHeight();
            drawDolphin();
        } else if (dolphinDirection == Direction.DOWN && atBottomEdge()) {
            eraseDolphin();
            yPosition = 0;
            drawDolphin();
        } else if (dolphinDirection == Direction.LEFT && atLeftEdge()) {
            eraseDolphin();
            xPosition = dolphinJFrame.getContentPane().getWidth() - dolphinImages.get(Direction.LEFT).getIconWidth();
            drawDolphin();
        } else if (dolphinDirection == Direction.RIGHT && atRightEdge()) {
            eraseDolphin();
            xPosition = 0;
            drawDolphin();
        }
    }
}
