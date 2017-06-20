package com.gs.buluo.store.kotlin.activity

import com.gs.buluo.store.kotlin.presenter.KotBasePresenter
import com.gs.buluo.store.view.activity.LoginActivity


/**
 * Created by admin on 2016/11/1.
 */
abstract class KotBaseActivity : android.support.v7.app.AppCompatActivity() {
    var rootView: android.view.View? = null
        internal set

    open var presenter: KotBasePresenter? = null

    private var color = com.gs.buluo.store.R.color.titlebar_background

    @android.support.annotation.RequiresApi(android.os.Build.VERSION_CODES.KITKAT)
    override fun onCreate(@android.support.annotation.Nullable savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        com.gs.buluo.common.utils.AppManager.getAppManager().addActivity(this)
//        setExplode()//new Slide()  new Fade()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            addFlag()
        }
//        if (presenter != null && this is IBaseView) {
//            presenter!!.attach(this)
//        }

        rootView = createView()
        setContentView(rootView)
        bindView(savedInstanceState)
        initSystemBar(this)

        val view = rootView!!.findViewById(com.gs.buluo.store.R.id.back)
        view!!.setOnClickListener { finish() }
    }

    @android.support.annotation.RequiresApi(api = android.os.Build.VERSION_CODES.KITKAT)
    private fun addFlag() {
        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.LOLLIPOP)
    fun setExplode() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            window.exitTransition = android.transition.Explode()
        }
    }

    protected fun init() {

    }

    private fun createView(): android.view.View {
        val view = android.view.LayoutInflater.from(this).inflate(contentLayout, null)
        butterknife.ButterKnife.bind(this, view)
        return view
    }

    override fun onDestroy() {
        com.gs.buluo.common.utils.AppManager.getAppManager().finishActivity(this)
//        if (presenter != null) {
//            presenter!!.detachView()
//        }
        super.onDestroy()
    }

    @android.support.annotation.RequiresApi(android.os.Build.VERSION_CODES.KITKAT)
            /**
             * 设置状态栏颜色

             * @param colorInt
             */
    fun setBarColor(colorInt: Int) {
        color = colorInt
        initSystemBar(this)
    }

    @android.support.annotation.RequiresApi(android.os.Build.VERSION_CODES.KITKAT)
    private fun initSystemBar(activity: android.app.Activity) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity, true)
        }
        val tintManager = com.gs.buluo.common.utils.SystemBarTintManager(activity)
        tintManager.isStatusBarTintEnabled = true
        tintManager.setStatusBarTintResource(color)
    }

    private fun setTranslucentStatus(activity: android.app.Activity, on: Boolean) {

        val winParams = window.attributes
        val bits = android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        window.attributes = winParams
    }

    protected fun showLoadingDialog() {
        com.gs.buluo.common.widget.LoadingDialog.getInstance().show(rootView!!.context, getString(com.gs.buluo.store.R.string.loading), true)
    }

    protected fun showLoadingDialog(cancel: Boolean) {
        com.gs.buluo.common.widget.LoadingDialog.getInstance().show(rootView!!.context, getString(com.gs.buluo.store.R.string.loading), cancel)
    }

    protected fun showLoadingDialog(res: Int) {
        com.gs.buluo.common.widget.LoadingDialog.getInstance().show(rootView!!.context, res, true)
    }

    protected fun dismissDialog() {
        com.gs.buluo.common.widget.LoadingDialog.getInstance().dismissDialog()
    }

    protected val ctx: android.content.Context
        get() = this

    protected abstract fun bindView(savedInstanceState: android.os.Bundle?)

    protected abstract val contentLayout: Int


    protected fun checkUser(context: android.content.Context): Boolean {
        if (com.gs.buluo.store.TribeApplication.getInstance().userInfo == null) {
            com.gs.buluo.common.utils.ToastUtils.ToastMessage(context, getString(com.gs.buluo.store.R.string.login_first))
            val intent = android.content.Intent(context, LoginActivity::class.java)
            startActivity(intent)
            return false
        }
        return true
    }


    @org.greenrobot.eventbus.Subscribe(sticky = true, threadMode = org.greenrobot.eventbus.ThreadMode.MAIN)
    fun onUpdate(event: com.gs.buluo.common.UpdateEvent) {
        val updatePanel = com.gs.buluo.store.view.widget.panel.UpdatePanel(com.gs.buluo.common.utils.AppManager.getAppManager().currentActivity(), event)
        updatePanel.setCancelable(event.supported)
        updatePanel.show()
    }

}

