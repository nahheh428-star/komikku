// Yokai design system — a self-contained module.
//
// It is a separate Gradle module on purpose: upstream Komikku never touches this
// directory, so `git merge upstream/master` can never conflict with it, and the
// whole language can be re-synced from MaxManager by replacing one folder.
plugins {
    id("mihon.library")
    id("mihon.library.compose")
    kotlin("android")
}

android {
    namespace = "yokai.design"
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
        )
    }
}

dependencies {
    implementation(compose.foundation)
    implementation(compose.material3.core)
    implementation(compose.animation)
    implementation(compose.runtime)
}
