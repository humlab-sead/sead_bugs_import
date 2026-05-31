# Python Migration Analysis - SEAD Bugs Import

*Last updated: April 2026*

## Executive Summary

### Motivation

The development team has no senior Java developer but has strong Python
expertise. This is the primary driver for the migration. Python also aligns
with the rest of the SEAD infrastructure.

### Target Architecture (post-migration)

The migrated system will be **more loosely coupled** to the SEAD database than
the current Java code:

- **Reconciliation service** (existing): domain-specific matching rules (search
  by trace, by natural key, etc.) are implemented as HTTP endpoints in a
  dedicated reconciliation service. The importer calls those endpoints rather
  than embedding the logic locally.
- **Identity service** (existing): new SEAD entity IDs are minted by a central
  identity service, not allocated by Hibernate sequence generators.
- **SEAD-targeting repositories**: the Python importer accesses SEAD tables
  through narrow, intent-specific repository methods — not through a full JPA
  entity graph. This deliberately limits schema exposure; the SEAD schema has
  a history of silent breaking changes that cause hard-to-diagnose regressions.

The consequence for scope: the 29 `Updater`, 19 `Manager`, and 14 `Search`
classes — which implement the reconciliation heuristics in the Java code — do
**not** need to be ported into the Python importer. They become endpoints in
the reconciliation service instead. See the revised effort estimate in the
[Hard 15% section](#hard-15----time-estimate).

---

**Codebase Size**: 565 Java files, ~27,300 LOC (main sources)
- 331 test files
- 34 domain packages under `se.sead.bugsimport`

File breakdown by role (see [Codebase Breakdown](#codebase-breakdown-by-file-type) section):

| Role             | Count | Notes                                                  |
|------------------|-------|--------------------------------------------------------|
| Repository       | 64    | One per SEAD table                                     |
| Converter        | 58    | Access row → SEAD entity                               |
| bugsmodel        | 69    | Access data objects + `BugsTable` defs                 |
| seadmodel        | 50    | JPA entities (PostgreSQL tables)                       |
| Persister        | 37    | Save + trace/error logging                             |
| Mapper           | 36    | Orchestrate read + convert loop                        |
| Importer         | 36    | Top-level orchestrators (incl. abstract base)          |
| Updater          | 29    | Domain-specific update logic                           |
| Manager / Helper | 39    | Runtime caches, trace helpers, factories               |
| Search           | 14    | `@Order`-annotated lookup strategies                   |
| Framework / misc | ~93   | Mapping results, access layer, parsers, creators, etc. |

**~230 files** (Repository + Converter + Persister + Mapper + Importer) follow
highly repetitive structural patterns — amenable to systematic translation.
**~100 files** (Updater + Manager + Helper + Search + domain creators/parsers)
contain the non-trivial domain logic requiring careful porting.
**~119 model files** (seadmodel + bugsmodel) are mostly boilerplate
getter/setter classes.

**Migration Complexity**: **Medium** (reduced from Medium-High because reconciliation and identity logic move to external services)
**Estimated Effort**: 3-4 person-months for full migration with tests

**Risk Level**: Low-Medium — the template-method architecture translates
mechanically; the previously highest-risk cluster (Updater/Manager/Search)
moves to the reconciliation service. Remaining risks are SEAD schema
fragility (mitigated by thin repositories) and the UCanAccess/jaydebeapi
stack.

---

## Architecture Overview

### Current Java Stack
```
Spring Boot 1.5.20 (Java 8)
├── Spring Data JPA / Hibernate — 65 repositories, 65+ entities
├── Jackcess 2.1.4 — low-level MS Access row iteration
├── UCanAccess 5.0.1 — JDBC bridge (bundled in lib/UCanAccess/)
└── PostgreSQL JDBC Driver
```

**Note**: The runtime code uses Jackcess directly for row-level iteration.
UCanAccess 5.0.1 is bundled but used only for the console scripts.

### Proposed Python Stack
```
Python 3.12+
├── jaydebeapi + JPype1    — UCanAccess JDBC bridge (reads Access files)
├── psycopg2 / SQLAlchemy  — narrow SEAD-targeting repositories only
├── httpx / requests       — HTTP client for reconciliation + identity services
├── Click                  — CLI (--file, --importers arguments)
└── pytest + testcontainers— test harness
```

**Interaction model**:
```
MS Access (.mdb)
    │  jaydebeapi / UCanAccess
    ▼
Python importer
    ├─── reconciliation service  (HTTP)  → returns existing SEAD entity ID
    │         (hosts the logic previously in Updater/Manager/Search classes)
    ├─── identity service        (HTTP)  → mints new SEAD entity ID
    └─── SEAD PostgreSQL DB  (psycopg2)  → narrow write-only repositories
              (no full JPA entity graph; limited schema exposure)
```

**Java runtime dependency**: jaydebeapi calls UCanAccess via JPype, so a JRE
is still required on the import host. Java 17 is already present (confirmed).

---

## Codebase Breakdown by File Type

Counts are for `src/main/java` only (565 files, 331 test files counted
separately). Files in `seadmodel/` and `bugsmodel/` subdirectories are
bucketed by directory; all others by class-name suffix.

| Category       | Count   | Python equivalent                                            | Effort per file                             |
|----------------|---------|--------------------------------------------------------------|---------------------------------------------|
| **Repository** | 64      | SQLAlchemy `Session` queries (no separate repo class needed) | Low — can be inlined into persisters        |
| **Converter**  | 58      | `row_converter.convert()` method                             | Low — mechanical field mapping              |
| **bugsmodel**  | 69      | `@dataclass` + SQL query in `_read_rows()`                   | Low — getters become dataclass fields       |
| **seadmodel**  | 50      | SQLAlchemy declarative model                                 | Low-Med — JPA annotations → `mapped_column` |
| **Persister**  | 37      | `Persister` subclass                                         | Low — same structure every time             |
| **Mapper**     | 36      | `BugsSeadMapper` subclass                                    | Low — mostly wiring                         |
| **Importer**   | 36      | `Importer` subclass                                          | Low — 5-10 lines each                       |
| **Updater**    | 29      | Domain updater classes                                       | Med-High — custom logic                     |
| **Manager**    | 19      | Stateful cache/dataset managers                              | High — complex state                        |
| **Helper**     | 20      | Helper utilities                                             | Med — lookup + trace helpers                |
| **Search**     | 14      | `EntitySearch` strategy classes                              | Med — priority-ordered lookups              |
| **Extractor**  | 6       | Field extractor functions                                    | Med                                         |
| **Accessor**   | 4       | Data accessor wrappers                                       | Low                                         |
| **Framework**  | ~83     | Core base classes, mapping results, routing, CLI             | High (one-time)                             |
| **Total**      | **565** |                                                              |                                             |

### Effort distribution

```
Mechanical translation (~230 files): Repository, Converter, bugsmodel,
    seadmodel, Persister, Mapper, Importer
    → Highly repetitive; a script or template can scaffold most of these.
    → Estimated 60-70% of the total file count, ~30% of effort.

Domain logic (~100 files): Updater, Manager, Helper, Search,
    Extractor, Accessor, Creator, Parser
    → Non-trivial logic; must be read and ported carefully.
    → ~18% of the file count, ~50% of effort.

Framework / one-time (~83 files): core base classes, mapping result
    types, tracing, translation engines, CLI wiring
    → Written once, shared by all importers.
    → ~15% of file count, ~20% of effort.
```

This distribution is the main reason the estimate is 4-6 person-months rather
than the raw 565÷N rule of thumb: roughly half the effort is in a relatively
small number of complex domain files.

### Hard 15% — time estimate

The domain-logic cluster (Updater/Manager/Helper/Search/Extractor/Accessor,
~100 files) totals **6,511 LOC** out of 27,325 main-source LOC (23.8% by
volume).

**In the new architecture, most of this cluster does not need to be ported
into the Python importer.** The reconciliation and search logic moves to the
reconciliation service; the identity allocation moves to the identity service.
What remains in the importer is the thin interface that calls those services.

| Sub-cluster                                           | Files         | LOC          | Disposition                                 |
|-------------------------------------------------------|---------------|--------------|---------------------------------------------|
| **Updater** (29) + **Manager** (19) + **Search** (14) | 62            | ~5,100       | → Reconciliation service (not ported)       |
| **Helper** — trace helpers                            | ~12           | ~600         | → Thin HTTP call wrappers in Python         |
| **Extractor** / **Accessor**                          | 10            | ~300         | → Utility functions in Python (~1 day each) |
| **Helper** — data helpers (non-trace)                 | ~8            | ~500         | → Port to Python; straightforward           |
| **Total remaining in importer**                       | **~20 files** | **~800 LOC** |                                             |

Revised effort model:

$$\text{reconciliation-service work} \approx 62 \text{ files} \times 2.7 \text{ multiplier} = \text{not in this project's scope}$$

$$\text{importer hard-cluster work} \approx 20 \text{ files} \times 1.5 \text{ multiplier} \approx 5\% \text{ of total importer effort}$$

**Bottom line for the Python importer**: the hard cluster drops from ~45% to
**~10% of importer effort** (roughly 2–3 weeks), because the business rules
move out of scope.

> **Reconciliation service caveat**: the 62 Updater/Manager/Search files
> _do_ need to be ported — they just land in a different project. Each domain's
> per-domain breakdown table above (datescalendar 861 LOC, periods 480 LOC,
> etc.) remains a valid effort estimate for _that_ project. The chronology
> subsystem (4 domains, 2,053 LOC) is still the single hardest chunk.

**Per-domain breakdown** (sorted by LOC):

| Domain                             | LOC       | Files    | Weeks (est.)  | Notes                                                                                        |
|------------------------------------|-----------|----------|---------------|----------------------------------------------------------------------------------------------|
| `datescalendar`                    | 861       | 13       | 2.5           | Uncertainty handling, relative ranges, 5 manager subtypes — highest single-domain complexity |
| `periods`                          | 480       | 11       | 1.5           | 5 age-type updater strategies (C14, Calendar, Undetermined, Relative, Geographic)            |
| `datesperiod` + `datesradio`       | 712       | 10       | 1.5           | Chronology method logic; conceptually linked to `datescalendar`                              |
| `speciessynonyms`                  | 323       | 3        | 1.0           | `SynonymSpeciesManager` (217 LOC) — dense synonym tree walking                               |
| `sitelocations`                    | 436       | 7        | 1.0           | Manager hierarchy for country/region location matching                                       |
| `fossil`                           | 382       | 7        | 1.0           | `AnalysisEntityManager` with in-memory entity cache                                          |
| `tracing` framework                | 243       | 4        | 0.5           | `TraceEventManager` — must be done first; blocks all other importers                         |
| `translations`                     | 188       | 4        | 0.5           | Reflection-based field extraction — can use Python `getattr`/`inspect`                       |
| All remaining domains              | ~1,886    | ~47      | 1.5           | Mostly contained Updater/TraceHelper pairs; lower average complexity                         |
| **Total (reconciliation service)** | **6,511** | **~106** | **~11 weeks** | Separate project                                                                             |

> **Practical constraint**: the chronology subsystem
> (`datescalendar` + `datesperiod` + `datesradio` + `periods`, combined 2,053
> LOC) shares state and concepts. Assign a single developer to this group
> whether the work lands in the importer or the reconciliation service;
> plan 4–5 weeks for it sequentially.

---

## MS Access Reading: UCanAccess via jaydebeapi

### Why UCanAccess

The current Java code uses **Jackcess** for row-by-row iteration of Access
tables. For Python, the cleanest equivalent that avoids rewriting this logic
is to use **UCanAccess** — already bundled at `lib/UCanAccess/` — as a JDBC
data source accessed from Python via `jaydebeapi`.

**Advantages over the alternatives**:

| Approach                     | SQL queries          | Encoding | Linux | JVM req. | Status            |
|------------------------------|----------------------|----------|-------|----------|-------------------|
| **jaydebeapi + UCanAccess**  | ✅ full SQL           | ✅ robust | ✅     | ✅ needed | **Recommended**   |
| JPype + Jackcess (raw)       | ❌ row iteration only | ✅        | ✅     | ✅ needed | workable fallback |
| pyodbc + mdbtools            | ⚠️ partial           | ❌ issues | ✅     | ❌        | limited/fragile   |
| pandas + mdbtools subprocess | ❌ CSV only           | ❌        | ✅     | ❌        | prototype only    |

Because the import host already runs Java 17 (confirmed), the JVM dependency
is not an additional constraint.

### Setup

```bash
pip install jaydebeapi JPype1

# UCanAccess JARs already bundled:
# lib/UCanAccess/lib/jackcess-3.0.1.jar
# lib/UCanAccess/lib/hsqldb-2.5.0.jar
# lib/UCanAccess/lib/commons-lang3-3.8.1.jar
# lib/UCanAccess/lib/commons-logging-1.2.jar
# (UCanAccess 5.0.1 jar found via lib/UCanAccess/)
```

### Connection Pattern

```python
import jaydebeapi
import glob
import os

UCANACCESS_JARS = glob.glob("lib/UCanAccess/**/*.jar", recursive=True)

def open_access_db(mdb_path: str):
    """Open an MDB file via UCanAccess JDBC."""
    conn = jaydebeapi.connect(
        "net.ucanaccess.jdbc.UcanaccessDriver",
        f"jdbc:ucanaccess://{os.path.abspath(mdb_path)};memory=true;showSchema=true",
        [],
        UCANACCESS_JARS,
    )
    return conn
```

### Reading Tables

The current Java `BugsBiblioBugsTable` reads Access rows like this:
```java
reference.setReference(accessRow.getString("REFERENCE"));
reference.setAuthor(accessRow.getString("AUTHOR"));
```

The Python equivalent with UCanAccess is simpler — use SQL directly:

```python
from dataclasses import dataclass
from typing import Optional

@dataclass
class BugsBiblio:
    reference: Optional[str]
    author: Optional[str]
    title: Optional[str]
    notes: Optional[str]

    def compress_to_string(self) -> str:
        return f"{{{self.reference},{self.author},{self.title},{self.notes}}}"

    def bugs_identifier(self) -> str:
        return self.reference

    @staticmethod
    def bugs_table() -> str:
        return "TBiblio"

def read_biblio(conn) -> list[BugsBiblio]:
    cursor = conn.cursor()
    cursor.execute("SELECT REFERENCE, AUTHOR, TITLE, Notes FROM TBiblio")
    cols = [d[0].lower() for d in cursor.description]
    return [BugsBiblio(**dict(zip(cols, row))) for row in cursor.fetchall()]
```

The `memory=true` option loads the MDB into HSQLDB in memory, which
dramatically speeds up repeated queries — important for importers that call
`AccessSearcher` to look up a specific row by column value:

```python
def find_biblio_by_reference(conn, reference: str) -> Optional[BugsBiblio]:
    cursor = conn.cursor()
    cursor.execute(
        "SELECT REFERENCE, AUTHOR, TITLE, Notes FROM TBiblio WHERE REFERENCE = ?",
        [reference],
    )
    rows = cursor.fetchall()
    if len(rows) > 1:
        raise ValueError(f"Multiple rows found for reference: {reference}")
    if not rows:
        return None
    cols = [d[0].lower() for d in cursor.description]
    return BugsBiblio(**dict(zip(cols, rows[0])))
```

This directly replaces `AccessSearcher.search(SearchCriteria(...))` with
a parameterised SQL query, which is cleaner and does not require cursor
scanning.

---

## Migration Strategy

### Phase 1: Foundation (3-4 weeks)
**Goal**: Core framework + 2-3 simple importers working end-to-end

#### 1.1 Core Framework

The template-method architecture (Importer → BugsSeadMapper → Persister)
translates almost line-for-line to Python:

```python
# sead_bugs_import/core/importer.py
import logging
from abc import ABC, abstractmethod

logger = logging.getLogger(__name__)

class Importer(ABC):
    def __init__(self, mapper, persister, *required_importers):
        self.mapper = mapper
        self.persister = persister
        self.required_importers = list(required_importers)
        self._has_run = False

    def run(self, indent: int = 0) -> None:
        if self._has_run:
            return
        pad = " " * indent
        logger.info("%s%s.run()", pad, type(self).__name__)
        for dep in self.required_importers:
            dep.run(indent + 4)
        result = self.mapper.import_bugs_data()
        self.persister.persist(result)
        self._has_run = True
        logger.info("%s%s: done", pad, type(self).__name__)
```

The mapper base reads all rows from the Access table via UCanAccess,
applies value translations, then delegates to a per-importer row converter:

```python
# sead_bugs_import/core/mapper.py
class BugsSeadMapper(ABC):
    def __init__(self, access_conn, row_converter, translation_service):
        self.conn = access_conn
        self.row_converter = row_converter
        self.translations = translation_service

    def import_bugs_data(self) -> MappingResult:
        result = MappingResult()
        for bugs_row in self._read_rows():
            self.translations.translate(bugs_row)
            try:
                sead_items = self.row_converter.convert(bugs_row)
                result.add(bugs_row, sead_items)
            except Exception as exc:
                result.add_error(bugs_row, str(exc))
        return result

    @abstractmethod
    def _read_rows(self) -> list:
        ...
```

The persister base mirrors `Persister.java` directly:

```python
# sead_bugs_import/core/persister.py
class Persister(ABC):
    def __init__(self, session_factory, trace_persister):
        self.session_factory = session_factory
        self.trace_persister = trace_persister

    def persist(self, mapping_result: MappingResult) -> None:
        for mapping in mapping_result:
            if mapping.has_errors:
                self.trace_persister.save_error(mapping.bugs_data, mapping.errors)
            elif mapping.is_new or mapping.is_updated:
                self._do_save(mapping)

    def _do_save(self, mapping) -> None:
        with self.session_factory() as session:
            for sead_item in mapping.sead_data:
                try:
                    saved = self.save(session, sead_item)
                    mapping.set_saved(sead_item, saved)
                    self.trace_persister.save_trace(mapping.bugs_data, session)
                except Exception as exc:
                    self.trace_persister.save_error(mapping.bugs_data, [str(exc)])

    @abstractmethod
    def save(self, session, entity):
        ...
```

#### 1.2 SEAD-Targeting Repositories

**Design principle**: the Python importer does **not** expose the full SEAD
schema. Each domain gets a narrow repository that only knows about the columns
it writes. This decouples the importer from schema changes in unrelated tables
and makes breakage visible at the repository boundary.

```python
# sead_bugs_import/repositories/biblio_repository.py
class BiblioRepository:
    """Write-only repository for tbl_biblio. Knows nothing else about SEAD."""

    def __init__(self, conn: psycopg2.extensions.connection):
        self._conn = conn

    def save(self, biblio: BiblioBugsSeadDto) -> int:
        """Insert or update; returns the assigned biblio_id."""
        with self._conn.cursor() as cur:
            cur.execute(
                """
                INSERT INTO tbl_biblio (biblio_id, reference, authors, title, notes)
                VALUES (%s, %s, %s, %s, %s)
                ON CONFLICT (biblio_id) DO UPDATE
                    SET reference = EXCLUDED.reference,
                        authors   = EXCLUDED.authors,
                        title     = EXCLUDED.title,
                        notes     = EXCLUDED.notes
                RETURNING biblio_id
                """,
                (biblio.biblio_id, biblio.reference, biblio.authors,
                 biblio.title, biblio.notes),
            )
            return cur.fetchone()[0]
```

**Reconciliation and identity service calls**:

```python
# sead_bugs_import/services/reconciliation_client.py
import httpx

class ReconciliationClient:
    def __init__(self, base_url: str):
        self._client = httpx.Client(base_url=base_url)

    def find_biblio(self, reference: str) -> int | None:
        """Returns existing SEAD biblio_id, or None if not found."""
        r = self._client.get("/biblio/find", params={"reference": reference})
        r.raise_for_status()
        return r.json().get("biblio_id")

# sead_bugs_import/services/identity_client.py
class IdentityClient:
    def __init__(self, base_url: str):
        self._client = httpx.Client(base_url=base_url)

    def mint_biblio_id(self) -> int:
        """Allocates a new SEAD biblio_id from the identity service."""
        r = self._client.post("/biblio/id")
        r.raise_for_status()
        return r.json()["biblio_id"]
```

Replace the full JPA entity hierarchy with lightweight DTOs:

```python
from dataclasses import dataclass

@dataclass
class BiblioBugsSeadDto:
    """SEAD-facing DTO — only the columns this importer writes."""
    biblio_id: int | None
    reference: str
    authors: str | None
    title: str | None
    notes: str | None
```

This eliminates the 50 JPA entity files as a porting concern: each importer
instead gets a DTO with only the fields it uses, reducing schema coupling to
a minimum. SQLAlchemy ORM is **not** used for writes; psycopg2 raw SQL keeps
the schema surface area explicit and auditable.

---

### Phase 2: Domain Importers (8-10 weeks)

35 domain importers (excluding the abstract base), organized by dependency
depth and complexity:

#### Tier 1 — No dependencies (Week 1-2)
1. **COMPLETED** **BibliographyImporter** — single `TBiblio` table, no FK deps
2. **LabImporter** — simple lookup
3. **PeriodImporter** — basic conversions
4. **RdbSystemImporter** — lookup table
5. **EcocodeGroupImporter** — lookup table
6. **CountryImporter** — location data

#### Tier 2 — 1-2 dependencies (Week 3-6)
7. **COMPLETED** **SiteImporter** → Bibliography
8. **RdbCodeImporter** → RdbSystem
9. **RdbImporter** → RdbCode + RdbSystem
10. **EcocodeDefinitionImporter** (BugsDefinition + KochDefinition variants)
11. **BugsEcocodeImporter** / **KochEcocodesImporter** → EcocodeDefinition
12. **McrNamesImporter** / **MCRSummaryImporter**
13. **SiteReferencesImporter** → Site + Bibliography
14. **SiteLocationImporter** → Site + Location
15. **SiteOtherProxiesImporter** → Site

#### Tier 3 — 3+ dependencies or custom logic (Week 7-10)
16. **SampleGroupImporter** → Site
17. **SampleImporter** → SampleGroup
18. **IndexImporter** (TaxonomicOrder) → complex species tree
19. **FossilImporter** → Sample + IndexImporter + custom `AnalysisEntityManager`
20. **DatasetContactImporter** → multiple FK lookups
21. **DatesCalendarImporter** / **DatesPeriodImporter** / **DatesRadioImporter** → Sample
22. **AttributesImporter** → Sample
23. **CountSheetImporter** → Sample
24. **SpeciesAssociationImporter**, **SpeciesDistributionImporter**,
    **SpeciesBiologyImporter**, **SpeciesIdentificationKeysImporter**,
    **SynonymImporter** → IndexImporter
25. **GeochronologyImporter** → Lab + Sample
26. **TaxaSeasonalityImporter**, **TaxonomicNotesImporter** → IndexImporter
27. **BirmBeetleDataImporter**, **TaxaMeasuredAttributesImporter** → similar

**Per-importer migration checklist**:
- [ ] Bugs dataclass (replaces `TraceableBugsData` subclass + `BugsTable`) — 1 file per importer
- [ ] SEAD entity/entities (SQLAlchemy models, replaces JPA entities) — 1-3 files per importer
- [ ] `_read_rows()` — SQL query via UCanAccess connection
- [ ] `row_converter.convert()` — maps dataclass → SEAD entity (replaces `Converter`)
- [ ] `save()` in persister — calls `save_or_update()` (replaces `Persister`)
- [ ] Search strategies ordered by priority (replaces `Search` classes, if `@Order` present)
- [ ] Domain updaters/managers/helpers (replaces `Updater`/`Manager`/`Helper` — most effort)
- [ ] pytest tests with real MDB fixture

File count per importer varies widely: a simple leaf importer (e.g.
`BibliographyImporter`) translates to ~5 Python files; a complex one
(e.g. `FossilImporter`) may require 15+ including updaters, managers, and
multiple search strategies.

---

### Phase 3: Advanced Features (2-3 weeks)

#### 3.1 Trace System

The Java trace system has three interlocking parts:
- `PostEventListener` — JPA `@EntityListeners` fires after INSERT/UPDATE
- `TraceEventManager` — accumulates `BugsTrace` rows per flush
- `TracePersister` — writes traces and errors to `bugs_import.*`

In Python the cleanest equivalent is **explicit trace calls** in the persister
rather than SQLAlchemy event hooks, since trace records are tied to the outer
`BugsListSeadMapping` context (which knows the `TraceableBugsData` source),
not just the entity itself:

```python
# sead_bugs_import/tracing/trace_persister.py
from datetime import datetime

class TracePersister:
    def __init__(self, session_factory):
        self.session_factory = session_factory

    def save_trace(self, bugs_data, sead_table: str, sead_id: int,
                   trace_type: str = "INSERT") -> None:
        with self.session_factory() as session:
            trace = BugsTrace(
                bugs_table=bugs_data.bugs_table(),
                bugs_identifier=bugs_data.get_compressed_string_before_translation(),
                sead_table=sead_table,
                sead_reference_id=sead_id,
                type=trace_type,
                change_date=datetime.utcnow(),
            )
            session.add(trace)
            session.commit()

    def save_error(self, bugs_data, messages: list[str]) -> None:
        with self.session_factory() as session:
            for msg in messages:
                session.add(BugsError(
                    bugs_table=bugs_data.bugs_table(),
                    bugs_identifier=bugs_data.get_compressed_string_before_translation(),
                    message=msg,
                    change_date=datetime.utcnow(),
                ))
            session.commit()
```

Using SQLAlchemy `@event.listens_for` is *possible* but would replicate the
complexity of the Java AOP listener and is harder to test.

#### 3.2 Search Rules with Priority (replaces `@Order`)

Current pattern: Spring autowires a `List<EntitySearch>` sorted by `@Order(n)`,
each checked in priority order before falling back to DB.

Python equivalent — explicit sorted strategy list:

```python
from abc import ABC, abstractmethod

class EntitySearch(ABC):
    priority: int = 999

    @abstractmethod
    def find_for(self, bugs_entity): ...

class TraceSearch(EntitySearch):
    priority = 1
    def __init__(self, trace_helper): self.helper = trace_helper
    def find_for(self, bugs): return self.helper.get_from_last_trace(bugs)

class DatabaseSearch(EntitySearch):
    priority = 2
    def __init__(self, repo): self.repo = repo
    def find_for(self, bugs): return self.repo.find_by_natural_key(bugs)

# In mapper: instantiate and sort once
search_rules = sorted(
    [TraceSearch(trace_helper), DatabaseSearch(repo)],
    key=lambda r: r.priority,
)

def find_existing(bugs_entity):
    for rule in search_rules:
        result = rule.find_for(bugs_entity)
        if result is not None:
            return result
    return None
```

#### 3.3 Value Translation Service

Two engines in `BugsValueTranslationService`:
- `IdBasedTranslationEngine` — looks up `bugs_import.bugs_id_translation`
- `TypeTranslationEngine` — looks up `bugs_import.bugs_type_translation`

Python equivalent — load at startup, then apply as simple dict substitution:

```python
from functools import lru_cache

class TranslationService:
    def __init__(self, session_factory):
        self._session_factory = session_factory
        self._id_table: dict = {}
        self._type_table: dict = {}
        self._load()

    def _load(self) -> None:
        with self._session_factory() as s:
            for row in s.execute("SELECT ... FROM bugs_import.bugs_id_translation"):
                self._id_table[(row.table, row.column, row.value)] = row.translated
            for row in s.execute("SELECT ... FROM bugs_import.bugs_type_translation"):
                self._type_table[(row.type_name, row.value)] = row.translated

    def translate(self, bugs_data) -> None:
        # Apply substitutions to the bugs_data fields in place
        for wrapper in bugs_data.translation_wrappers():
            if wrapper.should_translate(bugs_data):
                key = (bugs_data.bugs_table(), wrapper.target_column,
                       wrapper.get_value(bugs_data))
                translated = self._id_table.get(key)
                if translated is not None:
                    wrapper.set_value(bugs_data, translated)
```

---

## High-Risk / High-Effort Areas

### 0. SEAD Schema Fragility (ongoing)
The SEAD schema changes without notice and has historically broken the Java
importer silently (wrong column names, added NOT NULL columns, FK renamings).

**Mitigation in the new design**:
- Each importer has an explicit DTO listing every column it writes. Schema
  changes break at the DTO boundary rather than deep inside ORM mapping.
- Repository SQL is written as explicit `INSERT ... ON CONFLICT DO UPDATE` —
  no ORM introspection of the schema at runtime.
- Add a startup schema-validation step: `SELECT column_name FROM
  information_schema.columns WHERE table_name = %s` and compare against
  the DTO field list. Fail fast with a clear error rather than a silent data
  corruption.

### 1. Species Taxonomy Hierarchy (2-3 weeks — reconciliation service)
`TaxonomicOrderPersister.java` implements a recursive tree walk:
Order → Family → Genus → Species → Author, de-duplicating against a cache
at each level. This is the single most complex piece.

In the new architecture this logic lives **in the reconciliation service**.
The importer makes a single call:

```python
# Importer side (thin)
sead_id = reconciliation_client.find_or_create_taxonomic_order(
    order=bugs_row.order, family=bugs_row.family,
    genus=bugs_row.genus, species=bugs_row.species,
    author=bugs_row.author,
)
```

The recursive cache management stays in the reconciliation service project.
Effort for the **importer**: ~0.5 weeks (HTTP call + DTO).
Effort for the **reconciliation service**: 2-3 weeks (unchanged from before).

### 2. Fossil/Abundance Importer (1-2 weeks)
`FossilImporter` uses `AnalysisEntityManager` to maintain an in-memory cache
of `AnalysisEntity` records (keyed by sample + species + dataset). Large
fossil datasets make this the most performance-sensitive importer.

**Migration concerns for the importer**:
- The reconciliation service handles entity lookup/creation; the importer
  receives the `analysis_entity_id` and inserts the abundance row directly.
- Bulk-insert via `psycopg2.extras.execute_values()` for performance.
- Transaction boundary: entire fossil table or per-sample-group — decide
  during Phase 1 API design with the reconciliation service team.

### 3. JPA Cascade Semantics (no longer applicable)
With the DTO + psycopg2 approach there is no ORM cascade graph to replicate.
Each repository writes exactly the rows it owns using explicit SQL.
Cross-entity integrity is enforced by PostgreSQL FKs and by calling the
identity service before writing dependent rows.

### 4. Audit Timestamps
The Java `PostEventListener` (@PrePersist/@PreUpdate) sets `date_updated`
on every entity. Equivalent without an ORM: set the timestamp explicitly
in each repository's `INSERT ... ON CONFLICT DO UPDATE` statement:

```sql
INSERT INTO tbl_biblio (..., date_updated)
VALUES (..., NOW())
ON CONFLICT (biblio_id) DO UPDATE
    SET ..., date_updated = NOW()
```

No event hooks needed; the pattern is uniform across all repositories.

### 5. Reconciliation Service API Contract
The hardest coordination risk in the new architecture is agreeing on what the
reconciliation service endpoints look like before the importer is coded.
Recommended approach: write the HTTP client calls (stubs) in Phase 1 of the
importer, produce an OpenAPI spec from those stubs, and use that as the
contract for the reconciliation service team to implement against.

---

## Testing Strategy

### Current: 331 Java test files
- `@SpringBootTest` + H2 in-memory DB
- `@ActiveProfiles("test")` loads `application-test.properties`
- Test data in `src/test/resources/{domain}/`

### Python approach

```python
# conftest.py
import pytest
import psycopg2
from testcontainers.postgres import PostgresContainer

@pytest.fixture(scope="session")
def pg_conn():
    with PostgresContainer("postgres:15") as pg:
        conn = psycopg2.connect(pg.get_connection_url())
        # Load SEAD schema subset needed by importers under test
        with open("target/test-classes/databasemodel.sql") as f:
            conn.cursor().execute(f.read())
        conn.commit()
        yield conn
        conn.close()

@pytest.fixture
def db(pg_conn):
    """Each test runs in a transaction that is rolled back on teardown."""
    pg_conn.autocommit = False
    yield pg_conn
    pg_conn.rollback()

@pytest.fixture(scope="session")
def access_conn():
    conn = open_access_db("bugsdata/test_data/bugsdata_samp000546.mdb")
    yield conn
    conn.close()

@pytest.fixture
def reconciliation_client(httpx_mock):
    """Return a mock reconciliation client for unit tests."""
    # Override with a real client pointed at a test instance for integration tests
    return MockReconciliationClient(httpx_mock)
```

Per-importer test pattern:
```python
def test_bibliography_import(db, access_conn, reconciliation_client):
    repo = BiblioRepository(db)
    trace = TracePersister(db)
    mapper = BibliographyMapper(access_conn, TranslationService(db))
    persister = BibliographyPersister(repo, reconciliation_client, trace)
    importer = BibliographyImporter(mapper, persister)
    importer.run()

    with db.cursor() as cur:
        cur.execute("SELECT COUNT(*) FROM tbl_biblio")
        assert cur.fetchone()[0] > 0
        cur.execute("SELECT COUNT(*) FROM bugs_import.bugs_trace WHERE bugs_table = 'TBiblio'")
        count = cur.fetchone()[0]
    assert count > 0
```

**Test migration effort**: 4-6 weeks; can run in parallel with importer work.
The existing MDB test fixtures (`bugsdata_samp000546.mdb`,
`bugsdata_samp000546_minimal.mdb`) are reusable directly.

---

## Deployment & Workflow Changes

### Before (Maven + Java)
```bash
mvn -Dmaven.test.skip=true clean package
java -jar target/bugs.import-0.1-SNAPSHOT.jar \
    --file=bugsdata/bugsdata_20231219.mdb \
    --importers=Bibliography,Lab,Site
```

### After (Python + pip/uv)
```bash
# Install (once)
pip install -e .       # or: uv sync

# Run
sead-bugs-import \
    --file bugsdata/bugsdata_20231219.mdb \
    --importers Bibliography,Lab,Site
```

The UCanAccess JARs are already in `lib/UCanAccess/` and are referenced at
runtime — no Maven build step needed for the import host.

### Configuration

```toml
# config.toml  (replaces config/application.properties)
[database]
url = "postgresql://sead_admin:secret@localhost:5432/sead_staging"

[access]
jar_dir = "lib/UCanAccess"
```

### Docker

```dockerfile
FROM python:3.12-slim
# JRE needed for UCanAccess
RUN apt-get update && apt-get install -y default-jre-headless && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY . .
RUN pip install -e .
ENTRYPOINT ["sead-bugs-import"]
```

No `mdbtools` needed — UCanAccess handles all Access file formats.

---

## Migration Roadmap

Estimates reflect the new architecture: reconciliation and identity logic are
**out of scope** for the importer (they go to the reconciliation/identity
services). The importer only needs to read from Access, call those services,
and write through narrow SEAD-targeting repositories.

| Phase                                               | Duration  | Files touched         | Deliverable                                                                                           |
|-----------------------------------------------------|-----------|-----------------------|-------------------------------------------------------------------------------------------------------|
| 1. Foundation (core + UCanAccess + service clients) | 2-3 weeks | ~20 framework files   | Core framework, HTTP service clients, `BibliographyImporter` end-to-end                               |
| 2. Tier 1 importers (6 simple, leaf)                | 1-2 weeks | ~25 files             | All leaf importers passing; establishes scaffold template                                             |
| 3. Tier 2 importers (9 medium)                      | 3 weeks   | ~70 files             | Site, Ecocode, RDB chain working                                                                      |
| 4. Tier 3 importers (20 complex)                    | 4 weeks   | ~150 files            | Species tree, Fossil, Dates, Geochronology — reconciliation calls replace local Updater/Manager logic |
| 5. Trace + search rules + translations              | 1-2 weeks | ~20 files             | Full audit trail + TranslationService                                                                 |
| 6. Tests                                            | 3 weeks   | ~200 test equivalents | pytest suite; overlaps phases 2-4                                                                     |
| 7. Parallel validation run                          | 2 weeks   | —                     | `bugs_trace` parity confirmed on real MDB                                                             |

**Total**: ~14-17 weeks with 1 developer (previously 19-23 weeks)

Reduction comes from: (a) reconciliation/identity logic out of importer scope,
(b) no SQLAlchemy entity graph to port (DTOs + psycopg2 instead),
(c) no Spring DI wiring — Python service injection is explicit and trivial.

> **Pre-condition**: the reconciliation service must expose endpoints for every
> domain before the Tier 3 importers can be completed. Agree on the API
> contract (request/response shapes) during Phase 1, even if the endpoints
> are implemented concurrently.

### Risk Mitigation

1. **Parallel validation**: run Java and Python on the same MDB file against
   a cloned staging DB; compare `bugs_trace` and `bugs_errors` row counts
   per importer.

2. **Rollback**: keep the Java JAR runnable for at least one import cycle
   after Python is in production.

3. **Prototype first**: implement `BibliographyImporter` end-to-end in week 1
   to validate the UCanAccess + psycopg2 + service-client stack before
   committing to the full effort.

4. **Schema isolation**: every SEAD-targeting repository must be reviewed
   against the current SEAD schema before the first production run.
   The limited surface area (one DTO per importer) makes this feasible
   as a pre-release checklist item.

---

## Recommendations

**Migration is recommended** given the stated constraints:
- No senior Java developer available — maintenance cost is already high
- Team has strong Python expertise across the SEAD infrastructure
- Reconciliation/identity service architecture removes the hardest porting
  work from the importer scope, reducing risk substantially
- SEAD schema fragility is better managed with explicit narrow repositories
  than with a full ORM entity graph

**Hybrid alternative (2-4 weeks effort, if timeline is tight):**  
Use Python + UCanAccess/jaydebeapi only for the Access extraction layer —
export each Access table to JSON, then feed it into the existing Java pipeline.
This removes the Java/Access FFI glue without rewriting anything else.
Useful as a stopgap while the reconciliation service API is being designed.

---

## Next Steps

1. **Agree on service API contracts**: before writing importer code, define
   the reconciliation service endpoints (OpenAPI spec) for the 5 most complex
   domains (taxonomy hierarchy, fossil entity, dates/chronology, site location,
   species synonyms). These are the blockers for Tier 3 importer work.

2. **Week 1 prototype**: `BibliographyImporter` in Python with jaydebeapi +
   UCanAccess + psycopg2 repository against `bugsdata_samp000546.mdb`;
   verify `bugs_trace` output matches the Java version.

3. **Schema validation harness**: write the startup schema-check utility
   during Phase 1 so every subsequent domain benefits from it immediately.

4. **Decision checkpoint after prototype**: if the UCanAccess/jaydebeapi
   stack has issues, evaluate the hybrid extraction approach as a fallback.

5. **Success criteria**:
   - `bugs_trace` row counts match Java ±0 for all importers
   - `bugs_errors` count matches Java ±0
   - Import wall-clock time within 2× of Java (acceptable given JVM startup
     overhead is a one-time cost per run)
   - Zero data-integrity regressions in the SEAD target tables
   - Schema validation passes on startup with the production SEAD schema

---

## Reconciliation Policy Authoring Checklist

Each row below is a domain for which the six Java source files required by
[`doc/reconciliation_policies/create-policy.instructions.md`](doc/reconciliation_policies/create-policy.instructions.md)
are all present: a `BugsTable`, a `TraceableBugsData` subclass, a JPA SEAD
entity, a RowConverter/Mapper, an Updater/Manager, and at least one lookup
repository.

Check the box once `doc/reconciliation_policies/{policy_file}` has been
committed and passes the validation checklist in the instructions.

Ordering follows the dependency graph (leaf importers first).

### Tier 1 — Leaf importers (no required importers)

| Done | Policy file                      | Importer class                | Access table         | SEAD table                  |
|------|----------------------------------|-------------------------------|----------------------|-----------------------------|
| [x]  | `bibliography.policy.yml`        | `BibliographyImporter`        | `TBiblio`            | `tbl_biblio`                |
| [x]  | `site.policy.yml`                | `SiteImporter`                | `TSite`              | `tbl_sites`                 |
| [x]  | `lab.policy.yml`                 | `LabImporter`                 | `TLab`               | `tbl_dating_labs`           |
| [ ]  | `period.policy.yml`              | `PeriodImporter`              | `TPeriod`            | `tbl_periods`               |
| [ ]  | `rdbsystem.policy.yml`           | `RdbSystemImporter`           | `TRDBSystem`         | `tbl_rdb_systems`           |
| [ ]  | `ecocodegroup.policy.yml`        | `EcocodeGroupImporter`        | `TEcoCodes`          | `tbl_ecocode_systems`       |
| [ ]  | `mcrnames.policy.yml`            | `McrNamesImporter`            | `TMCRNames`          | `tbl_mcr_names`             |
| [ ]  | `speciesassociation.policy.yml`  | `SpeciesAssociationImporter`  | `TAssociation`       | `tbl_species_associations`  |
| [ ]  | `speciesdistribution.policy.yml` | `SpeciesDistributionImporter` | `TDistrib`           | `tbl_species_distributions` |
| [ ]  | `speciesbiology.policy.yml`      | `TextBiologyImporter`         | `TBiology`           | `tbl_text_biology`          |
| [ ]  | `specieskeys.policy.yml`         | `IdentificationKeysImporter`  | `TKeys`              | `tbl_identification_keys`   |
| [ ]  | `speciessynonyms.policy.yml`     | `SynonymImporter`             | `TSynonym`           | `tbl_species_synonyms`      |
| [ ]  | `taxanotes.policy.yml`           | `TaxonomicNotesImporter`      | `TTaxoNotes`         | `tbl_taxonomic_notes`       |
| [ ]  | `taxaseasonality.policy.yml`     | `TaxaSeasonalityImporter`     | `TSeasonActiveAdult` | `tbl_taxa_seasonality`      |

### Tier 2 — 1-2 required importers

| Done | Policy file                         | Importer class             | Access table        | SEAD table                | Requires                   |
|------|-------------------------------------|----------------------------|---------------------|---------------------------|----------------------------|
| [ ]  | `rdbcode.policy.yml`                | `RdbCodeImporter`          | `TRDBCodes`         | `tbl_rdb_codes`           | RdbSystem                  |
| [ ]  | `rdb.policy.yml`                    | `RdbImporter`              | `TRDB`              | `tbl_rdb`                 | RdbSystem, RdbCode         |
| [ ]  | `ecocodedefinition_bugs.policy.yml` | `BugsDefinitionImporter`   | `TEcoDefBugs`       | `tbl_ecocode_definitions` | EcocodeGroup               |
| [ ]  | `ecocodedefinition_koch.policy.yml` | `KochDefinitionImporter`   | `TEcoDefKoch`       | `tbl_ecocode_definitions` | EcocodeGroup               |
| [ ]  | `ecocode_bugs.policy.yml`           | `BugsEcocodeImporter`      | `TEcoBugs`          | `tbl_ecocodes`            | EcocodeDefinition          |
| [ ]  | `ecocode_koch.policy.yml`           | `KochEcocodesImporter`     | `TEcoKoch`          | `tbl_ecocodes`            | EcocodeDefinition          |
| [ ]  | `mcrsummary.policy.yml`             | `MCRSummaryImporter`       | `TMCRSummary`       | `tbl_mcr_summary`         | McrNames                   |
| [ ]  | `birmbeetledata.policy.yml`         | `BirmBeetleDataImporter`   | `TBirmBeetleDat`    | `tbl_birm_beetle_data`    | McrNames                   |
| [ ]  | `sitereferences.policy.yml`         | `SiteReferencesImporter`   | `TSiteRef`          | `tbl_site_references`     | Site, Bibliography         |
| [ ]  | `sitelocations.policy.yml`          | `SiteLocationImporter`     | `TSiteLocation`     | `tbl_site_locations`      | Site                       |
| [ ]  | `siteotherproxies.policy.yml`       | `SiteOtherProxiesImporter` | `TSiteOtherProxies` | `tbl_site_other_proxies`  | Site                       |
| [ ]  | `species.policy.yml`                | `IndexImporter`            | `TINDEX`            | `tbl_taxa_tree_master`    | — (complex; see High-Risk) |

### Tier 3 — 3+ dependencies or custom logic

| Done | Policy file                | Importer class                   | Access table     | SEAD table                      | Requires      |
|------|----------------------------|----------------------------------|------------------|---------------------------------|---------------|
| [ ]  | `samplegroup.policy.yml`   | `SampleGroupImporter`            | `TCountsheet`    | `tbl_sample_groups`             | Site          |
| [ ]  | `sample.policy.yml`        | `SampleImporter`                 | `TSample`        | `tbl_samples`                   | SampleGroup   |
| [ ]  | `fossil.policy.yml`        | `FossilImporter`                 | `TFossil`        | `tbl_abundances`                | Sample, Index |
| [ ]  | `datescalendar.policy.yml` | `DatesCalendarImporter`          | `TDatesCalendar` | `tbl_dates_calendar`            | Sample        |
| [ ]  | `datesperiod.policy.yml`   | `DatesPeriodImporter`            | `TDatesPeriod`   | `tbl_dates_period`              | Sample        |
| [ ]  | `datesradio.policy.yml`    | `GeochronologyImporter`          | `TDatesRadio`    | `tbl_geochronology`             | Lab, Sample   |
| [ ]  | `attributes.policy.yml`    | `TaxaMeasuredAttributesImporter` | `TAttributes`    | `tbl_taxa_measured_attributes`  | Sample        |
| [ ]  | `countsheets.policy.yml`   | `SampleGroupImporter`            | `TCountsheet`    | `tbl_sample_group_descriptions` | SampleGroup   |

### Excluded domains (cannot use standard instructions)

| Domain            | Reason                                                                                                                                               |
|-------------------|------------------------------------------------------------------------------------------------------------------------------------------------------|
| `datasetcontacts` | No `BugsTable.java` — reads from `TSite.IDBy` and `TSite.Specimens` via custom string parsing in `SiteContactReader`; requires bespoke policy format |
| `translations`    | `BugsValueTranslationService` is a utility service, not an importer; no `TraceableBugsData` or `BugsTable`                                           |
| `locations`       | Internal sub-domain; location rows created as a side effect of site import, not from a dedicated Access table                                        |
| `tracing`         | Infrastructure only (`BugsTrace`, `BugsError` entities)                                                                                              |

---

## Appendix: Dependency Graph (Top Level)

```
Bibliography (leaf)
Lab (leaf)
Period (leaf)
RdbSystem (leaf) → RdbCode → Rdb
EcocodeGroup (leaf) → EcocodeDefinition (Bugs+Koch) → EcoCode (Bugs+Koch)
Country (leaf) → Location → SiteLocation  ──────────┐
                                                    │
Site ← Bibliography                                 │
  ├─ SiteReference ← Bibliography                   │
  ├─ SiteOtherProxies                               │
  └─ SiteLocation ──────────────────────────────────┘
       └─ SampleGroup → Sample
                          ├─ DatesCalendar / DatesPeriod / DatesRadio
                          ├─ CountSheet / Attributes
                          └─ Fossil ← Index (TaxonomicOrder)
                                        ├─ SpeciesAssociation
                                        ├─ SpeciesDistribution
                                        ├─ SpeciesBiology
                                        ├─ SpeciesKeys
                                        ├─ Synonym
                                        ├─ TaxaSeasonality
                                        └─ TaxonomicNotes
Lab → Geochronology ← Sample
```


