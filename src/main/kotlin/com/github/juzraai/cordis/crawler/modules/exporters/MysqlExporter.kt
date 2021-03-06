package com.github.juzraai.cordis.crawler.modules.exporters

import com.github.juzraai.cordis.crawler.model.*
import com.github.juzraai.cordis.crawler.modules.*
import com.github.juzraai.cordis.crawler.util.*
import java.util.*
import java.util.regex.*

/**
 * @author Zsolt Jurányi
 */
class MysqlExporter : ICordisCrawlerRecordExporter {

	private var db: Database? = null

	override fun initialize(configuration: CordisCrawlerConfiguration, modules: CordisCrawlerModuleRegistry) {
		val connection = configuration.mysqlExport ?: return

		// user@host:port/schema
		val m = Pattern.compile("(?<u>[^@]+)@(?<h>[^:/]+)(:(?<p>\\d+))?/(?<s>.*)").matcher(connection)
		if (m.find()) {
			db = Database(
					m.group("h"),
					m.group("p")?.toInt() ?: 3306,
					m.group("s"),
					m.group("u"),
					configuration.password
			)
			createTables()
		} else throw IllegalArgumentException("Invalid database connection parameters.")
	}

	override fun export(cordisCrawlerRecords: List<CordisCrawlerRecord>) {
		db?.also { MysqlExportSession(it, cordisCrawlerRecords).call() }
	}

	private fun createTables() {
		db?.also {
			javaClass.classLoader.getResourceAsStream("mysql_create_tables.sql").use { stream ->
				Scanner(stream).useDelimiter("\\A").use { scanner ->
					it.executeScript(scanner.next())
				}
			}
		}
	}
}