package com.gabys.sscproject;

import android.widget.ImageView;
import android.widget.TextView;

public class Block {
    private ImageView view;
    private TextView text;

    private boolean highlighted = false;

    private Block parent;
    private Block child;

    public Block() {
    }

    public Block(ImageView view) {
        this.view = view;
        this.highlighted = false;
        this.parent = null;
        this.child = null;
    }

    public Block(ImageView view, Block parent, Block child) {
        this.view = view;
        this.parent = parent;
        this.child = child;
    }

    public ImageView getView() {
        return view;
    }

    public void setView(ImageView view) {
        this.view = view;
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

    public TextView getText() {
        return text;
    }

    public void setText(TextView text) {
        this.text = text;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }
}
