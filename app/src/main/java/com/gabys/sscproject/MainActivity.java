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


public class MainActivity extends AppCompatActivity {

    Button b1;
    ConstraintLayout layout;

    Button pp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = findViewById(R.id.button1);
        layout = findViewById(R.id.layout1);

        attachShapeDragListener(b1);
        attachViewDragListener(layout);


        pp = findViewById(R.id.buttonPP);

        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b1.setY(b1.getY()+50);
            }
        });
    }

    private void attachShapeDragListener(TextView shape){
        shape.setOnLongClickListener(view -> {
            ClipData dragData = ClipData.newPlainText("", "");
            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);

            view.startDragAndDrop(dragData, myShadow, view,0);
            return true;
        });
    }

    private void attachViewDragListener(ConstraintLayout layout){
        layout.setOnDragListener((view, event) -> {

            View draggableItem = (View) event.getLocalState();

            switch(event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    layout.setAlpha(0.3f);
                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    layout.setAlpha(1.0f);
                    draggableItem.setVisibility(View.VISIBLE);
                    view.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    layout.setAlpha(1.0f);

                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        String draggedData = (String) event.getClipData().getItemAt(0).getText();
                        System.out.println("draggedData :" + draggedData);
                    }

                    draggableItem.setX(event.getX() - (draggableItem.getWidth() / 2.0f));
                    draggableItem.setY(event.getY() - (draggableItem.getHeight() / 2.0f));

                    ConstraintLayout parent = (ConstraintLayout) draggableItem.getParent();
                    parent.removeView(draggableItem);

                    ConstraintLayout dropArea = (ConstraintLayout) view;
                    dropArea.addView(draggableItem);

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
        });
    }
}