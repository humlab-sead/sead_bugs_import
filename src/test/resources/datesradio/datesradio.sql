INSERT INTO tbl_location_types(location_type_id, date_updated, description, location_type)
    VALUES (1, '2012-09-21 18:51:47.967181+02', 'Country or other nation state. For practical purposes territories such as England and Wales are considered countries (due to common usage)', 'Country');

INSERT INTO tbl_location_types(location_type_id, date_updated, description, location_type)
    VALUES (2, '2012-09-21 18:51:47.967181+02', 'Administrative units such as county, parish, län, socken. Country specific terms. Includes historical or non-active regions.', 'Sub-country administrative region');

INSERT INTO tbl_locations(location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated)
    VALUES (1, 'Country', 1, NULL, NULL, '2012-09-21 18:51:47.967181+02');

INSERT INTO tbl_locations(location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated)
    VALUES (2, 'Region', 2, NULL, NULL, '2012-09-21 18:51:47.967181+02');

INSERT INTO tbl_locations(location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated)
    VALUES (3, 'USA', 1, NULL, NULL, '2012-09-21 18:51:47.967181+02');

INSERT INTO tbl_sites(site_id, site_name, national_site_identifier, latitude_dd, longitude_dd, altitude, site_description)
    VALUES (1, 'Site name', NULL, 1.0, -1.0, 86, 'Test site');

