import org.apache.tools.ant.taskdefs.condition.Os
import java.io.File

task<Copy>("installGitHook") {
    group = "git hooks"
    val suffix = if (Os.isFamily(Os.FAMILY_WINDOWS)) "windows" else "macos"
    val fromFile = File(rootProject.rootDir, "scripts/pre-commit-$suffix")
    val intoFile = File(rootProject.rootDir, ".git/hooks")
    from(fromFile)
    into(intoFile)
    rename("pre-commit-$suffix", "pre-push")
    fileMode = 775
}
