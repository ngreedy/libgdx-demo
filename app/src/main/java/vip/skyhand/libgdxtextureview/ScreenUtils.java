package vip.skyhand.libgdxtextureview;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * Created by Mystery on 16/9/23.
 */

public class ScreenUtils {

    private static int screenWidth;
    private static int screenHeight;
    private static int stateHeight = 0;
    private static double screenSize;
    private static float screenDensity;

    private static int mCurrentScreenWidth;
    private static int mCurrentScreenHeight;
    private static int mScreenRealWidth;
    private static int mScreenRealHeight;
    private static WindowManager mScreenWindowManager = null;
    private static DisplayMetrics displayMetrics = null;

    public static float TOAST_YOFFSET = 95f;

    public static void initialization(Context context) {
        mScreenWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        Point point = new Point();
        mScreenWindowManager.getDefaultDisplay().getMetrics(dm);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mScreenWindowManager.getDefaultDisplay().getRealSize(point);
            mScreenRealWidth = point.x;
            mScreenRealHeight = point.y;
        }
        screenSize = Math.sqrt(dm.widthPixels * dm.widthPixels + dm.heightPixels * dm.heightPixels) / (dm.density * 160);
        screenDensity = dm.density;
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        if (screenWidth > screenHeight) {
            int temp = screenWidth;
            screenWidth = screenHeight;
            screenHeight = temp;
        }

        displayMetrics = context.getResources().getDisplayMetrics();
        Log.e("Screen", screenWidth + "," + screenHeight);

        getScreenWidthDip();
        getPhysicsWidth();
    }

    public static int getCurrentScreenWidth() {
        if (mScreenWindowManager != null) {
            DisplayMetrics dm = new DisplayMetrics();
            mScreenWindowManager.getDefaultDisplay().getMetrics(dm);
            return dm.widthPixels;
        } else {
            return screenWidth;
        }
    }

    public static int getCurrentScreenHeight() {
        if (mScreenWindowManager != null) {
            DisplayMetrics dm = new DisplayMetrics();
            mScreenWindowManager.getDefaultDisplay().getMetrics(dm);
            return dm.heightPixels;
        } else {
            return screenHeight;
        }
    }

    public static int getStateHeight(Activity activity) {
        if (stateHeight != 0) {
            Rect outRect = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
            stateHeight = outRect.top;
        }
        return stateHeight;
    }

    /*
     * 获取状态栏的高度
     * */
    public static int getStatusBarHeight(Context context) {
        try {
            Resources resources = context.getApplicationContext().getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            int height = resources.getDimensionPixelSize(resourceId);
            return height;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ScreenUtils.dip2px(20);
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static int getRealScreenHeight() {
        if (mScreenRealHeight > 0) {
            return mScreenRealHeight;
        }
        return getScreenWidth();
    }

    public static int getRealScreenWidth() {
        if (mScreenRealWidth > 0) {
            return mScreenRealWidth;
        }
        return getScreenHeight();
    }

    public static int getStateHeight() {
        return stateHeight;
    }

    public static double getScreenSize() {
        return screenSize;
    }

    public static float getScreenDensity() {
        return screenDensity;
    }

    public static int dip2px(float dip) {
        return (int) (dip * screenDensity + 0.5f);
    }

    public static int sp2px(float sp) {
        return (int) (sp * screenDensity + 0.5f);
    }

    public static int dpToPx(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
    }

    public static int pxToDp(float px) {
        return (int) (px / screenDensity);
    }

    public static int getScreenWidthDip() {
        int width = pxToDp(getScreenWidth());
        Log.i("getScreenWidthDip", "width: " + width);
        return width;
    }

    public static float getPhysicsWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        mScreenWindowManager.getDefaultDisplay().getMetrics(dm);
        float width = dm.widthPixels / dm.xdpi;
        Log.i("getPhysicsWidth", "width: " + width);
        return width;
    }

    //取屏幕左下角位置
    public static PointF getScreenLeftBottom() {
        return new PointF(0, screenHeight);
    }


    //取屏幕右下角位置
    public static PointF getScreenRightBottom() {
        return new PointF(screenWidth, screenHeight);
    }

    //取屏幕中下位置带下边距
    public static PointF getScreenMidBottomWithMargin(float bottomMargin) {
        int[] location = {ScreenUtils.getScreenWidth() / 2, ScreenUtils.getScreenHeight() - dip2px(bottomMargin)};
        PointF pointF = new PointF(location[0], location[1]);
        return pointF;
    }
}
