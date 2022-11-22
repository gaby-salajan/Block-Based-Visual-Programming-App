package com.gabys.sscproject.model;


import android.widget.TextView;

import com.gabys.sscproject.R;

public class BlockValue extends Block{

    private VarType varType;

    public BlockValue(TextView shape) {
        super(shape);
    }
    public BlockValue(TextView shape, VarType varType) {
        super(shape);
        this.varType = varType;
    }

    @Override
    public int getHighlightedShape() {
        return R.drawable.block_value_h;
    }

    @Override
    public int getNormalShape() {
        return R.drawable.block_value;
    }

    @Override
    public BlockType getType() {
        return BlockType.VALUE;
    }

    @Override
    public String getCmd() {
        return getText();
    }

    public VarType getVarType() {
        return varType;
    }

    public void setVarType(VarType varType) {
        this.varType = varType;
    }
}
