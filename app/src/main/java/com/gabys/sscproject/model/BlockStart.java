package com.gabys.sscproject.model;

import android.widget.TextView;

import com.gabys.sscproject.R;

public class BlockStart extends Block{

    public BlockStart(TextView shape) {
        super(shape);
    }

    @Override
    public int getHighlightedShape() {
        return R.drawable.block_start_h;
    }

    @Override
    public int getNormalShape() {
        return R.drawable.block_start;
    }

    @Override
    public BlockType getType() {
        return BlockType.START;
    }

    @Override
    public String getCmd() {
        return "start";
    }
}
