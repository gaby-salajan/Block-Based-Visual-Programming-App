package com.gabys.sscproject;

import android.widget.TextView;

public class BlockIF extends Block{
    Block conditionBlock;

    public BlockIF(TextView shape) {
        setShape(shape);
    }

    @Override
    public int getHighlightedShape() {
        return R.drawable.block_if_h;
    }

    @Override
    public int getNormalShape() {
        return R.drawable.block_if;
    }
}
