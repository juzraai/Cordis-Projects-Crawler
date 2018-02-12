package com.github.juzraai.cordis.crawler.model

import com.github.juzraai.cordis.crawler.model.cordis.*
import com.github.juzraai.cordis.crawler.model.openaire.sygma.*

/**
 * @author Zsolt Jurányi
 */
data class CordisProject(
		val rcn: Long,
		var project: Project? = null,
		//val result: Result? = null,
		var publications: List<Publication>? = null
)