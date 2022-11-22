package com.gabys.sscproject.logic;

import android.content.ClipData;
import android.content.ClipDescription;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.gabys.sscproject.R;
import com.gabys.sscproject.model.Block;
import com.gabys.sscproject.model.BlockStart;
import com.gabys.sscproject.model.BlockType;

import java.util.ArrayList;

public class DragHandler {

    Block potentialParent = null;
    Block currentBlock = null;
    Block potentialTrash = null;

    ArrayList<Block> blocks;
    TextView bin;
    View rootView;
    ConstraintLayout layout;

    public DragHandler(View rootView, ArrayList<Block> blocks, TextView bin) {
        this.rootView = rootView;
        this.blocks = blocks;
        this.bin = bin;
        this.layout = rootView.findViewById(R.id.layout1);
        attachViewDragListener();
    }

    private Block checkForPotentialParent(TextView shape, float x, float y){
        float minX, minY, maxX, maxY;

        Block currentBlock = getCurrentBlock(shape);

        for (Block b : blocks){
            if(shape.equals(b.getShape()))
                continue;

            assert currentBlock != null;
            if(currentBlock.getType() == BlockType.VALUE){
                minX = b.getShape().getX() + 0.75f * b.getShape().getWidth();
                minY = b.getShape().getY();
                maxX = b.getShape().getX() + 1.5f * b.getShape().getWidth();
                maxY = b.getShape().getY() + b.getShape().getHeight();
            }
            else{
                minX = b.getShape().getX();
                minY = b.getShape().getY() + b.getShape().getHeight();
                maxX = b.getShape().getX() + b.getShape().getWidth();
                maxY = b.getShape().getY() + 1.5f * b.getShape().getHeight();
            }

            if (minX <= x && minY <= y && x <= maxX && y <= maxY){
                highlightBlock(b);
                return blocks.get(blocks.indexOf(b));
            }
            else{
                removeHighlight(b);
            }
        }
        return null;
    }

    private void highlightBlock(Block b){
        if(b != null) {
            TextView shape = b.getShape();
            shape.setBackground(ContextCompat.getDrawable(rootView.getContext(), b.getHighlightedShape()));
        }
    }
    private void removeHighlight(Block b){
        if(b != null){
            TextView shape = b.getShape();
            shape.setBackground(ContextCompat.getDrawable(rootView.getContext(), b.getNormalShape()));
        }
    }

