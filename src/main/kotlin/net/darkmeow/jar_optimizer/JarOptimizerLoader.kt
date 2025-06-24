package net.darkmeow.jar_optimizer

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.multiple
import kotlinx.cli.required
import java.io.File

object JarOptimizerLoader {

    @JvmStatic
    fun main(args: Array<String>) {
        val parser = ArgParser("jar-optimizer")

        val input by parser.option(ArgType.String, shortName = "i", description = "Input JAR file").required()
        val output by parser.option(ArgType.String, shortName = "o", description = "Output JAR file").required()
        val keeps by parser.option(ArgType.String, shortName = "k", description = "Classes to keep").multiple()

        parser.parse(args)

        if (keeps.isEmpty()) {
            println("No classes to keep specified")
            return
        }

        JarOptimizer().optimize(File(input), File(output), ObjectOpenHashSet(keeps))
    }

}