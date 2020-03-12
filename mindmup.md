

# INDEX

Notes:

On import check CODE exists in tbl_taxonomic_order

AND genus  & species exists in respective combination

(Species can be moved in Bugs - between genera etc)

KEEP LOG OF ALL CHANGES IN BUGSCEP, BUT DO NOT TRACK THESE IN SEAD STRUCTURE

I.e. If a species changes CODE in BugsCEP, change the code in SEAD, and write a note in THE LOG (which will be put online eventually).

# TSeasonActiveAdult

Mapping

Code -> tbl_taxonomic_order.code => tbl_taxa_seasonality.taxon_id

HSeason -> convert via text lookup from tbl_seasons => tbl_taxa_seasonality.season_id

CountryCode -> convert via text lookup from tbl_locations.location_name => tbl_taxa_seasonality.location_id

lookup activity type from tbl_activity_types on 'Adult active' => tbl_taxa_seasonality.activity_type_id

requires

species + taxonomic order

countries

season notes

Se => ?

Sep => ?

How to deal with changes in datasets?

Currently only adding data, but the dataset for a species may change seasonal envelope. How to deal with the updated information: just add new data, or also remove the old?

This also applies to the use case of updating data that has been updated (what counts as being the updated information?, species, location + species?)

# TCountry

Mapping

Country -> tbl_locations.location_name

lookup tbl_location_types.location_type_id on location_type= 'Country' => tbl_locations.location_type_id

# TEcoKoch

Mapping

CODE => tbl_taxonomic_orders.code => tbl_ecocodes.taxon_id

BugsKochCode  => lookup tbl_ecocode_definitions.abbreviation => tbl_ecocodes.ecocode_definition_id

requires

kochecodefinitions

# TEcoDefKoch

Mapping

BugsKochCode => tbl_ecocode_definitions.abbreviation

FullName => tbl_ecocode_definitions.label

Definition => tbl_ecocode_definitions.definition (YES)

Notes => tbl_ecocode_definitions.notes (YES)

[lookup tbl_ecocode_groups.label = 'Koch group' and system = 'Kock system' => tbl_ecocode_definitions.ecocode_group_id ??? not dependent on KochGroup]

tbl_ecocode_group_id = tbl.ecocode_groups.ecocode_group_id where TEcoDefKoch.KochGroup = TEcoDefGroups.EcoGroupCode (...)

# TEcoDefGroups

May not have thought this one through properly as we lack the abbreviation column in SEAD.

One solution is:

tbl_ecocode_groups.label = EcoName  & " ("  & EcoGroupCode  & ")"

map this:

econame => tbl_ecocode_groups.label

ecogroupcode = > tbl_ecocode_groups.abbreviation

(Alternative is to add tbl_ecocode_groups.abbreviation)

Single group for BugsEcoCodes needs creation in tbl_ecocode_groups.

# TEcoBugs

Mapping

CODE => tbl_taxonomic_order.code = > tbl_ecocodes.taxon_id

BugsEcoCode => lookup tbl_ecocode_definition.abbreviation => tbl_ecocodes.ecocode_definition_id

requires

bugsecodefinitions

# TEcoDefBugs

Mapping

BugsEcoCODE => tbl_ecocode_definitions.abbreviation

EcoLabel => tbl_ecocode_definitions.label

Definitions => tbl_ecocode_definitions.definition ???

Notes => tbl_ecocode_definitions.notes ???

SortOrder => tbl_ecocode_definitions.sort_order

lookup tbl_ecocode_groups.lable = 'Bugs group' => tbl_ecocode_definitions.ecocode_group_id

# TRDB

Mapping

CODE => lookup tbl_taxonomic_orders.code => tbl_rdb.taxon_id

CountryCode => lookup tbl_locations.location_name type = 'Country' => tbl_rdb.location_id

RDBCode => lookup tbl_rdb_codes.rdb_code_id => tbl_rdb.rdb_code_id

convert country code

requires

species + taxonomic order

rdbcodes

countries

# TRDBCodes

Mapping

RDBCode => tbl_rdb_codes.rdb_code_id ??

Category => tbl_rdb_codes.rdb_category

RDBDefinition => tbl_rdb_codes.rdb_definition

RDBSystemCode => tbl_rdb_codes.rdb_system_id

requires

rdbsystems

# TRDBSystems

Mapping

RDBSystemCode => tbl_rdb_systems.rdb_system_id

RDBSystem => tbl_rdb_systems.rdb_system

