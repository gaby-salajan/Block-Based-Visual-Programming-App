package com.gabys.sscproject;

import android.widget.TextView;

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
}
