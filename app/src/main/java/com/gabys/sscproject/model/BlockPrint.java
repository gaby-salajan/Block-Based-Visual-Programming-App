package com.gabys.sscproject.model;

import android.widget.TextView;

import com.gabys.sscproject.R;

public class BlockPrint extends Block {
    public BlockPrint(TextView shape) {
        super(shape);
    }

    @Override
    public int getHighlightedShape() {
        return R.drawable.block_while_h;
    }

    @Override
    public int getNormalShape() {
        return R.drawable.block_while;
    }



    @Override
    public BlockType getType() {
        return BlockType.PRINT;
    }

    @Override
    public String getCmd() {
        return "print";
    }
}
