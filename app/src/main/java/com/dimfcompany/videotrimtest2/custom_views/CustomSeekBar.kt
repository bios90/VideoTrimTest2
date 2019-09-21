package com.dimfcompany.videotrimtest2.custom_views

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar


class CustomSeekBar : CrystalSeekbar
{
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun getBarHeight(): Float
    {
        return dp2px(56f)
    }



    override fun getThumbHeight(): Float
    {
        return dp2px(12f)
    }

    override fun getThumbWidth(): Float
    {
        return dp2px(12f)
    }

    private fun dp2pxInt(dp: Float): Int
    {
        return dp2px(dp).toInt()
    }

    private fun dp2px(dp: Float): Float
    {
        val r = Resources.getSystem()
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics)
    }
}