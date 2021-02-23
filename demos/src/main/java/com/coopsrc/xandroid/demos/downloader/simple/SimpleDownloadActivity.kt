package com.coopsrc.xandroid.demos.downloader.simple

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.coopsrc.xandroid.demos.databinding.ActivitySimpleDownloadBinding
import com.coopsrc.xandroid.downloader.ExDownloader
import com.coopsrc.xandroid.downloader.model.Progress
import com.coopsrc.xandroid.downloader.model.Status
import com.coopsrc.xandroid.downloader.model.TaskInfo
import com.coopsrc.xandroid.downloader.utils.DownloaderUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class SimpleDownloadActivity : AppCompatActivity() {

    private val url =
        "http://dlied4.myapp.com/myapp/1104466820/cos.release-40109/2017_com.tencent.tmgp.sgame_h600877_1.51.1.23_362bct.apk"
    private val url1 =
        "https://dlied5.myapp.com/myapp/1104466820/sgame/2017_com.tencent.tmgp.sgame_h164_1.33.1.23_fe680b.apk"

    private lateinit var taskInfo: TaskInfo
    private lateinit var progress: Progress

    private lateinit var disposable: Disposable

    private lateinit var mBinding: ActivitySimpleDownloadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySimpleDownloadBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        taskInfo = TaskInfo(url1)
        progress = Progress(taskInfo.tag)

        disposable = ExDownloader.create(taskInfo)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                progress = it
                updateProgress()
            }

    }

    override fun onDestroy() {
        super.onDestroy()

        DownloaderUtils.dispose(disposable)
        ExDownloader.stop(taskInfo).subscribe()
    }

    fun startDownload(view: View) {
        when (progress.status) {
            Status.Idle -> ExDownloader.start(taskInfo).subscribe()
            Status.Downloading -> ExDownloader.stop(taskInfo).subscribe()
            Status.Suspend -> ExDownloader.start(taskInfo).subscribe()
            Status.Complete -> Toast.makeText(this, "$progress", Toast.LENGTH_SHORT).show()
            Status.Failed -> ExDownloader.start(taskInfo).subscribe()
            Status.Deleted -> ExDownloader.start(taskInfo).subscribe()
            else -> Toast.makeText(this, "$progress", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateProgress() {
        when (progress.status) {
            Status.Suspend, Status.Idle, Status.Complete -> {
                initProgressBar()
                updateProgressBar()
            }
            Status.Prepared -> {
                initProgressBar()
            }
            Status.Downloading -> {
                updateProgressBar()
            }
        }
        mBinding.buttonDownload.text = when (progress.status) {
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
            Status.Deleted -> "文件被删除 --> 重新下载"
            else -> "开始"
        }
    }

    private fun initProgressBar() {
        mBinding.segmentProgressBar.setFixedCountSegments(progress.totalSize, 4)
    }

    private fun updateProgressBar() {
        mBinding.segmentProgressBar.setSegment(0, progress.downloadSize / 4)
        mBinding.segmentProgressBar.setSegment(1, progress.downloadSize / 4)
        mBinding.segmentProgressBar.setSegment(2, progress.downloadSize / 4)
        mBinding.segmentProgressBar.setSegment(3, progress.downloadSize / 4)
    }


}
