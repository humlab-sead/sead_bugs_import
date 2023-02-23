# SEAD Bugs Import

Preview markdown files in a separate window. Markdown is formatted exactly the
same as on GitHub.

[Overview](#overview) | [Prerequisites](#prerequisites) | [Installation](#installation) | [Usage](#usage) | [Command-line options](#options) | [Configuration](#configuration)

## Overview

## Prerequisites

To build and run the application you will need:

- [OpenJDK 1.8](openjdk.org)
- [Maven 3](https://maven.apache.org)
- Spring Boot 1.5.20.RELEASE
- Spring Data JPA
- PostgreSQL Driver
- [Jackcess](https://jackcess.sourceforge.io/) - Java Library for MS Access
- Hibernate
- H2 in-memory database for running test suite

## Installation

```bash
git clone https://github.com/humlab-sead/sead_bugs_import
cd sead_bugs_import
mvn -Dmaven.test.skip=true clean
mvn -Dmaven.test.skip=true package
```

```

docker run --rm -it sead_bugs_import:latest bash

```

## Usage

See wiki [How to perform a Bugs Import](https://github.com/humlab-sead/sead_bugs_import/wiki/How-to-perform-a-Bugs-Import) for a non-technical description of how to import Bugs data into the SEAD database.

> java -jar jar_file --file=mbd-file [--importers=..] [--validate-schema]

```
% wget -d --user-agent="Mozilla/5.0 (Windows NT x.y; rv:10.0) Gecko/20100101 Firefox/10.0"  https://www.bugscep.com/downloads/bugsdata.zip
% unzip /path/to/file.zip -d temp_for_zip_extract
% java -jar target/bugs.import-0.1-SNAPSHOT.jar --file=./data/bugsdata_20190503.mdb --importers=Bibliography,Lab
```

### Options

* `--file=` - _string_ Bugs MS Access database file to import. (**required**)
* `--importers=` - _string_ Comma separated list of named importers to run, where each item is a simple type name (no namespace) without the "Importer" suffix.
* `--validate-schema` - Validate schema (create/update) but do not run.
* `--no-run` - Do not run.

## Steps to perform a bugs import

1. Verify configuration
2. Setup target database (e.g. sead_staging_bugs)
3. Download and unpack latest bugs database from [http://www.bugscep.com](http://www.bugscep.com/downloads.html), [link to file](http://www.bugscep.com/downloads/bugsdata.zip)
4. Run Java application to do an import of downloaded bugs database.

```bash
% java -jar target/bugs.import-0.1-SNAPSHOT.jar --validate-schema
% java -jar target/bugs.import-0.1-SNAPSHOT.jar --file="./data/bugsdata_yyyymmdd.mdb"
```
5. Check flagged events/errors in import lag (ROGER - WHERE EXACTLY?)
6. Apply manual updates to SEAD records which cannot be fixed through corrections in BugsCEP or by altering import routines/mappings (e.g. where SEAD functionality exheeds Bugs)

### Verify configuration

The application uses PostgreSQL as target database in production, and an in-memory database (H2) when running tests.
Please checck that the connection properties defined in `application.properties` is properly set.

```ini
spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://host:port/sead_staging
spring.datasource.username=username
spring.datasource.password=123456

spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=false
spring.jpa.hibernate.ddl-auto=none
# spring.jpa.generate-ddl=true
```

Set xyz to ```update``` if target database schema should be updated to conform to the applications data model. Set to ```none``` if model is correct, or if changes are handled manually outside of code (which is better). (TODO: Add service that at startup, or when given a flag, generates update ddl  similar to using ```update```).

TODO: Move DB specifics to a Spring Boot profile instead (need source code changes @Profile(profile) attribute. (spring.profile.active)

Sample logging configuration:

```
logging.level.org.hibernate.event.internal.EntityCopyAllowedLoggedObserver=debug
logging.level.org.hibernate.type=trace

spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.event.merge.entity_copy_observer=log
```

### Setup target database

The application can, for test purposes, be run on an empty database owned by ```sead_master```:
```
create database sead_staging_bugs_empty owner to sead_master;
```
When run on an empty database the ```spring.jpa.hibernate.ddl-auto``` must be set to ```update``` or ```create```. The application will create the schemas (```bugs_import``` and ```public```) upon startup which is useful if you want to do a schema diff between the SEAD schema to the schema expected by the application.

Normally, though, the import should be incremental by using a previously imported target database as the current target. This feature is not tested!
The three SQL-scripts that reside in the ./sql folder  can be used to setup a new target database based on an existing SEAD database (including data).

|Script|Note|
| ---|---|
| 00_bugs_setup_staging_database.psql|Create a new "sead_staging_bug"s database.|
| 01_bugs_import_schema.psql|Create the ```bugs_import``` schema.|
| 02_setup_bugs_import_value_translations.psql|Populates ```bugs_import``` schema tables|

The commands must be executed as a superuser using e.g. ```psql``` or ```pgAdmin```. You might first need to terminate existing processes in the source and (new) target database.

```bash
#!/bin/bash

REPOSITORY="https://raw.githubusercontent.com/humlab-sead/sead_change_control/master"

source_db="template_bugs_import"
target_db="sead_staging_bugs"

dbhost=`cat ~/.default.sead.server`
dbuser=`cat  ~/.default.sead.username`

dbexec() {
    psql -bqw -h $dbhost -U $dbuser -c "$1" $target_db
}

kickout() {
    xsql="select pg_terminate_backend(pid) from pg_stat_activity where datname = '$1' and pid <> pg_backend_pid();"
    psql -bqw -h $dbhost -U $dbuser -c "$xsql" "postgres"
}

apply_crs() {
    project=$1
    items=$2
    for CR in ${items}; do
        echo "Applying ${project}/${CR}..."
        rm -f ${CR}.sql
        wget -q -nv ${REPOSITORY}/${project}/deploy/${CR}.sql --output-document=${CR}.sql
        psql --echo-errors --quiet -h $dbhost -U $dbuser --no-password --file="${CR}.sql" $target_db
        rm -f ${CR}.sql
    done
}

BugsCRs="\
    CS_BUGS_20190503_SETUP_SCHEMA\
    CS_BUGS_20190503_ADD_TRANSLATIONS"

GeneralCRs="\
    CS_DATINGLAB_20190503_ADD_UNKNOWN_LAB\
    CS_METHOD_20190503_ADD_BUGS_METHODS\
    CS_TAXA_20190503_ADD_SPECIES_ASSOC_TYPES\
    CS_TAXA_20190503_ATTRIBUTE_TYPE_LENGTH"

kickout $target_db
dropdb --echo --if-exists --host=$dbhost --username=$dbuser --no-password "$target_db"

kickout $source_db
createdb --echo --owner=sead_master --host=$dbhost --username=$dbuser --no-password --encoding="UTF8" -T "$source_db" "$target_db"

apply_crs  "bugs" "$BugsCRs"
apply_crs  "general" "$GeneralCRs"

echo "done!"

```

### Download latest bugs database

The latest version of the from Bugs download page e.g. using wget (or a browser):

```
% wget http://www.bugscep.com/downloads/bugsdata.zip
```

Unpack the file into a folder that can be accessed by the bugs import application. The path is specified as an command line argument to the import application.

## Authors

## Development using MS Visual Code

### Main flow
<img src='./doc/main_flow.svg'></img>

### Significant classes
<img src='./doc/significant_classes.svg'></img>

### Import flow
<img src='./doc/import_flow.svg'></img>

### Logging JPA Queries

You can add logging of (beautified) SQL queries to standard out via the following application.properties:

```
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Running tests

The [Java Test Runner](https://code.visualstudio.com/docs/java/java-testing) extension enables running Java tests in  vscode. Note that `Java by Red Hat` and `Debugger for Java` also must be installed and enabled in the current vscode workspace. You can run individual tests by using the CodeLens `Run|Debug` commands above the function in the code. You can also run individual tests by rightclicking nodes in the `Test Explorer's` tree.

Select `Java Test Runner` as output window, or click `Running tests...` on activity bar to see output from test ryns.

Tests are also automatically run from command line when e.g. `mvn package` or `mvn test` is executed.



## Credits

## License

[MIT](https://tldrlegal.com/license/mit-license)

## Issues

* Wrong DDL type generated for `text` columns (needs explicit annotation).
* DDL update drops columns in target table that hasn't been mapped in entity.
* DDL update drops constraints.
*


## Notes

* Use `man mdb-export` etc. to get help on [mdbtoos](https://github.com/mdbtools/mdbtools).