    private void attachDragListener(Block b) {
        b.getShape().setOnLongClickListener(view -> {
            ClipData dragData = ClipData.newPlainText("", "");
            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);

            view.startDragAndDrop(dragData, myShadow, view,0);
            return true;
        });
    }
    private void attachViewDragListener(){
        layout.setOnDragListener((view, event) -> {

            View draggableItem = (View) event.getLocalState();
            currentBlock = getCurrentBlock((TextView) draggableItem);

            assert currentBlock != null;
            if (currentBlock.getClass().equals(BlockStart.class))
                return handleDrag(draggableItem, event, view);
            else
                return handleChildDrag(draggableItem, event, view);
        });
    }


    private Block getCurrentBlock(TextView item){
        for(Block b : blocks){
            if (b.getShape().equals(item))
                return blocks.get(blocks.indexOf(b));
        }
        return null;
    }

    private void snapToParent(Block child, Block parent){


        if(child.getType() == BlockType.VALUE){
            child.setParent(parent);
            parent.setRightChild(child);

            child.getShape().setX(parent.getShape().getX() + parent.getShape().getWidth() - 30f);
            child.getShape().setY(parent.getShape().getY() + 30f);
        }
        else{
            child.setParent(parent);
            parent.setChild(child);

            child.getShape().setX(parent.getShape().getX());
            child.getShape().setY(parent.getShape().getY()+(parent.getShape().getHeight() - 30f));
        }




        removeHighlight(parent);
    }


    private boolean handleChildDrag(View draggableItem, DragEvent event, View view) {
        switch(event.getAction()) {

            case DragEvent.ACTION_DRAG_LOCATION:
                handleWhileDragging((TextView) draggableItem, event, view);
                return true;


            case DragEvent.ACTION_DROP:
                view.setAlpha(1.0f);

                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    String draggedData = (String) event.getClipData().getItemAt(0).getText();
                    System.out.println("draggedData :" + draggedData);
                }

                draggableItem.setX(event.getX() - (draggableItem.getWidth() / 2.0f));
                draggableItem.setY(event.getY() - (draggableItem.getHeight() / 2.0f));

                ConstraintLayout dropArea = (ConstraintLayout) view;
                dropArea.addView(draggableItem);

                if(potentialParent != null){
                    potentialParent.setHighlighted(false);
                    snapToParent(currentBlock, potentialParent);
                }
                if(potentialTrash != null){
                    layout.removeView(potentialTrash.getShape());
                    potentialTrash = null;
                    removeHighlightBin();
                }
                view.invalidate();

                return true;




            case DragEvent.ACTION_DRAG_ENTERED:
                ConstraintLayout parentArea = (ConstraintLayout) draggableItem.getParent();
                parentArea.removeView(draggableItem);
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
            case DragEvent.ACTION_DRAG_ENDED:
                draggableItem.setVisibility(View.VISIBLE);
                view.invalidate();
                return true;
            case DragEvent.ACTION_DRAG_STARTED:
                return true;
            default:
                Log.e("DragDrop", "Unknown action type received by OnDragListener.");
                return false;
        }
    }

    private boolean handleDrag(View draggableItem, DragEvent event, View view) {
        switch(event.getAction()) {
            case DragEvent.ACTION_DRAG_ENTERED:
                ConstraintLayout parentArea = (ConstraintLayout) draggableItem.getParent();
                parentArea.removeView(draggableItem);
                return true;

            case DragEvent.ACTION_DROP:
                view.setAlpha(1.0f);

                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    String draggedData = (String) event.getClipData().getItemAt(0).getText();
                    System.out.println("draggedData :" + draggedData);
                }

                draggableItem.setX(event.getX() - (draggableItem.getWidth() / 2.0f));
                draggableItem.setY(event.getY() - (draggableItem.getHeight() / 2.0f));

                ConstraintLayout dropArea = (ConstraintLayout) view;
                dropArea.addView(draggableItem);

                view.invalidate();

                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                draggableItem.setVisibility(View.VISIBLE);
                view.invalidate();
                return true;
            default:
                return true;
        }
    }

    private void handleWhileDragging(TextView draggableItem, DragEvent event, View view) {
        potentialParent = checkForPotentialParent(draggableItem, event.getX(), event.getY());
        potentialTrash = checkForTrash(draggableItem, event.getX(), event.getY());

        if(potentialTrash != null){
            highlightBin();
        }
        else
            removeHighlightBin();

        if(potentialParent != null){
            if(!potentialParent.isHighlighted()){
                highlightBlock(potentialParent);
                potentialParent.setHighlighted(true);
            }
        }
        else{
            if(currentBlock.getParent() != null)
                currentBlock.getParent().setChild(null);
            currentBlock.setParent(null);


            removeHighlight(potentialParent);
            view.invalidate();
        }
    }

    private Block checkForTrash(TextView dragItem, float x, float y){
        float minX, minY, maxX, maxY;

        for (Block b : blocks){
            if(b.equals(getCurrentBlock(dragItem))){
                minX = bin.getX();
                minY = bin.getY();
                maxX = bin.getX() + bin.getWidth();
                maxY = bin.getY() + bin.getHeight();

                if (minX <= x && minY <= y && x <= maxX && y <= maxY){
                    highlightBin();
                    return blocks.get(blocks.indexOf(b));
                }
                else{
                    removeHighlightBin();
                }
            }
        }
        return null;
    }

    private void removeHighlightBin() {
        bin.setBackground(ContextCompat.getDrawable(rootView.getContext(), R.drawable.trash_bin));
    }
    private void highlightBin() {
        bin.setBackground(ContextCompat.getDrawable(rootView.getContext(), R.drawable.trash_bin_h));
    }

    public void addBlock(Block block) {
        attachDragListener(block);
    }
}
