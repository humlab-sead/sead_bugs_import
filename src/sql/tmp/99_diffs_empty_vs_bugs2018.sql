# borttagna diffar, commentarer date_updated type, varchar(255) type, etc, drop not null
# todo fånga upp alla set null? används ej av import, kan ge problem om inget default, eller om FK
ALTER TABLE "public"."tbl_abundances" DROP COLUMN "abundance_element_id";

ALTER TABLE "public"."tbl_biblio" DROP COLUMN "author";
ALTER TABLE "public"."tbl_biblio" DROP COLUMN "biblio_keyword_id";
--ALTER TABLE "public"."tbl_biblio" DROP COLUMN "bugs_biblio_id";
--ALTER TABLE "public"."tbl_biblio" DROP COLUMN "collection_or_journal_id";
ALTER TABLE "public"."tbl_biblio" DROP COLUMN "doi";
--ALTER TABLE "public"."tbl_biblio" DROP COLUMN "edition";
ALTER TABLE "public"."tbl_biblio" DROP COLUMN "isbn";
--ALTER TABLE "public"."tbl_biblio" DROP COLUMN "keywords";
ALTER TABLE "public"."tbl_biblio" DROP COLUMN "notes";
--ALTER TABLE "public"."tbl_biblio" DROP COLUMN "number";
--ALTER TABLE "public"."tbl_biblio" DROP COLUMN "pages";
--ALTER TABLE "public"."tbl_biblio" DROP COLUMN "pdf_link";
--ALTER TABLE "public"."tbl_biblio" DROP COLUMN "publication_type_id";
--ALTER TABLE "public"."tbl_biblio" DROP COLUMN "publisher_id";
--ALTER TABLE "public"."tbl_biblio" DROP COLUMN "title";
--ALTER TABLE "public"."tbl_biblio" DROP COLUMN "volume";
ALTER TABLE "public"."tbl_biblio" DROP COLUMN "year";

ALTER TABLE "public"."tbl_contacts" DROP COLUMN "location_id";

ALTER TABLE "public"."tbl_dataset_masters" DROP COLUMN "contact_id";
ALTER TABLE "public"."tbl_dataset_masters" DROP COLUMN "biblio_id";
ALTER TABLE "public"."tbl_dataset_masters" DROP COLUMN "master_notes";
ALTER TABLE "public"."tbl_dataset_masters" DROP COLUMN "url";

ALTER TABLE "public"."tbl_datasets" DROP COLUMN "biblio_id";
ALTER TABLE "public"."tbl_datasets" DROP COLUMN "project_id";
ALTER TABLE "public"."tbl_datasets" DROP COLUMN "doi";

ALTER TABLE "public"."tbl_dating_labs" DROP COLUMN "contact_id";

ALTER TABLE "public"."tbl_ecocode_definitions" DROP COLUMN "sort_order";

ALTER TABLE "public"."tbl_ecocode_systems" DROP COLUMN "biblio_id";
ALTER TABLE "public"."tbl_ecocode_systems" DROP COLUMN "notes";

ALTER TABLE "public"."tbl_geochronology" DROP COLUMN "delta_13c";

ALTER TABLE "public"."tbl_location_types" DROP COLUMN "date_updated";

ALTER TABLE "public"."tbl_locations" DROP COLUMN "default_lat_dd";
ALTER TABLE "public"."tbl_locations" DROP COLUMN "default_long_dd";

ALTER TABLE "public"."tbl_sample_group_sampling_contexts" DROP COLUMN "sort_order";

ALTER TABLE "public"."tbl_seasons" DROP COLUMN "season_type";

--ALTER TABLE "public"."tbl_sites" ALTER COLUMN "altitude" TYPE numeric(19,2);
--ALTER TABLE "public"."tbl_sites" ALTER COLUMN "latitude_dd" TYPE numeric(19,2);
--ALTER TABLE "public"."tbl_sites" ALTER COLUMN "longitude_dd" TYPE numeric(19,2);
ALTER TABLE "public"."tbl_sites" DROP COLUMN "site_preservation_status_id";
ALTER TABLE "public"."tbl_sites" DROP COLUMN "doi";

--ALTER TABLE "public"."tbl_species_association_types" ALTER COLUMN "association_type_id" SET DEFAULT nextval('tbl_species_association_types_association_type_id_seq'::regclass);

ALTER TABLE "public"."tbl_species_associations" DROP COLUMN "referencing_type";

ALTER TABLE "public"."tbl_taxa_tree_orders" DROP COLUMN "date_updated";
ALTER TABLE "public"."tbl_taxa_tree_orders" DROP COLUMN "record_type_id";
ALTER TABLE "public"."tbl_taxa_tree_orders" DROP COLUMN "sort_order";

ALTER TABLE "public"."tbl_taxonomic_order_systems" DROP COLUMN "date_updated";
ALTER TABLE "public"."tbl_taxonomic_order_systems" DROP COLUMN "system_description";

