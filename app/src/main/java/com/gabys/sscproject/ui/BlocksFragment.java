package com.gabys.sscproject.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.gabys.sscproject.R;
import com.gabys.sscproject.model.*;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class BlocksFragment extends Fragment {
    private View rootView;
    ConstraintLayout layout;

    private FragmentManager fragmentManager;
    private CodeFragment codeFragment;
    private ArrayList<Block> blocksList;

    String varValue;
    VarType varType;

    private void init(){
        blocksList.add(new BlockStart(rootView.findViewById(R.id.b_start)));
        blocksList.add(new BlockWhile(rootView.findViewById(R.id.b_while)));
        blocksList.add(new BlockIF(rootView.findViewById(R.id.b_if)));
        blocksList.add(new BlockVar(rootView.findViewById(R.id.b_var)));
        blocksList.add(new BlockValue(rootView.findViewById(R.id.b_value), varType));
        blocksList.add(new BlockPrint(rootView.findViewById(R.id.b_print)));

        varValue = "";
    }

    public void addFragmentManager(FragmentManager fm){
        this.fragmentManager = fm;
    }
    public void addCodeFragment(CodeFragment cf) {
        this.codeFragment = cf;
    }

    private TextView copyBlock(Block oldBlock){
        TextView newShape = new TextView(codeFragment.getContext());

        newShape.setText(oldBlock.getShape().getText());
        newShape.setGravity(oldBlock.getShape().getGravity());
        newShape.setTextSize(20f);
        newShape.setTypeface(oldBlock.getShape().getTypeface());
        newShape.setBackground(oldBlock.getShape().getBackground());
        newShape.setLayoutParams(oldBlock.getShape().getLayoutParams());
        newShape.setWidth(oldBlock.getShape().getWidth());
        newShape.setHeight(oldBlock.getShape().getHeight());
        return newShape;
    }
    private boolean checkIfStartBlock(Block b){
        return b.getClass().equals(BlockStart.class);
    }

    private void addBlockToCodeScreen(Block block) throws Exception {

        Block newBlock = null;
        TextView tv = null;

        //TODO complete when adding new block
        switch(block.getType()){
            case START:
                tv = copyBlock(block);
                newBlock = new BlockStart(tv);
                break;
            case IF:
                tv = copyBlock(block);
                newBlock = new BlockIF(tv);
                break;
            case WHILE:
                tv = copyBlock(block);
                newBlock = new BlockWhile(tv);
                break;
            case VAR:
                tv = copyBlock(block);
                newBlock = new BlockVar(tv);
                break;
            case VALUE:
                tv = copyBlock(block);
                if(varValue.length() > 9){
                    tv.setTextSize(10f);
                }
                if(varValue.length() <= 9 && varValue.length() > 5){
                    tv.setTextSize(15f);
                }
                tv.setText(varValue);
                newBlock = new BlockValue(tv, varType);
                break;
            case PRINT:
                tv = copyBlock(block);
                newBlock = new BlockPrint(tv);
                break;
            case TEXT:
                break;
            default:
        }
        if(newBlock == null)
            throw new Exception("New Block not instantiated");
        else{
            if(newBlock.getShape().getParent() != null)
                ((ViewGroup)newBlock.getShape().getParent()).removeView(newBlock.getShape()); // <- fix
            codeFragment.addBlock(newBlock);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parentViewGroup, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.blocks_fragment, parentViewGroup, false);
        layout = requireActivity().findViewById(R.id.layout1);

        blocksList = new ArrayList<>();
        init();

        for (Block block : blocksList) {
            block.getShape().setOnClickListener(view -> {
                if(checkIfStartBlock(block)){
                    if(codeFragment.hasStartBlock())
                        Toast.makeText(rootView.getContext(), "Can only have 1 start block", Toast.LENGTH_SHORT).show();
                    else {
                        fragmentManager.beginTransaction().remove(BlocksFragment.this).commit();
                        fragmentManager.beginTransaction().show(codeFragment).commit();
                        try {
                            addBlockToCodeScreen(block);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                else if (block.getType() == BlockType.VALUE){
                    showCustomDialog(block);
                }
                else{
                    fragmentManager.beginTransaction().remove(BlocksFragment.this).commit();
                    fragmentManager.beginTransaction().show(codeFragment).commit();
                    try {
                        addBlockToCodeScreen(block);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return rootView;
    }
    //TODO custom dialog
    void showCustomDialog(Block selectedBlock) {
        final Dialog dialog = new Dialog(rootView.getContext());
        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.custom_dialog);

        //Initializing the views of the dialog.
        final EditText field1 = dialog.findViewById(R.id.field1);

        RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
        RadioButton radioB1 = dialog.findViewById(R.id.isNumberButton);
        RadioButton radioB2 = dialog.findViewById(R.id.isTextButton);
        RadioButton radioB3 = dialog.findViewById(R.id.isObjectButton);
        RadioButton radioB4 = dialog.findViewById(R.id.isConditionButton);

        Button submitButton = dialog.findViewById(R.id.submit_button);


        submitButton.setOnClickListener(v -> {

            varValue = field1.getText().toString();
            int radioButtonId = radioGroup.getCheckedRadioButtonId();

            if( radioButtonId == -1){
                Snackbar.make(rootView, "Select a type", Snackbar.LENGTH_SHORT).show();
            }
            else{
                if (radioButtonId == radioB1.getId()) {
                    varType = VarType.NUMBER;
                }
                if (radioButtonId == radioB2.getId()){
                    varType = VarType.TEXT;
                }
                if(radioButtonId == radioB3.getId()){
                    varType = VarType.OBJECT;
                }
                if(radioButtonId == radioB4.getId()){
                    varType = VarType.CONDITION;
                }
            }

            fragmentManager.beginTransaction().remove(BlocksFragment.this).commit();
            fragmentManager.beginTransaction().show(codeFragment).commit();
            try {
                addBlockToCodeScreen(selectedBlock);
            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.dismiss();
        });

        dialog.show();
    }
}
