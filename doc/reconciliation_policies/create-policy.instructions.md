---
applyTo: "doc/reconciliation_policies/*.policy.yml"
---

# How to create a `{domain}.policy.yml` file

This file explains how to extract a reconciliation policy from the Java source
and write it as a `{domain}.policy.yml`.

For the format reference, see [`_schema.yml`](_schema.yml).  
For a worked example, see [`bibliography.policy.yml`](bibliography.policy.yml).

---

## 1 — Locate the Java source files

Each domain lives under `src/main/java/se/sead/bugsimport/{domain}/`.  
Collect these six files before writing any YAML:

| File | What it provides |
|---|---|
| `bugsmodel/Bugs{Entity}.java` | Source field names, identity method, trace-key method |
| `bugsmodel/Bugs{Entity}BugsTable.java` | The raw SQL query |
| `seadmodel/{SeadEntity}.java` | SEAD table name, column names, PK sequence |
| `{Domain}RowConverter.java` or `{Domain}BugsSeadMapper.java` | Reconciliation rules (in order) |
| `{Domain}Updater.java` or `{Domain}Manager.java` | Field mappings + update-detection fields |
| `{Domain}DataRepository.java` or `{Domain}TraceHelper.java` | Lookup queries |

---

## 2 — Fill in each policy section

### `meta`
```yaml
meta:
  domain:      # simple name, e.g. bibliography
  bugs_table:  # value returned by BugsTable.getTableName()
  sead_table:  # @Table(name="...") on the JPA entity class
  policy_version: "1.0"
```

---

### `source`

**`sql`** — find `createItem()` in `Bugs{Entity}BugsTable.java`:
```java
// Java
return new UCanAccessRecord("SELECT REFERENCE, AUTHOR, TITLE, NOTES FROM TBiblio");
```
→ copy the SQL string verbatim.

**`fields`** — one entry per `SELECT` column.  
Set `role: identity` for the column used as the business key.  
Set `role: ignored` for columns that are read but not written to SEAD (e.g. `NOTES`).  
All others: `role: data`.

**`trace_key.template`** — find `compressToString()`:
```java
// Java
return "REFERENCE:" + getReference();
```
→ write as `"REFERENCE:{REFERENCE}"`.

**`identity_key`** — find `getBugsIdentifier()` or `getIdentifier()`:
```java
// Java
return getReference();
```
→ write the field name this returns, e.g. `REFERENCE`.

---

### `target`

```yaml
target:
  table:    # from @Table(name="...") on the JPA entity
  pk:       # @Id field name, e.g. biblio_id
  sequence: # nextval sequence, from @SequenceGenerator annotation
```

---

### `mappings`

Find `setXxx()` calls in `{Domain}Updater.java`:
```java
// Java
entity.setAuthors(bugs.getAuthor());
entity.setTitle(bugs.getTitle());
```
→ one mapping entry per setter.

**Simple copy:**
```yaml
- source_field: AUTHOR
  target_field: authors
  type: string
```

**Derived/computed field** — look for expressions that combine or transform values:
```java
// Java
entity.setFullReference(bugs.getAuthor() != null
    ? bugs.getAuthor() + " " + bugs.getTitle()
    : bugs.getReference());
```
```yaml
- target_field: full_reference
  transform: "source.author + ' ' + source.title if source.author else source.reference"
  type: string
```

Fields that appear in the Java entity but are never set by the Updater:
add a `role: ignored` entry in `source.fields`; omit them from `mappings`.

---

### `reconciliation`

The rules come from `{Domain}RowConverter.java`, method `convertForDataRow()`.  
Each `if`/`else if` block becomes one rule; preserve their order.

**Trace lookup (always rule 1):**
```java
// Java – checks the bugs_trace table first
if (traceHelper.existsInTrace(bugs)) { ... }
```
```yaml
- order: 1
  strategy: trace_lookup
  on_error: skip
```

**Database query (usually rule 2):**
```java
// Java – find by business key in the target table
BiblioData existing = dataRepository.findByBugsReference(bugs.getReference());
```
Look at the repository method to extract the SQL.
```yaml
- order: 2
  strategy: database_query
  query: "SELECT * FROM tbl_biblio WHERE bugs_reference ILIKE %s"
  bind: ["source.reference"]
```

**Create new (always last):**
```yaml
- order: 3
  strategy: create_new
```

---

### `update_detection`

Find the comparison block in `{Domain}Updater.java` or the Persister:
```java
// Java
if (!Objects.equals(existing.getAuthors(), updated.getAuthors())) { changed = true; }
```
List every field that is compared:
```yaml
update_detection:
  fields: [authors, title, full_reference]
```

---

### `dependencies`

Find the constructor of `{Domain}Importer.java`:
```java
// Java
@Autowired
public BibliographyImporter(..., SiteImporter required) {
    super(mapper, persister, required);
}
```
→ list each required importer (without the "Importer" suffix):
```yaml
dependencies:
  required_importers: [Site]
```
If the `super(...)` call has no trailing arguments beyond mapper+persister:
```yaml
dependencies:
  required_importers: []
```

---

### `tracing`

```yaml
tracing:
  bugs_table:     # same as meta.bugs_table
  sead_table:     # same as meta.sead_table
  identifier_expr: "source.trace_key()"   # always this expression
  write_on: [insert, update]              # update only if the Persister writes on update
```
Check the Persister to confirm whether it writes a trace row on update as well as insert.

---

### `helpers`

Only add entries here for multi-line helper functions that are reused in more
than one `transform` expression.  Leave as `helpers: []` if not needed.

---

## 3 — Validate before committing

1. Every `source.fields` entry with `role: data` has a corresponding `mappings` entry.
2. Reconciliation rules are numbered consecutively from 1, with exactly one
   `create_new` rule at the end.
3. `update_detection.fields` are a subset of `mappings[*].target_field`.
4. `tracing.bugs_table` matches `meta.bugs_table`.
5. No `transform` expression references Java identifiers (`get`, `set`, `Objects.equals` …).

---

## 4 — Record key decisions

Before committing the policy file, create
`doc/reconciliation_policies/log/{domain}.decisions.md` and record every
non-obvious choice made while reading the Java source.

Use this table template — add or remove rows as needed:

```markdown
# {Domain} domain — policy authoring decisions

| Policy section | Java source | Decision |
|---|---|---|
| `source.fields` — FieldX | `Bugs{Entity}BugsTable.createItem()` | role: ignored — no column in target table |
| `trace_key.template` | `Bugs{Entity}.compressToString()` | Literal braces → `{{`/`}}` in Python .format() syntax |
| `reconciliation.rules[N]` | `{Domain}RowConverter` branching logic | … |
| `update_detection` — FieldY | `{Domain}Updater.doUpdates()` null check | Null-suppression: incoming null does not overwrite existing value |
```

Good candidates to document:
- Any field set to `role: ignored` or `role: location` and why
- Non-obvious `trace_key.template` escaping
- Reconciliation rules where finding a match is an **error** rather than success
- Null-suppression or type-conversion behaviour in update detection
- Config flags that change runtime behaviour (`allow.*.updates`, etc.)
- Any helper function and which Java utility class it mirrors

See [`log/site.decisions.md`](log/site.decisions.md) for a worked example.

---

## 5 — Next step: generate code

Once the policy file is complete, copy
`codegen_instructions/bibliography.codegen.yml` alongside it, rename it to
`codegen_instructions/{domain}.codegen.yml`, and update the class names,
paths, and field lists to match the new domain.  Then pass the pair of files
to the LLM to generate the Python module.
