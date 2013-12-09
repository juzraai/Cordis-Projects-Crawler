CORDIS Projects Crawler
=======================

With this app/API you can fetch information of all [CORDIS projects](http://cordis.europa.eu/projects).

Users can use the binary with CLI, developers can use the classes! :)

####[Download latest release here!](https://github.com/juzraai/Cordis-Projects-Crawler/releases)


##1. Features

* two download mode: download all, or download one by RCN
* can download project data page and publication list JSON string
* detects CORDIS internal error messages
* retries downloads with increasing sleeps
* download files into output directory
* can skip already existing files
* filename templates can be configured
* can read RCNs from already downloaded files' names instead of crawling CORDIS list
* CLI for set up and run crawler
* file logging
* can parse all info from project page
* can parse publication list JSON strings
* can export projects' data to CSV file
* can export all data to MySQL database


##2. Requirements

* JRE 1.6


##3. Usage

###3.1. Users

Download all projects' information to "outputdir" directory:
```
java -jar cordis-crawler-VERSION.jar -a -d outputdir/
```

Download only project with RCN 90433:
```
java -jar cordis-crawler-VERSION.jar -1 90433 -d outputdir/
```

Export already downloaded project files to one CSV:
```
java -jar cordis-crawler-VERSION.jar -a -rd -xcsv myexport.csv
```

To see detailed usage information, run the binary with no arguments.


###3.2. Developers

Download all projects' information to "outputdir" directory:
```java
List<Project> projects = 
	new ProjectDownloader()
		.outputDir("outputdir/")
		.all();
```

Download only project with RCN 90433:
```java
Project project =
	new ProjectDownloader()
		.outputDir("outputdir/")
		.byRCN(90433);
```

All possible options:
```java
	new ProjectDownloader()
		.outputDir("outputdir/")	// sets output directory
		.projectFilename("%d.html")	// sets project data page filename
		.publistFilename("%d.json")	// sets publication list JSON filename
		.readRCNsFromDirectory()	// when you don't want to ask CORDIS for the list
		.skipExisting(false)		// turns on re-downloading
		.all(); // .byRCN(90433);
```

Export projects' data to CSV:
```java
List<Project> projects = new ProjectDownloader().outputDir("outputdir/");
Export2Csv.export(projects, "myexport.csv");
````

Javadoc will come soon! :-)


##4. Future plans, ideas

* automatically create database if not exists using schema.sql
* v2.0 with reorganized classes to increase flexibility
* configurable download settings


##5. Thanks to

Thanks to [ravindraharige](https://github.com/ravindraharige/cordis-crawler) for the XML download URL!