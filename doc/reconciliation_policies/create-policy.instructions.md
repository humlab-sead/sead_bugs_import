---
applyTo: "doc/reconciliation_policies/*.policy.yml"
---

# How to create a `{domain}.policy.yml` file

This guide explains how to extract one reconciliation policy from the Java
importer code and write it as `{domain}.policy.yml`.

Treat [`_schema.yml`](_schema.yml) as the authoritative format. Use
[`bibliography.policy.yml`](bibliography.policy.yml) as the smallest worked
example. Use [`site.policy.yml`](site.policy.yml) to see the kinds of business
rules that often need to be captured for richer domains.

If you discover a business rule that does not fit the current schema, do not
invent ad hoc keys in the policy file. Record the gap in the decisions log and
update the schema in a separate change before standardizing the new shape.

---

## 1 — Gather the source evidence

Each domain usually lives under `src/main/java/se/sead/bugsimport/{domain}/`.
Start with the domain package, then expand outward until you can explain the
full import behavior.

Read these files first:

| File | What to extract |
|---|---|
| `bugsmodel/Bugs{Entity}.java` | Source field names, `getBugsIdentifier()`, `compressToString()`, nullable behavior |
| `bugsmodel/*BugsTable.java` | Access table name, selected columns, row-to-object mapping |
| `seadmodel/{SeadEntity}.java` | Target table name, identity column, sequence, field names |
| `{Domain}RowConverter.java` or `{Domain}BugsSeadMapper.java` | Reconciliation rule order and match outcomes |
| `{Domain}Updater.java`, `{Domain}Manager.java`, or creator class | Target mappings, derived fields, update comparisons |
| `*Repository.java`, `*TraceHelper.java`, helper search classes | SQL fragments, trace behavior, alternative lookups |
| `{Domain}Importer.java` | Importer dependencies and constructor ordering |
| Persister class | Trace writes, update behavior, error handling |

Expand beyond that initial set when the domain uses any of the following:

- Helper or handler classes that resolve prerequisite data before the main row is reconciled
- Config flags that allow, block, or change updates or record creation
- Equality helpers or utility classes that normalize values before comparing them
- Multiple repository queries or variant query branches selected at runtime
- Trace helpers that reject rows because of previous errors or external edits

Important: `*BugsTable.java` often gives you the table name and selected fields,
but not a single raw SQL string you can copy. If the Java code reads directly
from an Access table and maps fields in `createItem()`, reconstruct the `source.sql`
from the table constant plus the fields actually read. Repository SQL belongs in
`reconciliation.rules`, not in `source.sql`.

---

## 2 — Write the policy in the current schema

### `meta`

Use the metadata shape from [`_schema.yml`](_schema.yml):

```yaml
meta:
  format_version: "1.0"
  generated_from: java_source
  reviewed: false
```

Add a short `notes` block when it helps explain why the domain is unusual,
for example prerequisite lookups, config-gated updates, or known schema gaps.

### `policy`

```yaml
policy:
  domain: bibliography
  description: >
    Imports bibliographic references from the BugsCEP TBiblio table into
    the SEAD tbl_biblio table.
```

The description should explain the real business behavior, not just the source
and target table names. Include the important match path, rejection cases, and
any prerequisite work that happens before insert or update.

### `source`

Use the current shape:

```yaml
source:
  system: bugscep
  table: TBiblio
  sql: "SELECT REFERENCE, AUTHOR, TITLE, Notes FROM TBiblio"
  fields:
    - name: REFERENCE
      type: string
      nullable: true
      role: natural_key
      comment: Lookup key used in traces and reconciliation.
  identity_key: REFERENCE
  trace_key:
    template: "{REFERENCE},{AUTHOR},{TITLE},{Notes}"
```

Role glossary:

- `natural_key`: the BugsCEP business key used by `getBugsIdentifier()` and usually by at least one reconciliation rule
- `data`: source data that affects mapping, lookup conditions, prerequisites, or update detection
- `ignored`: read from Access but intentionally not used to compute, match, or write the SEAD row

Do not invent new roles unless you are also updating [`_schema.yml`](_schema.yml).
If a field is not written to the target table but still drives prerequisite logic
or reconciliation, keep it as `role: data` and explain that in `comment`.

How to fill each source field:

- `name`: use the Access column name exactly as read in Java
- `type`: use the schema vocabulary; prefer the storage type implied by the Java getter and later conversions
- `nullable`: mark false only when the importer actually requires the field or the source cannot be null in practice
- `comment`: use it for lookup-only fields, suppressed fields, config-sensitive fields, and other non-obvious behavior

