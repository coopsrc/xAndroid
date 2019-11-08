package com.coopsrc.xandroid.example.downloader.list

import androidx.fragment.app.Fragment
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.coopsrc.xandroid.downloader.ExDownloader
import com.coopsrc.xandroid.downloader.model.Progress
import com.coopsrc.xandroid.downloader.model.Status
import com.coopsrc.xandroid.downloader.model.TaskInfo
import com.coopsrc.xandroid.downloader.utils.DownloaderUtils
import com.coopsrc.xandroid.downloader.utils.Logger
import com.coopsrc.xandroid.example.downloader.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.layout_item_1.view.*
import java.util.ArrayList

/**
 * A placeholder fragment containing a simple view.
 */
class ListActivityFragment : Fragment() {

    private val dataSet = mutableListOf<TaskInfo>()
    private lateinit var adapter: ListAdapter

    companion object {
        val mIconList = ArrayList<String>()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDataSet()
        initAdapter()

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        buttonStartAll.setOnClickListener {
            ExDownloader.startAll().subscribe()
        }

        buttonStopAll.setOnClickListener {
            ExDownloader.stopAll().subscribe()
        }
    }

    private fun initDataSet() {
//
//        mIconList.add("https://game.gtimg.cn/images/cjm/web201801/top-tag.png")
//        dataSet.add(TaskInfo("https://dlied6.myapp.com/myapp/1106467070/pubgmhd/2017_com.tencent.tmgp.pubgmhd_h100_0.3.2_ef3516.apk"))

        mIconList.add("https://img5.anzhi.com/data4/icon/201807/12/com.seasun.jxsj2.mi_94685100_72.png")
        dataSet.add(TaskInfo("https://yapkwww.cdn.anzhi.com/data4/apk/201807/12/4c993dcdd615b81441ef25dda24e3b34_44125000.apk"))

        mIconList.add("https://img2.anzhi.com/data1/icon/201802/27/com.ss.android.ugc.aweme_21877100_72.png")
        dataSet.add(TaskInfo("https://yapkwww.cdn.anzhi.com/data4/apk/201807/19/2d2a13349ca59634a0b85845fadcb62a_15510200.apk"))

        mIconList.add("https://img4.anzhi.com/data3/icon/201711/07/com.tencent.tmgp.jnbg2_94143100_72.png")
        dataSet.add(TaskInfo("https://yapkwww.cdn.anzhi.com/data1/apk/201712/22/61f714940c448c160e2726232b357e61_64054400.apk"))

        mIconList.add("https://img4.anzhi.com/data4/icon/201807/25/853e7758ecc5e25f38c0b3f40461d7f3_37661700_72.jpg")
        dataSet.add(TaskInfo("https://yapkwww.cdn.anzhi.com/data4/apk/201807/25/a0538840c750147dae46c6fce3ea1ac9_37661700.apk"))

        mIconList.add("https://img3.anzhi.com/data3/icon/201711/01/com.ss.android.ugc.live_15088700_72.png")
        dataSet.add(TaskInfo("https://yapkwww.cdn.anzhi.com/data4/apk/201807/31/e50218231678b06bf3c8bab3373e87ca_09759500.apk"))

        mIconList.add("https://img4.anzhi.com/data4/icon/201807/06/fm.xiami.main_61153500_72.png")
        dataSet.add(TaskInfo("https://yapkwww.cdn.anzhi.com/data4/apk/201807/19/4633a56d567a4cb5fc7558b542347d35_66027200.apk"))

    }

    private fun initAdapter() {
        adapter = ListAdapter(R.layout.layout_item_1, dataSet)
    }

    class ListAdapter(val resource: Int, private val dataSet: List<TaskInfo>) :
            RecyclerView.Adapter<ListAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(resource, parent, false)

            return ViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return dataSet.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindData(dataSet[position])
        }

        override fun onViewAttachedToWindow(holder: ViewHolder) {
            super.onViewAttachedToWindow(holder)

            holder.onAttach()
        }

        override fun onViewDetachedFromWindow(holder: ViewHolder) {
            super.onViewDetachedFromWindow(holder)

            holder.onDetach()
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
            private val tag = "ViewHolder"

            private var disposable: Disposable? = null
            private lateinit var taskInfo: TaskInfo

            private lateinit var progress: Progress

            init {
                itemView.buttonStart.setOnClickListener(this)
            }

            fun bindData(data: TaskInfo) {
                taskInfo = data
                progress = taskInfo.progress

                Glide.with(itemView).load(mIconList[adapterPosition]).into(itemView.imageViewIcon)

                updateProgress()
            }

            override fun onClick(v: View?) {

                Logger.i(tag, "$progress")

                when (progress.status) {
                    Status.Idle -> ExDownloader.start(taskInfo).subscribe()
                    Status.Downloading -> ExDownloader.stop(taskInfo).subscribe()
                    Status.Suspend -> ExDownloader.start(taskInfo).subscribe()
                    Status.Complete -> Toast.makeText(itemView.context, "$progress", Toast.LENGTH_SHORT).show()
                    Status.Failed -> ExDownloader.start(taskInfo).subscribe()
                    Status.Delete -> ExDownloader.start(taskInfo).subscribe()
                    else -> Toast.makeText(itemView.context, "$taskInfo", Toast.LENGTH_SHORT).show()
                }
            }

            fun onAttach() {
                disposable = ExDownloader.create(taskInfo)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            progress = it

                            updateProgress()
                        }
            }

            fun onDetach() {
                DownloaderUtils.dispose(disposable)
            }

            private fun updateProgress() {

                itemView.textViewProgress.text = progress.getProgress()
                itemView.progressBar.max = progress.totalSize.toInt()
                itemView.progressBar.progress = progress.downloadSize.toInt()
                itemView.buttonStart.text = when (progress.status) {
                    Status.Idle -> {
                        if (progress.downloadSize > 0) {
                            "继续"
                        } else {
                            "开始"
                        }
                    }
                    Status.Waiting -> "等待中……"
                    Status.Starting -> "正在开始……"
                    Status.Downloading -> "正在下载 --> 暂停"
                    Status.Suspend -> "下载暂停 --> 继续"
                    Status.Complete -> "下载完成"
                    Status.Failed -> "下载失败 --> 重试"
                    Status.Removed -> "任务删除"
                    Status.Delete -> "文件被删除 --> 重新下载"
                    else -> "开始"
                }

            }
        }
    }
}
