package mx.axa.insurance_policy_list.ui.customviews

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import mx.axa.insurance_policy_list.R
import mx.axa.insurance_policy_list.databinding.ViewDetailBinding

class DetailView(context: Context, private val attrs: AttributeSet?) : RelativeLayout(context, attrs) {
    private var bind: ViewDetailBinding = ViewDetailBinding.inflate(LayoutInflater.from(getContext()))

    init {
        addView(bind.root)
        initAttributes()
    }

    private fun initAttributes() {
        val attributes: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.DetailView)
        bind.tvDetail.text = attributes.getString(R.styleable.DetailView_detail)
    }

    fun setValue(value: String) {
        bind.tvValue.text = value
    }
}