For `identity_key`, use the field returned by `getBugsIdentifier()` or the equivalent.

For `trace_key.template`, mirror `compressToString()` as literally as possible.
Escape literal braces when needed so the template remains unambiguous.

### `target`

Use the nested identity shape from the schema:

```yaml
target:
  system: sead
  table: tbl_biblio
  identity:
    column: biblio_id
    type: integer
    strategy: sequence
    sequence: tbl_biblio_biblio_id_seq
```

Take `table` from the JPA entity. Take the identity column and sequence from the
entity annotations or the repository code that allocates IDs.

### `mappings`

Create one mapping per target column written by the importer. Use the current
schema shape. Exactly one of `source_field` or `transform` should be present.

Simple copy:

```yaml
- target_field: authors
  type: string
  nullable: true
  source_field: AUTHOR
```

Derived field:

```yaml
- target_field: full_reference
  type: string
  nullable: true
  transform:
    type: conditional
    condition: "source.AUTHOR is None or source.TITLE is None"
    if_true: "source.REFERENCE"
    if_false: "source.AUTHOR + ' ' + source.TITLE"
```

Direct expression field:

```yaml
- target_field: physical_sample_id
  type: integer
  nullable: false
  transform:
    type: expr
    expr: "parent.physical_sample_id"
```

Use structured transform objects from the schema, not free-form transform strings.

Use `transform.type: expr` when a field should come directly from an expression
such as `parent.<field>`, `generated.<field>`, `related.<output_name>.<field>`,
or a simple computed value and a named helper would add no useful behavior.

When deciding whether a field belongs in `mappings`:

- Include it if the importer writes the target column directly or derives it from source data
- Exclude it if the field only drives prerequisite checks, reconciliation, or update decisions without being written
- Keep any exclusion explicit in the relevant `source.fields[*].comment` or the decisions log

If one Bugs row expands to multiple rows in the main `target.table`, add an
`output` section and use `transform.type: generated` for fields that come from
each generated item rather than directly from the source row.

Example:

```yaml
output:
  mode: one_to_many
  item_helper: generate_site_location_items
  compare_fields: [site_id, location_id]
  on_missing_generated: insert
  on_missing_existing: mark_for_deletion
  on_match: return_as_is

mappings:
  - target_field: site_id
    type: integer
    nullable: true
    transform:
      type: call
      helper: resolve_site_id
      args: ["source.SiteCODE"]

  - target_field: location_id
    type: integer
    nullable: true
    transform:
      type: generated
      field: location_id
```

Use `output.mode: one_to_many` only when the Java converter returns a list of
target rows for one source row, for example via `convertListForDataRow()`.

If the importer also persists rows in other SEAD tables besides `target.table`,
add a `related_outputs` section. Use the short descriptive form when you only
need to document the side effect. Use the runnable form when the child or
supporting table needs enough structure for code generation.

Example:

```yaml
related_outputs:
  - name: sample_dimensions
    table: tbl_sample_dimensions
    mode: one_to_many
    relationship: child
    item_helper: generate_sample_dimension_items
    key_fields: [dimension_id, method_id, dimension]
    persistence: save_or_update
    comment: SamplePersister saves each generated dimension after the sample row.
```

Use `related_outputs` when Java persists helper-created or child rows through a
persister, cache, or cascade that is not fully represented by `target.table`.
Keep the main `mappings` focused on `target.table`; use `related_outputs` to
name the extra tables, row cardinality, identity shape, and persistence style.

For machine-runnable child definitions, extend one `related_outputs` entry with:

- `phase` when the child must be evaluated before the parent mapping can reference it
- `identity` for the child table primary key shape
- `mappings` for child-table columns
- `reconciliation` for existing-child lookup plus insert/keep/delete behavior
- `update_detection` for child-row change detection
- optional `tracing` when child rows write trace events

Runnable example:

```yaml
related_outputs:
  - name: sample_dimensions
    table: tbl_sample_dimensions
    mode: one_to_many
    relationship: child
    phase: after_parent
    item_helper: generate_sample_dimension_items
    key_fields: [physical_sample_id, dimension_id, method_id]
    persistence: save_or_update
    identity:
      column: sample_dimension_id
      type: integer
      strategy: sequence
      sequence: tbl_sample_dimensions_sample_dimension_id_seq
    mappings:
      - target_field: physical_sample_id
        type: integer
        nullable: false
        transform:
          type: expr
          expr: "parent.physical_sample_id"
      - target_field: dimension_id
        type: integer
        nullable: false
        transform:
          type: generated
          field: dimension_id
    reconciliation:
      existing_rows:
        table: tbl_sample_dimensions
        where: "physical_sample_id = :physical_sample_id"
        bind:
          physical_sample_id: "parent.physical_sample_id"
      compare_fields: [physical_sample_id, dimension_id, method_id]
      on_missing_generated: insert
      on_missing_existing: mark_for_deletion
      on_match: update
    update_detection:
      strategy: field_comparison
      fields: [dimension_value]
      on_change: mark_updated
```

