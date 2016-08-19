INSERT INTO tbl_location_types (location_type_id, date_updated, description, location_type) VALUES (1, '2012-09-21 18:51:47.967181+02', 'Country or other nation state. For practical purposes territories such as England and Wales are considered countries (due to common usage)', 'Country');
INSERT INTO tbl_location_types (location_type_id, date_updated, description, location_type) VALUES (2, '2012-09-21 18:51:47.967181+02', 'Administrative units such as county, parish, län, socken. Country specific terms. Includes historical or non-active regions.', 'Sub-country administrative region');
INSERT INTO tbl_location_types (location_type_id, date_updated, description, location_type) VALUES (7, '2012-09-21 18:51:47.967181+02', 'Geographical areas which are not necessarily defined as single administrative units. E.g. part of a continent: Central Europe, Southern Scandinavia; Geomorphological regions: Eastern Alps; An island: Andoya, Holmön. The terms may coincide with administrative units.', 'Aggregate/non-admin geographical region');

insert into tbl_locations (location_id, location_name, location_type_id)
    values (1, 'Country', 1);
insert into tbl_locations (location_id, location_name, location_type_id)
    values (2, 'Other country', 1);
insert into tbl_locations (location_id, location_name, location_type_id)
    values (3, 'Region', 2);
insert into tbl_locations (location_id, location_name, location_type_id)
    values (4, 'Other region', 2);
insert into tbl_locations (location_id, location_name, location_type_id)
    values (5, 'Region type', 2);
insert into tbl_locations (location_id, location_name, location_type_id)
    values (6, 'Møn', 2);
insert into tbl_locations (location_id, location_name, location_type_id)
    values (7, 'Skåne', 1);
insert into tbl_locations (location_id, location_name, location_type_id)
    values (8, 'Region type', 7);

insert into tbl_sites (site_id, site_name)
    values (1, 'all locations exists');
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values ('TSite', 'all locations exists', 'SITE000001', 'tbl_sites', 1);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (1, 1, 1);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (2, 1, 3);

insert into tbl_sites (site_id, site_name)
    values (2, 'new region');
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values ('TSite', 'new region', 'SITE000002', 'tbl_sites', 2);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (3, 2, 1);

insert into tbl_sites (site_id, site_name)
    values (3, 'change country');
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values ('TSite', 'change country', 'SITE000003', 'tbl_sites', 3);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (4, 3, 1);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (5, 3, 3);

insert into tbl_sites (site_id, site_name)
    values (4, 'change region');
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values ('TSite', 'change region', 'SITE000004', 'tbl_sites', 4);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (6, 4, 1);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (7, 4, 3);

insert into tbl_sites (site_id, site_name)
    values (5, 'No locations stored');
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values ('TSite', 'No locations stored', 'SITE000005', 'tbl_sites', 5);

insert into tbl_sites (site_id, site_name)
    values (6, 'Change country and region');
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values ('TSite', 'Change country and region', 'SITE000006', 'tbl_sites', 6);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (8, 6, 1);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (9, 6, 3);

insert into tbl_sites (site_id, site_name)
    values (7, 'Localized region');
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values ('TSite', 'Localized region', 'SITE000007', 'tbl_sites', 7);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (11, 7, 7);

insert into tbl_sites (site_id, site_name)
    values (8, 'Region with same name different type');
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values ('TSite', 'Region with same name different type', 'SITE000008', 'tbl_sites', 8);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (12, 8, 1);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (13, 8, 8);

insert into tbl_sites (site_id, site_name, date_updated)
    values (9, 'Sead changed after bugs import', '2016-01-01');
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id, change_date)
    values ('TSite', 'Sead changed after bugs import', 'SITE000009', 'tbl_sites', 9, '2015-01-01');
insert into tbl_site_locations (site_location_id, site_id, location_id) values (14, 9, 1);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (15, 9, 3);

insert into tbl_sites (site_id, site_name, date_updated)
    values (10, 'Sead locations changed after bugs import', '2015-01-01');
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id, change_date)
    values ('TSite', 'Sead changed after bugs import', 'SITE000010', 'tbl_sites', 10, '2015-01-01');
insert into tbl_site_locations (site_location_id, site_id, location_id, date_updated) values (16, 10, 1, '2015-01-01');
insert into tbl_site_locations (site_location_id, site_id, location_id, date_updated) values (17, 10, 3, '2016-01-01');
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id, change_date)
values ('TSite', '{SITE000010CountryRegion}', null, 'tbl_site_locations', 16, '2015-01-01');
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id, change_date)
values ('TSite', '{SITE000010CountryRegion}', null, 'tbl_site_locations', 17, '2015-01-01');

insert into tbl_sites (site_id, site_name)
    values (11, 'Country does not exist');
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values ('TSite', 'Country does not exist', 'SITE000011', 'tbl_sites', 11);
insert into tbl_sites (site_id, site_name)
    values (12, 'Region does not exists');
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values ('TSite', 'Region does not exist', 'SITE000012', 'tbl_sites', 12);
insert into tbl_sites (site_id, site_name)
    values (13, 'Empty  country');
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values ('TSite', 'Empty country', 'SITE000013', 'tbl_sites', 13);
insert into tbl_sites (site_id, site_name)
    values (14, 'Empty region');
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values ('TSite', 'Empty region', 'SITE000014', 'tbl_sites', 14);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (18, 14, 1);

insert into tbl_sites (site_id, site_name)
    values (16, 'Site deleted');
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id, manipulation_type)
    values ('TSite', 'Site deleted', 'SITE000016', 'tbl_sites', 16, 'DELETE');