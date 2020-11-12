package me.fabric.eyephonemod.gui.client.element;

public class BottomRightAnchoredPanel extends CenteredPanel {


    private int rightPadding;
    private int bottomPadding;
    private int parentWidth;
    private int parentHeight;

    public BottomRightAnchoredPanel(int width, int height, int rightPadding, int bottomPadding) {
        super(width, height);
        this.rightPadding = rightPadding;
        this.bottomPadding = bottomPadding;
    }

    @Override
    public void init(int parentWidth, int parentHeight) {
        this.parentWidth = parentWidth;
        this.parentHeight = parentHeight;
        configureChildPositions();
    }

    private void configureChildPositions() {
        drawX = parentWidth - width - rightPadding;
        drawY = parentHeight - height - bottomPadding;
        children.forEach(c -> {
            c.setParentX(drawX);
            c.setParentY(drawY);
        });
    }

    public void setRightPadding(int rightPadding) {
        this.rightPadding = rightPadding;
        configureChildPositions();
    }

    public void setBottomPadding(int bottomPadding) {
        this.bottomPadding = bottomPadding;
        configureChildPositions();
    }
}
