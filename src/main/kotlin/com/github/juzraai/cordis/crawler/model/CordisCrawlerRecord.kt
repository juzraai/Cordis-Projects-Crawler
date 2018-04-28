package com.github.juzraai.cordis.crawler.model

import com.github.juzraai.cordis.crawler.model.cordis.*
import com.github.juzraai.cordis.crawler.model.openaire.sygma.*

/**
 * @author Zsolt Jurányi
 */
open class CordisCrawlerRecord(
		val rcn: Long,
		var project: Project? = null,
		// TODO [v2.1] val result: Result? = null,
		var publications: List<Publication>? = null
		// TODO [v2.1] unified model
) {
	override fun toString(): String {
		return "CordisCrawlerRecord(rcn=$rcn, project=$project, publications=$publications)"
	}
}