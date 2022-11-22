package com.gabys.sscproject.model;

import android.widget.TextView;

public abstract class Block {
    private TextView shape;

    private Block parent;
    private Block child;
    private Block rightChild;

    private boolean highlighted = false;

    Block(){
        this.shape = null;
        this.parent = null;
        this.child = null;
        this.rightChild = null;
        this.highlighted = false;
    }

    Block(TextView shape){
        this.shape = shape;
        this.parent = null;
        this.child = null;
        this.rightChild = null;
        this.highlighted = false;
    }

    public abstract int getHighlightedShape();
    public abstract int getNormalShape();
    public abstract BlockType getType();

    public abstract String getCmd();

    public String getText(){
        return (String) shape.getText();
    }

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
    public Block getRightChild() {
        return rightChild;
    }
    public void setRightChild(Block rightChild) {
        this.rightChild = rightChild;
    }
}
