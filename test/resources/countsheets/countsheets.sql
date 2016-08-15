INSERT INTO tbl_location_types (location_type_id, date_updated, description, location_type) VALUES (1, '2012-09-21 18:51:47.967181+02', 'Country or other nation state. For practical purposes territories such as England and Wales are considered countries (due to common usage)', 'Country');
INSERT INTO tbl_location_types (location_type_id, date_updated, description, location_type) VALUES (7, '2012-09-21 18:51:47.967181+02', 'Geographical areas which are not necessarily defined as single administrative units. E.g. part of a continent: Central Europe, Southern Scandinavia; Geomorphological regions: Eastern Alps; An island: Andoya, Holmön. The terms may coincide with administrative units.', 'Aggregate/non-admin geographical region');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (804, 'Upper Egypt', 7, NULL, NULL, '2012-09-21 18:51:47.967181+02');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (64, 'Egypt', 1, NULL, NULL, '2012-09-21 18:51:47.967181+02');

insert into tbl_sample_group_sampling_contexts (sampling_context_id, sampling_context)
    values (1, 'Archaeological site');
insert into tbl_sample_group_sampling_contexts (sampling_context_id, sampling_context)
    values (2, 'Other modern');
insert into tbl_sample_group_sampling_contexts (sampling_context_id, sampling_context)
    values (3, 'Other Palaeo');
insert into tbl_sample_group_sampling_contexts (sampling_context_id, sampling_context)
    values (4, 'Pitfall traps');
insert into tbl_sample_group_sampling_contexts (sampling_context_id, sampling_context)
    values (5, 'Stratigraphic sequence');

insert into tbl_method_groups (method_group_id , group_name, description)
    values (1, 'Sampling', 'Sampling');
insert into tbl_methods (method_id, method_group_id, method_name, description)
    values (1, 1, 'Temporary record', 'Temporary record');

insert into tbl_sites (site_id, site_name, national_site_identifier, latitude_dd, longitude_dd, altitude, site_description)
    values (1, 'Abingdon', null, 0, 0, 1, 'SITE000006');
insert into tbl_sites (site_id, site_name, national_site_identifier, latitude_dd, longitude_dd, altitude, site_description)
    values (2, 'Aghnadarragh', null, 0, 0, 1, 'SITE000009');
insert into tbl_sites (site_id, site_name, national_site_identifier, latitude_dd, longitude_dd, altitude, site_description)
    values (3, 'Akrotiri', null, 0, 0, 1, 'SITE000010');
insert into tbl_sites (site_id, site_name, national_site_identifier, latitude_dd, longitude_dd, altitude, site_description)
    values (4, 'Dendermonde', null, 0, 0, 1, 'SITE000158');
insert into tbl_sites (site_id, site_name, national_site_identifier, latitude_dd, longitude_dd, altitude, site_description)
    values (5, 'Garden under Sandet (GUS)', null, 0, 0, 1, 'SITE000251');
insert into tbl_sites (site_id, site_name, national_site_identifier, latitude_dd, longitude_dd, altitude, site_description)
    values (6, 'Knepp Cattle Dung', null, 0, 0, 1, 'SITE000969');

insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id) values
  ('TSite', 'SITE000006Abingdon: Stert Street', 'SITE000006', 'tbl_sites', 1);
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id) values
  ('TSite', 'SITE000009Aghnadarragh', 'SITE000009', 'tbl_sites', 2);
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id) values
  ('TSite', 'SITE000010Akrotiri', 'SITE000010', 'tbl_sites', 3);
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id) values
  ('TSite', 'SITE000158Dendermonde', 'SITE000158', 'tbl_sites', 4);
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id) values
  ('TSite', 'SITE000251Garden under Sandet (GUS)', 'SITE000251', 'tbl_sites', 5);
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id) values
  ('TSite', 'SITE000969Knepp Cattle Dung', 'SITE000969', 'tbl_sites', 6);

insert into tbl_sample_groups (sample_group_id, site_id, sampling_context_id, method_id, sample_group_name)
    values (1, 1, 1, 1, 'Abingdon Stert Street_bugsdata');
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values ('TCountsheet', '', 'COUN000482', 'tbl_sample_groups', 1);

insert into tbl_sample_groups (sample_group_id, site_id, sampling_context_id, method_id, sample_group_name)
    values (2, 3, 1, 1, 'Akrotiri Silk');
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values ('TCountsheet', '', 'COUN000927', 'tbl_sample_groups', 2);


insert into tbl_sample_groups (sample_group_id, site_id, sampling_context_id, method_id, sample_group_name)
    values (3, 5, 1, 1, 'Garden Under Sandet 1995_bugsdata.XLS');
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values ('TCountsheet', '', 'COUN000132', 'tbl_sample_groups', 3);

insert into tbl_sample_groups (sample_group_id, site_id, sampling_context_id, method_id, sample_group_name)
    values (4, 1, 5, 1, 'A name that is changed');
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values ('TCountsheet', '', 'COUNUPDATE', 'tbl_sample_groups', 4);

insert into tbl_sample_groups (sample_group_id, site_id, sampling_context_id, method_id, sample_group_name)
    values (5, 3, 1, 1, 'Name exists outside of bugs');


insert into bugs_import.bugs_type_translations (bugs_table, bugs_column, triggering_column_value, target_column, replacement_value)
    values ('TCountsheet', 'sheetContext', 'Archaeological contexts', 'sheetContext', 'Archaeological site');