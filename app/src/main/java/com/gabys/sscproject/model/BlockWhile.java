package com.gabys.sscproject.model;

import android.widget.TextView;

import com.gabys.sscproject.R;

public class BlockWhile extends Block{

    public BlockWhile(TextView shape) {
        setShape(shape);
    }

    @Override
    public int getHighlightedShape(){
        return R.drawable.block_while_h;
    }
    @Override
    public int getNormalShape(){
        return R.drawable.block_while;
    }

    @Override
    public BlockType getType() {
        return BlockType.WHILE;
    }

    @Override
    public String getCmd() {
        return "while";
    }
}
