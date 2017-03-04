package com.tythor.loc2.titleScreenUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tythor.loc2.R;


/**
 * Created by Tyler on 2/22/2017.
 */

public class NamesRelativeLayout extends RelativeLayout {
    String[][] nameCategory = new String[3][3];

    boolean mitten = false;

    ImageView chai;
    ImageView chai2;
    ImageView chai3;

    int generatedID1;
    int generatedID2;

    public NamesRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        String[] mittens = {"claire", "le", "therese"};

        for(int i = 0; i < mittens.length; i++) {
            if(getResources().getResourceEntryName(getId()).equals(mittens[i])) {
                mitten = true;
            }
        }

        chai = createChai(context);
        chai2 = createChai(context, generatedID1);
        chai3 = createChai(context, generatedID2);

        String[] oneChai = {"dylan", "joel", "joey", "kyle", "le", "ryan", "therese", "tyler"};
        String[] twoChais = {"claire", "james", "johnathan", "michael", "tj"};
        String[] threeChais = {"david", "josh", "trevor"};

        nameCategory[0] = oneChai;
        nameCategory[1] = twoChais;
        nameCategory[2] = threeChais;

        for(int i = 0; i < nameCategory.length; i++) {
            for(int j = 0; j < nameCategory[i].length; j++) {
                if(getResources().getResourceEntryName(getId()).equals(nameCategory[i][j])) {
                    if(i == 0) {
                        addView(chai);
                    } else if(i == 1) {
                        addView(chai);
                        addView(chai2);
                    } else {
                        addView(chai);
                        addView(chai2);
                        addView(chai3);
                    }
                }
            }
        }
    }

    public ImageView createChai(Context context) {
        ImageView chai = new ImageView(context);

        if(mitten) {
            chai.setImageResource(R.drawable.mittensleft);
        } else {
            chai.setImageResource(R.drawable.playerleft1);
        }

        chai.setMaxHeight(convertDpToPx(15));
        chai.setMaxWidth(convertDpToPx(15));
        chai.setAdjustViewBounds(true);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                     ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(convertDpToPx(2.5f), 0, convertDpToPx(2.5f), 0);
        layoutParams.addRule(CENTER_HORIZONTAL);
        layoutParams.addRule(CENTER_VERTICAL);
        chai.setLayoutParams(layoutParams);

        chai.setX(chai.getX() - convertDpToPx(12.5f));

        generatedID1 = generateViewId();
        chai.setId(generatedID1);

        return chai;
    }

    public ImageView createChai(Context context, int subject) {
        ImageView chai = new ImageView(context);

        if(mitten) {
            chai.setImageResource(R.drawable.mittensleft);
        } else {
            chai.setImageResource(R.drawable.playerleft1);
        }

        chai.setMaxHeight(convertDpToPx(15));
        chai.setMaxWidth(convertDpToPx(15));
        chai.setAdjustViewBounds(true);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                     ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(convertDpToPx(2.5f), 0, convertDpToPx(2.5f), 0);
        layoutParams.addRule(CENTER_VERTICAL);
        layoutParams.addRule(LEFT_OF, subject);
        chai.setLayoutParams(layoutParams);

        chai.setX(chai.getX() - convertDpToPx(12.5f));

        generatedID2 = generateViewId();
        chai.setId(generatedID2);

        return chai;
    }

    public int convertDpToPx(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                               dp,
                                               getResources().getDisplayMetrics());
    }
}
