package com.sy.mobile.control;


import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

/**
 * 可设置最大数字的输入框
 * @author admin
 *
 */
public class EditTextMax extends EditText {

	public interface TextChanged {
		/**
		 * 内容改变
		 */
		public void afterText();
	}

	TextChanged change;
	int maxl = 0;

	public EditTextMax(Context context) {
		super(context);
		inti();
		// TODO Auto-generated constructor stub
	}

	public EditTextMax(Context context, AttributeSet attrs) {
		super(context, attrs);
		inti();
	}

	public void setMaxL(int max) {
		maxl = max;
	}

	private void inti() {
		addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				// TODO Auto-generated method stub
				Log.i("maxted", s+":11111111");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				// TODO Auto-generated method stub
				Log.i("maxted", s+":2222222");
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				Log.i("maxted", s+":3333333");
				if(change!=null)
					change.afterText();
				if (s != null && !s.equals("")) {
					int a = 0;
					try {
						a = Integer.parseInt(s.toString());
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						a = 0;
					}
					if (a > maxl)
						setText(maxl + "");
					return;
				}
			}
		});
	}

	/**
	 * 改变
	 */
	public void setOnChange(TextChanged change) {
		this.change = change;
	}
}