RDBVersion => tbl_rdb_systems.rdb_version

Publication Year => tbl_rdb_systems.rdb_system_date

First Published = tbl_rdb_systems.rdb_first_published

Ref => lookup tbl_biblio.bugs_reference => tbl_rdb_systems.biblio_id

Country/International => lookup tbl_locations.location_name and type = 'Country' => tbl_rdb_systems.location_id

convert country code

requires

countries

# TMCRSummaryData

Mapping

CODE => tbl_taxonomic_orders.code => tbl_mcr_summary_data.taxon_id

TMaxLo => tbl_mcr_summary_data.tmax_lo

TMaxHi => tbl_mcr_summary_data.tmax_hi

TMinLo => tbl_mcr_summary_data.tmin_lo

TMinHi => tbl_mcr_summary_data.tmin_hi

TRangeHi => tbl_mcr_summary_data.trange_hi

TRangeLo => tbl_mcr_summary_data.trange_lo

COGMidTMax => tbl_mcr_summary_data.cog_mid_tmax

COGMidTRange => tbl_mcr_summary_data.cod_mid_trange

requires

species + taxonomic orders

# TMCRNames

Mapping

CODE => lookup taxonomic_orders.code => tbl_mcr_names.taxon_id

MCRNumber => tbl_mcr_names.mcr_number

MCRName => tbl_mcr_names.mcr_species_name

CompareStatus => tbl_mcr_names.comparison_notes

MCRNameTrim => tbl_mcr_names.mcr_name_trim

requires

species + taxonomic orders

# TSynonym

Mapping

CODE => lookup taxonomic_orders.code => tbl_taxa_synonyms.taxon_id

SynGenus => lookup tbl_taxa_tree_genera.genus_name (bugs order) => tbl_taxa_synonyms.genus_id

???

this needs to be checked.

species association (type synonym of) code = target (official sead),   syngenus +  syn authority = source

create necessary taxon (including tree, as needed). and add species association.

Data extraction and insertion needs to handle this too.

Notes:

In data entry needs to find synonyms so that user knows they are entering a synonym, even though it is master species that is stored.

# TTaxoNotes

Mapping

Code => tbl_taxonomic_order.code => tbl_taxonomy_notes.taxon_id

Ref => tbl_biblio.bugs_reference => tbl_taxonomy_notes.biblio_id

Data => tbl_taxonomy_notes.taxonomy_notes

requires

species + taxonomic order

# TKeys

Mapping

Code -> tbl_taxonomic_order.code => tbl_text_identification_keys.taxon_id

Ref -> tbl_biblio.bugs_reference => tbl_text_identification_keys.biblio_id

Data -> tbl_text_identification_keys.key_text

requires

species + taxonomic order

# TDistrib

Mapping

Code -> tbl_taxonomic_order.code => tbl_text_distribution.taxon_id

Ref -> tbl_biblio.bugs_reference => tbl_text_distribution.biblio_id

Data -> tbl_text_distribution.distribution_text

requires

species + taxonomic order

# TBiblio

Use imported version

# TBiology

Mapping

Code -> tbl_taxonomic_order.code => tbl_text_biology.taxon.id

ref -> tbl_biblio.bugs_reference => tbl_text_biology.biblio.id

data -> tbl_text_biology.biology_text

required

species + taxonomic order code

# TSpeciesAssociation

Mapping

Code -> tbl_taxonomic_order.code => tbl_species_associations.taxon_id

AssociatedSpeciesCODE -> tbl_taxonomic_order.code => tbl_species_associations.associated_taxon_id

AssociationType -> tbl_tbl_species_association_types.association_type_name => tbl_species_associations.association_type_id (possible conversion needed)

Ref -> tbl_biblio.bugs_reference => tbl_species_associations.biblio_id

on association type does not exist raise error

empty type? => is associated with (conversion)

requires

species + taxonomic order

association types (?)

# TAttributes

Mapping

Code => tbl_taxonomic_order.code => tbl_taxa_measured_attributes.taxon_id

AttribType => tbl_taxa_measured_attributes.attribute_type

AttribMeasure => tbl_taxa_measured_attributes.measure

Value => tbl_taxa_measured_attributes.data

AttribUnits => tbl_taxa_measured_attributes.attribute_units

requires

species + taxonomic order

# TFossil

Mapping

CODE => tbl_taxonomic_order.code => tbl_abundances.taxon_id

SampleCODE => bugs_import.bugs_traces.bugsdata => tbl_analysis_entities.physical_sample_id

