# Import log of BugsCEP data 

Note the import system requires a JDK installation of Java and the `maven` build tool.

BugsCEP import has currently only been verified to work on Java JDK version 8.

## Import of BugsCEP data from 2023-12.19

1. Download latest version on BugsCEP data.

```bash
wget -O bugsdata/bugsdata_20231219.zip -d 
    --user-agent="Mozilla/5.0 (Windows NT x.y; rv:10.0) Gecko/20100101 Firefox/10.0" \
        https://www.bugscep.com/downloads/bugsdata.zip
unzip -p bugsdata/bugsdata_20231219.zip bugsdata.mdb > bugsdata/bugsdata_20231219.mdb
```

2. Create target (temporary) database for BugsCEP import.:

```bash
bin/copy-database --source sead_staging_202212 --target sead_staging_bugs --force --sync-sequence
```

3. Configure bugs-import:

Change database to `sead_staging_bugs` in file `config/application.properties`.

4. Build the BugsCEP import application:
   
```bash
mvn -Dmaven.test.skip=true clean
mvn -Dmaven.test.skip=true package 
```

4. Run BugsCEP import system:

```bash
nohup java -jar target/bugs.import-0.1-SNAPSHOT.jar --file=./bugsdata/bugsdata_20231219.mdb
```