Inside runnable `related_outputs` sections:

- `parent.<field>` refers to the main target row from `target.table`
- `generated.<field>` refers to one item emitted by `related_outputs[*].item_helper`
- `related.<output_name>.<field>` refers to a field from another runnable related output after that output has been evaluated
- `source.<field>` still refers to the original Bugs row

Use `phase: before_parent` when the parent row needs a value from the related
output during its own mapping, for example when a parent foreign key should be
set from `related.analysis_entity.analysis_entity_id` instead of a helper call.

Use `phase: after_parent` when the related output depends on the parent row or
is a true child row that should be evaluated after the main target row exists.

Use an insert-only child graph when Java creates supporting rows together with a
new parent row but does not revisit or mutate those child rows during later
updates of the parent row.

This pattern is common when the converter or updater does something like:

- create dataset and analysis-entity objects only when the parent row is new
- attach those objects through JPA cascade
- leave the existing child graph unchanged when a traced parent row is updated

In that case:

- keep the child table in `related_outputs` because the importer still persists it
- preserve the existing child identity through generated fields such as `dataset_id` or `analysis_entity_id`
- use child `reconciliation.on_match: return_as_is` when a traced parent keeps the stored child row unchanged
- use child `update_detection.strategy: never` when Java does not compare or mutate child fields after insert
- describe the insert-only behavior in both the child `comment` and the main policy `notes`

Example shape:

```yaml
related_outputs:
  - name: dataset
    table: tbl_datasets
    mode: zero_or_one
    relationship: supporting
    item_helper: generate_geochronology_dataset_items
    persistence: cascade
    key_fields: [dataset_id, dataset_name, data_type_id, method_id, master_set_id]
    identity:
      column: dataset_id
      type: integer
      strategy: sequence
      sequence: tbl_datasets_dataset_id_seq
    mappings:
      - target_field: dataset_name
        type: string
        nullable: true
        transform:
          type: generated
          field: dataset_name
    reconciliation:
      existing_rows:
        table: tbl_datasets
        where: "dataset_id = :dataset_id"
        bind:
          dataset_id: "generated.dataset_id"
      compare_fields: [dataset_name, data_type_id, method_id, master_set_id]
      on_missing_generated: insert
      on_missing_existing: keep
      on_match: return_as_is
      comment: Create the dataset only when the parent row is inserted; traced parent rows keep the stored dataset unchanged.
    update_detection:
      strategy: never
      fields: []
      on_change: ignore
```

Use `datesradio.policy.yml` as the concrete example for this pattern.

### `reconciliation`

Encode the actual match order from the Java converter or mapper using the schema shape:

```yaml
reconciliation:
  strategy: ordered_rules
  rules:
    - name: trace_lookup
      order: 1
      type: trace_lookup
      description: Look up the most recent trace row for this BugsCEP record.
      bugs_table: TBiblio
      identifier_expr: "source.REFERENCE"
      on_match: update
      on_error: skip

    - name: database_lookup
      order: 2
      type: database_query
      description: Find an existing target row by business key.
      table: tbl_biblio
      where: "bugs_reference ILIKE :reference"
      bind:
        reference: "source.REFERENCE"
      on_match: update

    - name: create_new
      order: 3
      type: create_new
      description: No existing target row found.
      on_match: insert
```

Guidance:

- Preserve rule order exactly; rule order is part of the business logic
- Capture what a successful match means with `on_match`, unless a `database_query.result_handling` block fully defines the zero/one/many outcomes instead
- Capture what happens when a prior trace error exists with `on_error`
- Use `search_chain` when the Java importer iterates a list of ordered search or check strategies and stops on the first match or error carrier
- Use `prerequisite` for work that must happen before rule 1, such as resolving lookup rows or checking config-gated preconditions
- Use `external_edit` on `trace_lookup` when a traced row must be rejected if SEAD changed after the last import
- Use `variants` when Java selects between multiple SQL branches at runtime
- Use `result_handling` when the importer distinguishes between zero, one, or many matches
- Use `description` to document exact error outcomes and messages when the structured fields do not carry the full business rule

