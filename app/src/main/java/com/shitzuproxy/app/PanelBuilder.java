package com.shitzuproxy.app;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
public class PanelBuilder {
    private final Context context;
    private LinearLayout content;
    private TextView aimbotTab;
    private TextView espTab;
    private View headerView;
    public PanelBuilder(Context context) {
        this.context = context;
    }
    public View getHeader() {
        return headerView;
    }
    public View build(Runnable onClose) {
        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackground(UiHelper.roundedStroke(UiHelper.PANEL, UiHelper.RED, 16, context));
        LinearLayout.LayoutParams rootParams = new LinearLayout.LayoutParams(UiHelper.dp(context, 330), LinearLayout.LayoutParams.WRAP_CONTENT);
        root.setLayoutParams(rootParams);
        headerView = buildHeader(onClose);
        root.addView(headerView);
        root.addView(buildTabs());
        ScrollView scrollView = new ScrollView(context);
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, UiHelper.dp(context, 380)));
        content = new LinearLayout(context);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(UiHelper.dp(context, 12), UiHelper.dp(context, 8), UiHelper.dp(context, 12), UiHelper.dp(context, 12));
        scrollView.addView(content);
        root.addView(scrollView);
        root.addView(buildFooter());
        showAimbot();
        return root;
    }
    private View buildHeader(Runnable onClose) {
        LinearLayout header = new LinearLayout(context);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(UiHelper.dp(context, 12), UiHelper.dp(context, 10), UiHelper.dp(context, 12), UiHelper.dp(context, 10));
        ImageView logo = new ImageView(context);
        logo.setImageResource(R.drawable.logo_circle);
        logo.setScaleType(ImageView.ScaleType.CENTER_CROP);
        logo.setLayoutParams(new LinearLayout.LayoutParams(UiHelper.dp(context, 36), UiHelper.dp(context, 36)));
        header.addView(logo);
        LinearLayout titles = new LinearLayout(context);
        titles.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams titlesParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        titlesParams.setMargins(UiHelper.dp(context, 10), 0, 0, 0);
        titles.setLayoutParams(titlesParams);
        TextView name = UiHelper.text(context, "SHITZU PROXY", 15, UiHelper.WHITE, true);
        titles.addView(name);
        TextView status = UiHelper.text(context, "Connected", 11, Color.parseColor("#FF4CAF50"), true);
        titles.addView(status);
        header.addView(titles);
        TextView close = UiHelper.text(context, "X", 16, UiHelper.WHITE, true);
        close.setGravity(Gravity.CENTER);
        close.setPadding(UiHelper.dp(context, 10), UiHelper.dp(context, 6), UiHelper.dp(context, 10), UiHelper.dp(context, 6));
        close.setBackground(UiHelper.rounded(UiHelper.RED, 8, context));
        close.setOnClickListener(v -> onClose.run());
        header.addView(close);
        return header;
    }
    private View buildTabs() {
        LinearLayout tabs = new LinearLayout(context);
        tabs.setOrientation(LinearLayout.HORIZONTAL);
        tabs.setPadding(UiHelper.dp(context, 12), 0, UiHelper.dp(context, 12), UiHelper.dp(context, 8));
        aimbotTab = UiHelper.text(context, "AIMBOT", 13, UiHelper.WHITE, true);
        aimbotTab.setGravity(Gravity.CENTER);
        aimbotTab.setPadding(0, UiHelper.dp(context, 10), 0, UiHelper.dp(context, 10));
        aimbotTab.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        aimbotTab.setOnClickListener(v -> showAimbot());
        espTab = UiHelper.text(context, "ESP", 13, UiHelper.WHITE, true);
        espTab.setGravity(Gravity.CENTER);
        espTab.setPadding(0, UiHelper.dp(context, 10), 0, UiHelper.dp(context, 10));
        espTab.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        espTab.setOnClickListener(v -> showEsp());
        tabs.addView(aimbotTab);
        tabs.addView(espTab);
        return tabs;
    }
    private View buildFooter() {
        TextView footer = UiHelper.text(context, "Shitzu Proxy v1.0", 10, UiHelper.GRAY, false);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, UiHelper.dp(context, 6), 0, UiHelper.dp(context, 10));
        return footer;
    }
    private void updateTabs(boolean aimbotSelected) {
        aimbotTab.setBackground(UiHelper.rounded(aimbotSelected ? UiHelper.RED : UiHelper.CARD, 10, context));
        espTab.setBackground(UiHelper.rounded(aimbotSelected ? UiHelper.CARD : UiHelper.RED, 10, context));
    }
    private void showAimbot() {
        updateTabs(true);
        content.removeAllViews();
        content.addView(UiHelper.sectionTitle(context, "AIMBOT"));
        content.addView(UiHelper.toggleRow(context, "Aimbot", true));
        content.addView(UiHelper.toggleRow(context, "Aim Lock", true));
        content.addView(UiHelper.toggleRow(context, "Visible Check", false));
        content.addView(UiHelper.toggleRow(context, "Target Team", false));
        content.addView(UiHelper.toggleRow(context, "Auto Fire", false));
        content.addView(UiHelper.toggleRow(context, "Silent Aim", false));
        content.addView(UiHelper.toggleRow(context, "Show FOV Circle", true));
        content.addView(UiHelper.cycleRow(context, "Target Bone", new String[]{"Head", "Chest", "Neck", "Pelvis"}, 0));
        content.addView(UiHelper.sliderRow(context, "FOV", 360, 90, ""));
        content.addView(UiHelper.sliderRow(context, "Smooth", 100, 35, ""));
        content.addView(UiHelper.sliderRow(context, "Hit Chance", 100, 85, "%"));
        content.addView(UiHelper.sliderRow(context, "Distance", 500, 250, "m"));
        content.addView(UiHelper.sectionTitle(context, "WEAPON"));
        content.addView(UiHelper.toggleRow(context, "No Recoil", true));
        content.addView(UiHelper.toggleRow(context, "No Spread", false));
        content.addView(UiHelper.toggleRow(context, "No Sway", false));
        content.addView(UiHelper.toggleRow(context, "Rapid Fire", false));
        content.addView(UiHelper.toggleRow(context, "No Scope", false));
        content.addView(UiHelper.sliderRow(context, "Recoil Control", 100, 60, "%"));
    }
    private void showEsp() {
        updateTabs(false);
        content.removeAllViews();
        content.addView(UiHelper.sectionTitle(context, "ESP"));
        content.addView(UiHelper.toggleRow(context, "ESP Master", true));
        content.addView(UiHelper.toggleRow(context, "Box ESP", true));
        content.addView(UiHelper.toggleRow(context, "Skeleton", false));
        content.addView(UiHelper.toggleRow(context, "Health Bar", true));
        content.addView(UiHelper.toggleRow(context, "Name", true));
        content.addView(UiHelper.toggleRow(context, "Distance", true));
        content.addView(UiHelper.toggleRow(context, "Weapon", false));
        content.addView(UiHelper.toggleRow(context, "Line", false));
        content.addView(UiHelper.toggleRow(context, "Radar", false));
        content.addView(UiHelper.toggleRow(context, "Chams", false));
        content.addView(UiHelper.toggleRow(context, "Glow", false));
        content.addView(UiHelper.toggleRow(context, "Offscreen Arrows", false));
        content.addView(UiHelper.cycleRow(context, "Box Type", new String[]{"2D", "3D", "Corner"}, 0));
        content.addView(UiHelper.cycleRow(context, "ESP Color", new String[]{"Red", "Green", "Blue", "Yellow"}, 0));
        content.addView(UiHelper.sliderRow(context, "ESP Range", 500, 300, "m"));
        content.addView(UiHelper.sliderRow(context, "Box Thickness", 10, 3, ""));
        content.addView(UiHelper.sliderRow(context, "Radar Size", 200, 120, ""));
    }
}
