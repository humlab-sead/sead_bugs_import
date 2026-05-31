# Reconciliation Migration Plan: sead_bugs_import → sead_shape_shifter

## Executive Summary

This document analyzes the migration of **automatic reconciliation logic** from the Java-based `sead_bugs_import` system into the Python-based `sead_shape_shifter` framework.

### Scope
**IN SCOPE:**
- Entity reconciliation patterns (trace-based and database-based lookup)
- Search rule strategies with priority ordering
- Transformation/mapping logic (Bugs → SEAD entity conversion)
- Import trace management for incremental updates

**OUT OF SCOPE:**
- Full data persistence (handled by separate SEAD import system)
- Domain model impedance mismatch (solved via Shape Shifter configurations)
- MS Access reading (Shape Shifter already supports this via UCanAccess)

### Key Findings

1. **Shape Shifter already has 80% of required infrastructure**:
   - ✅ Reconciliation service with OpenRefine API support
   - ✅ Multi-source data resolution (entity preview, SQL queries, custom sources)
   - ✅ Auto-accept thresholds and manual review workflows
   - ✅ MS Access loader via UCanAccess

2. **Core gaps to address**:
   - ❌ No trace-based lookup system (bugs_trace table integration)
   - ❌ No priority-ordered search rule chains
   - ❌ Limited field-level transformation rules (currently column mapping only)
   - ❌ No automatic "update vs insert" detection based on prior imports

3. **Migration Effort Estimate**: **3-4 weeks** (1 FTE)
   - Week 1: Trace integration + search rule framework
   - Week 2-3: Field transformation engine + reconciliation strategies
   - Week 4: Testing, validation, documentation

---

## Architecture Comparison

### Current Java Architecture (sead_bugs_import)

```
┌─────────────────┐
│ BugsSeadMapper  │ - Reads MS Access (Jackcess)
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ RowConverter    │ - Orchestrates search strategies
└────────┬────────┘
         │
         ├──► Search Strategy 1 (@Order 1): TraceHelper.getFromLastTrace()
         ├──► Search Strategy 2 (@Order 2): DatabaseLookup (by business key)
         └──► Create new entity if not found
         │
         ▼
┌─────────────────┐
│ EntityUpdater   │ - Field-level transformations
└────────┬────────┘   - Lookups for related entities (e.g., Bibliography, Species)
         │             - Conditional updates (only if changed)
         ▼
┌─────────────────┐
│ Persister       │ - saveOrUpdate() to PostgreSQL
└────────┬────────┘   - Creates BugsTrace entry (INSERT/UPDATE)
         │             - Logs errors to bugs_errors
         ▼
  PostgreSQL SEAD DB
```

**Key Components:**

1. **Trace System (`bugs_import.bugs_trace`)**
   - Tracks every import operation
   - Links Bugs entity → SEAD entity via `(bugs_table, bugs_identifier) → (sead_table, sead_id)`
   - Supports "compressed" identifiers for complex keys
   - Detects if SEAD data was edited since last import (date comparison)

2. **Search Rule Chain (Spring @Order annotation)**
   ```java
   @Component @Order(1)
   public class TraceSearch implements EntitySearch {
       // Check trace table first (fastest)
   }
   
   @Component @Order(2)  
   public class DatabaseSearch implements EntitySearch {
       // Fallback to database lookup by business key
   }
   ```

3. **Field Transformers (Updater classes)**
   ```java
   public class RdbUpdater {
       void update(Rdb original, BugsRDB bugsData) {
           setSpecies();      // Lookup TaxaSpecies
           setCountry();      // Lookup Location
           setReference();    // Lookup Biblio
       }
   }
   ```

### Target Python Architecture (sead_shape_shifter)

```
┌─────────────────┐
│ ShapeShifter    │ - Reads MS Access (UCanAccess)
│ Configuration   │ - Extracts entities via SQL/CSV
└────────┬────────┘
         │
         ▼
┌─────────────────────────────────┐
│ ReconciliationService (Existing)│
│                                 │
│ - ReconciliationSourceResolver  │ ◄── NEW: Add TraceSourceResolver
│ - OpenRefine API Client         │
│ - Auto-accept/Review Logic      │
└────────┬────────────────────────┘
         │
         │  NEW: Priority Search Chain
         ├──► SearchRule 1 (priority=1): TraceBasedSearch
         │     - Query bugs_trace for prior import
         │     - Return cached SEAD ID + entity
         │
         ├──► SearchRule 2 (priority=2): ReconciliationServiceSearch
         │     - Call OpenRefine API
         │     - Return top candidate(s)
         │
         └──► SearchRule 3 (priority=3): ManualMapping
              - Check user-defined mappings
         │
         ▼
┌─────────────────┐
│ FieldTransformer│ ◄── NEW: Field-level transformation engine
│ Pipeline        │    - Lookup related entities
└────────┬────────┘    - Apply transformation rules
         │             - Validate/enrich data
         ▼
┌─────────────────┐
│ CSV/Excel       │ - Output transformed data
│ Export          │ - Another system imports to SEAD
└─────────────────┘
```

**Key Enhancements Needed:**

1. **Trace Integration**: Read from `bugs_import.bugs_trace` to detect existing mappings
2. **Search Rule Framework**: Priority-ordered chain with fallback logic
3. **Field Transformation Engine**: Declarative rules for entity lookups and value transformations
4. **Incremental Update Detection**: Mark rows as INSERT vs UPDATE based on trace history

---

## Gap Analysis

### 1. Trace-Based Reconciliation

**Java Implementation:**
```java
@Component
public class SiteLocationTraceHelper extends SeadDataFromTraceHelper<...> {
    public SiteLocation getFromLastTrace(String traceIdentifier) {
        BugsTrace latest = getLatest(traceIdentifier);
        return getSeadDataFromTrace(latest);
    }
}
```

**Shape Shifter Gap:**
- ❌ No integration with `bugs_import.bugs_trace` table
- ❌ No concept of "prior import" history
- ✅ Has reconciliation mapping storage (but not trace-based)

**Proposed Solution:**
```python
class TraceLookupService:
    """Service for querying bugs_import.bugs_trace table."""
    
    def get_latest_trace(
        self, 
        bugs_table: str, 
        bugs_identifier: str,
        sead_table: str | None = None
    ) -> BugsTrace | None:
        """
        Query bugs_trace for most recent import of this Bugs entity.
        
        Args:
            bugs_table: Source table name (e.g., 'TBiblio')
            bugs_identifier: Bugs primary key or compressed identifier
            sead_table: Optional SEAD table name filter
            
        Returns:
            BugsTrace with (sead_table, sead_id, change_date) or None
        """
        query = """
            SELECT sead_table, sead_id, change_date, type
            FROM bugs_import.bugs_trace
            WHERE bugs_table = :bugs_table
              AND bugs_identifier = :bugs_id
        """
        if sead_table:
            query += " AND sead_table = :sead_table"
        query += " ORDER BY change_date DESC LIMIT 1"
        
        result = self.execute(query, {...})
        return BugsTrace.from_row(result) if result else None
    
    def get_sead_entity_from_trace(
        self, 
        trace: BugsTrace,
        entity_repository: type[SeadRepository]
    ) -> SeadEntity | None:
        """Fetch SEAD entity by ID from trace record."""
        return entity_repository.find_by_id(trace.sead_id)
```

