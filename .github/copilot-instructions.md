# SEAD Bugs Import - Copilot Instructions

## Project Overview
This is a Spring Boot 1.5 application that imports data from BugsCEP MS Access databases (.mdb) into a PostgreSQL SEAD database. It uses Jackcess/UCanAccess to read Access files and Spring Data JPA with custom repositories for persistence.

**Critical**: This project uses Java 8 (1.8) - do not suggest modern Java features.

**Note**: Migration to Python is under consideration to align with other SEAD infrastructure. Avoid suggesting major architectural changes or Java version upgrades.

## Architecture

### Core Import Pattern (Template Method)
Every importer follows this 3-component structure:

1. **Importer** (`@Service`) - Orchestrates the import, extends `Importer<BugsType, SeadType>`
2. **BugsSeadMapper** (`@Component`) - Reads from Access DB, converts Bugs → SEAD entities
3. **Persister** (`@Component`) - Saves to PostgreSQL with trace/error logging

**Example**: `BibliographyImporter` → `BibliographyBugsSeadMapper` → `BibliographyPersister`

### Dependency Chain
Importers declare dependencies via constructor: `super(dataMapper, persister, requiredImporter1, requiredImporter2)`. Required importers run first automatically (see [Importer.java](src/main/java/se/sead/bugsimport/Importer.java#L68-L78)).

### Data Flow
```
MS Access (.mdb) → BugsTable → TraceableBugsData → BugsSeadMapper 
→ MappingResult → Persister → PostgreSQL + BugsTrace + BugsError
```

### Tracing System
- Every insert/update logs to `bugs_import.bugs_trace` with SEAD table name + ID
- `@Order` annotated search rules check traces before database lookups (optimization)
- See [TraceEventManager.java](src/main/java/se/sead/bugsimport/tracing/TraceEventManager.java) for trace lifecycle

### Custom Repository Pattern
Uses `CreateAndReadRepository` with custom `saveOrUpdate()` method instead of standard JPA save(). This is configured via `@EnableJpaRepositories(repositoryBaseClass = CreateAndReadRepositoryImpl.class)` in [Application.java](src/main/java/se/sead/Application.java#L25).

## Key Conventions

### Package Structure
- `src/main/java/se/sead/bugsimport/{domain}/` - Each domain has:
  - `{Domain}Importer.java` (extends `Importer`)
  - `{Domain}BugsSeadMapper.java` / `{Domain}Persister.java`
  - `bugsmodel/` - Access DB entities (extends `TraceableBugsData`)
  - `seadmodel/` - PostgreSQL entities (extends `LoggableEntity`, JPA annotated)
  - `search/` - Search rules with `@Order(n)` for priority

### Naming Rules
- Bugs entities: `Bugs{Name}` (e.g., `BugsBiblio`, `BugsSite`)
- SEAD entities: Standard names (e.g., `Biblio`, `SeadSite`)
- Spring services: `{Domain}Importer` with `@Service`
- Components: `{Domain}BugsSeadMapper`, `{Domain}Persister` with `@Component`

### Testing Pattern
Tests use `@SpringBootTest` with `@ActiveProfiles("test")` pointing to H2 in-memory database. See [application-test.properties](src/test/resources/application-test.properties). Test data lives in `src/test/resources/{domain}/`.

## Build & Run Workflow

### Build Commands
```bash
# Clean build (skip tests for speed)
mvn -Dmaven.test.skip=true clean package

# Full build with tests
mvn clean package

# Or use Makefile shortcuts
make rebuild        # Skip tests
make rebuild-all    # With tests
```

### Running Import
```bash
# Basic import
java -jar target/bugs.import-0.1-SNAPSHOT.jar --file=./bugsdata/bugsdata_YYYYMMDD.mdb

# Selective importers (comma-separated, no "Importer" suffix)
java -jar target/bugs.import-0.1-SNAPSHOT.jar \
  --file=./bugsdata/bugsdata_20231219.mdb \
  --importers=Bibliography,Lab,Site

# Schema validation only
java -jar target/bugs.import-0.1-SNAPSHOT.jar --validate-schema
```

**Importer names**: Use simple class name without "Importer" suffix (e.g., `Bibliography` not `BibliographyImporter`). See [ApplicationArgumentManager.java](src/main/java/se/sead/configuration/ApplicationArgumentManager.java#L28-L43).

### Configuration
Database settings in [config/application.properties](config/application.properties):
- `spring.datasource.url` - Target PostgreSQL database
- `spring.jpa.hibernate.ddl-auto` - **Always `none` in production** (schema managed externally)
- Access file via `--file` argument, NOT in properties

## Common Patterns

### Adding a New Importer
1. Create domain package: `se.sead.bugsimport.{domain}/`
2. Create `bugsmodel/{BugsEntity}.java` extending `TraceableBugsData`
3. Create `seadmodel/{SeadEntity}.java` extending `LoggableEntity` with JPA annotations
4. Create `{Domain}Importer.java` (`@Service`), mapper, and persister
5. Wire dependencies in constructor, add to required importers if needed
6. Add test in `src/test/java/se/sead/{domain}/`

### Working with Traces
When entities might be updated across imports, use trace-based lookups:
```java
@Component
@Order(1)  // Check trace FIRST before database
public class {Entity}TraceSearch implements {Entity}Search {
    @Autowired
    private {Entity}TraceHelper traceHelper;
    
    public SeadEntity findFor(BugsEntity bugs) {
        return traceHelper.getFromLastTrace(bugs.compressToString());
    }
}
```

### Error Handling
Mappers catch exceptions and add to `BugsListSeadMapping`. Persister logs these to `bugs_import.bugs_errors` table:

```sql
-- Check import errors after run:
SELECT bugs_table, bugs_identifier, message, change_date 
FROM bugs_import.bugs_errors 
ORDER BY change_date DESC;
```

Trace logs (all successful imports) go to `bugs_import.bugs_trace` with indexed lookups on `bugs_table + bugs_identifier` and `sead_table + sead_reference_id`.

## External Dependencies
- **BugsCEP**: MS Access database from http://www.bugscep.com/downloads/bugsdata.zip
- **PostgreSQL**: Target database must have `bugs_import` schema pre-created
- **Jackcess/UCanAccess**: Libs in `lib/UCanAccess/` - already bundled, don't update

## Docker
**Note**: Docker setup in [docker/Dockerfile](docker/Dockerfile) is legacy and not actively maintained. Use direct Java execution for production imports.

## Important Gotchas
- **Never change `spring.jpa.hibernate.ddl-auto` to `update` in production** - it drops unmapped columns
- Repository method is `saveOrUpdate()`, not `save()`
- Test database is H2, production is PostgreSQL - SQL differences matter
- Access file paths are absolute or relative to execution directory
- Import order matters - use required importers, not manual sequencing
- **Incremental updates**: The trace system supports detecting existing records, but full incremental update functionality is work-in-progress. Current imports typically target fresh databases copied from staging.
