package com.github.juzraai.cordis.crawler.modules.seeds

import com.github.juzraai.cordis.crawler.model.*
import java.io.*

/**
 * @author Zsolt Jurányi
 */
class CordisProjectRcnDirectorySeed : ICordisProjectRcnSeed {

	private var configuration: CordisCrawlerConfiguration? = null

	override fun initialize(configuration: CordisCrawlerConfiguration) {
		this.configuration = configuration
	}

	override fun projectRcns() = if ("dir".equals(configuration?.seed, true)) {
		// TODO using ICordisProjextXmlCache.enumerateCachedRcns would be more elegant
		File(configuration?.directory, "project")
				.listFiles(FileFilter { it.isFile && it.name.matches(Regex("\\d+\\.xml\\.gz")) })
				.map { it.name.replace(Regex("\\D"), "").toLong() }.asSequence()
	} else null
}