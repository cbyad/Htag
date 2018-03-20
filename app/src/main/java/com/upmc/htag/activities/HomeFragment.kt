package com.upmc.htag.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.upmc.htag.R
import kotlinx.android.synthetic.main.fragment_home_main.view.*

/**
 * Created by cb_mac on 06/03/2018.
 */

/**
 * A placeholder fragment containing a simple view.
 */
class HomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_home_main, container, false)
        //rootView.section_label.text = getString(R.string.section_format)
        Log.i("hjkjhj",rootView.info_text_home.text.toString())
        return rootView
    }

    companion object {
        /**
         * Returns a new instance of this fragment
         */
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}