# INDEX

| Source    | Target | Column | Lookup |
| --------- | ------ | ------ | ------ |
| CODE      |        |        |        |
| FAMILY    |        |        |        |
| GENUS     |        |        |        |
| SPECIES   |        |        |        |
| AUTHORITY |        |        |        |

- On import check CODE exists in tbl_taxonomic_order AND genus & species exists in respective combination (Species can be moved in Bugs - between genera etc)
- KEEP LOG OF ALL CHANGES IN BUGSCEP, BUT DO NOT TRACK THESE IN SEAD STRUCTURE
  - I.e. If a species changes CODE in BugsCEP, change the code in SEAD, and write a note in THE LOG (which will be put online eventually).

# TSynonym

| Source         | Target            | Column         | Lookup                                       |
| -------------- | ----------------- | -------------- | -------------------------------------------- |
| _SynSpecies_   | tbl_taxa_synonyms | family_id      |                                              |
| CODE           | tbl_taxa_synonyms | taxon_id       | taxonomic_orders.code                        |
| SynGenus       | tbl_taxa_synonyms | genus_id       | tbl_taxa_tree_genera.genus_name (bugs order) |
| _SynAuthority_ | tbl_taxa_synonyms | author_id      |                                              |
| Notes          | tbl_taxa_synonyms | notes          |                                              |
| ???            | tbl_taxa_synonyms | synonyms       |                                              |
| ???            | tbl_taxa_synonyms | reference_type |                                              |
| Ref            | tbl_taxa_synonyms | biblio_id      | tbl_biblio.bugs_reference                    |

- this needs to be checked.
- species association (type synonym of) code = target (official sead), syngenus + syn authority = source
- create necessary taxon (including tree, as needed). and add species association.

Data extraction and insertion needs to handle this too.

Notes: In data entry needs to find synonyms so that user knows they are entering a synonym, even though it is master species that is stored.

## TSynonymNotes
(empty)

# TKeys
| Source | Target                       | Column    | Lookup                    |
| ------ | ---------------------------- | --------- | ------------------------- |
| Ref    | tbl_text_identification_keys | biblio_id | tbl_biblio.bugs_reference |
| Data   | tbl_text_identification_keys | key_text  |                           |

Requires: species + taxonomic order

# TSpeciesAssociation
| Source                | Target                   | Column              | Lookup                                              |
| --------------------- | ------------------------ | ------------------- | --------------------------------------------------- |
| AssociatedSpeciesCODE | tbl_species_associations | associated_taxon_id | tbl_taxonomic_order.code                            |
| Code                  | tbl_species_associations | taxon_id            | tbl_taxonomic_order.code                            |
| AssociationType       | tbl_species_associations | association_type_id | tbl_species_association_types.association_type_name |
| Ref                   | tbl_species_associations | biblio_id           | tbl_biblio.bugs_reference                           |
| ???                   | tbl_species_associations | referencing_type    | value = null                                        |

- Possible conversion needed for AssociationType
- If association type does not exist raise error
- empty type? => is associated with (conversion)

Requires: species + taxonomic order, association types (?)

# TAttributes
| Source        | Target                       | Column            | Lookup                   |
| ------------- | ---------------------------- | ----------------- | ------------------------ |
| Code          | tbl_taxa_measured_attributes | taxon_id          | tbl_taxonomic_order.code |
| AttribMeasure | tbl_taxa_measured_attributes | attribute_measure |                          |
| AttribType    | tbl_taxa_measured_attributes | attribute_type    |                          |
| AttribUnits   | tbl_taxa_measured_attributes | attribute_units   |                          |
| Value         | tbl_taxa_measured_attributes | data              |                          |

Requires: species + taxonomic order

# TBirmBEETLEdat

# TTaxoNotes
| Source | Target             | Column         | Lookup                    |
| ------ | ------------------ | -------------- | ------------------------- |
| Code   | tbl_taxonomy_notes | taxon_id       | tbl_taxonomic_order.code  |
| Ref    | tbl_taxonomy_notes | biblio_id      | tbl_biblio.bugs_reference |
| Data   | tbl_taxonomy_notes | taxonomy_notes |                           |

Requires: species + taxonomic order

# TFossil
Mapping is missing!

| Source         | Target | Column | Lookup |
| -------------- | ------ | ------ | ------ |
| FossilBugsCODE |        |        |        |
| CODE           |        |        |        |
| SampleCode     |        |        |        |
| Abundance      |        |        |        |