**Integration with Reconciliation:**
```python
class TraceBasedReconciliationResolver(ReconciliationSourceResolver):
    """Use bugs_trace table as primary reconciliation source."""
    
    async def resolve(
        self, 
        entity_name: str, 
        entity_spec: EntityReconciliationSpec
    ) -> list[dict]:
        """
        Query entity preview data and augment with trace lookups.
        
        For each row:
        1. Extract bugs_identifier (from config-defined key columns)
        2. Query bugs_trace for prior import
        3. If found: set sead_id, confidence=1.0, status='trace-matched'
        4. If not found: proceed to OpenRefine reconciliation
        """
        source_data = await self._get_source_data(entity_name)
        trace_service = TraceLookupService(self.db_session)
        
        for row in source_data:
            bugs_id = self._build_bugs_identifier(row, entity_spec)
            trace = trace_service.get_latest_trace(
                bugs_table=entity_spec.bugs_table_name,  # NEW config field
                bugs_identifier=bugs_id,
                sead_table=entity_spec.sead_table_name   # NEW config field
            )
            
            if trace:
                row['sead_id'] = trace.sead_id
                row['confidence'] = 1.0
                row['match_status'] = 'trace-matched'
                row['notes'] = f'From prior import on {trace.change_date}'
            else:
                row['sead_id'] = None
                row['match_status'] = 'needs-reconciliation'
        
        return source_data
```

**Configuration Extension:**
```yaml
reconciliation:
  version: "2.0"
  service_url: "http://localhost:8000/reconcile"
  
  entities:
    site:
      site_id:
        # NEW: Trace configuration
        trace:
          enabled: true
          bugs_table_name: "TSite"
          sead_table_name: "tbl_sites"
          identifier_columns: ["SiteCode"]  # Build bugs_identifier from these
          compressed: false  # Use compressed format (col1:val1|col2:val2)
        
        # Existing reconciliation config
        source: null  # Use entity preview
        remote:
          service_type: "site"
        property_mappings:
          latitude: "Latitude"
          longitude: "Longitude"
```

**Effort**: 1 week

---

### 2. Priority-Ordered Search Rules

**Java Implementation:**
```java
// Spring autowires these in priority order via @Order annotation
@Autowired
private List<RdbSearch> searchStrategies;

@Override
public Rdb convertForDataRow(BugsRDB bugsData) {
    for (RdbSearch search : searchStrategies) {
        Rdb found = search.findFor(bugsData);
        if (found != NO_ENTITY_FOUND) {
            return update(found, bugsData);
        }
    }
    return create(bugsData);  // Not found in any strategy
}
```

**Shape Shifter Gap:**
- ❌ No search rule chain framework
- ✅ Has single reconciliation service call
- ❌ No fallback/priority logic

**Proposed Solution:**
```python
from abc import ABC, abstractmethod
from typing import Any, Generic, TypeVar

T = TypeVar('T')

class SearchRule(ABC, Generic[T]):
    """Base class for entity search strategies."""
    
    priority: int = 999  # Lower = higher priority
    
    @abstractmethod
    def find_for(self, source_row: dict[str, Any]) -> T | None:
        """Search for entity. Return None if not found."""
        pass

class TraceBasedSearch(SearchRule[int]):
    """Priority 1: Check bugs_trace table."""
    
    priority = 1
    
    def __init__(self, trace_service: TraceLookupService, entity_spec: EntityReconciliationSpec):
        self.trace_service = trace_service
        self.entity_spec = entity_spec
    
    def find_for(self, source_row: dict) -> int | None:
        bugs_id = build_identifier(source_row, self.entity_spec.trace.identifier_columns)
        trace = self.trace_service.get_latest_trace(
            self.entity_spec.trace.bugs_table_name,
            bugs_id,
            self.entity_spec.trace.sead_table_name
        )
        return trace.sead_id if trace else None

class ReconciliationServiceSearch(SearchRule[ReconciliationCandidate]):
    """Priority 2: Call OpenRefine reconciliation API."""
    
    priority = 2
    
    def __init__(self, client: ReconciliationClient, entity_spec: EntityReconciliationSpec):
        self.client = client
        self.entity_spec = entity_spec
    
    def find_for(self, source_row: dict) -> ReconciliationCandidate | None:
        query = build_reconciliation_query(source_row, self.entity_spec)
        results = self.client.reconcile([query])
        
        if results and results[0].candidates:
            top = results[0].candidates[0]
            if top.score >= self.entity_spec.auto_accept_threshold:
                return top
        return None

class ManualMappingSearch(SearchRule[int]):
    """Priority 3: Check user-defined manual mappings."""
    
    priority = 3
    
    def __init__(self, entity_spec: EntityReconciliationSpec):
        self.mappings = {m.source_value: m.sead_id for m in entity_spec.mapping}
    
    def find_for(self, source_row: dict) -> int | None:
        target_value = source_row.get(self.entity_spec.target_field)
        return self.mappings.get(target_value)

# Orchestrator
class SearchRuleEngine:
    """Execute search rules in priority order."""
    
    def __init__(self, rules: list[SearchRule]):
        self.rules = sorted(rules, key=lambda r: r.priority)
    
    def search(self, source_row: dict) -> tuple[Any, str]:
        """
        Run search rules until match found.
        
        Returns:
            (matched_entity, strategy_name) or (None, 'not-found')
        """
        for rule in self.rules:
            result = rule.find_for(source_row)
            if result is not None:
                return result, rule.__class__.__name__
        
        return None, 'not-found'
```

**Usage in ReconciliationService:**
```python
class ReconciliationService:
    
    async def reconcile_entity(
        self, 
        project_name: str,
        entity_name: str,
        target_field: str,
        use_trace: bool = True
    ) -> ReconciliationResult:
        """Reconcile entity with priority search rules."""
        
        entity_spec = self._get_entity_spec(project_name, entity_name, target_field)
        source_data = await self._resolve_source(entity_name, entity_spec)
        
        # Build search rule chain
        rules: list[SearchRule] = []
        
        if use_trace and entity_spec.trace.enabled:
            rules.append(TraceBasedSearch(self.trace_service, entity_spec))
        
        rules.append(ReconciliationServiceSearch(self.recon_client, entity_spec))
        
        if entity_spec.mapping:
            rules.append(ManualMappingSearch(entity_spec))
        
        engine = SearchRuleEngine(rules)
        
        # Apply search rules
        results = []
        for row in source_data:
            sead_id, strategy = engine.search(row)
            results.append({
                **row,
                'sead_id': sead_id,
                'match_strategy': strategy,
                'confidence': 1.0 if strategy == 'TraceBasedSearch' else 0.0
            })
        
        return ReconciliationResult(rows=results, ...)
```

**Effort**: 3-4 days

---

### 3. Field-Level Transformation Rules

