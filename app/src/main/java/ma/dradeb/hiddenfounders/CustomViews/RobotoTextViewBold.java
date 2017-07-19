package ma.dradeb.hiddenfounders.CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Youness on 09/07/2017.
 */

public class RobotoTextViewBold extends TextView {

    public static Typeface typeface;
    public RobotoTextViewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RobotoTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotoTextViewBold(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Bold.ttf");
            setTypeface(typeface);
        }
    }
}
