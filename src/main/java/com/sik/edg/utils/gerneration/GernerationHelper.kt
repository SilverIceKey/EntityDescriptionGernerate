package com.sik.edg.utils.gerneration

import com.alibaba.fastjson.JSONObject
import com.sik.edg.utils.explain.ExplainUtils
import java.io.File
import java.io.OutputStream

/**
 * 生成工具类
 */
object GernerationHelper {
    val JsonFile = ".\\"
    /**
     * 生成实体描述
     */
    @JvmStatic
    fun gernerateDescription(clazz: Class<*>){
        val os = File(JsonFile+clazz.simpleName+".txt").outputStream()
        val descriptionJson = JSONObject()
        descriptionJson["title"] = ExplainUtils.getClassTitle(clazz)
        descriptionJson["description"] = ExplainUtils.getClassDescription(clazz)
        descriptionJson["type"] = "object"
        descriptionJson["properties"] = ExplainUtils.getExplainValuesToJson(clazz)
        descriptionJson["required"] = ExplainUtils.getRequiredField(clazz)
        os.write(descriptionJson.toJSONString().toByteArray())
        os.flush()
        os.close()
    }
}