**Java Implementation:**
```java
@Component
public class RdbUpdater {
    
    @Autowired
    private TaxonomicOrderRepository taxonomicOrderRepository;
    @Autowired
    private LocationRepository locationRepository;
    
    public void update(Rdb original, BugsRDB bugsData) {
        new Updater(original, bugsData).update();
    }
    
    private class Updater {
        void update() {
            boolean changed = false;
            changed |= setSpecies();    // Lookup by Bugs code
            changed |= setCountry();    // Lookup by ISO code
            changed |= setReference();  // Lookup by title
            
            if (changed) {
                original.markAsUpdated();
            }
        }
        
        private boolean setSpecies() {
            String bugsCode = bugsData.getSpeciesCode();
            TaxaSpecies species = taxonomicOrderRepository.findByBugsCode(bugsCode);
            if (species == null) {
                original.addError("Species not found: " + bugsCode);
                return false;
            }
            boolean changed = !Objects.equals(original.getSpecies(), species);
            original.setSpecies(species);
            return changed;
        }
    }
}
```

**Shape Shifter Gap:**
- ✅ Has column-level translations (`options.translations`)
- ✅ Has reconciliation for ID lookups
- ❌ No field-level transformation rules (lookups, validations, enrichments)
- ❌ No conditional transformation logic

**Proposed Solution:**

Add transformation rules to entity configuration:

```yaml
entities:
  rdb:
    type: data
    source: null
    columns:
      - code
      - species_code
      - country_code
      - reference_title
    
    # NEW: Field transformations
    transformations:
      - field: species_id
        type: lookup
        lookup:
          entity: species           # Look up in 'species' entity
          match_on:
            source: species_code    # Source column
            target: bugs_code       # Target column in species entity
          return: species_id        # Return this column value
          on_not_found: error       # error | warn | null
        
      - field: location_id
        type: lookup
        lookup:
          entity: country
          match_on:
            source: country_code
            target: iso_code
          return: location_id
      
      - field: biblio_id
        type: reconciliation
        reconciliation:
          service_type: "bibliography"
          query_field: reference_title
          auto_accept_threshold: 0.98
      
      - field: rdb_code
        type: expression
        expression: "upper(code.strip())"  # Python expression
      
      - field: last_updated
        type: constant
        value: "now()"
```

**Implementation:**
```python
from abc import ABC, abstractmethod
from typing import Any

class FieldTransformer(ABC):
    """Base class for field transformations."""
    
    @abstractmethod
    async def transform(self, row: dict[str, Any], field_name: str) -> Any:
        """Transform field value. Return new value or raise error."""
        pass

class LookupTransformer(FieldTransformer):
    """Lookup value from another entity."""
    
    def __init__(self, config: dict, project: ShapeShiftProject):
        self.config = config
        self.project = project
        self._cache = {}  # Cache lookup results
    
    async def transform(self, row: dict, field_name: str) -> Any:
        source_col = self.config['match_on']['source']
        target_col = self.config['match_on']['target']
        return_col = self.config['return']
        
        source_value = row.get(source_col)
        if source_value is None:
            return None
        
        # Check cache
        cache_key = f"{source_col}:{source_value}"
        if cache_key in self._cache:
            return self._cache[cache_key]
        
        # Lookup in target entity
        target_entity = self.config['entity']
        lookup_data = await self._get_entity_data(target_entity)
        
        for lookup_row in lookup_data:
            if lookup_row.get(target_col) == source_value:
                result = lookup_row.get(return_col)
                self._cache[cache_key] = result
                return result
        
        # Not found
        on_not_found = self.config.get('on_not_found', 'error')
        if on_not_found == 'error':
            raise ValueError(f"Lookup failed: {source_value} not found in {target_entity}")
        elif on_not_found == 'warn':
            logger.warning(f"Lookup failed: {source_value}")
            return None
        else:
            return None

class ExpressionTransformer(FieldTransformer):
    """Evaluate Python expression."""
    
    def __init__(self, expression: str):
        self.expression = expression
    
    async def transform(self, row: dict, field_name: str) -> Any:
        # Safely evaluate expression with row context
        # Use ast.literal_eval or restricted eval for safety
        return eval(self.expression, {"__builtins__": {}}, row)

class TransformationEngine:
    """Orchestrate field transformations."""
    
    def __init__(self, transformations: list[dict], project: ShapeShiftProject):
        self.transformers: dict[str, FieldTransformer] = {}
        
        for tf_config in transformations:
            field = tf_config['field']
            tf_type = tf_config['type']
            
            if tf_type == 'lookup':
                self.transformers[field] = LookupTransformer(
                    tf_config['lookup'], project
                )
            elif tf_type == 'expression':
                self.transformers[field] = ExpressionTransformer(
                    tf_config['expression']
                )
            elif tf_type == 'reconciliation':
                self.transformers[field] = ReconciliationTransformer(
                    tf_config['reconciliation']
                )
    
    async def transform_rows(self, rows: list[dict]) -> list[dict]:
        """Apply all transformations to each row."""
        transformed = []
        
        for row in rows:
            new_row = row.copy()
            
            for field_name, transformer in self.transformers.items():
                try:
                    new_row[field_name] = await transformer.transform(row, field_name)
                except Exception as e:
                    new_row['_errors'] = new_row.get('_errors', [])
                    new_row['_errors'].append(f"{field_name}: {str(e)}")
            
            transformed.append(new_row)
        
        return transformed
```

**Usage:**
```python
class ShapeShiftService:
    
    async def preview_entity(
        self, 
        project_name: str, 
        entity_name: str,
        apply_transformations: bool = True
    ) -> PreviewResult:
        """Generate entity preview with optional transformations."""
        
        # Existing preview logic
        config = self._load_config(project_name)
        entity_cfg = config.tables[entity_name]
        
        # Extract data
        data = await self._extract_entity_data(entity_name, entity_cfg)
        
        # NEW: Apply transformations
        if apply_transformations and entity_cfg.transformations:
            engine = TransformationEngine(
                entity_cfg.transformations, 
                config
            )
            data = await engine.transform_rows(data)
        
        return PreviewResult(rows=data, ...)
```

**Effort**: 1 week

---

### 4. Incremental Update Detection

**Java Implementation:**
```java
// Trace helper checks if SEAD entity was edited since last import
public SeadType getFromLastTrace(String traceIdentifier) {
    BugsTrace latest = getLatest(traceIdentifier);
    SeadType seadData = getSeadDataFromTrace(latest);
    
    if (seadDataExistsAndHasBeenEditedSinceImport(seadData, latest)) {
        seadData.addError("SEAD data updated since last import");
    }
    
    return seadData;
}

private static boolean seadDataExistsAndHasBeenEditedSinceImport(
    LoggableEntity entity, 
    BugsTrace trace
) {
    return entity != null 
        && entity.getDateUpdated().isAfter(trace.getChangeDate());
}
```

**Shape Shifter Gap:**
- ❌ No concept of "insert vs update" detection
- ❌ No warnings for SEAD entities edited outside of import process

**Proposed Solution:**

Extend trace lookup to include update detection:

