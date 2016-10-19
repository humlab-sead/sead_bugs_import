insert into tbl_taxa_tree_authors (author_id, author_name) values (1, 'L.');
insert into tbl_taxa_tree_orders (order_id, order_name) values(1, 'ORDER PENDING CLASSIFICATION');
insert into tbl_taxa_tree_families (family_id, family_name, order_id) values (1, 'CARABIDAE', 1);
insert into tbl_taxa_tree_genera (genus_id, genus_name, family_id) values (2, 'Cicindela', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id, author_id) values (1, 'sylvatica', 2, 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id, author_id) values (2, 'campestris', 2, 1);
insert into tbl_taxonomic_order_systems (taxonomic_order_system_id, system_name) values (1, 'BugsCEP taxonomic order');
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (1, 1, 1.0000000, 1);
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (2, 2, 2.0000000, 1);

INSERT INTO tbl_location_types (location_type_id, date_updated, description, location_type) VALUES (1, '2012-09-21 18:51:47.967181+02', 'Country or other nation state. For practical purposes territories such as England and Wales are considered countries (due to common usage)', 'Country');
INSERT INTO tbl_location_types (location_type_id, date_updated, description, location_type) VALUES (2, '2012-09-21 18:51:47.967181+02', 'Administrative units such as county, parish, län, socken. Country specific terms. Includes historical or non-active regions.', 'Sub-country administrative region');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (1, 'Country', 1, NULL, NULL, '2012-09-21 18:51:47.967181+02');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (2, 'Region', 2, NULL, NULL, '2012-09-21 18:51:47.967181+02');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (3, 'USA', 1, NULL, NULL, '2012-09-21 18:51:47.967181+02');

insert into tbl_sites (site_id, site_name, national_site_identifier, latitude_dd, longitude_dd, altitude, site_description)
values (1, 'Site name', null, 1.0, -1.0, 86, 'Test site');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (1, 'TSite', 'Site name', 'SITE000001', 'tbl_sites', 1);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (1, 1, 1);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (2, 1, 2);

insert into tbl_sample_group_sampling_contexts (sampling_context_id, sampling_context)
values (1, 'Archaeological site');

insert into tbl_method_groups (method_group_id , group_name, description)
values (1, 'Sampling', 'Sampling');
insert into tbl_methods (method_id, method_group_id, method_name, description)
values (1, 1, 'Temporary record', 'Temporary record');
insert into tbl_methods (method_id, method_group_id, method_name, description)
values (2, 1, 'Presence/Absence', 'Presence/Absence');

insert into tbl_method_groups (method_group_id , group_name, description)
values (2, 'Palaeobiological', 'Palaeobiological');
insert into tbl_methods (method_id, method_group_id, method_name, method_abbrev_or_alt_name)
values (3, 2, 'Palaeoentomology', 'Fossil insect');

insert into tbl_method_groups (method_group_id, group_name, description)
values (3, 'Dating by radiometric methods', 'Dating by radiometric methods');
insert into tbl_methods (method_id, method_group_id, method_name, method_abbrev_or_alt_name)
values (4, 3, 'C14 Std', 'C14 Std');

insert into tbl_sample_groups (sample_group_id, site_id, sampling_context_id, method_id, sample_group_name)
values (1, 1, 1, 1, 'Testsheet 1');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (2, 'TCountsheet', '{COUN000001,Testsheet 1,SITE000001,Archaeological contexts,Presence/Absence}', 'COUN000001', 'tbl_sample_groups', 1);

insert into tbl_sample_groups (sample_group_id, site_id, sampling_context_id, method_id, sample_group_name)
values (2, 1, 1, 1, 'No sheet type match');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (3, 'TCountsheet', '{COUN000002,No sheet type match,SITE000001,Archaeological contexts,NMI}', 'COUN000002', 'tbl_sample_groups', 2);

insert into tbl_sample_groups (sample_group_id, site_id, sampling_context_id, method_id, sample_group_name)
values (3, 1, 1, 1, 'New data sheet');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (4, 'TCountsheet', '{COUN000003,New data sheet,SITE000001,Archaeological contexts,Presence/Absence}', 'COUN000003', 'tbl_sample_groups', 3);

insert into tbl_alt_ref_types (alt_ref_type_id, alt_ref_type) values (1, 'Other alternative sample name');
insert into tbl_sample_types (sample_type_id, type_name) values (1, 'Unspecified');

insert into tbl_physical_samples (physical_sample_id, sample_group_id, alt_ref_type_id, sample_type_id, sample_name)
values (1, 1, 1, 1, 'Exists w. no dimensions');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (5, 'TSample', '', 'SAMP000001', 'tbl_physical_samples', 1);

insert into tbl_physical_samples (physical_sample_id, sample_group_id, alt_ref_type_id, sample_type_id, sample_name)
values (2, 1, 1, 1, 'A sample');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (6, 'TSample', '', 'SAMP000002', 'tbl_physical_samples', 2);

insert into tbl_physical_samples (physical_sample_id, sample_group_id, alt_ref_type_id, sample_type_id, sample_name)
values (3, 1, 1, 1, 'A sample with geochron data');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (7, 'TSample', '', 'SAMP000003', 'tbl_physical_samples', 3);

insert into tbl_physical_samples (physical_sample_id, sample_group_id, alt_ref_type_id, sample_type_id, sample_name)
values (4, 2, 1, 1, 'A sample from an erroneous countsheet');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (8, 'TSample', '', 'SAMP000004', 'tbl_physical_samples', 4);

insert into tbl_physical_samples (physical_sample_id, sample_group_id, alt_ref_type_id, sample_type_id, sample_name)
values (5, 3, 1, 1, 'A new sample data');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (9, 'TSample', '', 'SAMP000005', 'tbl_physical_samples', 5);

insert into tbl_data_type_groups (data_type_group_id, data_type_group_name) values (1, 'Presence only');
insert into tbl_data_types (data_type_id, data_type_group_id, data_type_name) values (1, 1, 'Presence/Absence');
insert into tbl_data_type_groups (data_type_group_id, data_type_group_name) values (2, 'Composite scale');
insert into tbl_data_types (data_type_id, data_type_group_id, data_type_name) values (2, 2, 'Undefined other');

insert into tbl_dataset_masters (master_set_id, master_name) values (1, 'Bugs database');

insert into tbl_datasets (dataset_id, master_set_id, method_id, data_type_id, dataset_name) values (1, 1, 3, 1, 'COUN000001');
insert into tbl_analysis_entities (analysis_entity_id, physical_sample_id, dataset_id) values (1, 1, 1);
insert into tbl_abundances (abundance_id, taxon_id, analysis_entity_id, abundance) values (1, 1, 1, 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values (10, 'TFossil', null, 'FOSI000005', 'tbl_datasets', 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values (11, 'TFossil', null, 'FOSI000005', 'tbl_analysis_entities', 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values (12, 'TFossil', null, 'FOSI000005', 'tbl_abundances', 1);
insert into tbl_abundances (abundance_id, taxon_id, analysis_entity_id, abundance) values (2, 2, 1, 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values (13, 'TFossil', null, 'FOSI000006', 'tbl_abundances', 2);

-- stored analysis entity for other dataset method
insert into tbl_datasets (dataset_id, master_set_id, method_id, data_type_id, dataset_name) values (2, 1, 4, 2, 'DATE000001');
insert into tbl_analysis_entities (analysis_entity_id, physical_sample_id, dataset_id) values (2, 3, 2);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values (14, 'TFossil', null, 'DATE000001', 'tbl_datasets', 2);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values (15, 'TFossil', null, 'DATE000001', 'tbl_analysis_entities', 2);