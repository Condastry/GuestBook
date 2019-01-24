package com.nevermore.guestbook.mvvm.ui

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import com.nevermore.guestbook.R
import com.nevermore.guestbook.mvvm.viewmodels.MainViewModel
import com.nevermore.guestbook.tools.hideKeyboard
import com.nevermore.guestbook.tools.isGone
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_base.view.*
import kotlinx.android.synthetic.main.fragment_posts.*

abstract class BaseFragment : Fragment() {
    protected val MSG_ERROR = "Error occured!"
    protected abstract val contentLayoutID: Int
    protected open val toolbarTitle = "GuestBook"
    protected open val isBackable = true
    protected open val isToolbarVisible = true
    protected open val inputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE

    protected lateinit var mainVM: MainViewModel
    protected val subscriptions = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainVM = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        addEventEmmiter(mainVM.eventEmmiter)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_base, container, false).apply {
            toolbar.apply {
                title = ""
                tvTitle.text = toolbarTitle
                (activity!! as AppCompatActivity).setSupportActionBar(this)
                isGone(!isToolbarVisible)

                if (!isBackable) {
                    navigationIcon = null
                }
                icLoguot.setOnClickListener { exitDialog() }
                setNavigationOnClickListener { activity!!.onBackPressed() }
            }
            activity!!.window.setSoftInputMode(inputMode)
            inflater.inflate(contentLayoutID, contentHolder, true)
        }
    }

    private fun exitDialog() {
        AlertDialog.Builder(this.activity!!).apply {
            setMessage("Logout?")
            setPositiveButton("yes") { _, _ ->
                mainVM.logout()
            }
            setNegativeButton("No", null)
            show()
        }
    }

    protected fun showMessage(message : String){
        Toast.makeText(activity!!, "Error occured!", Toast.LENGTH_SHORT).show()
    }

    protected fun addEventEmmiter( emmiter : Observable<String>){
        subscriptions.add(
            emmiter.observeOn(AndroidSchedulers.mainThread()).subscribe({
                onEvent(it)
            }, { showMessage(MSG_ERROR) })
        )
    }

    protected open fun onEvent(event : String){

    }

    override fun onPause() {
        super.onPause()
        activity!!.hideKeyboard()
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.clear()
    }
}