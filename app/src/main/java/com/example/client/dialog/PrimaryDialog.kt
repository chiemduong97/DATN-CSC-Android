package com.example.client.dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.example.client.R
import com.example.client.base.BaseDialog

class PrimaryDialog(private val onOk:() -> Unit, private val onCancel: () -> Unit) : BaseDialog(), View.OnClickListener {
    private var title: String? = null
    private var description: String? = null
    private var tvTitle: TextView? = null
    private var tvDescription: TextView? = null
    private var tvOk: TextView? = null
    private var tvCancel: TextView? = null

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.dialog_primary, null)
        tvTitle = rootView?.findViewById(R.id.tv_title)
        tvDescription = rootView?.findViewById(R.id.tv_description)
        tvOk = rootView?.findViewById(R.id.tv_ok)
        tvCancel = rootView?.findViewById(R.id.tv_cancel)
        return rootView
    }

    override fun bindEvent() {
        tvOk?.setOnClickListener(this)
        tvCancel?.setOnClickListener(this)
    }

    override fun bindComponent() {
        tvTitle?.text = title
        tvDescription?.text = description
    }

    override fun onClick(v: View) {
        dialog?.run {
            when (v.id) {
                R.id.tv_ok -> {
                    onOk.invoke()
                    dismiss()
                }
                R.id.tv_cancel -> {
                    onCancel.invoke()
                    dismiss()
                }
            }
        }

    }

    fun hideBtnCancel(): PrimaryDialog {
        tvCancel?.visibility = View.GONE
        return this
    }
    fun setTitle(title: String): PrimaryDialog {
        tvTitle?.visibility = View.VISIBLE
        this.title = title
        return this
    }
    fun setDescription(description: String): PrimaryDialog {
        this.description = description
        return this
    }

    fun show(fragmentManager: FragmentManager?) {
        fragmentManager ?: return
        show(fragmentManager, tag)
    }

}