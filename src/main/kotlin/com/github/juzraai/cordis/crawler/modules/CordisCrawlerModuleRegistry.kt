package com.github.juzraai.cordis.crawler.modules

import com.github.juzraai.cordis.crawler.model.*
import com.github.juzraai.cordis.crawler.modules.parsers.*
import com.github.juzraai.cordis.crawler.modules.processors.*
import com.github.juzraai.cordis.crawler.modules.readers.*
import com.github.juzraai.cordis.crawler.modules.readers.caches.*
import com.github.juzraai.cordis.crawler.modules.seeds.*
import mu.*
import java.io.*

/**
 * @author Zsolt Jurányi
 */
class CordisCrawlerModuleRegistry {

	companion object : KLogging()

	val seeds = mutableListOf(
			CordisProjectRcnSeed(),
			CordisProjectRcnRangeSeed(),
			CordisProjectRcnListSeed(),
			CordisProjectUrlSeed(),
			CordisProjectSearchUrlSeed(),
			CordisProjectRcnDirectorySeed(),
			AllCordisProjectRcnSeed()
	)

	val processors = mutableListOf(
			CordisProjectCrawler(this),
			OpenAirePublicationsCrawler(this)
	)

	val projectXmlReaders = mutableListOf(
			CordisProjectXmlFileCache(),
			CordisProjectXmlDownloader()
	)

	val projectXmlParsers = mutableListOf(
			CordisProjectXmlParser()
	)

	private fun allModules() = listOf(
			seeds,
			processors,
			projectXmlReaders,
			projectXmlParsers
	).flatten()

	fun close() {
		allModules().onEach {
			if (it is Closeable) try {
				logger.trace("Closing module: ${it.javaClass.name}")
				it.close()
			} catch (e: IOException) {
				logger.error("Could not close module: ${it.javaClass.name}", e)
			}
		}
	}

	fun initialize(configuration: CordisCrawlerConfiguration) {
		allModules().onEach {
			logger.trace("Initializing module: ${it.javaClass.name}")
			it.configuration = configuration
		}
	}
}