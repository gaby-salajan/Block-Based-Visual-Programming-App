package com.gabys.sscproject;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ImageView v1, v2;
    Block block1, block2;

    Block potentialParent = null;
    Block currentBlock = null;

    ConstraintLayout layout;

    Button pp;

    ArrayList<Block> blocks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        v1 = findViewById(R.id.imgV1);
        v2 = findViewById(R.id.imgV2);

        layout = findViewById(R.id.layout1);
        blocks = new ArrayList<>();


        block1 = new Block(v1);
        block2 = new Block(v2);

        blocks.add(block1);
        blocks.add(block2);

        attachDragListener(block1);
        attachDragListener(block2);
        attachViewDragListener(layout);


        pp = findViewById(R.id.buttonPP);

        //TODO continue proper highlight
        pp.setOnClickListener(view -> highlightBlock(block1));

    }

    private void attachDragListener(Block b){
        b.getView().setOnLongClickListener(view -> {
            ClipData dragData = ClipData.newPlainText("", "");
            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);

            view.startDragAndDrop(dragData, myShadow, view,0);
            return true;
        });
    }

    private Block checkForPotentialParent(ImageView view, float x, float y){
        float minX, minY, maxX, maxY;

        for (Block b : blocks){
            if(view.equals(b.getView()))
                continue;

            minX = b.getView().getX();
            minY = b.getView().getY();
            maxX = b.getView().getX() + b.getView().getWidth();
            maxY = b.getView().getY() + b.getView().getHeight();
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
            LayerDrawable def = (LayerDrawable) b.getView().getDrawable();
            GradientDrawable out = (GradientDrawable) def.getDrawable(1);
            out.setStroke(10, getColor(R.color.red));
        }
    }
    private void removeHighlight(Block b){
        if(b != null){
            LayerDrawable def = (LayerDrawable) b.getView().getDrawable();
            GradientDrawable out = (GradientDrawable) def.getDrawable(1);
            out.setStroke(10, getColor(R.color.block_blue));
        }
    }
    private Block getCurrentBlock(ImageView item){
        for(Block b : blocks){
            if (b.getView().equals(item))
                return blocks.get(blocks.indexOf(b));
        }
        return null;
    }

    private void snapToParent(Block child, Block parent){
        child.setParent(parent);
        parent.setChild(child);

        child.getView().setX(parent.getView().getX());
        child.getView().setY(parent.getView().getY()+40);

        removeHighlight(parent);
    }

    //TODO clean mess
    private void attachViewDragListener(ConstraintLayout layout){
        layout.setOnDragListener((view, event) -> {

            View draggableItem = (View) event.getLocalState();
            currentBlock = getCurrentBlock((ImageView) draggableItem);

            switch(event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;

                case DragEvent.ACTION_DRAG_ENTERED:
                    ConstraintLayout parentArea = (ConstraintLayout) draggableItem.getParent();
                    parentArea.removeView(draggableItem);

                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:
                    handleWhileDragging((ImageView) draggableItem, event, view);

                    return true;

                case DragEvent.ACTION_DRAG_EXITED:
                    draggableItem.setVisibility(View.VISIBLE);
                    view.invalidate();
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
                    view.invalidate();

                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    draggableItem.setVisibility(View.VISIBLE);
                    view.invalidate();

                    if (event.getResult()){
                        Toast.makeText(MainActivity.this, "The drop was handled.", Toast.LENGTH_LONG).show();
                        //TODO remove
//                        if(currentBlock.getParent() == null)
//                            System.out.println("cp: null");
//                        else
//                            System.out.println("cp: " + currentBlock.getParent());
//
//                        if(currentBlock.getChild() == null)
//                            System.out.println("cc: null");
//                        else
//                            System.out.println("cc: " + currentBlock.getChild());
//
//                        if(potentialParent != null)
//                            if(potentialParent.getChild() == null)
//                                System.out.println("pc: null");
//                            else
//                                System.out.println("pc: " + potentialParent.getChild());
                    }

                    else
                        Toast.makeText(MainActivity.this, "The drop didn't work.", Toast.LENGTH_LONG).show();
                    return true;

                default:
                    Log.e("DragDrop", "Unknown action type received by OnDragListener.");
                    return false;
            }
        });
    }

    private void handleWhileDragging(ImageView draggableItem, DragEvent event, View view) {
        potentialParent = checkForPotentialParent(draggableItem, event.getX(), event.getY());
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
}