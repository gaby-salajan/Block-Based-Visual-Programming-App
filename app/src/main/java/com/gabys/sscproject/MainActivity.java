package com.gabys.sscproject;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    TextView v1, v2;
    Block block1, block2;

    Block potentialParent = null;
    Block currentBlock = null;

    ConstraintLayout layout;

    Button pp;

    ArrayList<Block> blocks;

    Block testB,b3;
    TextView test,v3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        v1 = findViewById(R.id.tv_while);
        v2 = findViewById(R.id.tv_start);

        layout = findViewById(R.id.layout1);
        blocks = new ArrayList<>();

        block1 = new BlockWhile(v1);
        block2 = new BlockStart(v2);


        blocks.add(block1);
        blocks.add(block2);



        attachDragListener(block1);
        attachDragListener(block2);
        attachViewDragListener(layout);

        test = findViewById(R.id.tv_if);
        testB = new BlockIF(test);
        blocks.add(testB);
        attachDragListener(testB);

        v3 = findViewById(R.id.tv3);
        b3 = new BlockInitVar(v3);
        blocks.add(b3);
        attachDragListener(b3);


        pp = findViewById(R.id.buttonPP);

        //TODO continue proper highlight
        pp.setOnClickListener(view -> highlightBlock(b3));

    }

    private void attachDragListener(Block b) {
        b.getShape().setOnLongClickListener(view -> {
            ClipData dragData = ClipData.newPlainText("", "");
            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);

            view.startDragAndDrop(dragData, myShadow, view,0);
            return true;
        });
    }

    private Block checkForPotentialParent(TextView shape, float x, float y){
        float minX, minY, maxX, maxY;

        for (Block b : blocks){
            if(shape.equals(b.getShape()))
                continue;

            minX = b.getShape().getX();
            minY = b.getShape().getY();
            maxX = b.getShape().getX() + b.getShape().getWidth();
            maxY = b.getShape().getY() + b.getShape().getHeight();
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
            shape.setBackground(getDrawable(b.getHighlightedShape()));
        }
    }
    private void removeHighlight(Block b){
        if(b != null){
            TextView shape = b.getShape();
            shape.setBackground(getDrawable(b.getNormalShape()));
        }
    }

    private Block getCurrentBlock(TextView item){
        for(Block b : blocks){
            if (b.getShape().equals(item))
                return blocks.get(blocks.indexOf(b));
        }
        return null;
    }

    private void snapToParent(Block child, Block parent){
        child.setParent(parent);
        parent.setChild(child);

        child.getShape().setX(parent.getShape().getX());
        child.getShape().setY(parent.getShape().getY()+(parent.getShape().getHeight() - 30f));

        removeHighlight(parent);
    }

    //TODO clean mess
    //TODO case for different blocks
    private void attachViewDragListener(ConstraintLayout layout){
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
    private boolean handleChildDrag(View draggableItem, DragEvent event, View view) {
        switch(event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                return true;

            case DragEvent.ACTION_DRAG_ENTERED:
                ConstraintLayout parentArea = (ConstraintLayout) draggableItem.getParent();
                parentArea.removeView(draggableItem);

                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                handleWhileDragging((TextView) draggableItem, event, view);

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

                if (event.getResult())
                    Toast.makeText(MainActivity.this, "The drop was handled.", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(MainActivity.this, "The drop didn't work.", Toast.LENGTH_LONG).show();
                return true;

            default:
                Log.e("DragDrop", "Unknown action type received by OnDragListener.");
                return false;
        }
    }

    private boolean handleDrag(View draggableItem, DragEvent event, View view) {
        switch(event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                return true;

            case DragEvent.ACTION_DRAG_ENTERED:
                ConstraintLayout parentArea = (ConstraintLayout) draggableItem.getParent();
                parentArea.removeView(draggableItem);

                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
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

                view.invalidate();

                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                draggableItem.setVisibility(View.VISIBLE);
                view.invalidate();

                if (event.getResult())
                    Toast.makeText(MainActivity.this, "The drop was handled.", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(MainActivity.this, "The drop didn't work.", Toast.LENGTH_LONG).show();
                return true;

            default:
                Log.e("DragDrop", "Unknown action type received by OnDragListener.");
                return false;
        }
    }

    private void handleWhileDragging(TextView draggableItem, DragEvent event, View view) {
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