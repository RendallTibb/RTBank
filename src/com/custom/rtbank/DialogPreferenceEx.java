package com.custom.rtbank;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;

public class DialogPreferenceEx extends DialogPreference {
	public DialogPreferenceEx(Context oContext, AttributeSet attrs)
	{
		super(oContext, attrs);		
	}

	public interface OnClickListener {
        public void onClick(DialogInterface dialog, int which);
    }
}
