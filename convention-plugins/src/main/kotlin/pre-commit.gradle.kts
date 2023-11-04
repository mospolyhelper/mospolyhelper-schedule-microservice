import org.apache.tools.ant.taskdefs.condition.Os
import java.io.File

task<Copy>("installGitHook") {
    val prefix = "pre-push"
    group = "git hooks"
    val suffix = if (Os.isFamily(Os.FAMILY_WINDOWS)) "windows" else "macos"
    val fromFile = File(rootProject.rootDir, "scripts/$prefix-$suffix")
    val intoFile = File(rootProject.rootDir, ".git/hooks")
    from(fromFile)
    into(intoFile)
    rename("$prefix-$suffix", prefix)
    fileMode = 775
}