```python
@dataclass
class TraceMatchResult:
    """Result of trace-based lookup."""
    
    sead_id: int
    sead_table: str
    trace_date: datetime
    sead_updated_at: datetime | None
    operation: str  # 'INSERT' | 'UPDATE' | 'UNCHANGED'
    has_conflict: bool  # True if SEAD entity edited since last import
    conflict_message: str | None

class TraceLookupService:
    
    def get_match_with_conflict_detection(
        self,
        bugs_table: str,
        bugs_identifier: str,
        sead_table: str
    ) -> TraceMatchResult | None:
        """
        Lookup trace and check for conflicts.
        
        Detects:
        1. If entity exists in prior import (trace found)
        2. If SEAD entity was manually edited since import
        3. Recommended operation (INSERT/UPDATE/SKIP)
        """
        trace = self.get_latest_trace(bugs_table, bugs_identifier, sead_table)
        if not trace:
            return None  # Not previously imported
        
        # Query SEAD entity's last update timestamp
        sead_entity = self._query_sead_entity(sead_table, trace.sead_id)
        
        if not sead_entity:
            # Entity was deleted in SEAD
            return TraceMatchResult(
                sead_id=trace.sead_id,
                sead_table=sead_table,
                trace_date=trace.change_date,
                sead_updated_at=None,
                operation='INSERT',  # Re-insert
                has_conflict=True,
                conflict_message=f"Entity deleted in SEAD since import"
            )
        
        # Check if SEAD entity edited after import
        has_conflict = (
            sead_entity.date_updated 
            and sead_entity.date_updated > trace.change_date
        )
        
        return TraceMatchResult(
            sead_id=trace.sead_id,
            sead_table=sead_table,
            trace_date=trace.change_date,
            sead_updated_at=sead_entity.date_updated,
            operation='UPDATE',
            has_conflict=has_conflict,
            conflict_message=(
                f"SEAD entity edited on {sead_entity.date_updated} "
                f"(after import on {trace.change_date})"
                if has_conflict else None
            )
        )
```

**Configuration:**
```yaml
reconciliation:
  entities:
    site:
      site_id:
        trace:
          enabled: true
          conflict_handling: warn  # warn | error | overwrite | skip
```

**Output Enhancement:**

Add conflict detection columns to CSV/Excel output:

```csv
site_code,site_name,sead_id,_operation,_has_conflict,_conflict_message
AGE001,Ageröd,1234,UPDATE,true,SEAD entity edited on 2024-12-15 (after import on 2024-11-01)
NEW001,New Site,,INSERT,false,
```

**Effort**: 2-3 days

---

## Migration Strategy

### Phase 1: Infrastructure (Week 1)

**Tasks:**
1. Add `bugs_import` schema to Shape Shifter database connections
2. Implement `TraceLookupService` with SQLAlchemy models for `bugs_trace`
3. Create `SearchRule` base class and `SearchRuleEngine`
4. Extend reconciliation configuration schema with `trace` section
5. Unit tests for trace lookup and search rule priority

**Deliverables:**
- Working trace lookup service
- Search rule framework with 3 example rules
- Updated configuration schema documentation

### Phase 2: Reconciliation Integration (Week 2)

**Tasks:**
1. Implement `TraceBasedReconciliationResolver`
2. Add trace search rule to reconciliation service
3. Update auto-reconciliation workflow to use trace-first approach
4. Add conflict detection logic
5. Integration tests with sample `bugs_trace` data

**Deliverables:**
- Trace-based reconciliation working end-to-end
- Conflict detection in preview/export
- Updated reconciliation workflow documentation

### Phase 3: Field Transformations (Week 3)

**Tasks:**
1. Implement `FieldTransformer` base class
2. Create `LookupTransformer`, `ExpressionTransformer`, `ReconciliationTransformer`
3. Extend entity configuration schema with `transformations` section
4. Add transformation engine to entity preview pipeline
5. Error handling and validation

**Deliverables:**
- Working transformation engine
- 3+ transformer types implemented
- Example configurations for common Bugs transformations

### Phase 4: Testing & Documentation (Week 4)

**Tasks:**
1. Create test dataset from real BugsCEP data
2. Build 2-3 example migration configs (Site, Bibliography, RDB)
3. Comprehensive integration tests
4. Performance testing (compare with Java import)
5. User documentation and migration guide

**Deliverables:**
- Full test suite passing
- Migration guide with examples
- Performance benchmarks
- API documentation

---

## Configuration Migration Examples

### Example 1: Simple Entity (Bibliography)

**Java Code:**
```java
@Component
public class BibliographyRowConverter {
    @Override
    public Biblio convertForDataRow(BugsBiblio bugsData) {
        Biblio fromTrace = traceHelper.getFromLastTrace(bugsData.getBugsIdentifier());
        if (fromTrace == null) {
            return createOrReadFromBugsAuthor(bugsData);
        }
        return update(fromTrace, bugsData);
    }
    
    private Biblio createOrReadFromBugsAuthor(BugsBiblio bugsData) {
        Biblio fromSead = repo.getByBugsReferenceIgnoreCase(bugsData.getReference());
        return fromSead != null ? update(fromSead, bugsData) : update(new Biblio(), bugsData);
    }
}
```

**Shape Shifter Config:**
```yaml
entities:
  bibliography:
    type: data
    source: null
    keys: [BugsReference]
    surrogate_id: biblio_id
    columns:
      - BugsReference
      - Autor
      - Year
      - Title
      - FullReference
    
    reconciliation:
      biblio_id:
        trace:
          enabled: true
          bugs_table_name: "TBiblio"
          sead_table_name: "tbl_biblio"
          identifier_columns: [BugsReference]
        
        remote:
          service_type: "bibliography"
        
        auto_accept_threshold: 0.95
```

### Example 2: Complex Entity with Lookups (RDB)

**Java Code:**
```java
@Component
public class RdbUpdater {
    void update(Rdb original, BugsRDB bugsData) {
        // Lookup species by Bugs code
        TaxaSpecies species = taxonomicOrderRepository.findByBugsCode(bugsData.getSpeciesCode());
        original.setSpecies(species);
        
        // Lookup country
        Location country = locationRepository.findByIsoCode(bugsData.getCountryCode());
        original.setLocation(country);
    }
}
```

**Shape Shifter Config:**
```yaml
entities:
  rdb:
    type: data
    source: null
    keys: [TRdb#]
    surrogate_id: rdb_id
    columns:
      - TRdb#
      - CODE
      - SpeciesCode
      - CountryCode
    
    # Field transformations
    transformations:
      - field: species_id
        type: lookup
        lookup:
          entity: species
          match_on:
            source: SpeciesCode
            target: bugs_code
          return: species_id
          on_not_found: error
      
      - field: location_id
        type: lookup
        lookup:
          entity: country
          match_on:
            source: CountryCode
            target: iso_code
          return: location_id
          on_not_found: warn
    
    reconciliation:
      rdb_id:
        trace:
          enabled: true
          bugs_table_name: "TRdb"
          sead_table_name: "tbl_rdb"
          identifier_columns: [TRdb#]
```

### Example 3: Multi-Stage Reconciliation (Site)

**Java Code:**
```java
public SeadSite convertForDataRow(BugsSite bugsData) {
    // 1. Check trace
    SeadSite fromTrace = traceHelper.getFromLastTrace(bugsData.getBugsIdentifier());
    if (fromTrace != null) return update(fromTrace, bugsData);
    
    // 2. Search by site code
    SeadSite bySiteCode = siteRepository.findBySiteCode(bugsData.getSiteCode());
    if (bySiteCode != null) return update(bySiteCode, bugsData);
    
    // 3. Reconcile by name + coordinates
    SeadSite byReconciliation = reconciliationService.findSite(
        bugsData.getSiteName(), 
        bugsData.getLatitude(), 
        bugsData.getLongitude()
    );
    return update(byReconciliation != null ? byReconciliation : new SeadSite(), bugsData);
}
```

