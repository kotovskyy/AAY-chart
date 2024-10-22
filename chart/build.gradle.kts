plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    id("org.jetbrains.dokka") version "1.5.0"
    id("maven-publish")
}

group = "com.github.kotovskyy"
version = "1.0.0"

kotlin {
    androidTarget {
        publishLibraryVariants("release", "debug")
    }
    jvm("desktop") {
        jvmToolchain(11)
    }
    ios {
        binaries {
            framework {
                baseName = "AAYChart" // Change to your desired framework name
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    js(IR) {
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                    webpackConfig.cssSupport {
                        enabled.set(true)
                    }
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
            }
        }

        val androidMain by getting {
            dependencies {
                api("androidx.appcompat:appcompat:1.5.1")
                api("androidx.core:core-ktx:1.9.0")
            }
        }

        val desktopMain by getting {
            dependencies {
                api(compose.preview)
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by getting {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }

        val jsMain by getting
    }
}

android {
    compileSdkVersion(34)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(34)
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenAndroid") {
            pom {
                name.set("AAY-chart")
                description.set("AAY-chart library for Android.")
                url.set("https://github.com/kotovskyy/AAY-chart")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/kotovskyy/AAY-chart.git")
                    developerConnection.set("scm:git:ssh://git@github.com:kotovskyy/AAY-chart.git")
                    url.set("https://github.com/kotovskyy/AAY-chart")
                }
            }
        }
    }
}