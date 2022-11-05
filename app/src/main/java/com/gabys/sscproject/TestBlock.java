package com.gabys.sscproject;

import android.widget.TextView;

public class TestBlock extends Block{

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
