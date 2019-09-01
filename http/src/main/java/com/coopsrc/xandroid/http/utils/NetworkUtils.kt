package com.coopsrc.xandroid.http.utils

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import androidx.annotation.RequiresPermission

/**
 * Created by tingkuo.
 * Date: 2017-12-25
 * Time: 08:58
 */
class NetworkUtils private constructor() {

    init {
        throw UnsupportedOperationException(this.javaClass.simpleName + " cannot be instantiated")
    }

    companion object {
        private val tag = "NetworkUtils"

        @JvmStatic
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager ?: return false

            val activeNetworkInfo: NetworkInfo?
            activeNetworkInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork
                connectivityManager.getNetworkInfo(network)
            } else {
                connectivityManager.activeNetworkInfo
            }

            return activeNetworkInfo != null && activeNetworkInfo.isAvailable
        }

        @JvmStatic
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        fun isNetworkConnected(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager ?: return false

            val activeNetworkInfo: NetworkInfo?
            activeNetworkInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork
                connectivityManager.getNetworkInfo(network)
            } else {
                connectivityManager.activeNetworkInfo
            }

            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

        @JvmStatic
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        private fun isNetworkAvailable(context: Context, type: Int): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager ?: return false

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                connectivityManager.allNetworks
                        .map { connectivityManager.getNetworkInfo(it) }
                        .filter { it != null && it.type == type && it.isAvailable }
                        .forEach { return true }
            } else {
                connectivityManager.allNetworkInfo
                        .filter { it != null && it.type == type && it.isAvailable }
                        .forEach { return true }
            }

            return false
        }

        @JvmStatic
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        private fun isNetworkConnected(context: Context, type: Int): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager ?: return false

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                connectivityManager.allNetworks
                        .map { connectivityManager.getNetworkInfo(it) }
                        .filter { it != null && it.type == type && it.isConnected }
                        .forEach { return true }
            } else {
                connectivityManager.allNetworkInfo
                        .filter { it != null && it.type == type && it.isConnected }
                        .forEach { return true }
            }

            return false
        }

        @JvmStatic
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        fun isWifiAvailable(context: Context): Boolean {

            return isNetworkAvailable(context, ConnectivityManager.TYPE_WIFI)
        }

        @JvmStatic
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        fun isWifiConnected(context: Context): Boolean {

            return isNetworkConnected(context, ConnectivityManager.TYPE_WIFI)
        }

        @JvmStatic
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        fun isMobileAvailable(context: Context): Boolean {

            return isNetworkAvailable(context, ConnectivityManager.TYPE_MOBILE)
        }

        @JvmStatic
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        fun isMobileConnected(context: Context): Boolean {

            return isNetworkConnected(context, ConnectivityManager.TYPE_MOBILE)
        }

        @JvmStatic
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        fun isVpnAvailable(context: Context): Boolean {

            return isNetworkAvailable(context, ConnectivityManager.TYPE_VPN)
        }

        @JvmStatic
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        fun isVpnConnected(context: Context): Boolean {

            return isNetworkConnected(context, ConnectivityManager.TYPE_VPN)
        }

        @JvmStatic
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        fun isBluetoothAvailable(context: Context): Boolean {

            return isNetworkAvailable(context, ConnectivityManager.TYPE_BLUETOOTH)
        }

        @JvmStatic
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        fun isBluetoothConnected(context: Context): Boolean {

            return isNetworkConnected(context, ConnectivityManager.TYPE_BLUETOOTH)
        }

        @JvmStatic
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        fun getNetworkType(context: Context): Int {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager ?: return -1

            val activeNetworkInfo: NetworkInfo?
            activeNetworkInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork
                connectivityManager.getNetworkInfo(network)
            } else {
                connectivityManager.activeNetworkInfo
            }

            return activeNetworkInfo?.type ?: -1

        }

        @JvmStatic
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        fun getNetworkTypeName(context: Context): String {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager ?: return "UNKNOWN"

            val activeNetworkInfo: NetworkInfo?
            activeNetworkInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork
                connectivityManager.getNetworkInfo(network)
            } else {
                connectivityManager.activeNetworkInfo
            }

            return if (activeNetworkInfo != null) {
                activeNetworkInfo.typeName
            } else "UNKNOWN"

        }

        @JvmStatic
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        fun getNetworkSubtype(context: Context): Int {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager ?: return -1

            val activeNetworkInfo: NetworkInfo?
            activeNetworkInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork
                connectivityManager.getNetworkInfo(network)
            } else {
                connectivityManager.activeNetworkInfo
            }

            return activeNetworkInfo?.subtype ?: -1

        }

        @JvmStatic
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        fun getNetworkSubtypeName(context: Context): String {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager ?: return "UNKNOWN"

            val activeNetworkInfo: NetworkInfo?
            activeNetworkInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork
                connectivityManager.getNetworkInfo(network)
            } else {
                connectivityManager.activeNetworkInfo
            }

            return if (activeNetworkInfo != null) {
                activeNetworkInfo.subtypeName
            } else "UNKNOWN"

        }

        @JvmStatic
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        fun getNetworkState(context: Context): NetworkInfo.State {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager ?: return NetworkInfo.State.UNKNOWN

            val activeNetworkInfo: NetworkInfo?
            activeNetworkInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork
                connectivityManager.getNetworkInfo(network)
            } else {
                connectivityManager.activeNetworkInfo
            }

            return if (activeNetworkInfo != null) {
                activeNetworkInfo.state
            } else NetworkInfo.State.UNKNOWN

        }

        @JvmStatic
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        fun getNetworkExtraInfo(context: Context): String {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager ?: return "UNKNOWN"

            val activeNetworkInfo: NetworkInfo?
            activeNetworkInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork
                connectivityManager.getNetworkInfo(network)
            } else {
                connectivityManager.activeNetworkInfo
            }

            return if (activeNetworkInfo != null) {
                activeNetworkInfo.extraInfo
            } else "UNKNOWN"

        }

        @JvmStatic
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        fun getNetworkInfo(context: Context): NetworkInfo? {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager ?: return null

            val activeNetworkInfo: NetworkInfo?
            activeNetworkInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork
                connectivityManager.getNetworkInfo(network)
            } else {
                connectivityManager.activeNetworkInfo
            }

            return activeNetworkInfo
        }
    }
}