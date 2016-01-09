package com.capsule.apps.rxbookman.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.capsule.apps.rxbookman.R;
import com.capsule.apps.rxbookman.utils.LogUtils;

/**
 * @Author: larry
 * History: 12/15/15.
 */
public class OpenTextView extends TextView {

    private static final String TAG = LogUtils.makeLogTag(OpenTextView.class);

    protected boolean mFixWindowWordEnabled;

    public OpenTextView(Context context) {
        super(context);
        TypefaceCache.setCustomTypeface(context, this, null);
    }

    public OpenTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypefaceCache.setCustomTypeface(context, this, attrs);
        readCustomAttributes(context, attrs);
    }

    public OpenTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypefaceCache.setCustomTypeface(context, this, attrs);
        readCustomAttributes(context, attrs);
    }

    public void setFixWindowWordEnabled(boolean enabled) {
        this.mFixWindowWordEnabled = enabled;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!mFixWindowWordEnabled) {
            super.setText(text, type);
            return;
        }

        Spannable out;
        int lastSpace = String.valueOf(text).lastIndexOf(' ');
        if (lastSpace != -1 && lastSpace < text.length() - 1) {
            CharSequence tmpText = replaceCharacter(text, lastSpace, "\u00A0");
            out = new SpannableString(tmpText);

            if (text instanceof Spanned) {
                TextUtils.copySpansFrom((Spanned) text, 0, text.length(), null, out, 0);
            }
        } else {
            out = new SpannableString(text);
        }
        super.setText(out, type);
    }

    private void readCustomAttributes(Context context, AttributeSet attrs) {
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.OpenTextView, 0, 0);
        if (array != null) {
            mFixWindowWordEnabled = array.getBoolean(R.styleable.OpenTextView_fixWidowWords, false);

            if (mFixWindowWordEnabled) {
                setText(getText());
            }
        }
    }

    private CharSequence replaceCharacter(CharSequence source, int charIndex, CharSequence replacement) {
        if (charIndex != -1 && charIndex < source.length() - 1) {
            return TextUtils.concat(source.subSequence(0, charIndex), replacement, source.subSequence(charIndex + 1, source.length()));
        }
        return source;
    }
}