Use `search_chain` in this shape when Java uses strategy objects instead of one repository query:

```yaml
reconciliation:
  strategy: ordered_rules
  rules:
    - name: strategy_chain
      order: 1
      type: search_chain
      description: Apply the ordered Java search strategies.
      steps:
        - name: trace_lookup
          order: 1
          type: trace_lookup
          description: Check the latest trace first.
          bugs_table: TMCRNames
          identifier_expr: "source.CODE"
          on_match: update

        - name: repository_lookup
          order: 2
          type: database_query
          description: Check the repository-backed value lookup.
          table: tbl_mcr_names
          where: "taxon_id = :taxon_id"
          bind:
            taxon_id: "resolve_taxon_id(source.CODE)"
          on_match: update

      on_no_match: fall_through

    - name: create_new
      order: 2
      type: create_new
      description: No strategy matched.
      on_match: insert
```

Use `history_check` as a `search_chain` step when Java returns an error carrier
or blocks further processing because earlier imported SEAD rows were edited
after the last trace.

Do not collapse all lookup behavior into one generic query. Keep trace lookup,
database lookup, search-chain steps, and create-new behavior separate.

For one-to-many outputs, keep list expansion semantics in `output` and keep the
reconciliation rule focused on how the importer loads and compares the existing
stored list.

### `update_detection`

Use the current schema shape:

```yaml
update_detection:
  strategy: field_comparison
  fields:
    - authors
    - title
    - full_reference
  on_change: mark_updated
  comment: Comparison notes go here.
```

Take `fields` from the actual comparison logic in the updater, manager, persister,
or equality helper. If Java normalizes line endings, compares decimals numerically,
or suppresses incoming nulls, describe that in `comment` and in the decisions log.

If updates are gated by configuration, add a `config` list:

```yaml
update_detection:
  strategy: field_comparison
  fields: [site_name]
  on_change: mark_updated
  config:
    - name: allow_site_updates
      property: allow.site.updates
      default: false
      description: Set to true to permit overwriting existing rows.
```

Use:

- `field_comparison` when Java compares a known list of target fields
- `always` when every matched record is treated as updated
- `never` when matched records are returned unchanged

### `dependencies`

Dependencies are a flat list in the current schema:

```yaml
dependencies: []
```

Populate it from the importer constructor or `super(...)` call. Only list actual
importer dependencies. Do not list schema prerequisites or reference data setup as
importer dependencies; explain those in comments or the decisions log instead.

### `tracing`

Use the current shape:

```yaml
tracing:
  bugs_table: TBiblio
  sead_table: tbl_biblio
  write_on: [insert, update]
```

Take `bugs_table` from the Bugs table constant or trace helper, and `sead_table`
from the JPA entity or persister. Only include `update` in `write_on` if the
persister really writes traces for updates.

Do not add `identifier_expr` here. The trace identifier belongs on the
`trace_lookup` reconciliation rule.

### Shared `emit` shape

Use one shared `emit` structure whenever the policy needs to record a
machine-readable runtime outcome such as an error, warning, ignored row, or
flagged row.

Current sections that use this shared shape:

- `resolvers[*].steps[*].emit`
- `postprocess[*].on_conflict`

Shape:

```yaml
emit:
  severity: error|warning|ignored|flag
  code: <string>
  message: <string>
  set_flagged: true|false
```

Guidance:

- use a short stable `code` so fixtures or later generators can check it
- keep `message` aligned with the Java runtime text when exact parity matters
- use `set_flagged: true` only when Java really marks the target row as flagged instead of only adding an error

### `postprocess`

Use `postprocess` when the Java importer first creates provisional main-table
rows and then groups or merges them before final persistence.

Keep this section narrow. It is for observed grouped merge behavior such as the
calendar-date From/To range merge, not for inventing a general workflow DSL.

Use it when Java does all of the following:

- maps one Bugs row to a provisional target row first
- groups those provisional rows by one or more business keys
- pairs rows inside the group by complementary conditions such as From versus To
- mutates or replaces fields on one retained row before persistence
- emits a structured conflict outcome when the pair logic cannot choose a single result

Example:

