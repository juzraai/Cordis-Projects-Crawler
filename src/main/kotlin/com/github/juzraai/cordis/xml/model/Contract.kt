package com.github.juzraai.cordis.xml.model

import org.simpleframework.xml.*
import java.util.*

/**
 * @author Zsolt Jurányi
 */
@Default(required = false)
data class Contract(
		var duration: Int? = null,
		var endDate: Date? = null,
		var startDate: Date? = null
)