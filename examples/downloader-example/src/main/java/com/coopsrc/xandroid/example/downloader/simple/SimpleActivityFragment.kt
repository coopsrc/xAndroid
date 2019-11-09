package com.coopsrc.xandroid.example.downloader.simple

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.coopsrc.xandroid.downloader.ExDownloader
import com.coopsrc.xandroid.downloader.model.Progress
import com.coopsrc.xandroid.downloader.model.Status
import com.coopsrc.xandroid.downloader.model.TaskInfo
import com.coopsrc.xandroid.downloader.utils.DownloaderUtils
import com.coopsrc.xandroid.example.downloader.R
import com.coopsrc.xandroid.utils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_simple.*

/**
 * A placeholder fragment containing a simple view.
 */
class SimpleActivityFragment : Fragment() {
    private val TAG = "SimpleActivity"

    private var url =
        "https://dlied5.myapp.com/myapp/1104466820/sgame/2017_com.tencent.tmgp.sgame_h164_1.33.1.23_fe680b.apk"
    // https://sqimg.qq.com/qq_product_operations/im/qqlogo/logo.png
    private var mDisposable: Disposable? = null
    private lateinit var mTaskInfo: TaskInfo

    private lateinit var mProgress: Progress

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_simple, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        url =
            "https://dlied6.myapp.com/myapp/1106467070/pubgmhd/2017_com.tencent.tmgp.pubgmhd_h100_0.3.2_ef3516.apk"
        mTaskInfo = TaskInfo(url)
        mProgress = Progress(mTaskInfo.tag)
        mDisposable = ExDownloader.create(mTaskInfo)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                LogUtils.tag(TAG).d("$it")
                mProgress = it
                updateProgress(it)
            }

        buttonStart.setOnClickListener {
            LogUtils.tag(TAG).i("$mProgress")

            when (mProgress.status) {
                Status.Idle -> ExDownloader.start(mTaskInfo).subscribe()
                Status.Downloading -> ExDownloader.stop(mTaskInfo).subscribe()
                Status.Suspend -> ExDownloader.start(mTaskInfo).subscribe()
                Status.Complete -> Toast.makeText(context, "$mProgress", Toast.LENGTH_SHORT).show()
                Status.Failed -> ExDownloader.start(mTaskInfo).subscribe()
                Status.Delete -> ExDownloader.start(mTaskInfo).subscribe()
                else -> Toast.makeText(context, "$mProgress", Toast.LENGTH_SHORT).show()
            }


        }
    }

    override fun onDestroy() {
        super.onDestroy()

        DownloaderUtils.dispose(mDisposable)
        ExDownloader.stop(mTaskInfo).subscribe()
    }

    private fun updateProgress(progress: Progress) {

        textView.text = progress.getProgress()
        progressBar.max = progress.totalSize.toInt()
        progressBar.progress = progress.downloadSize.toInt()

        buttonStart.text = when (progress.status) {
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
