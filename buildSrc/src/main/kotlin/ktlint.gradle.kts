import kotlin.collections.listOf

configurations.create("ktlint")

fun DependencyHandler.ktlint(dependencyNotation: Any): Dependency? =
    add("ktlint", dependencyNotation)

val NamedDomainObjectContainer<Configuration>.ktlint: NamedDomainObjectProvider<Configuration>
    get() = named<Configuration>("ktlint")

dependencies {
    val ktlintVersion: String by project
    ktlint("com.pinterest:ktlint:$ktlintVersion")
}

task<JavaExec>("ktlintCheck") {
    group = "verification"
    description = "Check Kotlin code style."
    mainClass.set("com.pinterest.ktlint.Main")
    classpath = configurations.ktlint.get()
    args = listOf("src/**/*.kt")
    // to generate report in checkstyle format prepend following args:
    // "--reporter=plain", "--reporter=checkstyle,output=${buildDir}/ktlint.xml"
    // see https://github.com/pinterest/ktlint#usage for more
}

task<JavaExec>("ktlintFormat") {
    group = "formatting"
    description = "Fix Kotlin code style deviations."
    mainClass.set("com.pinterest.ktlint.Main")
    classpath = configurations.ktlint.get()
    args = listOf("-F", "src/**/*.kt")
}
