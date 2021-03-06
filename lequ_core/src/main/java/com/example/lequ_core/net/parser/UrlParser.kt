@file:Suppress("PackageName")

package com.example.lequ_core.net.parser

import com.example.lequ_core.net.RestCretor
import okhttp3.HttpUrl

/**
 * ================================================
 * Url解析器
 *
 *
 * Created by JessYan on 17/07/2017 17:44  采用jessYan动态url更替方案
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */

interface UrlParser {

    /**
     * 这里可以做一些初始化操作
     */
    fun init(restCretor: RestCretor) : UrlParser

    /**
     * @param domainUrl 用于替换的 URL 地址
     * @param url       旧 URL 地址
     * @return
     */
    fun parseUrl(domainUrl: HttpUrl?, url: HttpUrl): HttpUrl
}
