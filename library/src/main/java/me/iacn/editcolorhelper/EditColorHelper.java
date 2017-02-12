package me.iacn.editcolorhelper;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by iAcn on 2017/2/12
 * Emali iAcn0301@foxmail.com
 */

public class EditColorHelper {

    private static Field mEditor;
    private static Field mCursorDrawableRes;

    public static void setColor(EditText editText, int color) {
        // Update underline color
        setUnderlineColor(editText, color);

        // Update Highlight Color
        setHighlightColor(editText, color);

        try {
            getEditorFieldFromReflect();
            Object editor = mEditor.get(editText);

            // Update Cursor Color
            setCursorColor(editText, color, editor);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setUnderlineColor(EditText editText, int color) {
        ColorStateList colorStateList = ColorUtils.getPureColorList(color);
        editText.setBackgroundTintList(colorStateList);
    }

    public static void setHighlightColor(EditText editText, int color) {
        // The value 102 is Android original EditText highlight transparency
        int translateColor = ColorUtils.setAlphaComponent(color, 102);
        editText.setHighlightColor(translateColor);
    }

    public static void setCursorColor(EditText editText, int color) throws Exception {

    }

    private static void setCursorColor(EditText editText, int color, Object editor) throws Exception {
        getCursorFieldFromReflect();

        int cursorId = mCursorDrawableRes.getInt(editText);
        Drawable drawable = editText.getContext().getDrawable(cursorId);

        if (drawable != null) {
            drawable.setTint(color);
        }

        Field cursorDrawableField = editor.getClass().getDeclaredField("mCursorDrawable");
        cursorDrawableField.setAccessible(true);
        cursorDrawableField.set(editor, new Drawable[]{drawable, drawable});
    }

    private static void getEditorFieldFromReflect() {
        if (mEditor == null) {
            mEditor = ReflectUtils.getDeclaredField(TextView.class, "mEditor");
        }
    }

    private static void getCursorFieldFromReflect() {
        if (mCursorDrawableRes == null) {
            mCursorDrawableRes = ReflectUtils.getDeclaredField(TextView.class, "mCursorDrawableRes");
        }
    }
}