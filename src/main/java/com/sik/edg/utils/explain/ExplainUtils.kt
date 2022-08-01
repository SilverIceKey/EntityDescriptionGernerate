package com.sik.edg.utils.explain

import com.alibaba.fastjson.JSONObject

object ExplainUtils {
    /**
     * 根据属性获取类里的介绍说明
     */
    fun <T> getExplainValueWithKey(clazz: Class<T>, key: String): String {
        for (declaredField in clazz.declaredFields) {
            if (declaredField.name == key) {
                val anno = declaredField.getAnnotation(Explain::class.java)
                if (anno != null) {
                    return anno.explainValue
                }
            }
        }
        return "未知"
    }

    fun <T> getClassEntity(clazz: Class<T>): JSONObject {
        val descriptionJson = JSONObject()
        descriptionJson["title"] = getClassTitle(clazz)
        descriptionJson["description"] = getClassDescription(clazz)
        descriptionJson["type"] = "object"
        descriptionJson["properties"] = getExplainValuesToJson(clazz)
        descriptionJson["required"] = getRequiredField(clazz)
        return descriptionJson
    }

    /**
     * 获取class的介绍说明
     */
    fun <T> getClassDescription(clazz: Class<T>): String {
        var explain: Explain = clazz.getAnnotation(Explain::class.java)
        return explain.explainValue
    }

    /**
     * 获取class标题
     */
    fun <T> getClassTitle(clazz: Class<T>): String {
        var explain: Title = clazz.getAnnotation(Title::class.java)
        return explain.titleValue
    }

    /**
     * 获取必要属性
     */
    fun <T> getRequiredField(clazz: Class<T>): String {
        val requiredFields = arrayListOf<String>()
        for (declaredField in clazz.declaredFields) {
            if (declaredField.getAnnotation(Required::class.java) != null) {
                requiredFields.add(declaredField.name)
            }
        }
        return requiredFields.toString()
    }

    /**
     * 获取类里所有介绍说明
     */
    fun <T> getExplainValues(clazz: Class<T>): Map<String, String> {
        val explainValues = mutableMapOf<String, String>()
        for (declaredField in clazz.declaredFields) {
            explainValues[declaredField.name] = declaredField.getAnnotation(Explain::class.java)?.explainValue ?: "未知"
        }
        return explainValues
    }

    /**
     * 获取类里所有介绍说明
     */
    fun <T> getExplainValuesToJson(clazz: Class<T>): JSONObject {
        val properties = JSONObject(true)
        for (declaredField in clazz.declaredFields) {
            val fieldDescription = JSONObject(true)
            val explain = declaredField.getAnnotation(Explain::class.java) ?: continue
            fieldDescription["type"] = toLowFirst(declaredField.type.simpleName)
            fieldDescription["description"] = explain.explainValue
            if (!isBaseType(fieldDescription["type"].toString())){
                properties[declaredField.name] = getClassEntity(declaredField.type)
            }else{
                properties[declaredField.name] = fieldDescription
            }
        }
        return properties
    }

    /**
     * 首字母转小写
     */
    private fun toLowFirst(str: String): String {
        val chars = str.toCharArray()
        chars[0] = chars[0].toLowerCase()
        return String(chars)
    }

    /**
     * 是否是基础类型
     */
    private fun isBaseType(type: String): Boolean {
        var isBaseType = true
        val lowCaseType = type.toLowerCase()
        if (lowCaseType != "string" &&
            lowCaseType != "integer" &&
            lowCaseType != "float" &&
            lowCaseType != "double" &&
            lowCaseType != "long" &&
            lowCaseType != "short" &&
            lowCaseType != "boolean" &&
            lowCaseType != "localdatetime" &&
            lowCaseType != "byte"
        ) {
            isBaseType = false
        }
        return isBaseType
    }
}