# TDatesRadio (32)

| Source              | Target                | Column                | Lookup                                               |
| ------------------- | --------------------- | --------------------- | ---------------------------------------------------- |
| DateCode            | tbl_datasets          | dataset_name          | Create tbl_datasets                                  |
| DatingMethod        | tbl_datasets          | method_id             | tbl_methods.method_abbrev_or_alt_name = DatingMethod |
| SampleCode          | tbl_analysis_entities | physical_sample_id    | bugs_import.bugs_traces.bugsdata                     |
|                     | tbl_analysis_entities | dataset_id            | Cretae new dataset                                   |
|                     | tbl_geochronology     | analysis_entity_id    | Create tbl_analysis_entities                         |
| LabNr               | tbl_geochronology     | lab_number            |                                                      |
|                     | tbl_geochronology     | dating_lab_id         |                                                      |
| Uncertainty         | tbl_geochronology     | dating_uncertainty_id | tbl_dating_uncertainty.uncertainty = Uncertainty     |
| Date                | tbl_geochronology     | age                   |                                                      |
| AgeErrorOrPlusError | tbl_geochronology     | error_older           |                                                      |
| AgeErrorMinus       | tbl_geochronology     | error_younger         |                                                      |
|                     | tbl_geochronology     | delta_13c             |                                                      |
| LabId               | tbl_geochronology     | dating_lab_id         | tbl_dating_labs.international_lab_id = LabId         |
| Notes               | tbl_geochronology     | notes                 |                                                      |
| _MaterialType_      | ???                   | ???                   | ???                                                  |

## 1. Create tbl_datasets

| Column        | Value                                                       |
| ------------- | ----------------------------------------------------------- |
| master_set_id | lookup tbl_dataset_masters.master_name = "Bugs database"    |
| data_type_id  | lookup tbl_data_types.data_type_name = "Undefined other"    |
| method_id     | lookup tbl_methods.method_abbrev_or_alt_name = DatingMethod |
| dataset_name  | DateCODE                                                    |
| biblio_id     | null                                                        |

-  Should data_type_id not be the correct data type, or is that not known? 
- 
## 2. Create tbl_analysis_entities

| Column             | Value                                                      |
| ------------------ | ---------------------------------------------------------- |
| physical_sample_id | Lookup bugs_import.bugs_traces.bugsData contain SampleCODE |
| dataset_id         | created dataset                                            |

## 3. Create tbl_geochronology

| Column             | Value                                               |
| ------------------ | --------------------------------------------------- |
| analysis_entity_id | created analysis entity                             |
| dating_lab_id      | lookup tbl_dating_labs.international_lab_id = LabId |
| lab_number         | LabNr                                               |
| age                | Date                                                |
| error_older        | AgeErrorOrPlusError                                 |
| error_younger      | AgeErrorMinus or AgeErrorOrPlusError                |
| notes              | Notes                                               |

- Age -> different precision and scale than rest of application?
- How to deal with dating specifications with only a note?
  - -> fix by conversions.
- How to deal with dating specifications without data?
  - -> Skip completely empty items.
  - -> function for ignoring specific items (error on empty date).
- conversions AgeErrorMinus:  Null => AgeErrorOrPlusError
- dating methods converted to abbreviations in sead: see mapdatingmethod($1, $2)

Translations: dating uncertainty

| From | To   |
| ---- | ---- |
| c    | Ca.  |
| ca   | Ca.  |
| from | From |
| to   | To   |

Translations: LabId

| From       | To   |
| ---------- | ---- |
| Birmingham | Birm |

Requires: samples

# TDatesMethods

Mapping not specified.

| Source    | Target | Column | Lookup |
| --------- | ------ | ------ | ------ |
| Abbrev    |        |        |        |
| Method    |        |        |        |
| Type      |        |        |        |
| SortOrder |        |        |        |

# TLab
| Source     | Target          | Column               | Lookup                                                            |
| ---------- | --------------- | -------------------- | ----------------------------------------------------------------- |
| LabId      | tbl_dating_labs | international_lab_id |                                                                   |
| Labname    | tbl_dating_labs | lab_name             |                                                                   |
| Country    | tbl_dating_labs | country_id           | lookup tbl_locations.location_name = Country and type = 'Country' |
| _Address_  |                 |                      |                                                                   |
| _Telehone_ |                 |                      |                                                                   |
| _Website_  |                 |                      |                                                                   |
| _email_    |                 |                      |                                                                   |