Abundance => tbl_abundances.abundance

lookup tbl_abundance_elements.element_name = 'MNI' => tbl_abundance.abundance_element_id

group analysis entities based on dataset + physical sample

analysis entity dataset from countsheet (countsheet code = dataset_name, sheet-type = data_type, dataset_master = 'bugs database', method = 'palaeoentomology')

dataset datatype = countsheet.sheet type  = tbl_data_types.data_type_name

requires

samples

index

# TSample

Mapping

CountsheeCODE => bugs_import.bugs_traces.bugsdata => tbl_physical_samples.sample_group_id

RefNrContext => tbl_physical_samples.sample_name

lookup tbl_sample_types.type_name = 'Unspecified' => tbl_physical_samples.sample_type_id

lookup tbl_alt_ref_types.alt_ref_type = 'Other alternative sample name' => tbl_physical_samples.alt_ref_type_id

ZorDepthTop => create tbl_sample_dimensions

dimension_value  = ZorDepthTop

method_id = tbl_methods.method_abbrev_or_alt_name = 'Depth from datum', ?? this method points to method group 17 => coordinate and altitude systems

dimension_id = tbl_dimensions.dimension_name = 'Upper boundary depth from unknown reference' ?? This dimension is pointing to method group 14 => Size measurement

ZorDepthBottom => create tbl_sample_dimensions

dimension_value  = ZorDepthBottom

method_id = tbl_methods.method_abbrev_or_alt_name = 'Depth from datum',

dimension_id = tbl_dimensions.dimension_name = 'Lower boundary depth from unknown reference'

Ignore the following as no data in Bugs:

X => tbl_sample_dimensions.dimension_value [method_id = ?, dimension_id = ?]

Y => tbl_sample_dimensions.dimension_value [method_id = ?, dimension_id = ?]

Data in X or Y produce warning.

requires

countsheets

# TCountsheet

Mapping

CountsheeName => tbl_sample_groups.sample_group_name

SiteCODE => bugs_import.bugs_traces.bugsData => tbl_sample_groups.site_id

SheetContext => converted lookup tbl_sample_group_sampling_contexts.sampling_context => tbl_sample_groups.sampling_context_id

SheetType =>

Lookup tbl_method.method_name = 'Temporary record' => tbl_sample_groups.method_id

empty context generate error.

conversions for context

'Archaeological contexts' => 'Archaeological site'

requires

sites

# TSite

Mapping

SiteName => tbl_sites.site_name

Region => tbl_locations.location_name + type = 'Unprocessed Bugs Transfer' => tbl_site_locations.location_id

Country => tbl_locations.location_name + type = 'Country' => tbl_site_locations.location_id

NGR => tbl_sites.national_site_identifier

LatDD => tbl_sites.latitude_dd

LongDD => tbl_sites.longitude_dd

Alt => tbl_sites.altitude

Interp => tbl_sites.description

SiteCODE => bugs_import.bugs_traces.bugsdata

requires

countries

conversions for region

'Alpes Maritime' => 'Alpes-Maritimes'

'Ameraliksfjord' => 'Ameraliksfjordur'

'Angermannland' => 'Angermanland'

'Co. Down' => 'County Dowm'

'Co. Louth' => 'County Louth'

'Inverness shire' => 'Inverness-shire'

'Leics.' => 'Leicestershire'

'Møen' => 'Møn'

'Noord Brabant' => 'Noord-Brabant

'Noord Holland' => 'Noord-Holland'

'North' => 'North Holland'

'Not located' => NULL

'Notts.' => 'Nottinghamshire'

'Ostergottland' => 'Ostergotland'

'Ostobottnia media' => 'Ostrobothnia media'

'Ostrobottnia australis' => 'Ostrobothnia australis'

'Ostrobottnia borealis' => 'Ostrobothnia borealis'

'Ostrobottnia media' => 'Ostrobothnia media'

'Reykjavik' => 'Reykjavík'

'S Uist, Outer Hebrides' => 'South Uist, Outer Hebrides'

'Sjaelland' => 'Zealand'

'Vagsøy' => 'Vågsøy'

'Yorks' => 'Yorkshire'

'Ångermanland' => 'Angermanland'

--UPDATE tbl_bugs_tsite SET "Country" = 'United Kingdom' WHERE "SiteCODE" = 'SITE000006';

region = Isle of Man, Jersey, Guernsey => location type = Country

# TSIteRef

Mapping

