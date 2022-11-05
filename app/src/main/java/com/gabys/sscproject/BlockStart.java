package com.gabys.sscproject;

import android.widget.TextView;

public class BlockStart extends Block{

    public BlockStart(TextView shape) {
        setParent(null);
        setShape(shape);
    }

    @Override
    public int getHighlightedShape() {
        return R.drawable.block_start_h;
    }

    @Override
    public int getNormalShape() {
        return R.drawable.block_start;
    }
}
