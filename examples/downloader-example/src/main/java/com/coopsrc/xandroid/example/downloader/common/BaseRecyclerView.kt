package com.coopsrc.xandroid.example.downloader.common

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View

/**
 * Created by zhangtk.
 * Date: 2017-10-12
 * Time: 17:32
 */

class BaseRecyclerView : RecyclerView,
        RecyclerView.OnChildAttachStateChangeListener,
        View.OnClickListener, View.OnLongClickListener,
        View.OnKeyListener, View.OnFocusChangeListener,
        View.OnTouchListener, View.OnHoverListener {

    var onItemClickListener: OnItemClickListener? = null
    var onItemLongClickListener: OnItemLongClickListener? = null
    var onItemKeyListener: OnItemKeyListener? = null
    var onItemFocusChangeListener: OnItemFocusChangeListener? = null
    var onItemTouchListener: OnItemTouchListener? = null
    var onItemHoverListener: OnItemHoverListener? = null

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(v: View, position: Int): Boolean
    }

    interface OnItemKeyListener {
        fun onItemKey(v: View, keyCode: Int, event: KeyEvent, position: Int): Boolean
    }

    interface OnItemFocusChangeListener {
        fun onItemFocusChange(v: View, hasFocus: Boolean, position: Int)
    }

    interface OnItemTouchListener {
        fun onItemTouch(v: View, event: MotionEvent, position: Int): Boolean
    }

    interface OnItemHoverListener {
        fun onItemHover(v: View, event: MotionEvent, position: Int): Boolean
    }


    constructor(context: Context) : super(context) {
        initListeners()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initListeners()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        initListeners()
    }

    private fun initListeners() {
        this.addOnChildAttachStateChangeListener(this)
    }

    override fun onChildViewAttachedToWindow(view: View) {
        view.setOnClickListener(this)
        view.setOnLongClickListener(this)
        view.setOnKeyListener(this)
        view.onFocusChangeListener = this
        view.setOnTouchListener(this)
        view.setOnHoverListener(this)
    }

    override fun onChildViewDetachedFromWindow(view: View) {
        view.setOnClickListener(null)
        view.setOnLongClickListener(null)
        view.setOnKeyListener(null)
        view.onFocusChangeListener = null
        view.setOnTouchListener(null)
        view.setOnHoverListener(null)
    }

    override fun onClick(view: View) {
        if (onItemClickListener != null) {
            onItemClickListener!!.onItemClick(view, getChildAdapterPosition(view))
        }
    }

    override fun onFocusChange(view: View, hasFocus: Boolean) {
        if (onItemFocusChangeListener != null) {
            onItemFocusChangeListener!!.onItemFocusChange(view, hasFocus, getChildAdapterPosition(view))
        }
    }

    override fun onKey(view: View, keyCode: Int, event: KeyEvent): Boolean {
        return if (onItemKeyListener != null) {
            onItemKeyListener!!.onItemKey(view, keyCode, event, getChildAdapterPosition(view))
        } else false
    }

    override fun onLongClick(view: View): Boolean {
        return if (onItemLongClickListener != null) {
            onItemLongClickListener!!.onItemLongClick(view, getChildAdapterPosition(view))
        } else false

    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {

        return if (onItemTouchListener != null) {
            onItemTouchListener!!.onItemTouch(view, event, getChildAdapterPosition(view))
        } else false

    }

    override fun onHover(view: View, event: MotionEvent): Boolean {

        return if (onItemHoverListener != null) {
            onItemHoverListener!!.onItemHover(view, event, getChildAdapterPosition(view))
        } else false

    }
}
