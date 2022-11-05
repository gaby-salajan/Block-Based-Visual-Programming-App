package com.gabys.sscproject;

import android.widget.TextView;

public abstract class Block {
    private TextView shape;

    private Block parent;
    private Block child;

    private boolean highlighted = false;


    public abstract int getHighlightedShape();
    public abstract int getNormalShape();




    public TextView getShape() {
        return shape;
    }
    public void setShape(TextView shape) {
        this.shape = shape;
    }
    public Block getParent() {
        return parent;
    }
    public void setParent(Block parent) {
        this.parent = parent;
    }
    public Block getChild() {
        return child;
    }
    public void setChild(Block child) {
        this.child = child;
    }
    public boolean isHighlighted() {
        return highlighted;
    }
    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }
}
