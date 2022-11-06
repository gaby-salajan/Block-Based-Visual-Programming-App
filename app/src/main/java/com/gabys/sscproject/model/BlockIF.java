package com.gabys.sscproject.model;

import android.widget.TextView;

import com.gabys.sscproject.R;

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