INSERT INTO bugs_import.bugs_trace(bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    VALUES (1, 'TSite', 'Site name', 'SITE000001', 'tbl_sites', 1);

INSERT INTO tbl_site_locations(site_location_id, site_id, location_id)
    VALUES (1, 1, 1);

INSERT INTO tbl_site_locations(site_location_id, site_id, location_id)
    VALUES (2, 1, 2);

INSERT INTO tbl_sample_group_sampling_contexts(sampling_context_id, sampling_context)
    VALUES (1, 'Archaeological site');

INSERT INTO tbl_method_groups(method_group_id, group_name, description)
    VALUES (1, 'Sampling', 'Sampling');

INSERT INTO tbl_methods(method_id, method_group_id, method_name, description)
    VALUES (1, 1, 'Presence/Absence', 'Presence/Absence');

INSERT INTO tbl_method_groups(method_group_id, group_name, description)
    VALUES (2, 'Coordinate and altitude systems', 'Coordinate and altitude systems');

INSERT INTO tbl_methods(method_id, method_group_id, method_name, method_abbrev_or_alt_name)
    VALUES (2, 2, 'Depth from datum', 'Depth from datum');

INSERT INTO tbl_method_groups(method_group_id, group_name, description)
    VALUES (3, 'Dating by radiometric methods', 'Dating by radiometric methods');

INSERT INTO tbl_methods(method_id, method_group_id, method_name, method_abbrev_or_alt_name)
    VALUES (3, 3, 'C14 Std', 'C14 Std');

INSERT INTO tbl_dimensions(dimension_id, method_group_id, dimension_name)
    VALUES (1, 2, 'Upper boundary depth from unknown reference');

INSERT INTO tbl_dimensions(dimension_id, method_group_id, dimension_name)
    VALUES (2, 2, 'Lower boundary depth from unknown reference');

INSERT INTO tbl_sample_groups(sample_group_id, site_id, sampling_context_id, method_id, sample_group_name)
    VALUES (1, 1, 1, 1, 'Testsheet 1');

INSERT INTO bugs_import.bugs_trace(bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    VALUES (2, 'TCountsheet', '', 'COUN000001', 'tbl_sample_groups', 1);

INSERT INTO tbl_alt_ref_types(alt_ref_type_id, alt_ref_type)
    VALUES (1, 'Other alternative sample name');

INSERT INTO tbl_sample_types(sample_type_id, type_name)
    VALUES (1, 'Unspecified');

INSERT INTO tbl_physical_samples(physical_sample_id, sample_group_id, alt_ref_type_id, sample_type_id, sample_name)
    VALUES (1, 1, 1, 1, 'Exists w. no dimensions');

INSERT INTO bugs_import.bugs_trace(bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    VALUES (4, 'TSample', '', 'SAMP000001', 'tbl_physical_samples', 1);

INSERT INTO tbl_dating_labs(dating_lab_id, international_lab_id, lab_name, country_id)
    VALUES (1, 'A', 'Arizona', 3);

INSERT INTO bugs_import.bugs_trace(bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    VALUES (5, 'TLab', '{A,Arizona,USA}', 'A', 'tbl_dating_labs', 1);

INSERT INTO tbl_dating_labs(dating_lab_id, international_lab_id, lab_name)
    VALUES (2, 'Unknown', 'Unknown lab, or non-certified lab');

INSERT INTO tbl_data_type_groups(data_type_group_id, data_type_group_name)
    VALUES (1, 'Composite scale');

INSERT INTO tbl_data_types(data_type_id, data_type_group_id, data_type_name)
    VALUES (1, 1, 'Undefined other');

INSERT INTO tbl_dataset_masters(master_set_id, master_name)
    VALUES (1, 'Bugs database');

INSERT INTO tbl_dating_uncertainty(dating_uncertainty_id, uncertainty)
    VALUES (1, '>');

INSERT INTO tbl_dating_uncertainty(dating_uncertainty_id, uncertainty)
    VALUES (2, '?');

INSERT INTO tbl_datasets(dataset_id, master_set_id, method_id, data_type_id, dataset_name)
    VALUES (1, 1, 3, 1, 'DATE000001');

INSERT INTO tbl_analysis_entities(analysis_entity_id, physical_sample_id, dataset_id)
    VALUES (1, 1, 1);

INSERT INTO tbl_geochronology(geochron_id, analysis_entity_id, dating_lab_id, lab_number, age, error_older, error_younger, notes, dating_uncertainty_id)
    VALUES (1, 1, 1, 'lab-00001', 6000, 70, 70, 'previously inserted not updated bugs dating', NULL);

INSERT INTO bugs_import.bugs_trace(bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    VALUES (6, 'TDatesRadio', NULL, 'DATE000001', 'tbl_datasets', 1);

INSERT INTO bugs_import.bugs_trace(bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    VALUES (7, 'TDatesRadio', NULL, 'DATE000001', 'tbl_analysis_entities', 1);

INSERT INTO bugs_import.bugs_trace(bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    VALUES (8, 'TDatesRadio', NULL, 'DATE000001', 'tbl_geochronology', 1);

INSERT INTO tbl_datasets(dataset_id, master_set_id, method_id, data_type_id, dataset_name)
    VALUES (2, 1, 3, 1, 'DATE000003');

INSERT INTO tbl_analysis_entities(analysis_entity_id, physical_sample_id, dataset_id)
    VALUES (2, 1, 2);

INSERT INTO tbl_geochronology(geochron_id, analysis_entity_id, dating_lab_id, lab_number, age, error_older, error_younger, notes, dating_uncertainty_id)
    VALUES (2, 2, 1, 'lab-00003', 6000, 70, 70, 'updated', NULL);

INSERT INTO bugs_import.bugs_trace(bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    VALUES (9, 'TDatesRadio', NULL, 'DATE000003', 'tbl_datasets', 2);

INSERT INTO bugs_import.bugs_trace(bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    VALUES (10, 'TDatesRadio', NULL, 'DATE000003', 'tbl_analysis_entities', 2);

INSERT INTO bugs_import.bugs_trace(bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    VALUES (11, 'TDatesRadio', NULL, 'DATE000003', 'tbl_geochronology', 2);

INSERT INTO tbl_datasets(dataset_id, master_set_id, method_id, data_type_id, dataset_name)
    VALUES (3, 1, 3, 1, 'DATE000004');

INSERT INTO tbl_analysis_entities(analysis_entity_id, physical_sample_id, dataset_id)
    VALUES (3, 1, 3);

INSERT INTO tbl_geochronology(geochron_id, analysis_entity_id, dating_lab_id, lab_number, age, error_older, error_younger, notes, dating_uncertainty_id)
    VALUES (3, 3, 1, 'lab-00004', 6000, 70, 70, 'previously inserted not updated with uncertainty', 1);

INSERT INTO bugs_import.bugs_trace(bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    VALUES (12, 'TDatesRadio', NULL, 'DATE000004', 'tbl_datasets', 3);

INSERT INTO bugs_import.bugs_trace(bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    VALUES (13, 'TDatesRadio', NULL, 'DATE000004', 'tbl_analysis_entities', 3);

INSERT INTO bugs_import.bugs_trace(bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    VALUES (14, 'TDatesRadio', NULL, 'DATE000004', 'tbl_geochronology', 3);

INSERT INTO tbl_datasets(dataset_id, master_set_id, method_id, data_type_id, dataset_name)
    VALUES (4, 1, 3, 1, 'DATE000012');

INSERT INTO tbl_analysis_entities(analysis_entity_id, physical_sample_id, dataset_id)
    VALUES (4, 1, 4);

INSERT INTO tbl_geochronology(geochron_id, analysis_entity_id, dating_lab_id, lab_number, age, error_older, error_younger, notes, dating_uncertainty_id, date_updated)
    VALUES (4, 4, 1, 'lab-00012', 6000, 70, 70, 'previously inserted and later updated', NULL, '2016-01-01');

INSERT INTO bugs_import.bugs_trace(bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    VALUES (15, 'TDatesRadio', NULL, 'DATE000012', 'tbl_datasets', 4);

INSERT INTO bugs_import.bugs_trace(bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    VALUES (16, 'TDatesRadio', NULL, 'DATE000012', 'tbl_analysis_entities', 4);

INSERT INTO bugs_import.bugs_trace(bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id, change_date)
    VALUES (17, 'TDatesRadio', NULL, 'DATE000012', 'tbl_geochronology', 4, '2015-01-01');


/*
insert into tbl_datasets (dataset_id, master_set_id, method_id, data_type_id, dataset_name) values (5, 1, 3, 1, 'DATE000013');
insert into tbl_analysis_entities (analysis_entity_id, physical_sample_id, dataset_id) values (5, 1, 5);
insert into tbl_geochronology (geochron_id, analysis_entity_id, dating_lab_id, lab_number, age, error_older, error_younger, notes, dating_uncertainty_id)
values (5, 5, XXX, 'abc-1', 6000, 100, 100, 'Lab not set', 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (18, 'TDatesRadio', null, 'DATE000013', 'tbl_datasets', 5);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (19, 'TDatesRadio', null, 'DATE000013', 'tbl_analysis_entities', 5);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (20, 'TDatesRadio', null, 'DATE000013', 'tbl_geochronology', 5);
 */