SiteCODE => bugs_import.bugs_traces.bugsdata => tbl_site_references.site_id

Ref => tbl_biblio.bugs_reference => tbl_site_references.biblio_id

requires

sites

# TSiteOtherProxies

Mapping

SiteCODE => bugs_import.bugs_trace.bugsdata => tbl_site_other_records.site_id

HasPollen => lookup tbl_record_types.record_type_name('External pollen data') =>  tbl_site_other_records.record_type_id

HasPlantMacro => lookup tbl_record_types.record_type_name('External plant macro data') =>  tbl_site_other_records.record_type_id

HasDiatoms => lookup tbl_record_types.record_type_name('Diatoms') =>  tbl_site_other_records.record_type_id

HasChironomids => lookup tbl_record_types.record_type_name('Chironomids') =>  tbl_site_other_records.record_type_id

HasSoilChemistry => lookup tbl_record_types.record_type_name('Soil chemistry/properties') =>  tbl_site_other_records.record_type_id

HasIsotopes => lookup tbl_record_types.record_type_name('Isotopes') =>  tbl_site_other_records.record_type_id

HasAnimalBones => lookup tbl_record_types.record_type_name('Animal bones') =>  tbl_site_other_records.record_type_id

HasArcheology => lookup tbl_record_types.record_type_name('Other archaeology') =>  tbl_site_other_records.record_type_id

HasMolluscs => lookup tbl_record_types.record_type_name('Molluscs') =>  tbl_site_other_records.record_type_id

bugs columns generate new row

requires

sites

# TDatesRadio

Mapping

DateCode => tbl_Datasets.dataset_name

SampleCode => bugs_import.bugs_traces.bugsdata => tbl_analysis_entities.physical_sample_id

LabNr => tbl_geochronology.lab_number

Uncertainty => lookup tbl_dating_uncertainty.uncertainty = Uncertainty => tbl_geochronology.dating_uncertainty_id

Date => tbl_geochronology.age

AgeErrorOrPlusError => tbl_geochronology.error_older

AgeErrorMinus => tbl_geochronology.error_younger

DatingMethod => lookup tbl_methods.method_abbrev_or_alt_name = DatingMethod => tbl_datasets.method_id

LabId => lookup tbl_dating_labs.international_lab_id = LabId => tbl_geochronology.dating_lab_id

Notes => tbl_geochronology.notes

1. create tbl_datasets

master_set_id = lookup tbl_dataset_masters.master_name = 'Bugs database"

data_type_id = lookup tbl_data_types.data_type_name = 'Undefined other' ? ???? Should this not be the correct data type, or is that not known?

method_id = lookup tbl_methods.method_abbrev_or_alt_name = DatingMethod

dataset_name = DateCODE

2. create tbl_analysis_entities

physical_sample_id = Lookup bugs_import.bugs_traces.bugsData contain SampleCODE

dataset_id = created dataset

3. create tbl_geochronology

analysis_entity_id = created analysi entity

dating_lab_id = lookup tbl_dating_labs.international_lab_id = LabId

lab_number = LabNr

age = Date

error_older = AgeErrorOrPlusError

error_younger = AgeErrorMinus || AgeErrorOrPlusError

notes = Notes

Age -> different precision and scale than rest of application?

Dating material??

How to deal with dating specifications with only a note?

-> fix by conversions.

How to deal with dating specifications without data?

-> Skip completely empty items.

-> function for ignoring specific items (error on empty date).

conversions AgeErrorMinus

Null => AgeErrorOrPlusError

dating methods converted to abbreviations in sead: see mapdatingmethod($1, $2)

dating uncertainty updates

c => Ca.

ca => Ca.

from => From

to => To

LabId transformations

Birmingham => Birm

requires

samples

# TLab

Mapping

LabId => tbl_dating_labs.international_lab_id

Labname => tbl_dating_labs.lab_name

Country => lookup tbl_locations.location_name = Country and type = 'Country' => tbl_dating_labs.country_id

# TDatesCalendar

Mapping

    SampleCODE      => lookup bugs_import.bugs_trace.bugsdata => tbl_relative_dates.physical_sample_id

    Uncertainty     => lookup tbl_dating_uncertainty.uncertainty => tbl_relative_dates.dating_uncertainty_id

    Date + BCADBP   => concatenate 'CAL_' + Date + BCADBP => lookup tbl_relative_ages."Abbreviation" => tbl_relative_dates.relative_age_id

    DatingMethod    => lookup tbl_methods.method_abbrev_or_alt_name => tbl_relative_dates.method_id

    Notes           => tbl_relative_dates.notes

    date + bcadbp   => new relative age, set age data from values;

                    -> relative age type from dating method (?)

