package com.gabys.sscproject.logic;

import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.gabys.sscproject.R;
import com.gabys.sscproject.model.Block;
import com.gabys.sscproject.model.BlockStart;

import java.util.ArrayList;

public class BlockService {
    DragHandler dragHandler;
    Compiler compiler;

    private ArrayList<Block> blocks;
    private TextView bin;
    private View rootView;
    private ConstraintLayout layout;


    public BlockService(View rootView){
        this.rootView = rootView;
        this.bin = rootView.findViewById(R.id.bin);
        layout = rootView.findViewById(R.id.layout1);
        blocks = new ArrayList<>();
        dragHandler = new DragHandler(rootView, blocks, bin);
        compiler = new Compiler(rootView.getContext());
    }

    public void addBlock(Block block) {
        blocks.add(block);
        layout.addView(block.getShape());
        dragHandler.addBlock(block);
    }

    public boolean hasStartBlock() {
        for(Block b : blocks){
            if(b.getClass().equals(BlockStart.class))
                return true;
        }
        return false;
    }
    public Block getStartBlock(){
        for(Block b : blocks){
            if (b.getClass().equals(BlockStart.class))
                return blocks.get(blocks.indexOf(b));
        }
        return null;
    }

    public void compileCode(){
        compiler.compileCode(getStartBlock());
    }

}
