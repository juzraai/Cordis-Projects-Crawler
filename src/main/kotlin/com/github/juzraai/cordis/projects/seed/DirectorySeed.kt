package com.github.juzraai.cordis.projects.seed

import com.github.juzraai.cordis.projects.cli.*

/**
 * @author Zsolt Jurányi
 */
class DirectorySeed : IRcnSeedGenerator {

	override fun generateRcns(scope: String?, configuration: CpcConfiguration): Sequence<Long>? {
		if ("dir".equals(scope, true)) {
			// TODO fetch RCNs from CORDIS cache dir
		}
		return null
	}
}