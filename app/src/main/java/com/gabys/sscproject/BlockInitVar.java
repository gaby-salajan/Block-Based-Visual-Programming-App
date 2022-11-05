package com.gabys.sscproject;

import android.widget.TextView;

public class BlockInitVar extends Block{
    public BlockInitVar(TextView shape) {
        setShape(shape);
    }

    @Override
    public int getHighlightedShape() {
        return R.drawable.block_init_variable_h;
    }

    @Override
    public int getNormalShape() {
        return R.drawable.block_init_variable;
    }
}