**Shape Shifter Config:**
```yaml
entities:
  site:
    type: data
    source: null
    keys: [SiteCode]
    surrogate_id: site_id
    columns:
      - SiteCode
      - SiteName
      - Latitude
      - Longitude
      - CountryCode
    
    transformations:
      - field: location_id
        type: lookup
        lookup:
          entity: country
          match_on: {source: CountryCode, target: iso_code}
          return: location_id
    
    reconciliation:
      site_id:
        # Search rule priority automatically determined:
        # 1. Trace (priority=1)
        # 2. OpenRefine reconciliation (priority=2)
        # 3. Manual mappings (priority=3)
        
        trace:
          enabled: true
          bugs_table_name: "TSite"
          sead_table_name: "tbl_sites"
          identifier_columns: [SiteCode]
          conflict_handling: warn
        
        remote:
          service_type: "site"
        
        property_mappings:
          name: SiteName
          latitude: Latitude
          longitude: Longitude
        
        auto_accept_threshold: 0.90
        review_threshold: 0.70
```

---

## Testing Strategy

### Unit Tests

```python
# Test trace lookup
def test_trace_lookup_service():
    service = TraceLookupService(db_session)
    
    # Insert test trace
    trace = BugsTrace(
        bugs_table='TBiblio',
        bugs_identifier='REF001',
        sead_table='tbl_biblio',
        sead_id=123,
        change_date=datetime(2024, 1, 1)
    )
    db_session.add(trace)
    db_session.commit()
    
    # Test lookup
    result = service.get_latest_trace('TBiblio', 'REF001', 'tbl_biblio')
    assert result is not None
    assert result.sead_id == 123

# Test search rule priority
def test_search_rule_engine():
    trace_rule = TraceBasedSearch(priority=1)
    recon_rule = ReconciliationServiceSearch(priority=2)
    
    engine = SearchRuleEngine([recon_rule, trace_rule])  # Wrong order
    
    # Engine should sort by priority
    assert engine.rules[0].priority == 1
    assert engine.rules[1].priority == 2

# Test field transformation
@pytest.mark.asyncio
async def test_lookup_transformer():
    config = {
        'entity': 'species',
        'match_on': {'source': 'species_code', 'target': 'bugs_code'},
        'return': 'species_id',
        'on_not_found': 'error'
    }
    
    transformer = LookupTransformer(config, project)
    row = {'species_code': 'BUG001'}
    
    result = await transformer.transform(row, 'species_id')
    assert result == 456  # Expected species_id
```

### Integration Tests

```python
@pytest.mark.asyncio
async def test_end_to_end_reconciliation_with_trace():
    """Test full reconciliation workflow with trace lookup."""
    
    # 1. Setup: Create project config
    project = create_test_project('bugs_import_test')
    
    # 2. Load MS Access data
    access_file = 'tests/data/sample_bugs.mdb'
    config = project.load_config()
    
    # 3. Extract entity preview
    service = ShapeShiftService(project_service)
    preview = await service.preview_entity('bugs_import_test', 'bibliography')
    
    assert len(preview.rows) > 0
    
    # 4. Run reconciliation
    recon_service = ReconciliationService(project_service)
    result = await recon_service.reconcile_entity(
        'bugs_import_test', 
        'bibliography', 
        'biblio_id',
        use_trace=True
    )
    
    # 5. Verify trace matches prioritized
    trace_matched = [r for r in result.rows if r['match_strategy'] == 'TraceBasedSearch']
    assert len(trace_matched) > 0
    
    # 6. Export to CSV
    export_path = await service.export_entity('bugs_import_test', 'bibliography', format='csv')
    
    df = pd.read_csv(export_path)
    assert 'biblio_id' in df.columns
    assert '_operation' in df.columns  # INSERT/UPDATE
```

---

## Risk Mitigation

### Risk 1: Performance Impact of Trace Lookups

**Mitigation:**
- Index `bugs_trace` on `(bugs_table, bugs_identifier, sead_table)`
- Implement in-memory LRU cache for trace lookups
- Batch trace queries (1 query for all entity rows vs. N queries)

**Benchmark Target**: <100ms for trace lookup of 1000 entities

### Risk 2: Conflict Handling Complexity

**Mitigation:**
- Start with `conflict_handling: warn` (non-blocking)
- Provide clear conflict reports in CSV output
- Document escalation process for conflicts

### Risk 3: Transformation Rule Complexity

**Mitigation:**
- Limit initial scope to `lookup` and `expression` transformers
- Defer complex transformations (hierarchical lookups, conditional logic)
- Use Python expressions cautiously (validate safety)

### Risk 4: Configuration Migration Burden

**Mitigation:**
- Create migration tool: `bugs_importer_to_shapeshifter.py`
- Auto-generate 80% of config from Java source analysis
- Provide templates for common patterns

---

## Success Criteria

### Must Have (Week 4)
- ✅ Trace-based reconciliation working for 3+ entity types
- ✅ Search rule priority working correctly
- ✅ Field transformations (lookup, expression) functional
- ✅ Conflict detection working
- ✅ CSV export with reconciliation metadata
- ✅ Documentation and examples

### Should Have (Post-MVP)
- ⚠️ Reconciliation UI showing trace matches
- ⚠️ Transformation rule validation/testing
- ⚠️ Bulk trace query optimization
- ⚠️ Migration wizard for Java → YAML configs

### Nice to Have (Future)
- 💡 Trace-based reconciliation for all 36 Bugs entities
- 💡 Complex transformation rules (hierarchical lookups)
- 💡 Reconciliation confidence scoring based on trace history
- 💡 Automated regression testing against Java import

---

## Next Steps

### Immediate Actions (Week 1)

1. **Stakeholder Review** (1-2 days)
   - Present this plan to SEAD team
   - Confirm scope and priorities
   - Identify pilot entity types for testing

2. **Environment Setup** (1 day)
   - Setup `bugs_import` schema access
   - Load sample `bugs_trace` data
   - Prepare test BugsCEP database

3. **Sprint Planning** (1 day)
   - Break down tasks into 2-day sprints
   - Assign developers
   - Setup progress tracking

4. **Prototype** (2-3 days)
   - Implement minimal trace lookup
   - Test with Bibliography entity
   - Validate approach

### Decision Points

**Go/No-Go Decision (End of Week 1):**
- Prototype working?
- Performance acceptable?
- Team capacity confirmed?

**Pilot Success (End of Week 2):**
- 1 entity end-to-end working
- Reconciliation accuracy >95%
- Configuration manageable

**Production Readiness (End of Week 4):**
- 3+ entities working
- All tests passing
- Documentation complete
- Team trained

---

## Appendix A: Glossary

- **Trace**: Record in `bugs_import.bugs_trace` linking Bugs entity to SEAD entity
- **Search Rule**: Strategy for finding SEAD entity match (trace, database, reconciliation)
- **Field Transformer**: Rule for converting/enriching field values
- **Reconciliation**: Process of matching local data to authoritative SEAD entities
- **Conflict**: Situation where SEAD entity was edited after Bugs import

## Appendix B: References

- Java source: `/home/roger/source/sead_bugs_import/`
- Python target: `/home/roger/source/sead_shape_shifter/`
- Existing reconciliation: `backend/app/services/reconciliation_service.py`
- User guide: `docs/RECONCILIATION_WORKFLOW.md`

## Appendix C: Effort Summary

