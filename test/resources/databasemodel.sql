create schema bugs_import;
create table bugs_import.bugs_trace (
  bugs_trace_id integer generated by default as identity,
  bugs_table varchar(100) not null,
  bugs_data varchar,
  bugs_identifier varchar,
  sead_table varchar(255) not null,
  sead_reference_id integer not null,
  change_date timestamp not null DEFAULT current_timestamp(),
  manipulation_type varchar(50),
  constraint pk_bugs_trace_bugs_trace_id primary key(bugs_trace_id)
);

create table bugs_import.bugs_errors (
  bugs_error_id integer generated by default as identity,
  bugs_table varchar(100) not null,
  bugs_data varchar,
  bugs_identifier varchar,
  message longvarchar,
  change_date timestamp not null DEFAULT current_timestamp(),
  constraint pk_bugs_import_bugs_error_id primary key(bugs_error_id)
);

CREATE TABLE tbl_taxa_tree_orders
(
  order_id integer generated by default as identity,
  date_updated timestamp DEFAULT current_timestamp(),
  order_name varchar(50) DEFAULT NULL,
  record_type_id integer,
  sort_order integer,
  constraint pk_taxa_tree_orders_order_id primary key(order_id)
);
CREATE TABLE tbl_taxa_tree_families
(
  family_id integer generated by default as identity,
  date_updated timestamp DEFAULT current_timestamp(),
  family_name varchar(100) DEFAULT NULL,
  order_id integer NOT NULL,
  constraint pg_taxa_tree_families_family_id primary key(family_id),
  CONSTRAINT fk_taxa_tree_families_order_id FOREIGN KEY (order_id)
      REFERENCES tbl_taxa_tree_orders (order_id)
      ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE tbl_taxa_tree_genera
(
  genus_id integer generated by default as identity,
  date_updated timestamp DEFAULT current_timestamp(),
  family_id integer,
  genus_name varchar(100) DEFAULT NULL,
  constraint pk_taxa_tree_genera_genus_id primary key(genus_id),
  CONSTRAINT fk_taxa_tree_genera_family_id FOREIGN KEY (family_id)
      REFERENCES tbl_taxa_tree_families (family_id)
      ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE tbl_taxa_tree_authors
(
  author_id integer generated by default as identity,
  author_name varchar(100) DEFAULT NULL,
  date_updated timestamp DEFAULT current_timestamp(),
  constraint pk_taxa_tree_authors_author_id primary key(author_id)
);
CREATE TABLE tbl_taxa_tree_master
(
  taxon_id integer generated by default as identity,
  author_id integer,
  date_updated timestamp DEFAULT current_timestamp(),
  genus_id integer,
  species varchar(255) DEFAULT NULL,
  constraint pk_taxa_tree_master_taxon_id primary key(taxon_id),
  CONSTRAINT fk_taxa_tree_master_author_id FOREIGN KEY (author_id)
      REFERENCES tbl_taxa_tree_authors (author_id)
      ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT fk_taxa_tree_master_genus_id FOREIGN KEY (genus_id)
      REFERENCES tbl_taxa_tree_genera (genus_id)
      ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE tbl_taxonomic_order_systems
(
  taxonomic_order_system_id integer generated by default as identity,
  date_updated timestamp DEFAULT current_timestamp(),
  system_description LONGVARCHAR,
  system_name varchar(50) DEFAULT NULL,
  constraint pk_taxonomic_order_systems_taxonomic_system_order_id primary key(taxonomic_order_system_id)
);
CREATE TABLE tbl_taxonomic_order
(
  taxonomic_order_id integer generated by default as identity,
  date_updated timestamp DEFAULT current_timestamp(),
  taxon_id integer DEFAULT 0,
  taxonomic_code decimal(18,10) DEFAULT 0,
  taxonomic_order_system_id integer DEFAULT 0,
  constraint pk_taxonomic_order_taxonomic_order_id primary key (taxonomic_order_id),
  CONSTRAINT fk_taxonomic_order_taxon_id FOREIGN KEY (taxon_id)
      REFERENCES tbl_taxa_tree_master (taxon_id)
      ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_taxonomic_order_taxonomic_order_system_id FOREIGN KEY (taxonomic_order_system_id)
      REFERENCES tbl_taxonomic_order_systems (taxonomic_order_system_id)
      ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE tbl_mcrdata_birmbeetledat
(
  mcrdata_birmbeetledat_id integer generated by default as identity,
  date_updated timestamp DEFAULT current_timestamp(),
  mcr_data LONGVARCHAR,
  mcr_row smallint not null,
  taxon_id integer NOT NULL,
  CONSTRAINT pk_mcrdata_birmbeetledat PRIMARY KEY (mcrdata_birmbeetledat_id),
  CONSTRAINT fk_mcrdata_birmbeetledat_taxon_id FOREIGN KEY (taxon_id)
      REFERENCES tbl_taxa_tree_master (taxon_id)
      ON UPDATE CASCADE ON DELETE NO ACTION
);
create table tbl_biblio
(
	biblio_id integer generated by default as identity,
	bugs_author varchar(255) DEFAULT NULL,
	bugs_reference varchar(60) DEFAULT NULL,
	bugs_title varchar,
  date_updated timestamp DEFAULT current_timestamp(),
	CONSTRAINT pk_biblio_id PRIMARY KEY (biblio_id)
);
create table tbl_text_biology
(
  biology_id integer generated by default as identity,
  biblio_id integer NOT NULL,
  biology_text longvarchar,
  date_updated timestamp DEFAULT current_timestamp(),
  taxon_id integer NOT NULL,
  CONSTRAINT pk_text_biology PRIMARY KEY (biology_id),
  CONSTRAINT fk_text_biology_biblio_id FOREIGN KEY (biblio_id)
      REFERENCES tbl_biblio (biblio_id),
  CONSTRAINT fk_text_biology_taxon_id FOREIGN KEY (taxon_id)
      REFERENCES tbl_taxa_tree_master (taxon_id) 
);
create table tbl_location_types
(
  location_type_id integer generated by default as identity,
  date_updated timestamp DEFAULT current_timestamp(),
  description longvarchar,
  location_type varchar(40),
  CONSTRAINT pk_location_types PRIMARY KEY (location_type_id)
);
create table tbl_locations
(
  location_id integer generated by default as identity,
  location_name varchar(255) NOT NULL,
  location_type_id integer NOT NULL,
  default_lat_dd decimal(18,10),
  default_long_dd decimal(18,10),
  date_updated timestamp DEFAULT current_timestamp(),
  CONSTRAINT pk_locations PRIMARY KEY (location_id),
  CONSTRAINT fk_locations_location_type_id FOREIGN KEY (location_type_id)
      REFERENCES tbl_location_types (location_type_id)
);
create table tbl_taxonomy_notes (
  taxonomy_notes_id integer generated by default as identity,
  biblio_id integer NOT NULL,
  date_updated timestamp DEFAULT current_timestamp(),
  taxon_id integer NOT NULL,
  taxonomy_notes longvarchar,
  CONSTRAINT pk_taxonomy_notes PRIMARY KEY (taxonomy_notes_id),
  CONSTRAINT fk_taxonomic_notes_biblio_id FOREIGN KEY (biblio_id)
      REFERENCES tbl_biblio (biblio_id),
  CONSTRAINT fk_taxonomic_notes_taxon_id FOREIGN KEY (taxon_id)
      REFERENCES tbl_taxa_tree_master (taxon_id) 
);
create table tbl_text_identification_keys (
  key_id integer generated by default as identity,
  biblio_id integer NOT NULL,
  date_updated timestamp DEFAULT current_timestamp(),
  key_text LONGVARCHAR,
  taxon_id integer NOT NULL,
  CONSTRAINT pk_text_identification_keys PRIMARY KEY (key_id),
  CONSTRAINT fk_text_identification_keys_biblio_id FOREIGN KEY (biblio_id)
  REFERENCES tbl_biblio (biblio_id),
  CONSTRAINT fk_text_identification_keys_taxon_id FOREIGN KEY (taxon_id)
  REFERENCES tbl_taxa_tree_master (taxon_id)
);
create table tbl_text_distribution (
  distribution_id integer generated by default as identity,
  biblio_id integer NOT NULL,
  date_updated timestamp DEFAULT current_timestamp(),
  distribution_text LONGVARCHAR,
  taxon_id integer NOT NULL,
  CONSTRAINT pk_text_distribution PRIMARY KEY (distribution_id),
  CONSTRAINT fk_text_distribution_biblio_id FOREIGN KEY (biblio_id)
  REFERENCES tbl_biblio (biblio_id),
  CONSTRAINT fk_text_distribution_taxon_id FOREIGN KEY (taxon_id)
  REFERENCES tbl_taxa_tree_master (taxon_id)
);
create table tbl_mcr_summary_data(
  mcr_summary_data_id integer generated by default as identity,
  cog_mid_tmax smallint DEFAULT 0,
  cog_mid_trange smallint DEFAULT 0,
  date_updated timestamp DEFAULT current_timestamp(),
  taxon_id integer NOT NULL,
  tmax_hi smallint DEFAULT 0,
  tmax_lo smallint DEFAULT 0,
  tmin_hi smallint DEFAULT 0,
  tmin_lo smallint DEFAULT 0,
  trange_hi smallint DEFAULT 0,
  trange_lo smallint DEFAULT 0,
  CONSTRAINT pk_mcr_summary_data PRIMARY KEY (mcr_summary_data_id),
  CONSTRAINT fk_mcr_summary_data_taxon_id FOREIGN KEY (taxon_id)
      REFERENCES tbl_taxa_tree_master (taxon_id),
  CONSTRAINT key_mcr_summary_data_taxon_id UNIQUE (taxon_id)
);
create table tbl_sites (
  site_id integer generated by default as identity,
  altitude decimal(18,10),
  latitude_dd decimal(18,10),
  longitude_dd decimal(18,10),
  national_site_identifier varchar(255),
  site_description LONGVARCHAR DEFAULT NULL,
  site_name varchar(50) DEFAULT NULL,
  site_preservation_status_id integer,
  date_updated timestamp DEFAULT current_timestamp(),
  CONSTRAINT pk_sites PRIMARY KEY (site_id)
);
create table tbl_site_locations (
  site_location_id integer generated by default as identity,
  date_updated timestamp DEFAULT current_timestamp(),
  location_id integer NOT NULL,
  site_id integer NOT NULL,
  CONSTRAINT pk_site_location PRIMARY KEY (site_location_id),
  CONSTRAINT fk_locations_location_id FOREIGN KEY (location_id)
  REFERENCES tbl_locations (location_id),
  CONSTRAINT fk_locations_site_id FOREIGN KEY (site_id)
  REFERENCES tbl_sites (site_id)
);

create table bugs_import.id_based_translations (
  id_based_translation_id integer generated by default as identity,
  bugs_definition LONGVARCHAR,
  bugs_table varchar(50),
  target_column varchar(50),
  replacement_value longvarchar,
  constraint pk_id_based_translation primary key(id_based_translation_id)
);

create table bugs_import.bugs_type_translations (
  type_translation_id integer generated by default as identity,
  bugs_table varchar(50),
  bugs_column varchar(50),
  triggering_column_value longvarchar,
  target_column  varchar(50),
  replacement_value LONGVARCHAR,
  constraint pk_type_translation_id primary key (type_translation_id)
);

CREATE TABLE tbl_record_types (
  record_type_id integer generated by default as identity,
  record_type_name varchar(50),
  record_type_description longvarchar,
  date_updated timestamp DEFAULT current_timestamp(),
  CONSTRAINT pk_record_types PRIMARY KEY (record_type_id)
);

CREATE TABLE tbl_site_other_records (
  site_other_records_id integer generated by default as identity,
  site_id integer,
  biblio_id integer,
  record_type_id integer,
  description longvarchar,
  date_updated timestamp DEFAULT current_timestamp(),
  CONSTRAINT pk_site_other_records PRIMARY KEY (site_other_records_id),
  CONSTRAINT fk_site_other_records_biblio_id FOREIGN KEY (biblio_id)
  REFERENCES tbl_biblio (biblio_id),
  CONSTRAINT fk_site_other_records_record_type_id FOREIGN KEY (record_type_id)
  REFERENCES tbl_record_types (record_type_id),
  CONSTRAINT fk_site_other_records_site_id FOREIGN KEY (site_id)
  REFERENCES tbl_sites (site_id)
);

CREATE TABLE tbl_site_references (
  site_reference_id integer generated by default as identity,
  site_id integer,
  biblio_id integer NOT NULL,
  date_updated timestamp DEFAULT current_timestamp(),
  CONSTRAINT pk_site_references PRIMARY KEY (site_reference_id),
  CONSTRAINT fk_site_references_biblio_id FOREIGN KEY (biblio_id)
  REFERENCES tbl_biblio (biblio_id),
  CONSTRAINT fk_site_references_site_id FOREIGN KEY (site_id)
  REFERENCES tbl_sites (site_id)
);

CREATE TABLE tbl_sample_group_sampling_contexts (
  sampling_context_id integer generated by default as identity,
  sampling_context varchar(40) NOT NULL,
  description longvarchar,
  sort_order smallint NOT NULL DEFAULT 0,
  date_updated timestamp DEFAULT current_timestamp(),
  CONSTRAINT pk_sample_group_sampling_contexts PRIMARY KEY (sampling_context_id)
);

CREATE TABLE tbl_method_groups (
  method_group_id integer generated by default as identity,
  date_updated timestamp DEFAULT current_timestamp(),
  description longvarchar NOT NULL,
  group_name varchar(100) NOT NULL,
  CONSTRAINT pk_method_groups PRIMARY KEY (method_group_id)
);

CREATE TABLE tbl_units (
  unit_id integer generated by default as identity,
  date_updated timestamp DEFAULT current_timestamp(),
  description longvarchar,
  unit_abbrev varchar(15),
  unit_name varchar(50) NOT NULL,
  CONSTRAINT pk_units PRIMARY KEY (unit_id)
);

CREATE TABLE tbl_methods (
  method_id integer generated by default as identity,
  biblio_id integer,
  date_updated timestamp DEFAULT current_timestamp(),
  description longvarchar,
  method_abbrev_or_alt_name varchar(50),
  method_group_id integer NOT NULL,
  method_name varchar(50) NOT NULL,
  record_type_id integer,
  unit_id integer,
  CONSTRAINT pk_methods PRIMARY KEY (method_id),
  CONSTRAINT fk_methods_biblio_id FOREIGN KEY (biblio_id)
  REFERENCES tbl_biblio (biblio_id),
  CONSTRAINT fk_methods_method_group_id FOREIGN KEY (method_group_id)
  REFERENCES tbl_method_groups ,
  CONSTRAINT fk_methods_record_type_id FOREIGN KEY (record_type_id)
  REFERENCES tbl_record_types (record_type_id) ,
  CONSTRAINT fk_methods_unit_id FOREIGN KEY (unit_id)
  REFERENCES tbl_units (unit_id)
);

CREATE TABLE tbl_sample_groups
(
  sample_group_id integer generated by default as identity,
  site_id integer DEFAULT 0,
  sampling_context_id integer,
  method_id integer NOT NULL,
  sample_group_name varchar(100) DEFAULT NULL,
  sample_group_description longvarchar,
  date_updated timestamp DEFAULT current_timestamp(),
  CONSTRAINT pk_sample_groups PRIMARY KEY (sample_group_id),
  CONSTRAINT fk_sample_group_sampling_context_id FOREIGN KEY (sampling_context_id)
  REFERENCES tbl_sample_group_sampling_contexts (sampling_context_id),
  CONSTRAINT fk_sample_groups_method_id FOREIGN KEY (method_id)
  REFERENCES tbl_methods (method_id),
  CONSTRAINT fk_sample_groups_site_id FOREIGN KEY (site_id)
  REFERENCES tbl_sites (site_id)
);

CREATE TABLE tbl_season_types
(
  season_type_id integer generated by default as identity,
  date_updated timestamp DEFAULT current_timestamp(),
  description longvarchar,
  season_type varchar(30),
  CONSTRAINT pk_season_types PRIMARY KEY (season_type_id)
);

CREATE TABLE tbl_seasons
(
  season_id integer generated by default as identity,
  date_updated timestamp DEFAULT current_timestamp(),
  season_name varchar(20) DEFAULT NULL,
  season_type varchar(30) DEFAULT NULL,
  season_type_id integer,
  sort_order smallint DEFAULT 0,
  CONSTRAINT pk_seasons PRIMARY KEY (season_id),
  CONSTRAINT fk_seasons_season_type_id FOREIGN KEY (season_type_id)
  REFERENCES tbl_season_types (season_type_id)
);

CREATE TABLE tbl_activity_types
(
  activity_type_id integer generated by default as identity,
  activity_type varchar(50) DEFAULT NULL,
  description longvarchar,
  date_updated timestamp DEFAULT current_timestamp(),
  CONSTRAINT pk_activity_types PRIMARY KEY (activity_type_id)
);

CREATE TABLE tbl_taxa_seasonality
(
  seasonality_id integer generated by default as identity,
  activity_type_id integer NOT NULL,
  season_id integer DEFAULT 0,
  taxon_id integer NOT NULL,
  location_id integer NOT NULL,
  date_updated timestamp DEFAULT current_timestamp(),
  CONSTRAINT pk_taxa_seasonality PRIMARY KEY (seasonality_id),
  CONSTRAINT fk_taxa_seasonality_activity_type_id FOREIGN KEY (activity_type_id)
  REFERENCES tbl_activity_types (activity_type_id),
  CONSTRAINT fk_taxa_seasonality_location_id FOREIGN KEY (location_id)
  REFERENCES tbl_locations (location_id),
  CONSTRAINT fk_taxa_seasonality_season_id FOREIGN KEY (season_id)
  REFERENCES tbl_seasons (season_id),
  CONSTRAINT fk_taxa_seasonality_taxon_id FOREIGN KEY (taxon_id)
  REFERENCES tbl_taxa_tree_master (taxon_id)
);

CREATE TABLE tbl_ecocode_systems
(
  ecocode_system_id integer generated by default as identity,
  biblio_id integer,
  date_updated timestamp DEFAULT current_timestamp(),
  definition longvarchar,
  name varchar(50),
  notes longvarchar,
  CONSTRAINT pk_ecocode_systems PRIMARY KEY (ecocode_system_id),
  CONSTRAINT fk_ecocode_systems_biblio_id FOREIGN KEY (biblio_id)
  REFERENCES tbl_biblio (biblio_id)
);

CREATE TABLE tbl_ecocode_groups
(
  ecocode_group_id integer generated by default as identity,
  date_updated timestamp DEFAULT current_timestamp(),
  definition longvarchar,
  ecocode_system_id integer DEFAULT 0,
  name varchar(150),
  abbreviation varchar(255),
  CONSTRAINT pk_ecocode_groups PRIMARY KEY (ecocode_group_id),
  CONSTRAINT fk_ecocode_groups_ecocode_system_id FOREIGN KEY (ecocode_system_id)
  REFERENCES tbl_ecocode_systems (ecocode_system_id)
);

CREATE TABLE tbl_ecocode_definitions
(
  ecocode_definition_id integer generated by default as identity,
  abbreviation varchar(10),
  date_updated timestamp DEFAULT current_timestamp(),
  definition longvarchar,
  ecocode_group_id integer DEFAULT 0,
  name varchar(150),
  notes longvarchar,
  sort_order smallint DEFAULT 0,
  CONSTRAINT pk_ecocode_definitions PRIMARY KEY (ecocode_definition_id),
  CONSTRAINT fk_ecocode_definitions_ecocode_group_id FOREIGN KEY (ecocode_group_id)
  REFERENCES tbl_ecocode_groups (ecocode_group_id)
);

create table tbl_rdb_systems (
  rdb_system_id integer generated by default as identity,
  biblio_id integer NOT NULL,
  location_id integer NOT NULL, -- geaographical relevance of rdb code system, e.g. uk, international, new forest
  rdb_first_published smallint,
  rdb_system varchar(10),
  rdb_system_date integer,
  rdb_version varchar(10),
  date_updated timestamp DEFAULT current_timestamp(),
  CONSTRAINT pk_rdb_systems PRIMARY KEY (rdb_system_id),
  CONSTRAINT fk_rdb_systems_biblio_id FOREIGN KEY (biblio_id)
  REFERENCES tbl_biblio (biblio_id),
  CONSTRAINT fk_rdb_systems_location_id FOREIGN KEY (location_id)
  REFERENCES tbl_locations (location_id)
);

CREATE TABLE tbl_rdb_codes
(
  rdb_code_id integer generated by default as identity,
  date_updated timestamp DEFAULT current_timestamp(),
  rdb_category varchar(4),
  rdb_definition varchar(200),
  rdb_system_id integer DEFAULT 0,
  CONSTRAINT pk_rdb_codes PRIMARY KEY (rdb_code_id),
  CONSTRAINT fk_rdb_codes_rdb_system_id FOREIGN KEY (rdb_system_id)
  REFERENCES tbl_rdb_systems (rdb_system_id)
);

CREATE TABLE tbl_rdb
(
  rdb_id integer generated by default as identity,
  location_id integer NOT NULL, -- geographical source/relevance of the specific code. e.g. the international iucn classification of species in the uk.
  rdb_code_id integer,
  taxon_id integer NOT NULL,
  date_updated timestamp DEFAULT current_timestamp(),
  CONSTRAINT pk_rdb PRIMARY KEY (rdb_id),
  CONSTRAINT fk_rdb_rdb_code_id FOREIGN KEY (rdb_code_id)
  REFERENCES tbl_rdb_codes (rdb_code_id),
  CONSTRAINT fk_rdb_taxon_id FOREIGN KEY (taxon_id)
  REFERENCES tbl_taxa_tree_master (taxon_id),
  CONSTRAINT fk_tbl_rdb_tbl_location_id FOREIGN KEY (location_id)
  REFERENCES tbl_locations (location_id)
);

CREATE TABLE tbl_mcr_names
(
  taxon_id integer generated by default as identity,
  comparison_notes varchar(255),
  date_updated timestamp DEFAULT current_timestamp(),
  mcr_name_trim varchar(80),
  mcr_number smallint DEFAULT 0,
  mcr_species_name varchar(200),
  CONSTRAINT pk_mcr_names PRIMARY KEY (taxon_id),
  CONSTRAINT fk_mcr_names_taxon_id FOREIGN KEY (taxon_id)
  REFERENCES tbl_taxa_tree_master (taxon_id)
);
CREATE TABLE tbl_taxa_measured_attributes
(
  measured_attribute_id integer generated by default as identity,
  attribute_measure varchar(20),
  attribute_type varchar(25),
  attribute_units varchar(10),
  data numeric(18,10) DEFAULT 0,
  date_updated timestamp DEFAULT current_timestamp(),
  taxon_id integer NOT NULL,
  CONSTRAINT pk_taxa_measured_attributes PRIMARY KEY (measured_attribute_id),
  CONSTRAINT fk_taxa_measured_attributes_taxon_id FOREIGN KEY (taxon_id)
  REFERENCES tbl_taxa_tree_master (taxon_id)
);
CREATE TABLE tbl_contacts
(
  contact_id integer generated by default as identity,
  address_1 varchar(255),
  address_2 varchar(255),
  location_id integer,
  email varchar,
  first_name varchar(50),
  last_name varchar(100),
  phone_number varchar(50),
  url longvarchar,
  date_updated timestamp DEFAULT current_timestamp(),
  CONSTRAINT pk_contacts PRIMARY KEY (contact_id)
);
CREATE TABLE tbl_dating_labs
(
  dating_lab_id integer generated by default as identity,
  contact_id integer, -- address details are stored in tbl_contacts
  international_lab_id varchar(10) NOT NULL, -- international standard radiocarbon lab identifier....
  lab_name varchar(100), -- international standard name of radiocarbon lab, from http://www.radiocarbon.org/info/labcodes.html
  country_id integer,
  date_updated timestamp DEFAULT current_timestamp(),
  CONSTRAINT pk_dating_labs PRIMARY KEY (dating_lab_id),
  CONSTRAINT fk_dating_labs_contact_id FOREIGN KEY (contact_id)
  REFERENCES tbl_contacts (contact_id)
);
CREATE TABLE tbl_alt_ref_types
(
  alt_ref_type_id integer generated by default as identity,
  alt_ref_type varchar(50) NOT NULL,
  date_updated timestamp DEFAULT current_timestamp(),
  description longvarchar,
  CONSTRAINT pk_alt_ref_types PRIMARY KEY (alt_ref_type_id)
);
CREATE TABLE tbl_sample_types
(
  sample_type_id integer generated by default as identity,
  type_name varchar(40) NOT NULL,
  description longvarchar,
  date_updated timestamp DEFAULT current_timestamp(),
  CONSTRAINT pk_sample_types PRIMARY KEY (sample_type_id)
);
CREATE TABLE tbl_physical_samples
(
  physical_sample_id integer generated by default as identity,
  sample_group_id integer NOT NULL DEFAULT 0,
  alt_ref_type_id integer, -- type of name represented by primary sample name, e.g. lab number, museum number etc.
  sample_type_id integer NOT NULL, -- physical form of sample, e.g. bulk sample, kubienta subsample, core subsample, dendro core, dendro slice...
  sample_name varchar(50) NOT NULL, -- reference number or name of sample. multiple references/names can be added as alternative references.
  date_updated timestamp DEFAULT current_timestamp(),
  date_sampled varchar, -- Date samples were taken.
  CONSTRAINT pk_physical_samples PRIMARY KEY (physical_sample_id),
  CONSTRAINT fk_physical_samples_sample_name_type_id FOREIGN KEY (alt_ref_type_id)
  REFERENCES tbl_alt_ref_types (alt_ref_type_id),
  CONSTRAINT fk_physical_samples_sample_type_id FOREIGN KEY (sample_type_id)
  REFERENCES tbl_sample_types (sample_type_id),
  CONSTRAINT fk_samples_sample_group_id FOREIGN KEY (sample_group_id)
  REFERENCES tbl_sample_groups (sample_group_id)
);
CREATE TABLE tbl_dimensions
(
  dimension_id integer generated by default as identity,
  date_updated timestamp DEFAULT current_timestamp(),
  dimension_abbrev varchar(10),
  dimension_description longvarchar,
  dimension_name varchar(50) NOT NULL,
  unit_id integer,
  method_group_id integer, -- Limits choice of dimension by datingMethod group (e.g. size measurements, coordinate systems)
  CONSTRAINT pk_dimensions PRIMARY KEY (dimension_id),
  CONSTRAINT fk_dimensions_method_group_id FOREIGN KEY (method_group_id)
  REFERENCES tbl_method_groups (method_group_id),
  CONSTRAINT fk_dimensions_unit_id FOREIGN KEY (unit_id)
  REFERENCES tbl_units (unit_id)
);
CREATE TABLE tbl_sample_dimensions
(
  sample_dimension_id integer generated by default as identity,
  physical_sample_id integer NOT NULL,
  dimension_id integer NOT NULL, -- details of the dimension measured
  method_id integer NOT NULL, -- datingMethod describing dimension measurement, with link to units used
  dimension_value numeric(20,10) NOT NULL, -- numerical value of dimension, in the units indicated in the documentation and interface.
  date_updated timestamp DEFAULT current_timestamp(),
  CONSTRAINT pk_sample_dimensions PRIMARY KEY (sample_dimension_id),
  CONSTRAINT fk_sample_dimensions_dimension_id FOREIGN KEY (dimension_id)
  REFERENCES tbl_dimensions (dimension_id),
  CONSTRAINT fk_sample_dimensions_measurement_method_id FOREIGN KEY (method_id)
  REFERENCES tbl_methods (method_id),
  CONSTRAINT fk_sample_dimensions_physical_sample_id FOREIGN KEY (physical_sample_id)
  REFERENCES tbl_physical_samples (physical_sample_id)
);
CREATE TABLE tbl_data_type_groups
(
  data_type_group_id integer generated by default as identity,
  data_type_group_name varchar(25),
  date_updated timestamp DEFAULT current_timestamp(),
  description longvarchar,
  CONSTRAINT pk_data_type_groups PRIMARY KEY (data_type_group_id)
);
CREATE TABLE tbl_data_types
(
  data_type_id integer generated by default as identity,
  data_type_group_id integer NOT NULL,
  data_type_name varchar(25),
  date_updated timestamp DEFAULT current_timestamp(),
  definition longvarchar,
  CONSTRAINT pk_samplegroup_data_types PRIMARY KEY (data_type_id),
  CONSTRAINT fk_data_types_data_type_group_id FOREIGN KEY (data_type_group_id)
  REFERENCES tbl_data_type_groups (data_type_group_id)
);
CREATE TABLE tbl_dataset_masters
(
  master_set_id integer generated by default as identity,
  contact_id integer,
  biblio_id integer, -- primary reference for master dataset if available, e.g. buckland & buckland 2006 for bugscep
  master_name varchar(100), -- identification of master dataset, e.g. mal, bugscep, dendrolab
  master_notes longvarchar, -- description of master dataset, its form (e.g. database, lab) and any other relevant information for tracing it.
  url longvarchar, -- website or other url for master dataset, be it a project, lab or... other
  date_updated timestamp DEFAULT current_timestamp(),
  CONSTRAINT pk_dataset_masters PRIMARY KEY (master_set_id),
  CONSTRAINT fk_dataset_masters_biblio_id FOREIGN KEY (biblio_id)
  REFERENCES tbl_biblio (biblio_id),
  CONSTRAINT fk_dataset_masters_contact_id FOREIGN KEY (contact_id)
  REFERENCES tbl_contacts (contact_id)
);
CREATE TABLE tbl_project_stages
(
  project_stage_id integer generated by default as identity,
  stage_name varchar, -- stage of project in investigative cycle, e.g. desktop study, prospection, final excavation
  description longvarchar, -- explanation of stage name term, including details of purpose and general contents
  date_updated timestamp DEFAULT current_timestamp(),
  CONSTRAINT dataset_stage_id PRIMARY KEY (project_stage_id)
);
CREATE TABLE tbl_project_types
(
  project_type_id integer generated by default as identity,
  project_type_name varchar, -- descriptive name for project type, e.g. consultancy, research, teaching; also combinations consultancy/teaching
  description longvarchar, -- project type combinations can be used where appropriate, e.g. teaching/research
  date_updated timestamp DEFAULT current_timestamp(),
  CONSTRAINT pk_project_type_id PRIMARY KEY (project_type_id)
);
CREATE TABLE tbl_projects
(
  project_id integer generated by default as identity,
  project_type_id integer,
  project_stage_id integer,
  project_name varchar(150), -- name of project (e.g. phil's phd thesis, malmö ringroad vägverket)
  project_abbrev_name varchar(25), -- optional. abbreviation of project name or acronym (e.g. vgv, swedab)
  description longvarchar, -- brief description of project and any useful information for finding out more.
  date_updated timestamp DEFAULT current_timestamp(),
  CONSTRAINT pk_projects PRIMARY KEY (project_id),
  CONSTRAINT fk_projects_project_stage_id FOREIGN KEY (project_stage_id)
  REFERENCES tbl_project_stages (project_stage_id),
  CONSTRAINT fk_projects_project_type_id FOREIGN KEY (project_type_id)
  REFERENCES tbl_project_types (project_type_id)
);
CREATE TABLE tbl_datasets
(
  dataset_id integer generated by default as identity,
  master_set_id integer,
  data_type_id integer NOT NULL,
  method_id integer,
  biblio_id integer,
  updated_dataset_id integer,
  project_id integer,
  dataset_name varchar(50) NOT NULL, -- something uniquely identifying the dataset for this site. may be same as sample group name, or created adhoc if necessary, but preferably with some meaning.
  date_updated timestamp DEFAULT current_timestamp(),
  CONSTRAINT pk_datasets PRIMARY KEY (dataset_id),
  CONSTRAINT fk_datasets_biblio_id FOREIGN KEY (biblio_id)
  REFERENCES tbl_biblio (biblio_id),
  CONSTRAINT fk_datasets_data_type_id FOREIGN KEY (data_type_id)
  REFERENCES tbl_data_types (data_type_id),
  CONSTRAINT fk_datasets_master_set_id FOREIGN KEY (master_set_id)
  REFERENCES tbl_dataset_masters (master_set_id),
  CONSTRAINT fk_datasets_method_id FOREIGN KEY (method_id)
  REFERENCES tbl_methods (method_id),
  CONSTRAINT fk_datasets_project_id FOREIGN KEY (project_id)
  REFERENCES tbl_projects (project_id),
  CONSTRAINT fk_datasets_updated_dataset_id FOREIGN KEY (updated_dataset_id)
  REFERENCES tbl_datasets (dataset_id)
);
CREATE TABLE tbl_contact_types
(
  contact_type_id integer generated by default as identity,
  contact_type_name varchar(150) NOT NULL,
  date_updated timestamp DEFAULT current_timestamp(),
  description longvarchar,
  CONSTRAINT pk_contact_types PRIMARY KEY (contact_type_id)
);
CREATE TABLE tbl_dataset_contacts
(
  dataset_contact_id integer generated by default as identity,
  contact_id integer NOT NULL,
  contact_type_id integer NOT NULL,
  dataset_id integer NOT NULL,
  date_updated timestamp DEFAULT current_timestamp(),
  CONSTRAINT pk_dataset_contacts PRIMARY KEY (dataset_contact_id),
  CONSTRAINT fk_dataset_contacts_contact_id FOREIGN KEY (contact_id)
  REFERENCES tbl_contacts (contact_id),
  CONSTRAINT fk_dataset_contacts_contact_type_id FOREIGN KEY (contact_type_id)
  REFERENCES tbl_contact_types (contact_type_id),
  CONSTRAINT fk_dataset_contacts_dataset_id FOREIGN KEY (dataset_id)
  REFERENCES tbl_datasets (dataset_id)
);
CREATE TABLE tbl_analysis_entities
(
  analysis_entity_id integer generated by default as identity,
  physical_sample_id integer,
  dataset_id integer,
  date_updated timestamp DEFAULT current_timestamp(),
  CONSTRAINT pk_analysis_entities PRIMARY KEY (analysis_entity_id),
  CONSTRAINT fk_analysis_entities_dataset_id FOREIGN KEY (dataset_id)
  REFERENCES tbl_datasets (dataset_id),
  CONSTRAINT fk_analysis_entities_physical_sample_id FOREIGN KEY (physical_sample_id)
  REFERENCES tbl_physical_samples (physical_sample_id)
);
CREATE TABLE tbl_dating_uncertainty
(
  dating_uncertainty_id integer generated by default as identity,
  date_updated timestamp DEFAULT current_timestamp(),
  description longvarchar,
  uncertainty varchar,
  CONSTRAINT tbl_dating_uncertainty_pkey PRIMARY KEY (dating_uncertainty_id)
);
CREATE TABLE tbl_geochronology
(
  geochron_id integer generated by default as identity,
  analysis_entity_id integer NOT NULL,
  dating_lab_id integer,
  lab_number varchar(40),
  age numeric(20,5), -- radiocarbon (or other radiometric) age.
  error_older numeric(20,5), -- plus (+) side of the measured error (set same as error_younger if standard +/- error)
  error_younger numeric(20,5), -- minus (-) side of the measured error (set same as error_younger if standard +/- error)
  delta_13c numeric(10,5), -- delta 13c where available for calibration correction.
  notes longvarchar, -- notes specific to this date
  date_updated timestamp DEFAULT current_timestamp(),
  dating_uncertainty_id integer,
  CONSTRAINT pk_geochronology PRIMARY KEY (geochron_id),
  CONSTRAINT fk_geochronology_analysis_entity_id FOREIGN KEY (analysis_entity_id)
  REFERENCES tbl_analysis_entities (analysis_entity_id),
  CONSTRAINT fk_geochronology_dating_labs_id FOREIGN KEY (dating_lab_id)
  REFERENCES tbl_dating_labs (dating_lab_id),
  CONSTRAINT fk_geochronology_dating_uncertainty_id FOREIGN KEY (dating_uncertainty_id)
  REFERENCES tbl_dating_uncertainty (dating_uncertainty_id)
);
CREATE TABLE tbl_relative_age_types
(
  relative_age_type_id integer generated by default as identity,
  age_type varchar, -- name of chronological age type, e.g. archaeological period, single calendar date, calendar age range, blytt-sernander
  description longvarchar, -- description of chronological age type, e.g. period defined by archaeological and or geological dates representing cultural activity period, climate period defined by palaeo-vegetation records
  date_updated timestamp DEFAULT current_timestamp(),
  CONSTRAINT tbl_relative_age_types_pkey PRIMARY KEY (relative_age_type_id)
);
CREATE TABLE tbl_relative_ages
(
  relative_age_id integer generated by default as identity,
  relative_age_type_id integer,
  relative_age_name varchar(50), -- name of the dating period, e.g. bronze age. calendar ages should be given appropriate names such as ad 1492, 74 bc
  description longvarchar, -- a description of the (usually) period.
  c14_age_older numeric(20,5), -- c14 age of younger boundary of period (where relevant).
  c14_age_younger numeric(20,5), -- c14 age of later boundary of period (where relevant). leave blank for calendar ages.
  cal_age_older numeric(20,5), -- (approximate) age before present (1950) of earliest boundary of period. or if calendar age then the calendar age converted to bp.
  cal_age_younger numeric(20,5), -- (approximate) age before present (1950) of latest boundary of period. or if calendar age then the calendar age converted to bp.
  notes longvarchar, -- any further notes not included in the description, such as reliability of definition or fuzzyness of boundaries.
  date_updated timestamp DEFAULT current_timestamp(),
  location_id integer,
  abbreviation varchar, -- Standard abbreviated form of name if available
  CONSTRAINT pk_relative_ages PRIMARY KEY (relative_age_id),
  CONSTRAINT fk_relative_ages_location_id FOREIGN KEY (location_id)
  REFERENCES tbl_locations (location_id),
  CONSTRAINT fk_relative_ages_relative_age_type_id FOREIGN KEY (relative_age_type_id)
  REFERENCES tbl_relative_age_types (relative_age_type_id)
);
CREATE TABLE tbl_relative_dates
(
  relative_date_id integer generated by default as identity,
  relative_age_id integer,
  physical_sample_id integer NOT NULL,
  method_id integer, -- dating datingMethod used to attribute sample to period or calendar date.
  notes longvarchar, -- any notes specific to the dating of this sample to this calendar or period based age
  date_updated timestamp DEFAULT current_timestamp(),
  dating_uncertainty_id integer,
  CONSTRAINT pk_relative_dates PRIMARY KEY (relative_date_id),
  CONSTRAINT fk_relative_dates_dating_uncertainty_id FOREIGN KEY (dating_uncertainty_id)
  REFERENCES tbl_dating_uncertainty (dating_uncertainty_id),
  CONSTRAINT fk_relative_dates_method_id FOREIGN KEY (method_id)
  REFERENCES tbl_methods (method_id),
  CONSTRAINT fk_relative_dates_physical_sample_id FOREIGN KEY (physical_sample_id)
  REFERENCES tbl_physical_samples (physical_sample_id),
  CONSTRAINT fk_relative_dates_relative_age_id FOREIGN KEY (relative_age_id)
  REFERENCES tbl_relative_ages (relative_age_id)
);
CREATE TABLE tbl_abundances
(
  abundance_id integer generated by default as identity,
  taxon_id integer NOT NULL,
  analysis_entity_id integer NOT NULL,
  abundance integer NOT NULL DEFAULT 0, -- usually count value (abundance) but can be presence (1) or catagorical or relative scale, as defined by tbl_data_types through tbl_datasets
  date_updated timestamp DEFAULT current_timestamp(),
  CONSTRAINT pk_abundances PRIMARY KEY (abundance_id),
  CONSTRAINT fk_abundances_analysis_entity_id FOREIGN KEY (analysis_entity_id)
  REFERENCES tbl_analysis_entities (analysis_entity_id),
  CONSTRAINT fk_abundances_taxon_id FOREIGN KEY (taxon_id)
  REFERENCES tbl_taxa_tree_master (taxon_id)
);
CREATE TABLE tbl_species_association_types
(
  association_type_id integer generated by default as identity,
  association_type_name varchar(255),
  association_description longvarchar,
  date_updated timestamp DEFAULT current_timestamp(),
  CONSTRAINT tbl_association_types_pkey PRIMARY KEY (association_type_id)
);
CREATE TABLE tbl_species_associations
(
  species_association_id integer generated by default as identity,
  associated_taxon_id integer NOT NULL, -- Taxon with which the primary taxon (taxon_id) is associated.
  biblio_id integer, -- Reference where relationship between taxa is described or mentioned
  date_updated timestamp DEFAULT current_timestamp(),
  taxon_id integer NOT NULL, -- Primary taxon in relationship, i.e. this taxon has x relationship with the associated taxon
  association_type_id integer, -- Type of association between primary taxon (taxon_id) and associated taxon. Note that the direction of the association is important in most cases (e.g. x predates on y)
  referencing_type longvarchar,
  CONSTRAINT pk_species_associations PRIMARY KEY (species_association_id),
  CONSTRAINT fk_species_associations_associated_taxon_id FOREIGN KEY (taxon_id)
  REFERENCES tbl_taxa_tree_master (taxon_id),
  CONSTRAINT fk_species_associations_association_type_id FOREIGN KEY (association_type_id)
  REFERENCES tbl_species_association_types (association_type_id),
  CONSTRAINT fk_species_associations_biblio_id FOREIGN KEY (biblio_id)
  REFERENCES tbl_biblio (biblio_id),
  CONSTRAINT fk_species_associations_taxon_id FOREIGN KEY (taxon_id)
  REFERENCES tbl_taxa_tree_master (taxon_id)
);
CREATE TABLE tbl_ecocodes
(
  ecocode_id integer generated by default as identity,
  date_updated timestamp DEFAULT current_timestamp(),
  ecocode_definition_id integer DEFAULT 0,
  taxon_id integer DEFAULT 0,
  CONSTRAINT pk_ecocodes PRIMARY KEY (ecocode_id),
  CONSTRAINT fk_ecocodes_ecocodedef_id FOREIGN KEY (ecocode_definition_id)
  REFERENCES tbl_ecocode_definitions (ecocode_definition_id),
  CONSTRAINT fk_ecocodes_taxon_id FOREIGN KEY (taxon_id)
  REFERENCES tbl_taxa_tree_master (taxon_id)
);