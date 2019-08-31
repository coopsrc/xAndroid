package com.coopsrc.xandroid.downloader.core

import android.content.Context
import com.coopsrc.xandroid.downloader.db.DatabaseModule
import com.coopsrc.xandroid.downloader.utils.Constants

/**
 * Created by tingkuo.
 * Date: 2018-07-23
 * Time: 20:10
 */
class Config {

    internal var context: Context? = null

    private var maxTask = Constants.Config.maxTask
    internal var maxRange = Constants.Config.maxRange
    internal var savePath = Constants.Config.savePath(null)
    internal var autoStart = Constants.Config.autoStart
    private var enableBackground = Constants.Config.enableBackground

    internal var withDebug = Constants.Config.withDebug

    internal var taskRepo: ITaskRepo = LocalTaskRepo(maxTask)

    internal var limitSpeed = Constants.Config.limitSpeed

    internal constructor()

    private constructor(builder: Builder) {

        this.context = builder.context

        this.maxTask = builder.maxTask
        this.maxRange = builder.maxRange
        this.savePath = builder.savePath
        this.autoStart = builder.autoStart
        this.limitSpeed = builder.limitSpeed

        this.enableBackground = builder.enableBackground

        this.withDebug = builder.withDebug

        if (builder.enableBackground) {
            this.taskRepo = RemoteTaskRepo(builder.context, maxTask)
        } else {
            this.taskRepo = LocalTaskRepo(maxTask)
        }

        DatabaseModule.instance.init(builder.context)
    }

    class Builder(internal var context: Context) {

        internal var maxTask = Constants.Config.maxTask
        internal var maxRange = Constants.Config.maxRange
        internal var savePath = Constants.Config.savePath(context)
        internal var autoStart = Constants.Config.autoStart
        internal var limitSpeed = Constants.Config.limitSpeed
        internal var enableBackground = Constants.Config.enableBackground

        var withDebug = Constants.Config.withDebug

        fun maxTask(maxTask: Int): Builder {
            this.maxTask = maxTask

            return this
        }

        fun maxRange(maxRange: Int): Builder {

            this.maxRange = when {
                maxRange > Constants.Config.maxRange -> Constants.Config.maxRange
                maxRange <= 0 -> Constants.Config.maxRange
                else -> maxRange
            }

            return this
        }

        fun savePath(savePath: String): Builder {
            this.savePath = savePath

            return this
        }

        fun autoStart(autoStart: Boolean): Builder {
            this.autoStart = autoStart

            return this
        }

        fun limitSpeed(limitSpeed: Long): Builder {
            this.limitSpeed = limitSpeed

            return this
        }

        fun enableBackground(enableBackground: Boolean): Builder {
            this.enableBackground = enableBackground

            return this
        }

        fun withDebug(withDebug: Boolean): Builder {
            this.withDebug = withDebug

            return this
        }

        fun build(): Config {
            return Config(this)
        }
    }

    override fun toString(): String {
        return "Config(context=$context, maxTask=$maxTask, maxRange=$maxRange, savePath='$savePath', autoStart=$autoStart, enableBackground=$enableBackground, withDebug=$withDebug, taskRepo=$taskRepo, limitSpeed=$limitSpeed)"
    }
}