package com.gabys.sscproject.ui;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gabys.sscproject.R;
import com.gabys.sscproject.logic.BlockService;
import com.gabys.sscproject.logic.Compiler;
import com.gabys.sscproject.logic.DragHandler;
import com.gabys.sscproject.model.Block;
import com.gabys.sscproject.model.BlockIF;
import com.gabys.sscproject.model.BlockValue;
import com.gabys.sscproject.model.BlockVar;
import com.gabys.sscproject.model.BlockStart;
import com.gabys.sscproject.model.BlockWhile;

import java.util.ArrayList;


public class CodeFragment extends Fragment {
    View rootView;
    Button pp;
    BlockService blockService;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parentViewGroup, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (rootView == null){
            rootView = inflater.inflate(R.layout.code_fragment, parentViewGroup, false);
        }
        blockService = new BlockService(rootView);


        pp = rootView.findViewById(R.id.buttonPP);
        pp.setOnClickListener(view -> {
            blockService.compileCode();
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void addBlock(Block block) {
        blockService.addBlock(block);

    }

    public boolean hasStartBlock() {
        return blockService.hasStartBlock();
    }
}