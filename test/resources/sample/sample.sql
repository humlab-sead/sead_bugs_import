INSERT INTO tbl_location_types (location_type_id, date_updated, description, location_type) VALUES (1, '2012-09-21 18:51:47.967181+02', 'Country or other nation state. For practical purposes territories such as England and Wales are considered countries (due to common usage)', 'Country');
INSERT INTO tbl_location_types (location_type_id, date_updated, description, location_type) VALUES (2, '2012-09-21 18:51:47.967181+02', 'Administrative units such as county, parish, län, socken. Country specific terms. Includes historical or non-active regions.', 'Sub-country administrative region');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (1, 'Country', 1, NULL, NULL, '2012-09-21 18:51:47.967181+02');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (2, 'Region', 2, NULL, NULL, '2012-09-21 18:51:47.967181+02');

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
    values (3, 'Size measurement', 'Size measurement');

insert into tbl_dimensions (dimension_id, method_group_id, dimension_name) values (1, 3, 'Upper boundary depth from unknown reference');
insert into tbl_dimensions (dimension_id, method_group_id, dimension_name) values (2, 3, 'Lower boundary depth from unknown reference');

insert into tbl_sample_groups (sample_group_id, site_id, sampling_context_id, method_id, sample_group_name)
values (1, 1, 1, 1, 'Testsheet 1');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (2, 'TCountsheet', '', 'COUN000001', 'tbl_sample_groups', 1);
insert into tbl_sample_groups (sample_group_id, site_id, sampling_context_id, method_id, sample_group_name)
values (2, 1, 1, 1, 'Testsheet 2');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (3, 'TCountsheet', '', 'COUN000002', 'tbl_sample_groups', 2);

insert into tbl_alt_ref_types (alt_ref_type_id, alt_ref_type) values (1, 'Other alternative sample name');
insert into tbl_sample_types (sample_type_id, type_name) values (1, 'Unspecified');

insert into tbl_physical_samples (physical_sample_id, sample_group_id, alt_ref_type_id, sample_type_id, sample_name)
    values (1, 1, 1, 1, 'Exists w. no dimensions');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
  values (4, 'TSample', '', 'SAMP000001', 'tbl_physical_samples', 1);

insert into tbl_physical_samples (physical_sample_id, sample_group_id, alt_ref_type_id, sample_type_id, sample_name)
values (2, 1, 1, 1, 'Exists w. dimensions');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (5, 'TSample', '', 'SAMP000002', 'tbl_physical_samples', 2);

insert into tbl_sample_dimensions (sample_dimension_id, physical_sample_id, dimension_id, method_id, dimension_value)
    values (1, 2, 1, 2, 718);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values (6, 'TSample', '', 'SAMP000002', 'tbl_sample_dimensions', 1);
insert into tbl_sample_dimensions (sample_dimension_id, physical_sample_id, dimension_id, method_id, dimension_value)
    values (2, 2, 2, 2, 720);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values (7, 'TSample', '', 'SAMP000002', 'tbl_sample_dimensions', 2);

insert into tbl_physical_samples (physical_sample_id, sample_group_id, alt_ref_type_id, sample_type_id, sample_name)
values (3, 1, 1, 1, 'This should be updated');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (8, 'TSample', '', 'SAMP000004', 'tbl_physical_samples', 3);

insert into tbl_sample_dimensions (sample_dimension_id, physical_sample_id, dimension_id, method_id, dimension_value)
values (3, 3, 1, 2, 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (9, 'TSample', '', 'SAMP000004', 'tbl_sample_dimensions', 3);
insert into tbl_sample_dimensions (sample_dimension_id, physical_sample_id, dimension_id, method_id, dimension_value)
values (4, 3, 2, 2, 720);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (10, 'TSample', '', 'SAMP000004', 'tbl_sample_dimensions', 4);

insert into tbl_physical_samples (physical_sample_id, sample_group_id, alt_ref_type_id, sample_type_id, sample_name)
values (4, 1, 1, 1, 'Update delete dimensions');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (11, 'TSample', '', 'SAMP000005', 'tbl_physical_samples', 4);

insert into tbl_sample_dimensions (sample_dimension_id, physical_sample_id, dimension_id, method_id, dimension_value)
values (5, 4, 1, 2, 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (12, 'TSample', '', 'SAMP000005', 'tbl_sample_dimensions', 5);
insert into tbl_sample_dimensions (sample_dimension_id, physical_sample_id, dimension_id, method_id, dimension_value)
values (6, 4, 2, 2, 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (13, 'TSample', '', 'SAMP000005', 'tbl_sample_dimensions', 6);

insert into tbl_physical_samples (physical_sample_id, sample_group_id, alt_ref_type_id, sample_type_id, sample_name, date_updated)
values (5, 1, 1, 1, 'Sead data newer', '2016-01-01');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id, change_date)
values (14, 'TSample', '', 'SAMP000010', 'tbl_physical_samples', 5, '2015-01-01');

insert into tbl_physical_samples (physical_sample_id, sample_group_id, alt_ref_type_id, sample_type_id, sample_name)
values (6, 1, 1, 1, 'Try to update newer sample dimension data');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (15, 'TSample', '', 'SAMP000011', 'tbl_physical_samples', 6);
insert into tbl_sample_dimensions (sample_dimension_id, physical_sample_id, dimension_id, method_id, dimension_value, date_updated)
values (7, 6, 1, 2, 1, '2016-01-01');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id, change_date)
values (16, 'TSample', '', 'SAMP000011', 'tbl_sample_dimensions', 7, '2015-01-01');
insert into tbl_sample_dimensions (sample_dimension_id, physical_sample_id, dimension_id, method_id, dimension_value, date_updated)
values (8, 6, 2, 2, 1, '2015-01-01');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id, change_date)
values (17, 'TSample', '', 'SAMP000011', 'tbl_sample_dimensions', 8, '2015-01-01');