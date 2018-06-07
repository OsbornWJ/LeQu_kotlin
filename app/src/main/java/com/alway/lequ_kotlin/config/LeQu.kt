package com.alway.lequ_kotlin.config

import android.content.Context
import android.os.Handler

/**
 * 创建人：wenjie on 2018/6/6
 * 邮箱： Osbornjie@163.com
 * 功能：
 */

object LeQu {

    val lequConfig: LeQuConfig
        get() = LeQuConfig.instance

    val applicationContext: Context
        get() = getConfiguration(ConfigKeys.APPLICATION_CONTEXT)

    val handler: Handler
        get() = getConfiguration(ConfigKeys.HANDLER)

    fun <T> getConfiguration(key: Any): T {
        return lequConfig.getConfiguration(key)
    }

    fun init(context: Context): LeQuConfig {
        LeQuConfig.instance
                .latteConfigs
                .put(ConfigKeys.APPLICATION_CONTEXT, context.applicationContext)
        return LeQuConfig.instance
    }

}