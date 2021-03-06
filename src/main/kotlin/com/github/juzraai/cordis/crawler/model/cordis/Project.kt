package com.github.juzraai.cordis.crawler.model.cordis

import org.simpleframework.xml.*
import java.util.*

/**
 * @author Zsolt Jurányi
 */
@Root(name = "project")
@Default(required = false)
data class Project(
		@field:Attribute(name = "type", required = false)
		var type: String? = null,

		var acronym: String? = null,
		var availableLanguages: String? = null,
		var contentCreationDate: Date? = null,
		var contentUpdateDate: Date? = null,
		var contract: Contract? = null,
		var ecMaxContribution: Double? = null,
		var endDate: Date? = null,
		var language: String? = null,
		var lastUpdateDate: Date? = null,
		var objective: String? = null,
		var rcn: Long? = null,
		var reference: String? = null,
		var relations: Relations? = null,
		var sourceUpdateDate: Date? = null,
		var startDate: Date? = null,
		var status: String? = null,
		var statusDetails: String? = null,
		var teaser: String? = null,
		var title: String? = null,
		var totalCost: Double? = null
)