package com.alfa.chesstest;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by IT11 on 3/3/2015.
 */
public class BoardView extends RelativeLayout {

    public BoardView(Context context, int positionX, int positionY) {
        super(context);
        init(positionX, positionY);
    }

    private void init(int posX, int posY) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(90,90);
        setLayoutParams(params);
        if (posY % 2 == 0) {
            if(posX %2 == 0){
                this.setBackgroundColor(Color.BLUE);
            }else{
                this.setBackgroundColor(Color.WHITE);
            }
        } else {
            if(posX %2 == 0){
                this.setBackgroundColor(Color.WHITE);
            }else{
                this.setBackgroundColor(Color.BLUE);
            }
        }
    }

}