# TDatesCalendar

- FIXME: #55 Replaced column in target table (TDatesCalendar -> tbl_relative_dates)

| Source              | Target             | Column                 | Lookup                                                                              |
| ------------------- | ------------------ | ---------------------- | ----------------------------------------------------------------------------------- |
| Date + BCADBP       | tbl_relative_dates | relative_age_id        | concatenate 'CAL_' + Date + BCADBP <br/> => lookup tbl_relative_ages."Abbreviation" |
| SampleCODE (new)    | tbl_relative_dates | **analysis_entity_id** | ???                                                                                 |
| SampleCODE (delete) | tbl_relative_dates | ~~physical_sample_id~~ | bugs_import.bugs_trace.bugsdata                                                     |
| Uncertainty         | tbl_relative_dates | dating_uncertainty_id  | tbl_dating_uncertainty.uncertainty                                                  |
| DatingMethod        | tbl_relative_dates | method_id              | lookup tbl_methods.method_abbrev_or_alt_name                                        |
| Notes               | tbl_relative_dates | notes                  |                                                                                     |

- date + bcadbp => new relative age, set age data from values;
  - -> relative age type from dating method (?)
- How to map dating method to relative age type?
- Fix naming before or string lookup?
- set method by datingMethod ? -> not the same thing
- From + to from same sample generates one relative age (start and stop dates). type = calendar date range.
- -> a range is one relative dates also.
- Single for a sample => relative age type = calendar date
- Normalize date data

Translations: Uncertainty

| From     | To       |
| -------- | -------- |
| To >     | >        |
| from ca. | From ca. |
| Tc ca.   | To ca.   |
| Ca,      | Ca.      |

Requires: samples

# TDatesPeriod

- FIXME: #56 Breaking change in TDatePeriod import (column replaced)

| Source              | Target             | Column                 | Lookup                               |
| ------------------- | ------------------ | ---------------------- | ------------------------------------ |
| PeriodCode          | tbl_relative_dates | relative_age_id        | bugs_import.bugs_trace.bugsdata      |
| SampleCODE (new)    | tbl_relative_dates | **analysis_entity_id** | ???                                  |
| SampleCODE (delete) | tbl_relative_dates | ~~physical_sample_id~~ | bugs_import.bugs_trace.bugsdata      |
| Uncertainty         | tbl_relative_dates | dating_uncertainty_id  | tbl_dating_uncertainty.uncertainty   |
| DatingMethod        | tbl_relative_dates | method_id              | tbl_methods.metod_abbrev_or_alt_name |
| Notes               | tbl_relative_dates | notes                  |                                      |

Requires: periods, samples

Translations:
| From                                                       | To                       |
| ---------------------------------------------------------- | ------------------------ |
| dating method = 'ArchPer' and period.yeartype = 'C14'      | method name = ArchPerC14 |
| dating method = 'ArchPer' and period.yeartype = 'Calendar' | method name = ArchPerCal |
| dating method = 'GeolPer' and period.yeartype = 'C14'      | method name = GeolPerC14 |
| dating method = 'GeolPer' and period.yeartype = 'Calendar' | method name = GeolPerCal |

- in script ' ' (single space) uncertianty is ok, but cannot be read using external library, or not be stored via newer Access. Cannot test this but will build into functionality.

# TPeriods

| Source         | Target                | Column               | Lookup                                          |
| -------------- | --------------------- | -------------------- | ----------------------------------------------- |
| PeriodCODE     | tbl_relative_ages     | abbreviation         |                                                 |
| PeriodName     | tbl_relative_ages     | relative_age_name    |                                                 |
| PeriodType     | tbl_relative_ages     | relative_age_type_id | lookup tbl_relative_age_types age_type = Type   |
| PeriodDesc     | tbl_relative_ages     | description          |                                                 |
| PeriodRef      | tbl_relative_age_refs | biblio_id            | lookup tbl_biblio bugs_reference                |
| PeriodGeog     | tbl_relative_ages     | location_id          | lookup tbl_locations  location_name             |
| Begin + BP etc | tbl_relative_ages     | c14_age_older        | convert using type = 'begin' and method = 'C14' |
| End + BP etc   | tbl_relative_ages     | c14_age_younger      | convert using type = 'end' and method = 'C14'   |
| Begin + BP etc | tbl_relative_ages     | cal_age_older        | convert using type = 'begin' and method = 'cal' |
| End + BP etc   | tbl_relative_ages     | cal_age_younger      | convert using type = 'end' and method = 'cal'   |
| _YearsType_    | ???                   |                      |                                                 |
|                | tbl_relative_ages     | _notes_              | null                                            |

