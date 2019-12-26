package com.tb.mvvm_library.util

import android.content.Context

import java.io.File
import java.lang.reflect.Array
import java.lang.reflect.Field

import dalvik.system.DexClassLoader
import dalvik.system.PathClassLoader
/**
*@作者：tb
*@时间：2019/12/25
*@描述：热更新工具类
*/
object HotFixUtil {
    /**
     * 修复指定的类
     *
     * @param context        上下文对象
     * @param fixDexFilePath   修复的dex文件路径
     */
    fun fixDexFile(context: Context, fixDexFilePath: String?) {
        if (fixDexFilePath != null && File(fixDexFilePath).exists()) {
            try {
                injectDexToClassLoader(context, fixDexFilePath)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    /**
     * @param context
     * @param fixDexFilePath 修复文件的路径
     * @throws ClassNotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Throws(
        ClassNotFoundException::class,
        NoSuchFieldException::class,
        IllegalAccessException::class
    )
    private fun injectDexToClassLoader(context: Context, fixDexFilePath: String) {
        //读取 baseElements
        val pathClassLoader = context.classLoader as PathClassLoader
        val basePathList = getPathList(pathClassLoader)
        val baseElements = getDexElements(basePathList)

        //读取 fixElements
        val baseDexAbsolutePath = context.getDir("dex", 0).absolutePath
        val fixDexClassLoader = DexClassLoader(
            fixDexFilePath, baseDexAbsolutePath, fixDexFilePath, context.classLoader
        )
        val fixPathList = getPathList(fixDexClassLoader)
        val fixElements = getDexElements(fixPathList)

        //合并两份Elements
        val newElements = combineArray(baseElements, fixElements!!)

        //一定要重新获取，不要用basePathList，会报错
        val basePathList2 = getPathList(pathClassLoader)

        //新的dexElements对象重新设置回去
        setField(basePathList2, basePathList2!!.javaClass, "dexElements", newElements)
    }

    /**
     * 通过反射先获取到pathList对象
     *
     * @param obj
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Throws(
        ClassNotFoundException::class,
        NoSuchFieldException::class,
        IllegalAccessException::class
    )
    private fun getPathList(obj: Any): Any? {
        return getField(obj, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList")
    }

    /**
     * 从上面获取到的PathList对象中，进一步反射获得dexElements对象
     *
     * @param obj
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    private fun getDexElements(obj: Any?): Any? {
        return getField(obj, obj!!.javaClass, "dexElements")
    }

    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    private fun getField(obj: Any?, cls: Class<*>, str: String): Any? {
        val declaredField = cls.getDeclaredField(str)
        declaredField.isAccessible = true//设置为可访问
        return declaredField.get(obj)
    }

    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    private fun setField(obj: Any?, cls: Class<*>, str: String, obj2: Any) {
        val declaredField = cls.getDeclaredField(str)
        declaredField.isAccessible = true//设置为可访问
        declaredField.set(obj, obj2)
    }

    /**
     * 合拼dexElements ,并确保 fixElements 在 baseElements 之前
     *
     * @param baseElements
     * @param fixElements
     * @return
     */
    private fun combineArray(baseElements: Any?, fixElements: Any): Any {
        val componentType = fixElements.javaClass.componentType
        val length = Array.getLength(fixElements)
        val length2 = Array.getLength(baseElements!!) + length
        val newInstance = Array.newInstance(componentType!!, length2)
        for (i in 0 until length2) {
            if (i < length) {
                Array.set(newInstance, i, Array.get(fixElements, i))
            } else {
                Array.set(newInstance, i, Array.get(baseElements, i - length))
            }
        }
        return newInstance
    }
}
