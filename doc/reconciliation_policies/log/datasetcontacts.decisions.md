# DatasetContacts domain — policy authoring decisions

Recorded while creating `datasetcontacts.policy.yml` from the Java source.

| Policy section | Java source | Decision |
|---|---|---|
| importer classification | `DatasetContactImporter`, `DatasetContactBugsSeadMapper`, `DatasetContactsRowConverter` | Treat this as a standard policy using `TCountsheet` as the source, not as a custom non-policy importer |
| `source.table` | `DatasetContactBugsSeadMapper` uses `new CountsheetBugsTable()` | Model the importer as `TCountsheet -> tbl_dataset_contacts`, even though contact text is read from `TSite` through a helper |
| `source.fields.SiteCODE` | `DatasetContactUpdater` + `SiteContactReader.parse(siteCode)` | Keep `SiteCODE` as `role: data` because it drives the secondary `TSite` read for `IDBy` and `Specimens` |
| `output` | `DatasetContactsRowConverter.convertListForDataRow()` returns `List<DatasetContact>` | Use first-class one-to-many output semantics |
| `reconciliation` | `DatasetContactUpdater.update()` loads existing contacts by dataset and removes parsed duplicates by type + first name + last name | Model reconciliation as list-based by dataset rather than trace-first or per-row update |
| `compare semantics` | `DatasetContactByTypeAndNamesMatcher` | Document that item equality is by contact type and contact names, even though generated items are represented in the policy with resolved `contact_id` and `contact_type_id` |
| `on_missing_existing` | `DatasetContactUpdater.update()` never removes stored dataset contacts | Set `output.on_missing_existing: keep` |
| `related_outputs.contacts` | `ContactCacheAndRepositoryAccessor` + `DatasetContactPersister.syncContact()` | Record contact creation and cache/repository reuse as a supporting side effect outside `tbl_dataset_contacts` |
| `dependencies` | `DatasetContactImporter` constructor depends on `FossilImporter` | Require `Fossil` first because dataset resolution depends on previously imported fossil dataset state |