```yaml
postprocess:
  - name: merge_calendar_ranges
    phase: after_row_mapping_before_persist
    mode: pair_merge
    description: Group provisional RelativeDate rows by sample, then by dating method, note group, and uncertainty family before merging From/To pairs.
    group_by: ["source.SampleCODE"]
    partition_by:
      - "source.DatingMethod"
      - "normalized.note_group"
      - "normalized.uncertainty_family"
    pair_rules:
      left: "normalized.uncertainty_side == 'from'"
      right: "normalized.uncertainty_side == 'to'"
      include_singletons: true
    retain_row: left_when_present_else_right
    actions:
      - type: use_pair_range_relative_age
        target: relative_age_id
      - type: normalize_pair_uncertainty
        target: dating_uncertainty_id
        clear_when_closed_range: true
        convert_ca_pair_to_standard: true
      - type: copy_if_empty
        target: notes
        from_paired_field: notes
    on_conflict:
      severity: error
      code: too_many_uncertainties_same_kind
      message: "Too many uncertainties of same type for a single sample."
```

Guidance:

- `group_by` identifies the outer bucket, such as sample code
- `partition_by` splits that bucket further before pairing, such as dating method, note group, or uncertainty family
- `pair_rules.left` and `pair_rules.right` describe the complementary row kinds that Java pairs together
- `retain_row` documents which provisional row survives after merge
- `actions` describe the field-level mutations applied to the retained row
- `on_conflict` records the structured outcome when Java reports a merge error instead of one clean merged row

Use `datescalendar.policy.yml` as the concrete example for this pattern.

Treat `on_conflict` as the shared `emit` shape, not as a special postprocess-only
error block.

### `resolvers`

Use `resolvers` for helper-heavy lookup behavior that has ordered steps,
fallback branches, config-driven shortcuts, or emitted outcomes.

Prefer a resolver over a plain helper when the Java implementation does more
than one lookup or more than one return path.

Typical resolver cases:

- trace lookup with repository fallback
- unknown-value shortcuts driven by config
- fallback to a flagged or empty error container
- lookup flows that should stay machine-readable instead of hidden in prose

Example:

```yaml
resolvers:
  - name: resolve_dating_lab_id
    description: Resolve a dating lab by unknown shortcut, trace lookup, direct lookup, or not-found error.
    returns: entity_ref
    args: [lab_id]
    config:
      - name: unknown_sead_lab_identifier
        property: sead.unknown.lab.identifier
        default: Unknown
        description: SEAD lab ID returned for blank or configured unknown Bugs lab IDs.
      - name: unknown_bugs_lab_identifier
        property: bugs.unknown.lab.identifier
        default: Unknown
        description: Bugs lab identifier treated as the unknown-lab sentinel.
    steps:
      - name: unknown_shortcut
        when: "lab_id is None or lab_id.strip() == '' or lab_id == config.unknown_bugs_lab_identifier"
        action: database_query
        table: tbl_dating_labs
        where: "lab_id = :lab_id"
        bind:
          lab_id: "config.unknown_sead_lab_identifier"
        return: return_entity
      - name: trace_lookup
        action: trace_lookup
        bugs_table: TLab
        identifier_expr: "lab_id"
        return: return_entity
      - name: direct_lookup
        action: database_query
        table: tbl_dating_labs
        where: "lab_id = :lab_id"
        bind:
          lab_id: "lab_id"
        return: return_entity
      - name: not_found
        action: emit_issue
        emit:
          severity: error
          code: dating_lab_not_found
          message: "No lab found"
        return: empty_entity
    used_by: [dating_lab_id]
```

For now, `transform.type: call` still uses the `helper` field name even when it
references a resolver. Use the same resolver name there.

Use the shared `emit` shape for `action: emit_issue` steps.

### `known_divergences`

Use `known_divergences` when the policy intentionally does one of the following:

- matches the Java runtime even though the Java behavior looks wrong
- models the intended behavior instead of a likely Java bug
- documents a gap where exact parity is still blocked by schema limits or unclear runtime behavior

Example:

```yaml
known_divergences:
  - area: update_detection.age
    status: java_bug_suspected
    description: GeochronologyUpdater.setAge() returns numeric equality instead of difference, so Java can miss real age changes.
    policy_choice: match_intended_behavior
```

Guidance:

- keep `area` concrete, for example `update_detection.age` or `resolver.resolve_dating_lab_id`
- use `java_bug_suspected` when the code path looks internally inconsistent with nearby update logic
- use `behavior_ambiguous` when the runtime intent is unclear from the available source evidence
- use `schema_limit` when the policy is forced to simplify behavior because the current schema cannot express it directly
- use `match_java_runtime` only when you have chosen exact parity despite the odd behavior
- use `match_intended_behavior` when the policy deliberately reflects the likely intended logic instead of the literal Java result
- use `defer_exact_parity` when the policy is documenting the gap but not yet choosing one side

### `helpers`

