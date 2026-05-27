# Lab domain — policy authoring decisions

Recorded while creating `lab.policy.yml` from the Java source.

| Policy section | Java source | Decision |
|---|---|---|
| `source.fields` — `Address`, `Telephone` | `LabBugsTable.createItem()` reads both; `DatingLabUpdater` has no setter for either | `role: ignored` — no columns exist in `tbl_dating_labs` |
| `source.fields` — `Website`, `email` | Present as `@BugsColumn` annotations on `BugsLab.java` but **absent** from `LabBugsTable.createItem()` | Not included in `source.fields` at all — they are never fetched from Access |
| `trace_key.template` | `BugsLab.compressToString()`: `"{" + labId + ',' + labName + ',' + country + '}'` — only 3 of 7 fields | Template `{{{LabID},{Labname},{Country}}}` — outer `{{`/`}}` are escaped braces; Address and Telephone deliberately excluded from trace key |
| `mappings.country_id` — FK type | `DatingLabUpdater.setCountry()` calls `locationRepository.findCountryByName()` and sets a `Location` object | Typed as `fk` with explicit `fk_lookup` query; not a plain string copy |
| Country error — literal `"Country"` | `if(bugsData.getCountry().equals("Country")) → addError(new IgnoredItemErrorLog(""))` | A non-null but placeholder value triggers an `IgnoredItemError`, distinct from "No country found"; documented in `source.fields.Country` note |
| `reconciliation.rules[1]` — `allow_deleted: false` | `DatingLabTraceHelper` constructor: `super(TABLE_NAME, false, repository)` — the `false` is the `allowDeleted` flag in `SeadDataFromTraceHelper` | When the trace points to a deleted/missing `tbl_dating_labs` row, return null (fall through to rule 2) rather than an error entity |
| `reconciliation.rules[2]` | `DatingLabByLabIdSearch` calls `repository.findByLabId(bugsLab.getLabId())` | Maps to `SELECT * FROM tbl_dating_labs WHERE international_lab_id = %s` |
| `update_detection` — all three fields tracked | `DatingLabUpdater.Updater.update()` accumulates `updated` flag across `setLabId`, `setLabName`, `setCountry` | All three mapped fields included in `update_detection.fields` |
| `dependencies.required_importers` | `LabImporter` constructor: `super(dataMapper, persister, countryImporter)` | `[Country]` — country rows must pre-exist in `tbl_locations` |
| `helpers` | No reused transform expressions | `helpers: []` |
