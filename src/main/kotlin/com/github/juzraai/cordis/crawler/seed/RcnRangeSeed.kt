package com.github.juzraai.cordis.crawler.seed

import com.github.juzraai.cordis.crawler.*

/**
 * @author Zsolt Jurányi
 */
class RcnRangeSeed : IRcnSeedGenerator {

	override fun generateRcns(scope: String?, configuration: CordisCrawlerConfiguration): Sequence<Long>? {
		return if (null != scope && scope.matches(Regex("\\d+\\.\\.\\d+"))) {
			val bounds = scope.split(Regex("\\.\\.")).map(String::toLong).sorted()
			LongRange(bounds[0], bounds[1]).asSequence()
		} else null
	}
}