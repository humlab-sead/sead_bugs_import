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
values (3, 'Dating to period', 'Dating to period');
insert into tbl_methods (method_id, method_group_id, method_name, method_abbrev_or_alt_name)
values (3, 3, 'ArchPer', 'ArchCal');
insert into tbl_methods (method_id, method_group_id, method_name, method_abbrev_or_alt_name)
values (4, 3, 'GeolPer', 'GeolCal');

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
values (3, 'TSample', '', 'SAMP000001', 'tbl_physical_samples', 1);

insert into tbl_dating_uncertainty (dating_uncertainty_id, uncertainty) values (1, '>');
insert into tbl_dating_uncertainty (dating_uncertainty_id, uncertainty) values (2, '?');
insert into tbl_dating_uncertainty (dating_uncertainty_id, uncertainty) values (3, 'From');

insert into tbl_relative_age_types (relative_age_type_id, age_type) values (1, 'GeolCal');
insert into tbl_relative_age_types (relative_age_type_id, age_type) values (2, 'ArchCal');

insert into tbl_relative_ages (relative_age_id, relative_age_type_id, relative_age_name, abbreviation, description, cal_age_older, cal_age_younger, location_id)
  values (1, 2, null, 'CAL_100_AD', 'Autocreated from bugs import', 1850, 1850, null);
insert into tbl_relative_dates (relative_date_id, relative_age_id, physical_sample_id, method_id, dating_uncertainty_id, notes)
    values (1, 1, 1, 3, 3, 'Already stored');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values (4, 'TDatesCalendar', '{}', 'CALE000015', 'tbl_relative_dates', 1);

insert into tbl_relative_ages (relative_age_id, relative_age_type_id, relative_age_name, abbreviation, description, cal_age_older, cal_age_younger, location_id)
  values (2, 2, null, 'CAL_120_AD', 'Autocreated from bugs import', 1830, 1830, null);
insert into tbl_relative_dates (relative_date_id, relative_age_id, physical_sample_id, method_id, dating_uncertainty_id, notes)
    values (2, 2, 1, 3, 2, 'update change uncertainty');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values (5, 'TDatesCalendar', '{}', 'CALE000011', 'tbl_relative_dates', 2);

insert into tbl_relative_dates (relative_date_id, relative_age_id, physical_sample_id, method_id, dating_uncertainty_id, notes, date_updated)
    values (3, 1, 1, 3, 3, 'sead data changed since import', '2016-01-01');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id, change_date)
    values (6, 'TDatesCalendar', '{}', 'CALE000012', 'tbl_relative_dates', 3, '2015-01-01');

insert into tbl_relative_dates (relative_date_id, relative_age_id, physical_sample_id, method_id, dating_uncertainty_id, notes)
    values (4, 1, 1, 3, 3, 'update change date');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values (7, 'TDatesCalendar', '{}', 'CALE000016', 'tbl_relative_dates', 4);

