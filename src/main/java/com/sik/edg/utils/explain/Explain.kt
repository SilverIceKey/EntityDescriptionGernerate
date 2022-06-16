package com.sik.edg.utils.explain

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS,AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
/**
 * 属性介绍说明
 */
annotation class Explain(val explainValue:String = "未知")
