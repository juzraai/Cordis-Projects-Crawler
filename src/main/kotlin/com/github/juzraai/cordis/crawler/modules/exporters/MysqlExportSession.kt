package com.github.juzraai.cordis.crawler.modules.exporters

import com.github.juzraai.cordis.crawler.model.*
import com.github.juzraai.cordis.crawler.model.cordis.*
import com.github.juzraai.cordis.crawler.model.mysql.*
import com.github.juzraai.cordis.crawler.util.*
import mu.*
import java.util.concurrent.*

/**
 * @author Zsolt Jurányi
 */
class MysqlExportSession(private val db: Database, private val cordisCrawlerRecords: List<CordisCrawlerRecord>) :
		Callable<Unit> {

	companion object : KLogging() {
		private val converter = MySqlRecordConverter()
	}

	private val data = mutableMapOf<String, MutableCollection<ArrayRecord>>()

	override fun call() {
		cordisCrawlerRecords.forEach { cordisProject ->
			cordisProject.project?.also {
				processProject(it)
				processProjectPublications(cordisProject)
			}
		}
		data.forEach { table, records -> db.batchReplace(table, records.map(ArrayRecord::array)) }
	}

	private fun addData(table: String, record: ArrayRecord?) {
		if (null != record) fetchTable(table).add(record)
	}

	private fun addData(table: String, records: Collection<ArrayRecord>) {
		fetchTable(table).addAll(records)
	}

	private fun addRecordsAndRelations(table: String, ownerId: String, ownerType: String, records: Collection<Any>?, relationType: String? = null) {
		if (null != records) {
			addData(table, records.mapNotNull(converter::anyToArray))
			addRelations(ownerId, ownerType, records, relationType)
		}
	}

	private fun addRelations(ownerId: String, ownerType: String, records: Collection<Any>?, relationType: String? = null) {
		if (null != records) {
			addData("cordis_relation", records.map {
				converter.generateRelationArray(ownerId, ownerType, it, relationType)
			})
		}
	}

	private fun fetchTable(table: String): MutableCollection<ArrayRecord> {
		if (!data.containsKey(table)) data[table] = mutableSetOf()
		return data[table]!!
	}

	private fun processProgramme(programme: Programme) {
		addData("cordis_programme", converter.anyToArray(programme))
		programme.relations?.also { processRelations(programme.rcn!!.toString(), "Programme", it) }
		programme.parent?.also { processProgramme(it) }
	}

	private fun processProject(project: Project) {
		addData("cordis_project", converter.anyToArray(project))
		project.relations?.also { processRelations(project.rcn!!.toString(), "Project", it) }
	}

	private fun processProjectPublications(cordisCrawlerRecord: CordisCrawlerRecord) {
		cordisCrawlerRecord.publications?.filter { null != it.openAireId }?.forEach { p ->
			addRecordsAndRelations("cordis_publication", cordisCrawlerRecord.project!!.rcn!!.toString(), "Project", listOf(p), "relatedPublication")
			addRecordsAndRelations("cordis_string", p.openAireId!!, "Publication", p.authors, "author")
			addRecordsAndRelations("cordis_string", p.openAireId!!, "Publication", p.sourceJournals, "sourceJournal")
			addRecordsAndRelations("cordis_string", p.openAireId!!, "Publication", p.webResources, "webResource")
		}
	}

	private fun processRelations(ownerId: String, ownerType: String, relations: Relations) {
		addRecordsAndRelations("cordis_category", ownerId, ownerType, relations.categories?.filter { null != it.code })
		addRecordsAndRelations("cordis_region", ownerId, ownerType, relations.regions?.filter { null != it.rcn })
		relations.associations?.also {
			addRecordsAndRelations("cordis_call", ownerId, ownerType, it.calls?.filter { null != it.rcn })
			addRecordsAndRelations("cordis_person", ownerId, ownerType, it.persons?.filter { null != it.rcn })
			addRecordsAndRelations("cordis_webitem", ownerId, ownerType, it.webItems)

			it.organizations?.filter { null != it.rcn }?.forEach { o ->
				addRecordsAndRelations("cordis_organization", ownerId, ownerType, listOf(o))
				o.relations?.also { r -> processRelations(o.rcn!!.toString(), "Organization", r) }
			}

			it.programmes?.filter { null != it.rcn }?.forEach { p ->
				addRelations(ownerId, ownerType, listOf(p))
				processProgramme(p)
			}

			// only relations, because these are not full records
			// full records will be fetched with crawl
			addRelations(ownerId, ownerType, it.projects?.filter { null != it.rcn })
			addRelations(ownerId, ownerType, it.results?.filter { null != it.rcn })
		}
	}


}