Use `helpers` only for simple reusable functions referenced by `transform.type: call`.

Keep logic in `helpers` when it is mainly a pure computation, coercion, or
small expression wrapper without ordered lookup or fallback behavior.

```yaml
helpers:
  - name: float_to_decimal
    description: Converts a nullable float to a nullable Decimal.
    signature: "float_to_decimal(value: float | None) -> Decimal | None"
    expression: "Decimal(str(round(value, 10))) if value is not None else None"
    used_by: [latitude_dd]
```

If a helper is only implied by Java utility code and you cannot describe it clearly,
do not invent it. Either inline the simpler logic in a transform, use a resolver
for ordered lookup behavior, or record the gap.

---

## 3 — Check business-rule completeness before committing

Run both checks below.

Command:

```bash
make validate-policy-format
```

### A. Schema conformance

Confirm that the policy matches [`_schema.yml`](_schema.yml):

1. The top-level sections and field names match the schema exactly.
2. Every `reconciliation.rules[*].type` uses the schema vocabulary.
3. `dependencies` is a flat list, not a nested object.
4. `target.identity` uses the nested structure from the schema.
5. `transform` values use the structured schema form, not Java code or free-form YAML strings.

### B. Business-rule completeness

Confirm that the policy captures the behavior that matters at runtime:

1. Every field used for matching, prerequisite checks, update detection, or trace keys appears in `source.fields`.
2. Every target column written by Java appears in `mappings`.
3. Every reconciliation branch that can change the outcome is represented, including trace-first behavior and create-new fallback.
4. Config flags that allow, block, or change behavior are mentioned in the policy description, comments, or decisions log.
5. Prerequisite work that must happen before the main row is reconciled is represented in `reconciliation.prerequisite` or explained explicitly in the decisions log.
6. Error cases that block import or reject a matched row are described explicitly.
7. Update detection notes describe any normalization, null-suppression, or type conversion used by Java.
8. No expression or comment blindly copies Java API names without explaining the resulting behavior.

If a rule is important to runtime behavior but does not fit the schema, stop and
record that as a schema gap instead of hiding it in an unrelated field.

---

## 4 — Add or update fixtures for complex policy behavior

For domains that use complex schema features such as `resolvers`, `postprocess`,
shared `emit` outcomes, or `known_divergences`, add or update a fixture file in:

`doc/reconciliation_policies/fixtures/{domain}.fixture.yml`

Use fixtures to record representative scenarios that should stay aligned with the
policy structure.

Current fixture format:

```yaml
fixture_version: "1.0"
policy: datesradio
scenarios:
  - name: missing_lab_emits_error
    intent: resolver_path
    source_rows:
      - DateCODE: RAD_3
        LabID: MISSING-LAB
    policy_context:
      args:
        lab_id: MISSING-LAB
      step_hits:
        trace_lookup: false
        direct_lookup: false
      config:
        allow_dataset_updates: true
    expects:
      resolver: resolve_dating_lab_id
      resolver_path: [trace_lookup, direct_lookup, not_found]
      target_field: dating_lab_id
      emit_codes: [dating_lab_not_found]
```

Guidance:

- keep scenario names short and specific
- use `source_rows` field names that already exist in the referenced policy
- use `postprocess_merge` and `postprocess_conflict` for grouped merge cases
- use `resolver_path` for ordered resolver cases
- use `reconciliation_path` for ordered main-row reconciliation cases such as trace-first then fallback search chains
- use `related_output_graph` for supporting-row or child-row graph behavior such as dataset cloning or analysis-entity creation
- use `supporting_output_result` for a single supporting-row controller or updater when one related output can be compared directly without a larger child graph
- for `search_chain` reconciliation rules, record the executed step names in `reconciliation_path` in the same order they run, then append later top-level rules such as `create_new` if execution falls through
- keep `expects` tied to names already present in the policy: postprocess names, resolver names, step names, target fields, related outputs, emit codes, and known-divergence areas
- when a policy harness can return stable values, prefer adding a small result-object expectation such as `graph_result` instead of checking branch choice only
- use `postprocess_result`, `resolver_result`, `reconciliation_result`, or `graph_result` when the harness can return a stable object with concrete values
- when an existing Java fixture test can expose the same values, prefer comparing the same result-object shape on both the Java and policy sides
- add fixtures first for the domains where behavior was recently made more machine-readable, such as `datescalendar` and `datesradio`
- add `policy_context` only when a policy-side harness needs explicit inputs or step-hit outcomes that cannot be derived safely from `source_rows` alone

Current result-object expectation fields:

