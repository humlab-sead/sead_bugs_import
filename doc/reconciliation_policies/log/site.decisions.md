# Site domain — policy authoring decisions

Recorded while creating `site.policy.yml` from the Java source.

| Policy section | Java source | Decision |
|---|---|---|
| `source.fields` — `Region`, `Country` | `BugsSiteBugsTable.createItem()` | Set `role: location`; these columns drive Location resolution but are not written to `tbl_sites` directly |
| `source.fields` — `IDBy`, `Specimens` | `SeadSiteCreator.create()` — no setter ever called for these | Set `role: ignored`; no corresponding column in `tbl_sites` |
| `trace_key.template` | `BugsSite.compressToString()` wraps the whole value in literal `{…}` | Template written as `{{{SiteCODE},{SiteName},...}}` — outer `{{`/`}}` are escaped braces in Python `.format()` syntax |
| `identity_key` | `BugsSite.getBugsIdentifier()` returns `getCode()` | `SiteCODE` |
| `target.sequence` | `@SequenceGenerator(sequenceName = "tbl_sites_site_id_seq")` on `SeadSite` | `tbl_sites_site_id_seq` |
| `mappings` — 6 fields only | `SeadSiteCreator.create()` calls exactly `setName`, `setAltitude`, `setLatitude`, `setLongitude`, `setNationalSiteIdentifier`, `setDescription` | `IDBy` and `Specimens` confirmed absent from all setters |
| `mappings.transform` — float → Decimal | `SeadSiteCreator` delegates to `BigDecimalDefinition.convertToSeadContext(Float)` | Represented as `float_to_decimal(source.*)` helper in `helpers:` section |
| `reconciliation.prerequisite` | `BugsSiteTableConverter.getOrCreate()` country-existence guard runs before any rule | Modelled as a `prerequisite:` block outside the main `rules:` list; gated by `allow.create.country` (default `true`) |
| `reconciliation.rules[1]` — external-edit guard | `SiteFromTrace.getSeadSiteFromBugsCode()` calls `SeadDataFromTraceHelper.seadDataExistsAndHasBeenEditedSinceImport()` | Added `on_external_edit: error` with message "Sead data has been updated since last bugs import"; not present in Bibliography |
| `reconciliation.rules[2]` — two query variants | `BugsSiteTableConverter.UpdateHelper.getFromTraceOrByNameAndLocation()` branches on `siteLocationHandler.anyCreatedLocations()` | Two `variants:` entries under the same rule; result-handling table covers 0 / 1 / >1 results |
| `reconciliation.rules[2]` — 1-result case is an error | `UpdateHelper` sets `errorMessage = "Site name exists for non-imported site"` when exactly one site matches by name | Counter-intuitive: finding a unique name-match is an **error** (not a found record), because the site was not imported via Bugs |
| `update_detection` — null suppression | `SiteUpdater.doUpdates()` skips the setter when `createdNewVersion.getLatitude() == null` (likewise for longitude, altitude) | Documented in `update_detection.notes`; Decimal fields are null-suppressed, string fields use `Objects.equals` |
| `update_detection` — BigDecimal equality | `SeadSite.equals()` uses `BigDecimalDefinition.equalBigDecimalNumericValues()` (scale-insensitive) | Update-detection comparison for Decimal fields must be numeric, not `==` / `Object.equals` |
| `update_detection` — site_description normalisation | `SeadSite.equals()` does `.replace("\r\n", "\n")` before comparing | Comparison of `site_description` must normalise line endings |
| `update_detection.config.allow_site_updates` | `BugsSiteTableConverter` `@Value("${allow.site.updates:false}")` | Default `false`; when false, detected updates produce an error rather than a DB write |
| `dependencies.required_importers` | `SiteImporter` constructor: `super(dataMapper, persister)` — no extra args | Empty list |
| `helpers.float_to_decimal` | `BigDecimalDefinition.convertToSeadContext()` rounds to SEAD scale | Centralised as a named helper rather than inline transform to avoid repetition across 3 mapping entries |
