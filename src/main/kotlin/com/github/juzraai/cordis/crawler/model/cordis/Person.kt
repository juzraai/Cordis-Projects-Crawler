package com.github.juzraai.cordis.crawler.model.cordis

import org.simpleframework.xml.*

/**
 * @author Zsolt Jurányi
 */
@Default(required = false)
data class Person(
		@field:Attribute
		var context: Boolean? = null,

		@field:Attribute
		var type: String? = null,

		var address: Address? = null,
		var availableLanguages: String? = null,
		var firstNames: String? = null,
		var lastName: String? = null,
		var rcn: Long? = null,
		var title: String? = null
)