- `postprocess_result`: concrete merged-row details returned by a narrow postprocess harness
- `resolver_result`: concrete resolved value or emitted issue returned by a narrow resolver harness
- `reconciliation_result`: concrete ordered-reconciliation outcome returned by a narrow row-converter or reconciliation harness
- `graph_result`: concrete related-output or supporting-output values returned by a narrow policy harness
- `graph_issue`: concrete related-output error details returned when graph creation stops before producing child or supporting rows
- `row_changed`: concrete boolean return value from a narrow executable check or policy harness when the Java helper returns a value that matters even on an error path

Current `reconciliation_result.result_kind` values in the executable fixture layer:

- `update_existing`
- `insert_new`
- `return_existing_error`
- `return_guard_error`

Current fixture intents:

- `postprocess_merge`
- `postprocess_conflict`
- `resolver_path`
- `reconciliation_path`
- `related_output_graph`
- `supporting_output_result`

Current optional `policy_context` fields:

- `args`: named resolver or harness arguments
- `step_hits`: ordered lookup outcomes for narrow policy-side execution tests
- `config`: explicit configuration values needed to exercise a policy branch, such as `allow_dataset_updates`
- `state`: explicit stored-row or cache state needed to exercise a branch such as reusing an existing related output

Important: `make validate-policy-format` now does two fixture-related checks:

- fixture-to-policy alignment validation for all `*.fixture.yml` files
- a small executable comparison slice for selected `datescalendar`, `datesradio`, `datesperiod`, `lab`, `bibliography`, `rdbcode`, `speciesdistribution`, `taxanotes`, and `fossil` scenarios against current Java behavior

It also runs narrow policy-side execution tests for `datesradio` resolver paths
and `datescalendar` postprocess merge/conflict paths. It also runs narrow
policy-side reconciliation execution tests for `lab`, `bibliography`, and
`rdbcode`, `speciesdistribution`, and `taxanotes` ordered row matching,
including the `rdbcode` duplicate-value guard branch inside a `search_chain`.
It also runs narrow supporting-output execution tests for the `datesperiod`
dataset updater, the `datesperiod` analysis-entity mutation path, the
`datesradio` geochronology dataset creator, and the `datescalendar`
`relative_age` creator path, plus narrow policy-side related-output execution
tests for `fossil`, `datesperiod`, `datesradio`, and the `datescalendar`
relative-age-plus-dataset-plus-analysis-entity graph. The `datesradio` and
`datescalendar` fixtures now compare `resolver_result`, `postprocess_result`,
and `graph_result` objects where those harnesses can return concrete values, so
those checks assert returned data instead of path order or field lists alone.
`datescalendar` now has Java-side comparisons for both `postprocess_result` and
supporting or related-output `graph_result` shapes, and `datesradio` now has a
matching Java-side `resolver_result` comparison.
`fossil` now uses `policy_context.state` for one branch where an existing
analysis entity is reused from stored state, and that scenario compares the
returned `graph_result` object. It also uses `policy_context.state` for one
missing-sample-trace branch and one duplicate-analysis-entity branch where the
harness returns `graph_issue` instead of a graph result. The current fixture set
also uses the source row itself for one blank-sample-code branch where the
harness returns `graph_issue` plus `row_changed: true` to match the Java helper's
surprising return value.

`datesperiod` now uses the same resolver-path fixture pattern as `datesradio`
for `RelativeDateMethodManager`. The current fixture set covers blank fallback,
direct abbreviation lookup, computed lookup from `period_years_type`, and the
not-found error path, all with the same `resolver_result` object shape on the
policy and Java sides.

`datesperiod` now also uses a first single-supporting-output fixture slice for
`RelativeDateDatasetUpdater`. The current fixture set covers create-new,
update-existing, and keep-existing dataset paths, all with the same
`graph_result` object shape on the policy and Java sides.

`datesperiod` now also uses a second single-supporting-output fixture slice for
the `analysis_entity` mutation path inside `BaseRelativeDateUpdater`. The
current fixture set covers create-new, update-existing, and keep-existing
analysis-entity paths, all with the same `graph_result` object shape on the
policy and Java sides.

`datesperiod` now also uses a broader `related_output_graph` fixture slice for
the combined dataset plus analysis-entity graph. The current fixture set covers
create-new, update-existing, keep-existing, and blank-sample-code graph-issue
paths with shared `graph_result` or `graph_issue` shapes on the policy and Java
sides.

`lab` now uses the same resolver-path fixture pattern for `resolve_country_id`
through `DatingLabUpdater`. The current fixture set covers blank country,
placeholder `Country`, direct country lookup, and not-found, all with the same
`resolver_result` object shape on the policy and Java sides.

