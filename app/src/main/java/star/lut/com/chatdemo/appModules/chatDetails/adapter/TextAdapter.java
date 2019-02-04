package star.lut.com.chatdemo.appModules.chatDetails.adapter;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;

/**
 * Created by kamrulhasan on 13/11/18.
 */
public class TextAdapter {
    private String text;

    public TextAdapter(String text){
        this.text = text;
    }


    public SpannableString getModifiedText() {
        SpannableString spannableString = new SpannableString(text);
            String character = "";
            if (text.contains("*")){
                character = "*";
            }else if (text.contains("_")){
                character = "_";
            }else if (text.contains("~")){
                character = "~";
            }

            String temp = text;
            if (!character.equals("")) {
                int count = temp.length() - temp.replace(character, "").length();

                if (count == 2) {
                    spannableString = check(character);
                }
            }

        return spannableString;
    }

    private SpannableString check(String character) {
        SpannableString spannableString = new SpannableString(text);
        int x = -1;
        x = text.indexOf(character);
        if (x != -1) {
            StringBuilder temp = new StringBuilder(text);
            temp.deleteCharAt(x);
            text = temp.toString();
            int y = -1;
            y = text.indexOf(character);
            if (y != -1) {
                temp = new StringBuilder(text);
                temp.deleteCharAt(y);
                text = temp.toString();

                spannableString= new SpannableString(text);


                if (character.equals("*")) {
                    spannableString.setSpan(new StyleSpan(Typeface.BOLD), x, y, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (character.equals("_")) {
                    spannableString.setSpan(new StyleSpan(Typeface.ITALIC), x, y, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (character.equals("~")) {
                    spannableString.setSpan(new StyleSpan(Paint.STRIKE_THRU_TEXT_FLAG), x, y, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }


        return spannableString;


    }
}
