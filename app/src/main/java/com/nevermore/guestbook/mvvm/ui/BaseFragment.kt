package com.nevermore.guestbook.mvvm.ui

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.nevermore.guestbook.R
import com.nevermore.guestbook.mvvm.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_base.view.*


abstract class BaseFragment : Fragment() {
    protected abstract val contentLayoutID: Int
    protected open val toolbarTitle = "GuestBook"
    protected open val isBackable = true
    protected open val isToolbarVisible = true

    protected lateinit var mainVM: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainVM = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_base, container, false).apply {
            toolbar.apply {
                title = ""
                tvTitle.text = toolbarTitle
                (activity!! as AppCompatActivity).setSupportActionBar(this)
                visibility = if (isToolbarVisible) View.VISIBLE else View.GONE

                if (!isBackable) {
                    navigationIcon = null
                }
                icLoguot.setOnClickListener { exitDialog() }
                setNavigationOnClickListener { activity!!.onBackPressed() }
            }

            inflater.inflate(contentLayoutID, contentHolder, true)
        }
    }

    private fun exitDialog(){
        AlertDialog.Builder(this.activity!!).apply {
            setMessage("Logout?")
            setPositiveButton("yes", DialogInterface.OnClickListener { _, _->
                mainVM.logout()
            })
            setNegativeButton("No", null)
            show()
        }
    }

    override fun onPause() {
        super.onPause()
        hideSoftKeyboard()
    }

    private fun hideSoftKeyboard() {
        val imm = activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = activity!!.currentFocus ?: View(activity)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}