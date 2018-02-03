package com.github.juzraai.cordis.xml.io

import com.github.juzraai.cordis.crawler.*
import mu.*
import java.io.*
import java.util.*
import java.util.zip.*

/**
 * @author Zsolt Jurányi
 */
class CordisXmlFileCache : ICordisXmlCache {

	companion object : KLogging()

	override fun readCordisXmlByRcn(rcn: Long, configuration: CordisCrawlerConfiguration): String? {
		val file = file(rcn, configuration)
		if (file.exists()) {
			logger.trace("Reading XML: $file")
			try {
				Scanner(GZIPInputStream(file.inputStream())).use {
					return it.useDelimiter("\\A").next()
				}
			} catch (e: Exception) {
				logger.warn("Failed to read XML: $file - ${e.message}")
			}
		}
		return null
	}

	override fun storeCordisXmlForRcn(rcn: Long, xml: String, configuration: CordisCrawlerConfiguration) {
		val file = file(rcn, configuration)
		logger.trace("Storing XML: $file")
		try {
			file.parentFile?.mkdirs()
			OutputStreamWriter(GZIPOutputStream(file.outputStream())).use { it.write(xml) }
		} catch (e: Exception) {
			logger.warn("Failed to store XML: $file - ${e.message}")
		}
	}

	private fun file(rcn: Long, configuration: CordisCrawlerConfiguration) = File(configuration.directory, "$rcn.xml.gz")
}