- exclude for periodcode = '?'
- conversion for age values (see ImportFunctions.GetPeriodAge(date_value, bcad_value, age_direction, age_type))
  - -> if type = c14 convert iff bcad_value = 'BP'
  - -> if type = 'cal' convert iff bcad_value = 'BP'

| From                                                               | To              |
| ------------------------------------------------------------------ | --------------- |
| GetPeriodAge(prow.periodbegin, prow.beginbcad, 'beginbcad', 'c14') | c14_age_older   |
| GetPeriodAge(prow.periodend, prow.endbcad, 'endbcad', 'c14')       | c14_age_younger |
| GetPeriodAge(prow.periodbegin, prow.beginbcad, 'beginbcad', 'cal') | cal_age_older   |
| GetPeriodAge(prow.periodend, prow.endbcad, 'endbcad', 'cal')       | cal_age_younger |

| From                                                                | To                |
| ------------------------------------------------------------------- | ----------------- |
| date_value = null                                                   | null              |
| bcad_value = null & date_value = 0 & age_type = 'c14'               | 0                 |
| bcad_value = null & date_value = 0 & age_type != 'c14'              | null              |
| bcad_value = 'BP' & age_type = 'cal'                                | return null;      |
| bcad_value = 'BP' & age_type != 'cal' & age_direction = 'beginbcad' | date_value        |
| bcad_value = 'BP' & age_type != 'cal' & age_direction = 'endbcad'   | date_value        |
| (bcad_value = 'AD' or bcad_value = 'BC') & age_type = 'c14'         | null              |
| bcad_value = 'AD' & age_direction = 'beginbcad'                     | 1950 - date_value |
| bcad_value = 'AD' & age_direction = 'endbcad'                       | 1950 - date_value |
| bcad_value = 'BC' & age_direction = 'beginbcad'                     | date_value + 1950 |
| bcad_value = 'BC' & age_direction = 'endbcad'                       | date_value + 1950 |

# TSample
| Source         | Target                      | Column          | Lookup                                                                                                                                             |
| -------------- | --------------------------- | --------------- | -------------------------------------------------------------------------------------------------------------------------------------------------- |
| CountsheetCODE | tbl_physical_samples        | sample_group_id | bugs_import.bugs_traces.bugsdata                                                                                                                   |
| RefNrContext   | tbl_physical_samples        | sample_name     |                                                                                                                                                    |
|                | tbl_physical_samples        | sample_type_id  | tbl_sample_types.type_name = "Unspecified"                                                                                                         |
|                | tbl_physical_samples        | alt_ref_type_id | tbl_alt_ref_types.alt_ref_type = "Other alternative sample name"                                                                                   |
| ZorDepthTop    | tbl_sample_dimensions (new) |                 |                                                                                                                                                    |
|                | tbl_sample_dimensions       | dimension_id    | tbl_dimensions.dimension_name = "Upper boundary depth from unknown reference" ?? This dimension is pointing to method group 14 => Size measurement |
|                | tbl_sample_dimensions       | method_id       | tbl_methods.method_abbrev_or_alt_name = "Depth from datum", ?? this method points to method group 17 => coordinate and altitude systems            |
| ZorDepthTop    | tbl_sample_dimensions       | dimension_value |                                                                                                                                                    |
| ZorDepthBot    | tbl_sample_dimensions (new) |                 |                                                                                                                                                    |
|                | tbl_sample_dimensions       | dimension_id    | tbl_dimensions.dimension_name = "Lower boundary depth from unknown reference"                                                                      |
|                | tbl_sample_dimensions       | method_id       | tbl_methods.method_abbrev_or_alt_name = "Depth from datum",                                                                                        |
| ZorDepthBot    | tbl_sample_dimensions       | dimension_value |                                                                                                                                                    |
| __X__          | (none)                      |                 | Produce warning if has data                                                                                                                        |
| __Y__          | (none)                      |                 | Produce warning if has data                                                                                                                        |


Requires: countsheets

- Ignore the following as no data in Bugs:
   - X => tbl_sample_dimensions.dimension_value [method_id = ?, dimension_id = ?]
   - Y => tbl_sample_dimensions.dimension_value [method_id = ?, dimension_id = ?]

