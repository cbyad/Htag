package com.upmc.htag.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.upmc.htag.R

/**
 * Created by cb_mac on 06/03/2018.
 */

/**
 * A placeholder fragment containing a simple view.
 */
class LikeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_liked_model, container, false)
        return rootView
    }

    companion object {

        /**
         * Returns a new instance
         */
        fun newInstance(): LikeFragment {
            val fragment = LikeFragment()
            //val args = Bundle()
            //args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            //fragment.arguments = args
            return fragment
        }
    }
}