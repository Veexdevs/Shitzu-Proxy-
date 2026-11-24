package com.shitzuproxy.app;
import android.content.Context;
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
    public static final int PANEL = Color.parseColor("#FF111111");
    public static final int CARD = Color.parseColor("#FF1A1A1A");
    public static final int GRAY = Color.parseColor("#FF9E9E9E");
    public static final int WHITE = Color.WHITE;
    public static GradientDrawable rounded(int color, int radiusDp, Context context) {
        float radius = radiusDp * context.getResources().getDisplayMetrics().density;
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(radius);
        return drawable;
    }
    public static GradientDrawable roundedStroke(int color, int strokeColor, int radiusDp, Context context) {
        GradientDrawable drawable = rounded(color, radiusDp, context);
        float stroke = 1.5f * context.getResources().getDisplayMetrics().density;
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
    public static LinearLayout row(Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        layout.setPadding(dp(context, 12), dp(context, 10), dp(context, 12), dp(context, 10));
        layout.setBackground(rounded(CARD, 10, context));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, dp(context, 8));
        layout.setLayoutParams(params);
        return layout;
    }
    public static int dp(Context context, int value) {
        return (int) (value * context.getResources().getDisplayMetrics().density + 0.5f);
    }
    public static View toggleRow(Context context, String title, boolean checked) {
        LinearLayout layout = row(context);
        TextView label = text(context, title, 13, WHITE, false);
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        label.setLayoutParams(labelParams);
        layout.addView(label);
        View track = new View(context);
        LinearLayout.LayoutParams trackParams = new LinearLayout.LayoutParams(dp(context, 44), dp(context, 24), 0);
        track.setLayoutParams(trackParams);
        final boolean[] state = new boolean[]{checked};
        track.setBackground(rounded(state[0] ? RED : Color.parseColor("#FF333333"), 12, context));
        View thumb = new View(context);
        LinearLayout.LayoutParams thumbParams = new LinearLayout.LayoutParams(dp(context, 18), dp(context, 18), 0);
        thumbParams.setMargins(dp(context, 3), dp(context, 3), dp(context, 3), dp(context, 3));
        thumb.setLayoutParams(thumbParams);
        thumb.setBackground(rounded(WHITE, 9, context));
        LinearLayout trackLayout = new LinearLayout(context);
        trackLayout.setOrientation(LinearLayout.HORIZONTAL);
        trackLayout.setGravity(state[0] ? Gravity.END : Gravity.START);
        trackLayout.setLayoutParams(trackParams);
        trackLayout.setBackground(rounded(state[0] ? RED : Color.parseColor("#FF333333"), 12, context));
        trackLayout.addView(thumb);
        trackLayout.setOnClickListener(v -> {
            state[0] = !state[0];
            trackLayout.setBackground(rounded(state[0] ? RED : Color.parseColor("#FF333333"), 12, context));
            trackLayout.setGravity(state[0] ? Gravity.END : Gravity.START);
        });
        layout.addView(trackLayout);
        return layout;
    }
    public static View sliderRow(Context context, String title, int max, int progress, String suffix) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dp(context, 12), dp(context, 10), dp(context, 12), dp(context, 10));
        layout.setBackground(rounded(CARD, 10, context));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, dp(context, 8));
        layout.setLayoutParams(params);
        LinearLayout header = new LinearLayout(context);
        header.setOrientation(LinearLayout.HORIZONTAL);
        TextView label = text(context, title, 13, WHITE, false);
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        label.setLayoutParams(labelParams);
        header.addView(label);
        TextView value = text(context, progress + suffix, 12, RED, true);
        header.addView(value);
        layout.addView(header);
        SeekBar seekBar = new SeekBar(context);
        seekBar.setMax(max);
        seekBar.setProgress(progress);
        seekBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                value.setText(progress + suffix);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        layout.addView(seekBar);
        return layout;
    }
    public static View cycleRow(Context context, String title, String[] options, int startIndex) {
        LinearLayout layout = row(context);
        TextView label = text(context, title, 13, WHITE, false);
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        label.setLayoutParams(labelParams);
        layout.addView(label);
        TextView value = text(context, options[startIndex], 12, RED, true);
        value.setPadding(dp(context, 10), dp(context, 6), dp(context, 10), dp(context, 6));
        value.setBackground(rounded(Color.parseColor("#FF2A0000"), 8, context));
        final int[] index = new int[]{startIndex};
        value.setOnClickListener(v -> {
            index[0] = (index[0] + 1) % options.length;
            value.setText(options[index[0]]);
        });
        layout.addView(value);
        return layout;
    }
    public static TextView sectionTitle(Context context, String title) {
        TextView textView = text(context, title, 12, RED, true);
        textView.setPadding(0, dp(context, 6), 0, dp(context, 8));
        return textView;
    }
}