Requires: TCountsheet

## TCountsheet
| Source           | Target            | Column                   | Lookup                                                               |
| ---------------- | ----------------- | ------------------------ | -------------------------------------------------------------------- |
| _CountsheetCODE_ | ????              | ???                      |                                                                      |
| CountsheetName   | tbl_sample_groups | sample_group_name        |                                                                      |
| SiteCODE         | tbl_sample_groups | site_id                  | bugs_import.bugs_traces.bugsData                                     |
| SheetContext     | tbl_sample_groups | sampling_context_id      | converted lookup tbl_sample_group_sampling_contexts.sampling_context |
| SheetType        | tbl_sample_groups | method_id                | Lookup tbl_method.method_name = 'Temporary record'                   |
|                  | tbl_sample_groups | sample_group_description | null                                                                 |

- If empty context generate error.

- Translations: region
| From                    | To                  |
| ----------------------- | ------------------- |
| Archaeological contexts | Archaeological site |

Requires: TSite

# TSite (31)
| Source      | Target             | Column                      | Lookup                                                           |
| ----------- | ------------------ | --------------------------- | ---------------------------------------------------------------- |
| SiteCODE    | bugs_import        | bugs_traces.bugsdata        |                                                                  |
| SiteName    | tbl_sites          | site_name                   |                                                                  |
| Region      | tbl_site_locations | location_id                 | tbl_locations.location_name + type = 'Unprocessed Bugs Transfer' |
| Country     | tbl_site_locations | location_id                 | tbl_locations.location_name + type = 'Country'                   |
| NGR         | tbl_sites          | national_site_identifier    |                                                                  |
| LatDD       | tbl_sites          | latitude_dd                 |                                                                  |
| LongDD      | tbl_sites          | longitude_dd                |                                                                  |
| Alt         | tbl_sites          | altitude                    |                                                                  |
| Interp      | tbl_sites          | **site_description**        |                                                                  |
|             | tbl_sites          | site_preservation_status_id | null                                                             |
|             | tbl_sites          | site_location_accuracy      | null                                                             |
| _Specimens_ | ???                | ???                         |                                                                  |
  
**Requires**: TCountry

- Translations: region
| From                   | To                         |
| ---------------------- | -------------------------- |
| Alpes Maritime         | Alpes-Maritimes            |
| Ameraliksfjord         | Ameraliksfjordur           |
| Angermannland          | Angermanland               |
| Co. Down               | County Dowm                |
| Co. Louth              | County Louth               |
| Inverness shire        | Inverness-shire            |
| Leics.                 | Leicestershire             |
| Møen                   | Møn                        |
| Noord Brabant          | Noord-Brabant              |
| Noord Holland          | Noord-Holland              |
| North                  | North Holland              |
| Not located            |                            |
| Notts.                 | Nottinghamshire            |
| Ostergottland          | Ostergotland               |
| Ostobottnia media      | Ostrobothnia media         |
| Ostrobottnia australis | Ostrobothnia australis     |
| Ostrobottnia borealis  | Ostrobothnia borealis      |
| Ostrobottnia media     | Ostrobothnia media         |
| Reykjavik              | Reykjavík                  |
| S Uist, Outer Hebrides | South Uist, Outer Hebrides |
| Sjaelland              | Zealand                    |
| Vagsøy                 | Vågsøy                     |
| Yorks                  | Yorkshire                  |
| Ångermanland           | Angermanland               |

- UPDATE TSite SET "Country" = 'United Kingdom' WHERE "SiteCODE" = 'SITE000006';
- Translations region: Isle of Man, Jersey, Guernsey => location type = Country
  
