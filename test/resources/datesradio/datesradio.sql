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
values (1, 1, 'Presence/Absence', 'Presence/Absence');

insert into tbl_method_groups (method_group_id , group_name, description)
values (2, 'Coordinate and altitude systems', 'Coordinate and altitude systems');
insert into tbl_methods (method_id, method_group_id, method_name, method_abbrev_or_alt_name)
values (2, 2, 'Depth from datum', 'Depth from datum');

insert into tbl_method_groups (method_group_id, group_name, description)
values (3, 'Dating by radiometric methods', 'Dating by radiometric methods');
insert into tbl_methods (method_id, method_group_id, method_name, method_abbrev_or_alt_name)
    values (3, 3, 'C14 Std', 'C14 Std');

insert into tbl_dimensions (dimension_id, method_group_id, dimension_name) values (1, 2, 'Upper boundary depth from unknown reference');
insert into tbl_dimensions (dimension_id, method_group_id, dimension_name) values (2, 2, 'Lower boundary depth from unknown reference');

insert into tbl_sample_groups (sample_group_id, site_id, sampling_context_id, method_id, sample_group_name)
values (1, 1, 1, 1, 'Testsheet 1');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (2, 'TCountsheet', '', 'COUN000001', 'tbl_sample_groups', 1);

insert into tbl_alt_ref_types (alt_ref_type_id, alt_ref_type) values (1, 'Other alternative sample name');
insert into tbl_sample_types (sample_type_id, type_name) values (1, 'Unspecified');

insert into tbl_physical_samples (physical_sample_id, sample_group_id, alt_ref_type_id, sample_type_id, sample_name)
values (1, 1, 1, 1, 'Exists w. no dimensions');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (4, 'TSample', '', 'SAMP000001', 'tbl_physical_samples', 1);

insert into tbl_dating_labs (dating_lab_id, international_lab_id, lab_name, country_id)
values (1, 'A', 'Arizona', 3);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (5, 'TLab', '{A,Arizona,USA}', 'A', 'tbl_dating_labs', 1);

insert into tbl_data_type_groups (data_type_group_id, data_type_group_name) values (1, 'Composite scale');
insert into tbl_data_types (data_type_id, data_type_group_id, data_type_name) values (1, 1, 'Undefined other');
insert into tbl_dataset_masters (master_set_id, master_name) values (1, 'Bugs database');
insert into tbl_dating_uncertainty (dating_uncertainty_id, uncertainty) values (1, '>');
insert into tbl_dating_uncertainty (dating_uncertainty_id, uncertainty) values (2, '?');

insert into tbl_datasets (dataset_id, master_set_id, method_id, data_type_id, dataset_name)
    values (1, 1, 3, 1, 'DATE000001');
insert into tbl_analysis_entities (analysis_entity_id, physical_sample_id, dataset_id) values (1, 1, 1);
insert into tbl_geochronology (geochron_id, analysis_entity_id, dating_lab_id, lab_number, age, error_older, error_younger, notes, dating_uncertainty_id )
    values (1, 1, 1, 'lab-00001', 6000, 70, 70, 'previously inserted not updated bugs dating', null);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values (6, 'TDatesRadio', null, 'DATE000001', 'tbl_datasets', 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values (7, 'TDatesRadio', null, 'DATE000001', 'tbl_analysis_entities', 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values (8, 'TDatesRadio', null, 'DATE000001', 'tbl_geochronology', 1);

insert into tbl_datasets (dataset_id, master_set_id, method_id, data_type_id, dataset_name) values (2, 1, 3, 1, 'DATE000003');
insert into tbl_analysis_entities (analysis_entity_id, physical_sample_id, dataset_id) values (2, 1, 2);
insert into tbl_geochronology (geochron_id, analysis_entity_id, dating_lab_id, lab_number, age, error_older, error_younger, notes, dating_uncertainty_id )
    values (2, 2, 1, 'lab-00003', 6000, 70, 70, 'updated', null);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (9, 'TDatesRadio', null, 'DATE000003', 'tbl_datasets', 2);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (10, 'TDatesRadio', null, 'DATE000003', 'tbl_analysis_entities', 2);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (11, 'TDatesRadio', null, 'DATE000003', 'tbl_geochronology', 2);

insert into tbl_datasets (dataset_id, master_set_id, method_id, data_type_id, dataset_name)values (3, 1, 3, 1, 'DATE000004');
insert into tbl_analysis_entities (analysis_entity_id, physical_sample_id, dataset_id) values (3, 1, 3);
insert into tbl_geochronology (geochron_id, analysis_entity_id, dating_lab_id, lab_number, age, error_older, error_younger, notes, dating_uncertainty_id )
    values (3, 3, 1, 'lab-00004', 6000, 70, 70, 'previously inserted not updated with uncertainty', 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (12, 'TDatesRadio', null, 'DATE000004', 'tbl_datasets', 3);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (13, 'TDatesRadio', null, 'DATE000004', 'tbl_analysis_entities', 3);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (14, 'TDatesRadio', null, 'DATE000004', 'tbl_geochronology', 3);

insert into tbl_datasets (dataset_id, master_set_id, method_id, data_type_id, dataset_name) values (4, 1, 3, 1, 'DATE000012');
insert into tbl_analysis_entities (analysis_entity_id, physical_sample_id, dataset_id) values (4, 1, 4);
insert into tbl_geochronology (geochron_id, analysis_entity_id, dating_lab_id, lab_number, age, error_older, error_younger, notes, dating_uncertainty_id, date_updated)
    values (4, 4, 1, 'lab-00012', 6000, 70, 70, 'previously inserted and later updated', null, '2016-01-01');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (15, 'TDatesRadio', null, 'DATE000012', 'tbl_datasets', 4);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (16, 'TDatesRadio', null, 'DATE000012', 'tbl_analysis_entities', 4);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id, change_date)
values (17, 'TDatesRadio', null, 'DATE000012', 'tbl_geochronology', 4, '2015-01-01');
