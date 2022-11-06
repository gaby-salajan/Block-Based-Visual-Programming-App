package com.gabys.sscproject.model;

import android.widget.TextView;

import com.gabys.sscproject.model.Block;

public class TestBlock extends Block {

    public TestBlock(TextView shape) {
        setShape(shape);
    }

    @Override
    public int getHighlightedShape() {
        return 0;
    }

    @Override
    public int getNormalShape() {
        return 0;
    }
}