How to map dating method to relative age type?

Fix naming before or string lookup?

set method by datingMethod ?

 -> not the same thing

From + to from same sample generates one relative age (start and stop dates). type = calendar date range.

 -> a range is one relative dates also.

Single for a sample => relative age type = calendar date

Normalize date data

convertsions

uncertainty

    To >        => >

    from ca.    => From ca.

    Tc ca.      => To ca.

    Ca,         => Ca.

requires

samples

# TDatesPeriod

Mapping

PeriodCode => bugs_import.bugs_trace.bugsdata => tbl_relative_dates.relative_age_id

SampleCODE => bugs_import.bugs_trace.bugsdata => tbl_relative_dates.physical_sample_id

Uncertainty => lookup tbl_dating_uncertainty.uncertainty => tbl_relative_dates.dating_uncertainty_id

DatingMethod => lookup tbl_methods.metod_abbrev_or_alt_name => tbl_relative_dates.method_id

Notes => tbl_relative_dates.notes

requires

periods

samples

transformations

dating method = 'ArchPer' and period.yeartype = 'C14' => method name = ArchPerC14

dating method = 'ArchPer' and period.yeartype = 'Calendar' => method name = ArchPerCal

dating method = 'GeolPer' and period.yeartype = 'C14' => method name = GeolPerC14

dating method = 'GeolPer' and period.yeartype = 'Calendar' => method name = GeolPerCal

in script ' ' (single space) uncertianty is ok, but cannot be read using external library, or not be stored via newer Access. Cannot test this but will build into functionality.

# TPeriods

Mapping

Period Name => tbl_relative_ages.relative_age_name

Type => lookup tbl_relative_age_types.age_type = Type => tbl_relative_ages.relative_age_type_id

Description => tbl_relative_ages.description

Reference => lookup tbl_biblio.bugs_reference => tbl_relative_age_refs.biblio_id

Geography => lookup tbl_locations.location_name => tbl_relative_ages.location_id

Begin + BP etc => convert using type = 'begin' and method = 'C14' => tbl_relative_ages.c14_age_older

End + BP etc => convert using type = 'end' and method = 'C14' => tbl_relative_ages.c14_age_younger

Begin + BP etc => convert using type = 'begin' and method = 'cal' => tbl_relative_ages.cal_age_older

End + BP etc => convert using type = 'end' and method = 'cal' => tbl_relative_ages.cal_age_younger

PeriodCODE => tbl_relative_ages."Abbreviation"

exclude for periodcode = '?'

conversion for age values

(see ImportFunctions.GetPeriodAge(date_value, bcad_value, age_direction, age_type))

-> if type = c14 convert iff bcad_value = 'BP'

-> if type = 'cal' convert iff bcad_value = 'BP'

usage:

GetPeriodAge(prow.periodbegin, prow.beginbcad, 'beginbcad', 'c14') => c14_age_older

GetPeriodAge(prow.periodend, prow.endbcad, 'endbcad', 'c14') => c14_age_younger

GetPeriodAge(prow.periodbegin, prow.beginbcad, 'beginbcad', 'cal') => cal_age_older

GetPeriodAge(prow.periodend, prow.endbcad, 'endbcad', 'cal') => cal_age_younger

reduce this...

date_value = null => null

bcad_value = null  & date_value = 0  & age_type = 'c14' => 0

bcad_value = null  & date_value = 0  & age_type != 'c14' => null

bcad_value = 'BP'  & age_type = 'cal' => return null;

bcad_value = 'BP'  & age_type != 'cal'  & age_direction = 'beginbcad' => date_value

bcad_value = 'BP'  & age_type != 'cal'  & age_direction = 'endbcad' => date_value

(bcad_value = 'AD' or bcad_value = 'BC')  & age_type = 'c14' => null

bcad_value = 'AD'  & age_direction = 'beginbcad' => 1950 - date_value

bcad_value = 'AD'  & age_direction = 'endbcad' => 1950 - date_value

bcad_value = 'BC'  & age_direction = 'beginbcad' => date_value + 1950

bcad_value = 'BC'  & age_direction = 'endbcad' => date_value + 1950

Abbreviation column name should be changed!!!!

Note:

Data cleaning in Bugs needed... still
MindmupExport
Visar MindmupExport.