## TSiteOtherProxies
| Source           | Target                 | Column         | Lookup                                                                |
| ---------------- | ---------------------- | -------------- | --------------------------------------------------------------------- |
| SiteCODE         | tbl_site_other_records | site_id        | bugs_import.bugs_trace.bugsdata                                       |
| HasPollen        | tbl_site_other_records | record_type_id | lookup tbl_record_types.record_type_name('External pollen data')      |
| HasPlantMacro    | tbl_site_other_records | record_type_id | lookup tbl_record_types.record_type_name('External plant macro data') |
| HasDiatoms       | tbl_site_other_records | record_type_id | lookup tbl_record_types.record_type_name('Diatoms')                   |
| HasChironomids   | tbl_site_other_records | record_type_id | lookup tbl_record_types.record_type_name('Chironomids')               |
| HasSoilChemistry | tbl_site_other_records | record_type_id | lookup tbl_record_types.record_type_name('Soil chemistry/properties') |
| HasIsotopes      | tbl_site_other_records | record_type_id | lookup tbl_record_types.record_type_name('Isotopes')                  |
| HasAnimalBones   | tbl_site_other_records | record_type_id | lookup tbl_record_types.record_type_name('Animal bones')              |
| HasArcheology    | tbl_site_other_records | record_type_id | lookup tbl_record_types.record_type_name('Other archaeology')         |
| HasMolluscs      | tbl_site_other_records | record_type_id | lookup tbl_record_types.record_type_name('Molluscs')                  |
|                  | tbl_site_other_records | biblio_id      | null                                                                  |
|                  | tbl_site_other_records | description    | null                                                                  |

- Generate new row in tbl_site_other_records for each column.

Requires: TSite

## TSIteRef

| Source   | Target              | Column    | Lookup                           |
| -------- | ------------------- | --------- | -------------------------------- |
| SiteCODE | tbl_site_references | site_id   | bugs_import.bugs_traces.bugsdata |
| Ref      | tbl_site_references | biblio_id | tbl_biblio.bugs_reference        |

Requires: TSite


# TFossil
| Source           | Target                | Column                | Lookup                                             |
| ---------------- | --------------------- | --------------------- | -------------------------------------------------- |
| _FossilBugsCODE_ | ???                   |                       |                                                    |
| SampleCODE       | tbl_analysis_entities | physical_sample_id    | bugs_import.bugs_traces.bugsdata                   |
| CODE             | tbl_abundances        | taxon_id              | tbl_taxonomic_order.code                           |
|                  | tbl_abundances        | analysis_entity_id    | ???                                                |
|                  | tbl_abundances        | abundances_element_id | lookup tbl_abundance_elements.element_name = 'MNI' |
| Abundance        | tbl_abundances        | abundance             |                                                    |

- group analysis entities based on dataset + physical sample
- analysis entity dataset from countsheet (countsheet code = dataset_name, sheet-type = data_type, dataset_master = 'bugs database', method = 'palaeoentomology')
- dataset datatype = countsheet.sheet type = tbl_data_types.data_type_name

Requires: TSample, INDEX
## TFossilUncertainty
| Source         | Target | Column | Lookup |
| -------------- | ------ | ------ | ------ |
| FossilBugsCODE |        |        |        |
| Uncertainty    |        |        |        |


# TBiology
| Source | Target           | Column       | Lookup                    |
| ------ | ---------------- | ------------ | ------------------------- |
| Code   | tbl_text_biology | taxon_id     | tbl_taxonomic_order.code  |
| Ref    | tbl_text_biology | biblio_id    | tbl_biblio.bugs_reference |
| Data   | tbl_text_biology | biology_text |                           |

Require: species + taxonomic order code

# TDistrib
| Source | Target                | Column            | Lookup                    |
| ------ | --------------------- | ----------------- | ------------------------- |
| Code   | tbl_text_distribution | taxon_id          | tbl_taxonomic_order.code  |
| Ref    | tbl_text_distribution | biblio_id         | tbl_biblio.bugs_reference |
| Data   | tbl_text_distribution | distribution_text |                           |

Require: species + taxonomic order
# TBiblio

- TODO: Specify mappings
  
| Source    | Target     | Column | Lookup |
| --------- | ---------- | ------ | ------ |
| REFERENCE | tbl_biblio | ???    |        |
| AUTHOR    | tbl_biblio | ???    |        |
| TITLE     | tbl_biblio | ???    |        |
| Notes     | tbl_biblio | ???    |        |


| Source | Target     | Column         | Lookup |
| ------ | ---------- | -------------- | ------ |
|        | tbl_biblio | bugs_reference |        |
|        | tbl_biblio | doi            |        |
|        | tbl_biblio | isbn           |        |
|        | tbl_biblio | notes          |        |
|        | tbl_biblio | title          |        |
|        | tbl_biblio | year           |        |
|        | tbl_biblio | authors        |        |
|        | tbl_biblio | full_reference |        |
|        | tbl_biblio | url            |        |

- Note: Use imported version.

# TSeasonActiveAdult

