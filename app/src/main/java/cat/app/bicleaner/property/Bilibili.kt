package cat.app.bicleaner.property

import android.annotation.SuppressLint
import android.app.AndroidAppHelper
import android.content.Context
import cat.app.bicleaner.BuildConfig
import cat.app.bicleaner.utils.DexHelper
import cat.app.bicleaner.utils.Log
import cat.app.bicleaner.utils.Weak
import cat.app.bicleaner.utils.allClassesList
import cat.app.bicleaner.utils.findClass
import cat.app.bicleaner.utils.findDexClassLoader
import cat.app.bicleaner.utils.findFirstFieldByExactTypeOrNull
import cat.app.bicleaner.utils.from
import cat.app.bicleaner.utils.getObjectFieldOrNull
import cat.app.bicleaner.utils.getVersionCode
import cat.app.bicleaner.utils.runCatchingOrNull
import dalvik.system.BaseDexClassLoader
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import kotlin.concurrent.Volatile
import kotlin.math.max
import kotlin.system.measureTimeMillis
import kotlin.time.measureTimedValue

class Bilibili(private val mClassLoader: ClassLoader, private val mContext: Context) {
    init {
        instance = this
    }

    private val hookInfo: HookInfo = run {
        val (result, time) = measureTimedValue { readHookInfo(mContext) }
        Log.d("load hookinfo $time")
        Log.d(result)
        result
    }

    val mainActivityClass by Weak { "tv.danmaku.bili.MainActivityV2" from mClassLoader }
    val splashActivityClass by Weak {
        ("tv.danmaku.bili.ui.splash.SplashActivity" from mClassLoader)
            ?: ("tv.danmaku.bili.MainActivityV2" from mClassLoader)
    }
    val menuGroupItemClass by Weak { hookInfo.menuGroupItemClassName from mClassLoader }
    val settingRouterClass by Weak { hookInfo.settingRouterClassName from mClassLoader }
    val drawerClass by Weak { hookInfo.drawerClassName from mClassLoader }
    fun homeCenters() = hookInfo.homeUserCenterList.map {
        it.first from mClassLoader to it.second
    }