| Component | Effort | Risk |
|-----------|--------|------|
| Trace Integration | 1 week | Low |
| Search Rule Framework | 3-4 days | Low |
| Field Transformations | 1 week | Medium |
| Conflict Detection | 2-3 days | Low |
| Testing & Documentation | 1 week | Low |
| **TOTAL** | **3-4 weeks** | **Low-Medium** |

---

## Appendix D: Required OpenRefine Entity Types for Bugs Import

This section catalogs the additional entity types needed in `sead_authority_service` to support automatic reconciliation of all entities currently handled by `sead_bugs_import`.

### Analysis Methodology

Entity types were identified by:
1. Analyzing all `*Importer.java` classes in sead_bugs_import (36 importers)
2. Examining repository `findBy*` methods to understand reconciliation keys
3. Reviewing search strategy implementations (`*Search.java` classes)
4. Cross-referencing with SEAD database schema (tbl_* tables)

### Current State: sead_authority_service Entity Types

**Already Implemented** (27 types):
- ✅ `bibliographic_reference` (tbl_biblio) - with DOI, ISBN, title, authors, year
- ✅ `data_type` (tbl_data_types) - with data type group relationship
- ✅ `data_type_group` (tbl_data_type_groups)
- ✅ `dating_uncertainty` (tbl_dating_uncertainty)
- ✅ `feature_type` (tbl_feature_types)
- ✅ `feature` (tbl_features) - with feature type relationship
- ✅ `location_type` (tbl_location_types)
- ✅ `location` (tbl_locations) - with lat/long, location type relationship
- ✅ `method_group` (tbl_method_groups)
- ✅ `method` (tbl_methods) - with method abbreviation, method group relationship
- ✅ `modification_type` (tbl_modification_types)
- ✅ `record_type` (tbl_record_types)
- ✅ `relative_age_type` (tbl_relative_age_types)
- ✅ `relative_age` (tbl_relative_ages) - with abbreviation, relative age type relationship
- ✅ `site` (tbl_sites) - with national site identifier, lat/long, location relationships
- ✅ `sampling_context` (tbl_sample_group_sampling_contexts)
- ✅ `sample_group_description_type` (tbl_sample_group_description_types)
- ✅ `sample_description_type` (tbl_sample_description_types)
- ✅ `sample_location_type` (tbl_sample_location_types)
- ✅ `sample_type` (tbl_sample_types)
- ✅ `taxa_tree_author` (tbl_taxa_tree_authors)
- ✅ `taxa_tree_order` (tbl_taxa_tree_orders) - with record type relationship
- ✅ `taxa_tree_family` (tbl_taxa_tree_families) - with order relationship
- ✅ `taxa_tree_genus` (tbl_taxa_tree_genera) - with family relationship
- ✅ `taxa_tree_master` (tbl_taxa_tree_master) - with genus relationship
- ✅ `taxonomic_order_system` (tbl_taxonomic_order_systems)
- ✅ `taxonomy_note` (tbl_taxonomy_notes)
- ✅ `taxa_synonym` (tbl_taxa_synonyms) - with taxon relationship

### Missing Entity Types Required for Bugs Import

The following entity types need to be added to `sead_authority_service` to support full Bugs import reconciliation:

#### 1. **Dating Lab** (`dating_lab`)
**Table:** `tbl_dating_labs`  
**Primary Key:** `lab_id` (String/VARCHAR)  
**Label:** `lab_name`  
**Reconciliation Key:** `lab_id` (unique identifier like "Beta", "Ua", "GrA")  
**Used By:** `datesradio/` importer (radiocarbon dates)  
**Repository Method:** `DatingLabRepository.findByLabId(String labId)`  
**Search Strategy:** `DatingLabByLabIdSearch` (@Order 2)

**Notes:** 
- Lab IDs are semi-standardized codes (e.g., "Ua" = Uppsala, "Beta" = Beta Analytic)
- Critical for radiocarbon date provenance tracking
- Low cardinality (~50-100 labs globally)

#### 2. **RDB System** (`rdb_system`)
**Table:** `tbl_rdb_systems`  
**Primary Key:** `system_id`  
**Label:** `system_name`  
**Description:** `system_description`  
**Reconciliation Key:** `system_name` (e.g., "Koch 1989", "Appendix")  
**Used By:** `rdb/`, `rdbsystems/` importers (ecological data)  
**Repository Method:** Not explicitly named (CREATE_AND_READ pattern)  
**Search Strategy:** Trace-based only

**Notes:**
- RDB (Red Data Book) systems categorize species conservation status
- Typical names: "Koch 1989", "Appendix" (CITES appendices)
- Usually 5-10 systems in use

#### 3. **RDB Code** (`rdb_code`)
**Table:** `tbl_rdb_codes`  
**Primary Key:** `rdb_code_id`  
**Label:** `code` (e.g., "EN", "VU", "CR")  
**Description:** `definition`  
**Reconciliation Keys:**
  - `category` + `definition` + `system_id` (composite key)
**Used By:** `rdbcodes/` importer  
**Repository Method:** `RdbCodeRepository.findByCategoryAndDefinitionAndSystem()`  
**Search Strategy:** Trace + composite database lookup

**Notes:**
- Conservation status codes (EN=Endangered, VU=Vulnerable, CR=Critically Endangered)
- Codes vary by RDB system
- Requires fuzzy matching on definition text

#### 4. **Ecocode System** (`ecocode_system`)
**Table:** `tbl_ecocode_systems`  
**Primary Key:** `ecocode_system_id`  
**Label:** `name` (e.g., "Koch" for beetles, "Ellenberg" for plants)  
**Description:** `definition`  
**Reconciliation Key:** `name`  
**Used By:** `ecocodedefinitiongroups/` importer  
**Repository Method:** `EcocodeSystemRepository` (READ_ONLY)  
**Search Strategy:** Trace-based only

**Notes:**
- Ecocode systems define ecological indicator codes
- Common systems: "Koch" (Coleoptera), "Ellenberg" (plants)
- Low cardinality (~5-10 systems)

#### 5. **Ecocode Group** (`ecocode_group`)
**Table:** `tbl_ecocode_groups`  
**Primary Key:** `ecocode_group_id`  
**Label:** `name`  
**Reconciliation Keys:**
  - `system_id` + `abbreviation` (e.g., Koch system + "A")
**Used By:** `ecocodedefinitiongroups/` importer  
**Repository Method:** `EcocodeGroupRepository.findBySystemAndAbbreviation()`  
**Search Strategy:** Composite key lookup

**Notes:**
- Groups within ecocode systems (e.g., Koch group "A" = aquatic species)
- Abbreviations are single letters or short codes
- Medium cardinality (~20-30 groups per system)

#### 6. **Ecocode Definition** (`ecocode_definition`)
**Table:** `tbl_ecocode_definitions`  
**Primary Key:** `ecocode_definition_id`  
**Label:** `label` or `definition`  
**Reconciliation Keys:**
  - Complex (varies by system/group)
**Used By:** `ecocodedefinition/` importers (Koch, Bugs)  
**Repository Method:** `EcocodeDefinitionRepository` (CREATE_AND_READ)  
**Search Strategy:** Trace-based only

**Notes:**
- Detailed ecological indicator definitions
- High cardinality (~500-1000 definitions)
- Requires semantic matching due to description variations

