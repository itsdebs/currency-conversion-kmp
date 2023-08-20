
buildscript{
    dependencies{
//        classpath("dev.icerock.moko:resources-generator:0.23.0")

        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20")
        classpath("com.squareup.sqldelight:gradle-plugin:1.5.5")
    }
}
plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").apply(false)
    id("com.android.library").apply(false)
    id("org.jetbrains.compose").apply(false)
    id("org.jetbrains.kotlin.jvm").apply(false)
    kotlin("android").apply(false)
    kotlin("multiplatform").apply(false)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