`lab` now also uses a first ordered-reconciliation fixture slice for
`LabRowConverter`. The current fixture set covers trace-hit update,
international-lab-id fallback update, create-new, and the trace-hit
error-return branch where an existing error row is returned as-is, all with the
same `reconciliation_result` object shape on the policy and Java sides.

`bibliography` now uses the same ordered-reconciliation fixture pattern for
`BibliographyRowConverter`. The current fixture set covers trace-hit update,
trace-hit error return, case-insensitive database lookup update, and create-new,
all with the same `reconciliation_result` object shape on the policy and Java sides.

`rdbcode` now uses the same ordered-reconciliation fixture pattern for
`RdbCodeTableRowConverter`. The current fixture set covers trace-hit update,
trace-hit error return, duplicate-value guard return, and create-new, all with
the same `reconciliation_result` object shape on the policy and Java sides.

`speciesdistribution` now uses the same ordered-reconciliation fixture pattern
for `SpeciesDistributionTableRowConverter`. The current fixture set covers the
tuple-lookup update path and the create-new path, both with the same
`reconciliation_result` object shape on the policy and Java sides.

`taxanotes` now uses the same ordered-reconciliation fixture pattern for
`TaxoNotesTableRowConverter`. The current fixture set covers the tuple-lookup
update path and the create-new path, both with the same
`reconciliation_result` object shape on the policy and Java sides.

`datesradio` now uses a first single-supporting-output fixture slice for
`GeochronologyDatasetCreator`. The current fixture set covers the insert-path
dataset creation case with the same `graph_result` object shape on the policy
and Java sides.

`datesradio` now also uses a broader `related_output_graph` fixture slice for
`GeochronologyAnalysisEntityCreator`. The current fixture set covers the
dataset-plus-analysis-entity create path and the missing-sample error path with
shared `graph_result` or `graph_issue` shapes on the policy and Java sides.

`datescalendar` now also uses a first single-supporting-output fixture slice
for `RelativeAgeManager`. The current fixture set covers single calendar-date
relative-age creation with the same `graph_result` object shape on the policy
and Java sides.

`datescalendar` now also uses a broader `related_output_graph` fixture slice
for `RelativeDateUpdaterForCalendar`. The current fixture set covers the new
calendar-row create path with shared `graph_result` shapes for `relative_age`,
`dataset`, and `analysis_entity` on the policy and Java sides.
Treat the current fixture layer as validation support for authoring and
regression checks, not yet as a general policy runtime.

---

## 5 — Record key decisions

Before committing the policy file, create
`doc/reconciliation_policies/log/{domain}.decisions.md` and record every
non-obvious choice made while reading the Java source.

Use this table template and add rows as needed:

```markdown
# {Domain} domain — policy authoring decisions

| Policy section | Java source | Decision |
|---|---|---|
| `source.fields` — FieldX | `Bugs{Entity}.getBugsIdentifier()` | Marked as `natural_key` because it drives trace and lookup identity |
| `source.fields` — FieldY | `HelperClass` / `Repository` | Kept as `data` even though it is not written, because it drives prerequisite lookup |
| `trace_key.template` | `Bugs{Entity}.compressToString()` | Literal braces and delimiter order preserved |
| `reconciliation.rules[N]` | `{Domain}RowConverter` branching logic | Match found is rejected if prior SEAD edits are detected |
| `update_detection` | `{Domain}Updater.doUpdates()` | Incoming null does not overwrite existing non-null value |
| `schema gap` | multiple files | Business rule needs schema extension before it can be represented directly |
```

Good candidates to document:

- Fields that are read but not written, especially when they still drive matching or prerequisite logic
- Trace key formatting details and any escaping or delimiter choices
- Match cases that end in error instead of update or insert
- Config flags that change create, update, or rejection behavior
- Normalization rules used during comparison, such as line-ending cleanup or numeric equality
- Any place where the current schema could not express the rule cleanly

See [`log/site.decisions.md`](log/site.decisions.md) for a richer example.

---

## 6 — Generate code only after the policy is stable

Once the policy file is schema-valid and the decisions log is complete, copy
`codegen_instructions/bibliography.codegen.yml` alongside it, rename it to
`codegen_instructions/{domain}.codegen.yml`, and update the class names,
paths, and field lists to match the new domain. Then pass the pair of files
to the LLM to generate the Python module.

Do not start code generation while the policy still depends on undocumented
schema gaps or unresolved business rules.
