package ma.dradeb.hiddenfounders.CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.TextureView;
import android.widget.TextView;

/**
 * Created by Youness on 09/07/2017.
 */

public class RobotoTextViewThin extends TextView {

    public static Typeface typeface;
    public RobotoTextViewThin(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RobotoTextViewThin(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotoTextViewThin(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Thin.ttf");
            setTypeface(typeface);
        }
    }
}