| Source      | Target               | Column           | Lookup                                            |
| ----------- | -------------------- | ---------------- | ------------------------------------------------- |
| Code        | tbl_taxa_seasonality | taxon_id         | tbl_taxonomic_order.code                          |
| HSeason     | tbl_taxa_seasonality | season_id        | convert via text lookup from tbl_seasons          |
| CountryCode | tbl_taxa_seasonality | location_id      | tbl_locations.location_name=CountryCode           |
|             | tbl_taxa_seasonality | activity_type_id | tbl_activity_types.activity_type = "Adult active" |

Requires: species + taxonomic order, countries

season notes
Se => ?
Sep => ?

- How to deal with changes in datasets?
  - Currently only adding data, but the dataset for a species may change seasonal envelope. How to deal with the updated information: just add new data, or also remove the old?
- This also applies to the use case of updating data that has been updated (what counts as being the updated information?, species, location + species?)

# TLookupMonths
| Source      | Target | Column | Lookup |
| ----------- | ------ | ------ | ------ |
| SeasonCode  |        |        |        |
| SeasonName  |        |        |        |
| SeasonOrder |        |        |        |

# TCountry
| Source      | Target        | Column           | Lookup                                                          |
| ----------- | ------------- | ---------------- | --------------------------------------------------------------- |
| Country     | tbl_locations | location_name    |                                                                 |
|             | tbl_locations | location_type_id | tbl_location_types.location_type_id on location_typ = 'Country' |
| CountryCode | ???           |                  |                                                                 |
|             | tbl_locations | default_lat_dd   | null                                                            |
|             | tbl_locations | default_long_dd  | null                                                            |


# TMCRNames
| Source        | Target        | Column           | Lookup                |
| ------------- | ------------- | ---------------- | --------------------- |
| CODE          | tbl_mcr_names | taxon_id         | taxonomic_orders.code |
| MCRNumber     | tbl_mcr_names | mcr_number       |                       |
| MCRName       | tbl_mcr_names | mcr_species_name |                       |
| CompareStatus | tbl_mcr_names | comparison_notes |                       |
| MCRNameTrim   | tbl_mcr_names | mcr_name_trim    |                       |

Requires: species + taxonomic orders

## TMCRSummaryData
| Source       | Target               | Column         | Lookup                    |
| ------------ | -------------------- | -------------- | ------------------------- |
| CODE         | tbl_mcr_summary_data | taxon_id       | tbl_taxonomic_orders.code |
| TMaxLo       | tbl_mcr_summary_data | tmax_lo        |                           |
| TMaxHi       | tbl_mcr_summary_data | tmax_hi        |                           |
| TMinLo       | tbl_mcr_summary_data | tmin_lo        |                           |
| TMinHi       | tbl_mcr_summary_data | tmin_hi        |                           |
| TRangeHi     | tbl_mcr_summary_data | trange_hi      |                           |
| TRangeLo     | tbl_mcr_summary_data | trange_lo      |                           |
| COGMidTMax   | tbl_mcr_summary_data | cog_mid_tmax   |                           |
| COGMidTRange | tbl_mcr_summary_data | cod_mid_trange |                           |

Requires: species + taxonomic orders


# TEcoKoch (Koch EcoCodes)

| Source       | Target       | Column                | Lookup                               |
| ------------ | ------------ | --------------------- | ------------------------------------ |
| CODE         | tbl_ecocodes | taxon_id              | tbl_taxonomic_orders.code            |
| BugsKochCode | tbl_ecocodes | ecocode_definition_id | tbl_ecocode_definitions.abbreviation |

Requires: kochecodefinitions

## TEcoDefKoch

| Source       | Target                  | Column           | Lookup                                                            |
| ------------ | ----------------------- | ---------------- | ----------------------------------------------------------------- |
| BugsKochCode | tbl_ecocode_definitions | abbreviation     |                                                                   |
| KochCode     | ???                     | _???_            |                                                                   |
| FullName     | tbl_ecocode_definitions | label            |                                                                   |
| KochGroup    | tbl_ecocode_definitions | ecocode_group_id | tbl_ecocode_groups.name = 'Koch group' and system = 'Kock system' |
| Description  | tbl_ecocode_definitions | definition (YES) |                                                                   |
| Notes        | tbl_ecocode_definitions | notes (YES)      |                                                                   |
|              | tbl_ecocode_definitions | _name_           | ???                                                               |
|              | tbl_ecocode_definitions | _sort order_     | ???                                                               |

