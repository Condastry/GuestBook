package com.nevermore.guestbook.tools

import android.content.Context
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.nevermore.guestbook.R
import kotlinx.android.synthetic.main.view_input.view.*


class InputField @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs) {
    val layout: TextInputLayout
    val editText: TextInputEditText

    var text: String
        get() = editText.text?.toString() ?: ""
        set(value) = editText.setText(value)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_input, this, true)
        layout = this.inputLayout
        editText = this.inputEditText
        context.theme.obtainStyledAttributes(attrs, R.styleable.InputField, defStyleAttr, 0).apply {
            try {
                layout.hint = getString(R.styleable.InputField_inputHint)
                val inputType = getInt(R.styleable.InputField_android_inputType, InputType.TYPE_CLASS_TEXT)
                editText.inputType = inputType
            } finally {
                recycle()
            }
        }
    }

}