#### 7. **MCR Name** (`mcr_name`)
**Table:** `tbl_mcr_names`  
**Primary Key:** `mcr_name_id`  
**Label:** `name`  
**Reconciliation Key:** `name` (unique MCR beetle names from Birmingham dataset)  
**Used By:** `mcrnames/` importer (Modern Coleoptera Records)  
**Repository Method:** `McrNamesRepository` (CREATE_AND_READ)  
**Search Strategy:** `TraceHelperSearch` (@Order 1)

**Notes:**
- MCR (Modern Coleoptera Records) specific naming system
- Birmingham beetle database integration
- Medium cardinality (~200-500 names)

#### 8. **Contact Type** (`contact_type`)
**Table:** `tbl_contact_types`  
**Primary Key:** `contact_type_id`  
**Label:** `contact_type_name`  
**Description:** `description`  
**Reconciliation Key:** `contact_type_name`  
**Used By:** `datasetcontacts/` importer  
**Repository Method:** `ContactTypeRepository` (CREATE_AND_READ)  
**Search Strategy:** Trace-based

**Notes:**
- Defines roles of people associated with datasets
- Examples: "Principal Investigator", "Data Collector", "Data Entry"
- Low cardinality (~10-20 types)

#### 9. **Season Type** (`season_type`)
**Table:** `tbl_season_types`  
**Primary Key:** `season_type_id`  
**Label:** `season_type`  
**Description:** `description`  
**Reconciliation Key:** `season_type`  
**Used By:** `taxaseasonality/` importer  
**Repository Method:** Part of Season composite key  
**Search Strategy:** Used in `SeasonRepository.findByNameAndType()`

**Notes:**
- Categorizes seasonal activity types
- Examples: "Breeding", "Migration", "Hibernation"
- Low cardinality (~5-10 types)

#### 10. **Season** (`season`)
**Table:** `tbl_seasons`  
**Primary Key:** `season_id`  
**Label:** `season_name`  
**Reconciliation Keys:**
  - `name` + `season_type_id` (composite)
**Used By:** `taxaseasonality/` importer  
**Repository Method:** `SeasonRepository.findByNameAndType()`  
**Search Strategy:** Composite key lookup

**Notes:**
- Seasonal periods (e.g., "Spring Breeding", "Winter Migration")
- Tied to season type
- Medium cardinality (~20-40 seasons)

#### 11. **Activity Type** (`activity_type`)
**Table:** `tbl_activity_types`  
**Primary Key:** `activity_type_id`  
**Label:** `activity_type`  
**Description:** `description`  
**Reconciliation Key:** `activity_type`  
**Used By:** `taxaseasonality/` importer (composite lookup)  
**Repository Method:** Part of TaxaSeasonality composite key  
**Search Strategy:** `TaxaSeasonalityRepository.findByLocationAndTypeAndSpeciesAndSeason()`

**Notes:**
- Categorizes biological activities
- Examples: "Feeding", "Breeding", "Larval Stage"
- Low cardinality (~10-15 types)

#### 12. **Species Association Type** (`species_association_type`)
**Table:** `tbl_species_association_types`  
**Primary Key:** `association_type_id`  
**Label:** `association_type_name`  
**Description:** `description`  
**Reconciliation Key:** `association_type_name`  
**Used By:** `speciesassociation/` importer  
**Repository Method:** `SpeciesAssociationTypeRepository.findByName()`  
**Search Strategy:** Name-based lookup

**Notes:**
- Defines ecological relationships between species
- Examples: "Host", "Parasite", "Predator", "Prey"
- Low cardinality (~10-20 types)

#### 13. **Alternative Reference Type** (`alternative_reference_type`)
**Table:** `tbl_alternative_ref_types`  
**Primary Key:** `alternative_ref_type_id`  
**Label:** `alternative_ref_type`  
**Description:** `description`  
**Reconciliation Key:** `alternative_ref_type`  
**Used By:** `sample/` importer (for alternative sample identifiers)  
**Repository Method:** `AlternativeReferenceTypeRepository` (READ_ONLY)  
**Search Strategy:** Trace-based

**Notes:**
- Defines types of alternative identifiers for samples
- Examples: "Original field code", "Lab code", "Legacy ID"
- Low cardinality (~5-10 types)

### Entity Types Not Requiring Reconciliation Service

The following entities are **created during import** and do not require pre-existing reconciliation:

- ❌ **Sample** (tbl_samples) - Always new, identified by composite keys  
- ❌ **Sample Group** (tbl_sample_groups) - Created per dataset  
- ❌ **Geochronology** (tbl_geochronology) - Dating measurements, always new  
- ❌ **Relative Date** (tbl_relative_dates) - Stratigraphic dates, always new  
- ❌ **Site Location** (tbl_site_locations) - Site-location links, derived  
- ❌ **Site Reference** (tbl_site_references) - Site-bibliography links, derived  
- ❌ **Dataset** (tbl_datasets) - Created per import, composite key lookup  
- ❌ **Dataset Contact** (tbl_dataset_contacts) - Links datasets to people  
- ❌ **Analysis Entity** (tbl_analysis_entities) - Measurement records  
- ❌ **Taxa Measured Attributes** (tbl_taxa_measured_attributes) - Morphometric data  
- ❌ **Text Biology** (tbl_text_biology) - Free text ecology notes  
- ❌ **Text Distribution** (tbl_text_distribution) - Geographic distribution text  
- ❌ **Text Identification Keys** (tbl_text_identification_keys) - Taxonomy keys  
- ❌ **Taxonomic Notes** (tbl_taxonomy_notes) - Taxonomic remarks  
- ❌ **Taxa Seasonality** (tbl_taxa_seasonality) - Seasonal activity data  
- ❌ **Species Association** (tbl_species_associations) - Ecological relationships  
- ❌ **RDB** (tbl_rdb) - Species conservation assessments (composite: species + country + code)  
- ❌ **Ecocode** (tbl_ecocodes) - Species ecological codes (composite: species + definition)  
- ❌ **MCR Summary** (tbl_mcr_summaries) - Modern Coleoptera Records summary  
- ❌ **BIRM Beetle Data** (tbl_birmingham_beetle_data) - Birmingham Museum data  
- ❌ **Site Other Record** (tbl_site_other_records) - Misc site proxy data

### Summary: Required Additions to sead_authority_service

**Total New Entity Types Needed: 13**

| Priority | Entity Type | Complexity | Cardinality | Reconciliation Strategy |
|----------|-------------|------------|-------------|------------------------|
| **HIGH** | `dating_lab` | Low | ~50-100 | Exact match on lab_id |
| **HIGH** | `rdb_system` | Low | ~5-10 | Name matching |
| **HIGH** | `ecocode_system` | Low | ~5-10 | Name matching |
| **MEDIUM** | `rdb_code` | Medium | ~100-200 | Composite key + fuzzy definition |
| **MEDIUM** | `ecocode_group` | Medium | ~20-30/system | System + abbreviation |
| **MEDIUM** | `ecocode_definition` | High | ~500-1000 | Semantic matching required |
| **MEDIUM** | `mcr_name` | Medium | ~200-500 | Exact name matching |
| **LOW** | `contact_type` | Low | ~10-20 | Name matching |
| **LOW** | `season_type` | Low | ~5-10 | Name matching |
| **LOW** | `season` | Low | ~20-40 | Name + type composite |
| **LOW** | `activity_type` | Low | ~10-15 | Name matching |
| **LOW** | `species_association_type` | Low | ~10-20 | Name matching |
| **LOW** | `alternative_reference_type` | Low | ~5-10 | Name matching |

