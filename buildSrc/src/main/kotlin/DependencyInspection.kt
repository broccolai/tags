import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder


fun inspectDependenciesForProject(project: Project) {
    val inspectConfig = createInspectionConfiguration(project)
    val files = resolveDependencies(inspectConfig, project) ?: return

    if (files.isEmpty()) {
        println("No dependencies found for project ${project.name}.")
        return
    }

    files.forEach { file ->
        inspectClassFilesInJar(file, project)
    }
}

fun createInspectionConfiguration(project: Project): Configuration {
    return project.configurations.create("inspectConfig").apply {
        extendsFrom(project.configurations.findByName("implementation"))
        isCanBeResolved = true
    }
}

fun resolveDependencies(configuration: Configuration, project: Project): Set<File>? {
    return try {
        configuration.resolve()
    } catch (e: Exception) {
        println("Could not resolve dependencies for project ${project.name}.")
        null
    }
}

fun inspectClassFilesInJar(file: File, project: Project) {
    project.zipTree(file).visit {
        if (name.endsWith(".class")) {
            val majorVersion = extractMajorVersionFromClassFile(this.file)

            if (majorVersion == 65) {
                println("""
                    Warning: Class file in ${file.name} ${this.path}
                    uses Java class version $majorVersion (file version 65)
                """.trimIndent())
            }
        }
    }
}

fun extractMajorVersionFromClassFile(classFile: File): Int {
    val classFileBytes = classFile.readBytes()
    val byteBuffer = ByteBuffer.wrap(classFileBytes).order(ByteOrder.BIG_ENDIAN)

    // Skip the first 4 bytes for the magic number, then read the next two for minor and another two for major
    byteBuffer.position(4)
    val minorVersion = byteBuffer.short.toInt()
    return byteBuffer.short.toInt()
}
