import org.jetbrains.kotlin.ir.backend.js.compile

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.needtodo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.needtodo"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

}

dependencies {

    implementation ("androidx.core:core-ktx:1.7.0")
    implementation ("com.tbruyelle.rxpermissions2:rxpermissions:0.9.4@aar")
    implementation ("io.reactivex.rxjava2:rxandroid:2.0.2")
    implementation ("io.reactivex.rxjava2:rxjava:2.0.0")
    implementation ("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation ("com.google.android.material:material:<version>")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    implementation ("com.google.android.material:material:1.2.0")
    implementation ("androidx.recyclerview:recyclerview:1.1.0")
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.0.0")
    implementation ("de.hdodenhof:circleimageview:2.1.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.annotation:annotation:1.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("org.litepal.guolindev:core:3.2.3")
    debugImplementation("com.guolindev.glance:glance:1.1.0")
    constraints {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.0") {
            because("kotlin-stdlib-jdk7 is now a part of kotlin-stdlib")
        }
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0") {
            because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
        }
    }
    implementation("io.github.scwang90:refresh-layout-kernel:2.0.5")
    implementation("io.github.scwang90:refresh-header-classics:2.0.5")
    implementation("io.github.scwang90:refresh-header-radar:2.0.5")
    implementation("io.github.scwang90:refresh-header-falsify:2.0.5")
    implementation("io.github.scwang90:refresh-header-material:2.0.5")
    implementation("io.github.scwang90:refresh-header-two-level:2.0.5")
    implementation("io.github.scwang90:refresh-footer-ball:2.0.5")
    implementation("io.github.scwang90:refresh-footer-classics:2.0.5")
    implementation("org.greenrobot:eventbus:3.2.0")
}