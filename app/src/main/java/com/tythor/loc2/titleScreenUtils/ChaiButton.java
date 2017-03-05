package com.tythor.loc2.titleScreenUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.tythor.loc2.TitleView;

import static com.tythor.loc2.GameActivity.screenWidth;

/**
 * Created by Tyler on 2/25/2017.
 */

public class ChaiButton extends ImageView {
    private float pixelsPerMeter = (float) screenWidth / 568;

    public static boolean blocksChangeColor = true;
    public static boolean showStatusBox = true;
    public static boolean playRandomMusicButton = false;

    public ChaiButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        setImageBitmap(TitleView.offChai);

        flipBitmap();

        // Flip optionBoolean on touch
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    switch(getResources().getResourceEntryName(getId())) {
                        case "blocksChangeColorButton":
                            blocksChangeColor = !blocksChangeColor;
                            break;
                        case "showStatusBoxButton":
                            showStatusBox = !showStatusBox;
                            break;
                        case "playRandomMusicButton":
                            playRandomMusicButton = !playRandomMusicButton;
                            break;
                    }

                    flipBitmap();
                }
                return true;
            }
        });
    }

    private void flipBitmap() {
        setImageBitmap(TitleView.offChai);
        switch(getResources().getResourceEntryName(getId())) {
            case "blocksChangeColorButton":
                if(blocksChangeColor)
                    setImageBitmap(TitleView.onChai);
                break;
            case "showStatusBoxButton":
                if(showStatusBox)
                    setImageBitmap(TitleView.onChai);
                break;
            case "playRandomMusicButton":
                if(playRandomMusicButton)
                    setImageBitmap(TitleView.onChai);
                break;
        }
    }
}