**Implementation Recommendations:**

1. **Phase 1 (Week 1)**: Implement HIGH priority types
   - `dating_lab` - Critical for radiocarbon data
   - `rdb_system` - Required for conservation data
   - `ecocode_system` - Foundation for ecological codes

2. **Phase 2 (Week 2)**: Implement MEDIUM priority types  
   - `rdb_code` - Requires fuzzy matching implementation
   - `ecocode_group` - Simple composite key
   - `mcr_name` - Exact matching only

3. **Phase 3 (Week 3)**: Implement LOW priority types + semantic matching
   - All remaining lookup types (simple name matching)
   - `ecocode_definition` - Requires LLM/semantic matching for accurate reconciliation

4. **Testing Strategy**:
   - Load reference data from SEAD database into authority service
   - Test reconciliation with real Bugs data samples
   - Validate match accuracy vs current Java implementation
   - Benchmark performance (target: <100ms per entity reconciliation)

**Configuration Template** (entities.yml additions):

```yaml
dating_lab:
  name: "Dating Lab"
  table_name: "tbl_dating_labs"
  id_column: "lab_id"
  label_column: "lab_name"
  description_column: "country"
  alternate_identity_column: null
  materialized: false
  extra_columns:
    - "t.url"
    - "t.contact_person"
  embedding_config:
    dimension: 768
    ivfflat_lists: 10
    analyze: false

rdb_system:
  name: "RDB System"
  table_name: "tbl_rdb_systems"
  id_column: "system_id"
  label_column: "system_name"
  description_column: "system_description"
  alternate_identity_column: null
  materialized: false
  extra_columns: []
  embedding_config:
    dimension: 768
    ivfflat_lists: 10
    analyze: false

rdb_code:
  name: "RDB Code"
  table_name: "tbl_rdb_codes"
  id_column: "rdb_code_id"
  label_column: "code"
  description_column: "definition"
  alternate_identity_column: null
  type_or_grouping:
    foreign_table: "rdb_system"
    foreign_key_column: "system_id"
  materialized: false
  extra_columns:
    - "t.category"
    - "t.system_id"
  joins:
    - "join public.tbl_rdb_systems rs using (system_id)"
  embedding_config:
    dimension: 768
    ivfflat_lists: 50
    analyze: true  # Enable semantic search for fuzzy definition matching

ecocode_system:
  name: "Ecocode System"
  table_name: "tbl_ecocode_systems"
  id_column: "ecocode_system_id"
  label_column: "name"
  description_column: "definition"
  alternate_identity_column: null
  materialized: false
  extra_columns: []
  embedding_config:
    dimension: 768
    ivfflat_lists: 10
    analyze: false

ecocode_group:
  name: "Ecocode Group"
  table_name: "tbl_ecocode_groups"
  id_column: "ecocode_group_id"
  label_column: "name"
  description_column: "definition"
  alternate_identity_column: "abbreviation"
  type_or_grouping:
    foreign_table: "ecocode_system"
    foreign_key_column: "ecocode_system_id"
  materialized: false
  extra_columns:
    - "t.abbreviation"
    - "t.ecocode_system_id"
  joins:
    - "join public.tbl_ecocode_systems es using (ecocode_system_id)"
  embedding_config:
    dimension: 768
    ivfflat_lists: 20
    analyze: false

ecocode_definition:
  name: "Ecocode Definition"
  table_name: "tbl_ecocode_definitions"
  id_column: "ecocode_definition_id"
  label_column: "label"
  description_column: "definition"
  alternate_identity_column: null
  type_or_grouping:
    foreign_table: "ecocode_group"
    foreign_key_column: "ecocode_group_id"
  materialized: false
  extra_columns:
    - "t.abbreviation"
    - "t.ecocode_group_id"
  joins:
    - "join public.tbl_ecocode_groups eg using (ecocode_group_id)"
  embedding_config:
    dimension: 768
    ivfflat_lists: 100
    analyze: true  # Semantic search critical for ecological text matching

mcr_name:
  name: "MCR Name"
  table_name: "tbl_mcr_names"
  id_column: "mcr_name_id"
  label_column: "name"
  description_column: null
  alternate_identity_column: null
  materialized: false
  extra_columns: []
  embedding_config:
    dimension: 768
    ivfflat_lists: 50
    analyze: false

contact_type:
  name: "Contact Type"
  table_name: "tbl_contact_types"
  id_column: "contact_type_id"
  label_column: "contact_type_name"
  description_column: "description"
  alternate_identity_column: null
  materialized: false
  extra_columns: []
  embedding_config:
    dimension: 768
    ivfflat_lists: 10
    analyze: false

season_type:
  name: "Season Type"
  table_name: "tbl_season_types"
  id_column: "season_type_id"
  label_column: "season_type"
  description_column: "description"
  alternate_identity_column: null
  materialized: false
  extra_columns: []
  embedding_config:
    dimension: 768
    ivfflat_lists: 10
    analyze: false

season:
  name: "Season"
  table_name: "tbl_seasons"
  id_column: "season_id"
  label_column: "season_name"
  description_column: null
  alternate_identity_column: null
  type_or_grouping:
    foreign_table: "season_type"
    foreign_key_column: "season_type_id"
  materialized: false
  extra_columns:
    - "t.season_type_id"
  joins:
    - "join public.tbl_season_types st using (season_type_id)"
  embedding_config:
    dimension: 768
    ivfflat_lists: 20
    analyze: false

activity_type:
  name: "Activity Type"
  table_name: "tbl_activity_types"
  id_column: "activity_type_id"
  label_column: "activity_type"
  description_column: "description"
  alternate_identity_column: null
  materialized: false
  extra_columns: []
  embedding_config:
    dimension: 768
    ivfflat_lists: 10
    analyze: false

species_association_type:
  name: "Species Association Type"
  table_name: "tbl_species_association_types"
  id_column: "association_type_id"
  label_column: "association_type_name"
  description_column: "description"
  alternate_identity_column: null
  materialized: false
  extra_columns: []
  embedding_config:
    dimension: 768
    ivfflat_lists: 10
    analyze: false

alternative_reference_type:
  name: "Alternative Reference Type"
  table_name: "tbl_alternative_ref_types"
  id_column: "alternative_ref_type_id"
  label_column: "alternative_ref_type"
  description_column: "description"
  alternate_identity_column: null
  materialized: false
  extra_columns: []
  embedding_config:
    dimension: 768
    ivfflat_lists: 10
    analyze: false
```

**Notes on Embedding Configuration:**
- `analyze: true` - Enable for entities requiring semantic/fuzzy matching (rdb_code, ecocode_definition)
- `analyze: false` - Exact/simple matching sufficient (most lookup types)
- `ivfflat_lists` - Scaled by cardinality (10 for <20 items, 100 for >500 items)
- `dimension: 768` - Standard for sentence-transformers/all-mpnet-base-v2 model

---

**Document Version**: 1.1  
**Last Updated**: 2026-01-14  
**Author**: AI Migration Analyst  
**Status**: Draft for Review