- ??? tbl_ecocode_group_id = tbl.ecocode_groups.ecocode_group_id
  - where TEcoDefKoch.KochGroup = TEcoDefGroups.EcoGroupCode (...)

## TEcoBugs

| Source      | Target       | Column                | Lookup                              |
| ----------- | ------------ | --------------------- | ----------------------------------- |
| CODE        | tbl_ecocodes | taxon_id              | tbl_taxonomic_order.code            |
| BugsEcoCode | tbl_ecocodes | ecocode_definition_id | tbl_ecocode_definition.abbreviation |

## TEcoDefGroups

| Source       | Target             | Column            | Lookup |
| ------------ | ------------------ | ----------------- | ------ |
| econame      | tbl_ecocode_groups | name              |        |
| ecogroupcode | tbl_ecocode_groups | abbreviation      |        |
| ????         | tbl_ecocode_groups | definition        |        |
|              | tbl_ecocode_groups | ecocode_system_id | 2      |

- A single group "Bugs" (ID = 2) is created in tbl_ecocode_groups for BugsEcoCodes.

Requires: TEcoDefBugs

## TEcoDefBugs

| Source      | Target                  | Column           | Lookup                                  |
| ----------- | ----------------------- | ---------------- | --------------------------------------- |
| BugsEcoCODE | tbl_ecocode_definitions | abbreviation     |                                         |
| EcoLabel    | tbl_ecocode_definitions | label            |                                         |
| Definition  | tbl_ecocode_definitions | definition ???   |                                         |
| Notes       | tbl_ecocode_definitions | notes ???        |                                         |
| SortOrder   | tbl_ecocode_definitions | sort_order       |                                         |
|             | tbl_ecocode_definitions | ecocode_group_id | tbl_ecocode_groups.lable = 'Bugs group' |
|             | tbl_ecocode_definitions | _name_           | ???                                     |

Requires: TEcoDefGroups

# TRDB

| Source      | Target  | Column      | Lookup                                  |         |
| ----------- | ------- | ----------- | --------------------------------------- | ------- |
| CODE        | tbl_rdb | taxon_id    | tbl_taxonomic_orders.code               |         |
| CountryCode | tbl_rdb | location_id | tbl_locations.location_name = 'Country' | Convert |
| RDBCode     | tbl_rdb | rdb_code_id | tbl_rdb_codes.rdb_code_id               |         |

Requires: species + taxonomic order, TRDBCodes, TCountry

## TRDBCodes
| Source        | Target        | Column         | Lookup | Note             |
| ------------- | ------------- | -------------- | ------ | ---------------- |
| RDBCode       | tbl_rdb_codes | rdb_code_id ?? |        | Sequence in SEAD |
| Category      | tbl_rdb_codes | rdb_category   |        |                  |
| RDBDefinition | tbl_rdb_codes | rdb_definition |        |                  |
| RDBSystemCode | tbl_rdb_codes | rdb_system_id  |        |                  |

- RDBCode is a sequence in Bugs and SEAD, is rdb_code_id auto assigned or is it set to RDBCode?

Requires: TRDBSystems

## TRDBSystems
| Source            | Target          | Column              | Lookup                                           | Note             |
| ----------------- | --------------- | ------------------- | ------------------------------------------------ | ---------------- |
| RDBSystemCode     | tbl_rdb_systems | rdb_system_id       |                                                  | Sequence in SEAD |
| RDBSystem         | tbl_rdb_systems | rdb_system          |                                                  |                  |
| RDBVersion        | tbl_rdb_systems | rdb_version         |                                                  |                  |
| RDBSystemDate     | tbl_rdb_systems | rdb_system_date     |                                                  |                  |
| RDBFirstPublished | tbl_rdb_systems | rdb_first_published |                                                  |                  |
| Ref               | tbl_rdb_systems | biblio_id           | tbl_biblio.bugs_reference                        |                  |
| CountryCode       | tbl_rdb_systems | location_id         | tbl_locations.location_name and type = 'Country' |                  |

- Note: convert country code

Requires: TCountry

# Source Code

| Importer      | Bugs  | SEAD           | Mapper                    | Persister               |
| ------------- | ----- | -------------- | ------------------------- | ----------------------- |
| IndexImporter | INDEX | TaxonomicOrder | TaxonomicOrderIndexMapper | TaxonomicOrderPersister |


current_:_timestamp => now
longvarchar => text