package com.coopsrc.xandroid.example.downloader.compare

import androidx.fragment.app.Fragment
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.coopsrc.xandroid.downloader.ExDownloader
import com.coopsrc.xandroid.downloader.example.R
import com.coopsrc.xandroid.downloader.model.Progress
import com.coopsrc.xandroid.downloader.model.Status
import com.coopsrc.xandroid.downloader.model.TaskInfo
import com.coopsrc.xandroid.downloader.utils.DownloaderUtils
import com.coopsrc.xandroid.downloader.utils.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_compare.*
import kotlinx.android.synthetic.main.layout_item.view.*

/**
 * A placeholder fragment containing a simple view.
 */
class CompareActivityFragment : Fragment() {

    private val dataSet = mutableListOf<TaskInfo>()
    private lateinit var adapter: CompareAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_compare, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDataSet()
        initAdapter()

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun initDataSet() {
        val url = "https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk"
        val taskInfo = TaskInfo(url)

        dataSet.add(taskInfo)
    }

    private fun initAdapter() {
        adapter = CompareAdapter(R.layout.layout_item, dataSet)
    }

    class CompareAdapter(val resource: Int, private val dataSet: List<TaskInfo>) :
            RecyclerView.Adapter<CompareAdapter.ViewHolder>() {

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

                itemView.textViewType.text = taskInfo.type?.name ?: "Auto"
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
