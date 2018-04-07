package com.upmc.htag.views

import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import com.upmc.htag.R
import java.time.Duration
import android.graphics.Typeface
import android.widget.TextView

/**
 * Created by cb_mac on 21/03/2018.
 */

class HtagSnackbar {

    companion object {

        fun make(ctx : Context , view: View, text: String, duration: Int) : Snackbar{
            var sb : Snackbar = Snackbar.make(view,text,duration)
            sb.view.setBackgroundColor(ContextCompat.getColor(ctx, R.color.primaryLightColor))
            (sb.view.findViewById(android.support.design.R.id.snackbar_text) as TextView).typeface
            return  sb
        }
    }
}
