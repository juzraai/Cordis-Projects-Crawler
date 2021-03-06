package com.github.juzraai.cordis.crawler.util

import mu.*
import org.simpleframework.xml.convert.*
import org.simpleframework.xml.stream.*
import java.text.*
import java.util.*

/**
 * @author Zsolt Jurányi
 */
class DateConverter : Converter<Date> {

	companion object : KLogging()

	val patterns = listOf(
			Pair(Regex("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"), "yyyy-MM-dd HH:mm:ss"),
			Pair(Regex("\\d{4}-\\d{2}-\\d{2}"), "yyyy-MM-dd")
	)

	override fun read(node: InputNode) = try {
		val s = node.value ?: ""
		patterns.asSequence()
				.mapNotNull { parse(s, it.first, it.second) }
				.firstOrNull()
	} catch (e: Exception) {
		logger.warn("Unrecognized date format: ${node.value}")
		null
	}

	override fun write(node: OutputNode, value: Date?) {
		throw UnsupportedOperationException()
	}

	private fun parse(s: String, r: Regex, p: String): Date? {
		if (s.matches(r)) {
			try {
				return SimpleDateFormat(p).parse(s)
			} catch (e: Exception) {
			}
		}
		return null
	}
}