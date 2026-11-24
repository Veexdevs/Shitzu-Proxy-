package com.shitzuproxy.app;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
public class UiHelper {
    public static final int RED = Color.parseColor("#FFFF0000");
    public static final int DARK = Color.parseColor("#FF0A0A0A");
    public static final int PANEL = Color.parseColor("#FF0E0E0E");
    public static final int SIDEBAR = Color.parseColor("#FF0A0A0A");
    public static final int CARD = Color.parseColor("#FF161616");
    public static final int GRAY = Color.parseColor("#FF9E9E9E");
    public static final int WHITE = Color.WHITE;
    public static final int GREEN = Color.parseColor("#FF4CAF50");
    public static final int DIVIDER = Color.parseColor("#FF222222");
    public static GradientDrawable rounded(int color, int radiusDp, Context context) {
        float radius = radiusDp * context.getResources().getDisplayMetrics().density;
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(radius);
        return drawable;
    }
    public static GradientDrawable roundedStroke(int color, int strokeColor, int radiusDp, Context context) {
        GradientDrawable drawable = rounded(color, radiusDp, context);
        float stroke = 1.2f * context.getResources().getDisplayMetrics().density;
        drawable.setStroke((int) stroke, strokeColor);
        return drawable;
    }
    public static TextView text(Context context, String value, int sizeSp, int color, boolean bold) {
        TextView textView = new TextView(context);
        textView.setText(value);
        textView.setTextSize(sizeSp);
        textView.setTextColor(color);
        if (bold) {
            textView.setTypeface(null, Typeface.BOLD);
        }
        return textView;
    }
    public static int dp(Context context, int value) {
        return (int) (value * context.getResources().getDisplayMetrics().density + 0.5f);
    }
    public static View sectionHeader(Context context, String title) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        layout.setPadding(0, dp(context, 8), 0, dp(context, 4));
        View dot = new View(context);
        dot.setLayoutParams(new LinearLayout.LayoutParams(dp(context, 7), dp(context, 7)));
        dot.setBackground(rounded(RED, 4, context));
        layout.addView(dot);
        TextView label = text(context, title, 10, RED, true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(dp(context, 6), 0, 0, 0);
        label.setLayoutParams(params);
        layout.addView(label);
        return layout;
    }
    public static View compactToggle(Context context, String title, boolean checked) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        layout.setPadding(dp(context, 2), dp(context, 4), dp(context, 2), dp(context, 4));
        TextView label = text(context, title, 10, WHITE, false);
        label.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        layout.addView(label);
        final boolean[] state = new boolean[]{checked};
        LinearLayout track = new LinearLayout(context);
        track.setOrientation(LinearLayout.HORIZONTAL);
        track.setGravity(state[0] ? Gravity.END : Gravity.START);
        track.setLayoutParams(new LinearLayout.LayoutParams(dp(context, 30), dp(context, 16)));
        track.setBackground(rounded(state[0] ? RED : Color.parseColor("#FF444444"), 8, context));
        View thumb = new View(context);
        LinearLayout.LayoutParams thumbParams = new LinearLayout.LayoutParams(dp(context, 12), dp(context, 12));
        thumbParams.setMargins(dp(context, 2), dp(context, 2), dp(context, 2), dp(context, 2));
        thumb.setLayoutParams(thumbParams);
        thumb.setBackground(rounded(WHITE, 6, context));
        track.addView(thumb);
        track.setOnClickListener(v -> {
            state[0] = !state[0];
            track.setBackground(rounded(state[0] ? RED : Color.parseColor("#FF444444"), 8, context));
            track.setGravity(state[0] ? Gravity.END : Gravity.START);
        });
        layout.addView(track);
        return layout;
    }
    public static View compactSlider(Context context, String title, int max, int progress) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dp(context, 2), dp(context, 4), dp(context, 2), dp(context, 4));
        TextView label = text(context, title, 10, WHITE, false);
        layout.addView(label);
        SeekBar seekBar = new SeekBar(context);
        seekBar.setMax(max);
        seekBar.setProgress(progress);
        seekBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(context, 18)));
        seekBar.setProgressTintList(ColorStateList.valueOf(RED));
        seekBar.setThumbTintList(ColorStateList.valueOf(WHITE));
        layout.addView(seekBar);
        return layout;
    }
    public static View compactDropdown(Context context, String title, String[] options, int startIndex) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        layout.setPadding(dp(context, 2), dp(context, 4), dp(context, 2), dp(context, 4));
        TextView label = text(context, title, 10, WHITE, false);
        label.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        layout.addView(label);
        TextView value = text(context, options[startIndex], 9, GRAY, false);
        value.setGravity(Gravity.CENTER);
        value.setPadding(dp(context, 8), dp(context, 3), dp(context, 8), dp(context, 3));
        value.setBackground(rounded(Color.parseColor("#FF1E1E1E"), 4, context));
        final int[] index = new int[]{startIndex};
        value.setOnClickListener(v -> {
            index[0] = (index[0] + 1) % options.length;
            value.setText(options[index[0]]);
        });
        layout.addView(value);
        return layout;
    }
    public static View hotkeyRow(Context context, String label, String key, boolean red) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(dp(context, 2), dp(context, 3), dp(context, 2), dp(context, 3));
        TextView left = text(context, label, 9, red ? RED : GRAY, false);
        left.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        layout.addView(left);
        TextView right = text(context, key, 9, red ? RED : GRAY, false);
        layout.addView(right);
        return layout;
    }
    public static View divider(Context context) {
        View view = new View(context);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(context, 1)));
        view.setBackgroundColor(DIVIDER);
        return view;
    }
}
