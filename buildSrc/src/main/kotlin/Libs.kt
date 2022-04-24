object Libs {
    object KotlinX {
        const val serializationJson = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinxSerialization}"
        const val serializationCbor = "org.jetbrains.kotlinx:kotlinx-serialization-cbor:${Versions.kotlinxSerialization}"

        object Coroutines {
            private const val version = "1.5.2"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
            const val jvm = "org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:${Versions.kotlinCoroutines}"
            const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinCoroutines}"
        }
    }


    object Other {
        const val jdkDesugar = "com.android.tools:desugar_jdk_libs:1.1.5"
        const val junit = "junit:junit:4.13"
        const val ktorUtils = "io.ktor:ktor-utils:2.0.0-beta-1"
    }


    object Di {
        const val koinCore = "io.insert-koin:koin-core:${Versions.koin}"
        const val koinAndroid = "io.insert-koin:koin-android:${Versions.koin}"
        const val koinCompose = "io.insert-koin:koin-androidx-compose:${Versions.koin}"
    }
}

object Versions {
    const val kotlinCoroutines = "1.6.1"
    const val kotlinxSerialization = "1.3.2"

    // Di
    const val koin = "3.1.6"

}