    val ids: Map<String, Int> by lazy {
        hookInfo.ids
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun readHookInfo(context: Context): HookInfo {
        try {
            val hookInfoFile = File(context.cacheDir, Constant.HOOK_INFO_JSON)
            Log.d("Reading hook info: $hookInfoFile")
            val t = measureTimeMillis {
                if (hookInfoFile.isFile && hookInfoFile.canRead()) {
                    val lastUpdateTime = context.packageManager.getPackageInfo(
                        AndroidAppHelper.currentPackageName(),
                        0
                    ).lastUpdateTime
                    val lastModuleUpdateTime = try {
                        context.packageManager.getPackageInfo(BuildConfig.APPLICATION_ID, 0)
                    } catch (e: Throwable) {
                        null
                    }?.lastUpdateTime ?: 0
                    val info = FileInputStream(hookInfoFile).use {
                        Json.decodeFromStream<HookInfo>(it)
                    }
                    if (info.lastUpdateTime >= lastUpdateTime && info.lastUpdateTime >= lastModuleUpdateTime
                        && getVersionCode(context.packageName) == info.clientVersionCode
                        && BuildConfig.VERSION_CODE == info.moduleVersionCode
                        && BuildConfig.VERSION_NAME == info.moduleVersionName
                    ) return info
                }
            }
            Log.d("Read hook info completed: take $t ms")
        } catch (e: Throwable) {
            Log.w(e)
        }
        return initHookInfo(context).also {
            try {
                val hookInfoFile = File(context.cacheDir, Constant.HOOK_INFO_JSON)
                if (hookInfoFile.exists()) hookInfoFile.delete()
                FileOutputStream(hookInfoFile).use { o -> Json.encodeToStream(it, o) }
            } catch (e: Exception) {
                Log.e(e)
            }
        }
    }

    @Serializable
    data class HookInfo(
        val lastUpdateTime: Long,
        val clientVersionCode: Int,
        val moduleVersionCode: Int,
        val moduleVersionName: String,
        val ids: Map<String, Int>,
        val menuGroupItemClassName: String,
        val settingRouterClassName: String,
        val drawerClassName: String,
        val homeUserCenterList: List<Pair<String, String>>
    )

    companion object {
        @JvmStatic
        fun emptyHookInfo(): HookInfo {
            return HookInfo(0, 0, 0, "", mapOf(), "", "", "", listOf())
        }

        @JvmStatic
        fun findRealClassloader(classloader: BaseDexClassLoader): BaseDexClassLoader {
            val serviceField = classloader.javaClass.findFirstFieldByExactTypeOrNull(
                "com.bilibili.lib.tribe.core.internal.loader.DefaultClassLoaderService" from classloader
            )
            val delegateField = classloader.javaClass.findFirstFieldByExactTypeOrNull(
                "com.bilibili.lib.tribe.core.internal.loader.TribeLoaderDelegate" from classloader
            )
            return if (serviceField != null) {
                serviceField.type.declaredFields.filter { f ->
                    f.type == ClassLoader::class.java
                }.map { f ->
                    classloader.getObjectFieldOrNull(serviceField.name)
                        ?.getObjectFieldOrNull(f.name)
                }.firstOrNull { o ->
                    o?.javaClass?.name?.startsWith("com.bilibili") == false
                } as? BaseDexClassLoader ?: classloader
            } else if (delegateField != null) {
                val loaderField =
                    delegateField.type.findFirstFieldByExactTypeOrNull(ClassLoader::class.java)
                val out = classloader.getObjectFieldOrNull(delegateField.name)
                    ?.getObjectFieldOrNull(loaderField?.name)
                if (BaseDexClassLoader::class.java.isInstance(out)) out as BaseDexClassLoader else classloader
            } else classloader
        }

        @JvmStatic
        fun initHookInfo(context: Context): HookInfo {
            val classloader = context.classLoader
            val classesList =
                context.classLoader.allClassesList(Companion::findRealClassloader).asSequence()
            try {
                System.loadLibrary("bicleaner")
            } catch (e: Throwable) {
                Log.e(e)
                return emptyHookInfo()
            }
            val dexHelper = DexHelper(
                classloader.findDexClassLoader(Companion::findRealClassloader)
                    ?: return emptyHookInfo()
            )

            val mapIds = HashMap<String, Int>()
            val reg = Regex("^tv\\.danmaku\\.bili\\.[^.]*$")
            val mask = Modifier.STATIC or Modifier.PUBLIC or Modifier.FINAL
            classesList.filter {
                it.startsWith("tv.danmaku.bili")
            }.filter {
                it.matches(reg)
            }.flatMap { c ->
                c.findClass(classloader).declaredFields.filter {
                    it.modifiers == mask
                            && it.type == Int::class.javaPrimitiveType
                }
            }.forEach { mapIds[it.name] = it.get(null) as Int }

            // 处理需要的类的类名
            val navigationViewClass =
                "android.support.design.widget.NavigationView" from classloader
            val regex = Regex("^tv\\.danmaku\\.bili\\.ui\\.main2\\.[^.]*$")
            val drawerClass = classesList.filter {
                it.startsWith("tv.danmaku.bili.ui.main2")
            }.filter { it.matches(regex) }.firstOrNull { c ->
                c.findClass(classloader).declaredFields.any {
                    it.type == navigationViewClass
                }
            } ?: ""
            val settingRouterClass = dexHelper.findMethodUsingString(
                "UperHotMineSolution",
                false,
                -1,
                0,
                "V",
                -1,
                null,
                null,
                null,
                true
            ).asSequence().firstNotNullOfOrNull {
                dexHelper.decodeMethodIndex(it)
            }?.declaringClass?.interfaces?.firstOrNull()?.let {
                dexHelper.encodeClassIndex(it)
            }?.let {
                dexHelper.findField(it, null, true).asSequence().firstNotNullOfOrNull { f ->
                    dexHelper.decodeFieldIndex(f)
                }?.declaringClass
            }?.name ?: ""
            val menuGroupItemClass = "com.bilibili.lib.homepage.mine.MenuGroup\$Item"
            val homeCenterList = ArrayList<Pair<String, String>>()
            val contextIndex = dexHelper.encodeClassIndex(Context::class.java)
            val listIndex = dexHelper.encodeClassIndex(List::class.java)
            dexHelper.findMethodUsingString(
                "main.my-information.noportrait.0.show",
                false,
                -1,
                -1,
                null,
                -1,
                null,
                null,
                null,
                false
            ).asSequence().mapNotNull { dexHelper.decodeMethodIndex(it)?.declaringClass }
                .forEach { homeUserCenterClass ->
                    val homeUserCenterIndex = dexHelper.encodeClassIndex(homeUserCenterClass)
                    val addSettingMethod = dexHelper.findMethodUsingString(
                        "bilibili://main/scan",
                        true,
                        -1,
                        -1,
                        null,
                        homeUserCenterIndex,
                        null,
                        longArrayOf(contextIndex),
                        null,
                        false
                    ).asSequence().mapNotNull {
                        dexHelper.decodeMethodIndex(it) as? Method
                    }.firstOrNull {
                        it.parameterTypes.size == 2 &&
                                it.parameterTypes[1] != List::class.java
                    } ?: dexHelper.findMethodUsingString(
                        "activity://main/preference",
                        true,
                        -1,
                        -1,
                        null,
                        homeUserCenterIndex,
                        null,
                        longArrayOf(contextIndex, listIndex),
                        null,
                        true
                    ).asSequence().firstNotNullOfOrNull {
                        dexHelper.decodeMethodIndex(it)
                    } ?: dexHelper.findMethodUsingString(
                        "bilibili://main/preference",
                        true,
                        -1,
                        -1,
                        null,
                        homeUserCenterIndex,
                        null,
                        longArrayOf(contextIndex, listIndex),
                        null,
                        true
                    ).asSequence().firstNotNullOfOrNull {
                        dexHelper.decodeMethodIndex(it)
                    }
                    homeCenterList += homeUserCenterClass.name to (addSettingMethod?.name ?: "")
                }

            dexHelper.close()
            return HookInfo(
                lastUpdateTime = max(
                    context.packageManager.getPackageInfo(
                        AndroidAppHelper.currentPackageName(),
                        0
                    ).lastUpdateTime,
                    runCatchingOrNull {
                        context.packageManager.getPackageInfo(
                            BuildConfig.APPLICATION_ID,
                            0
                        )
                    }?.lastUpdateTime ?: 0
                ),
                clientVersionCode = getVersionCode(context.packageName),
                moduleVersionCode = BuildConfig.VERSION_CODE,
                moduleVersionName = BuildConfig.VERSION_NAME,
                ids = mapIds,
                drawerClassName = drawerClass,
                settingRouterClassName = settingRouterClass,
                menuGroupItemClassName = menuGroupItemClass,
                homeUserCenterList = homeCenterList
            )
        }

        @SuppressLint("StaticFieldLeak")
        @Volatile
        lateinit var instance: Bilibili
    }
}