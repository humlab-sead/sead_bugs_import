INSERT INTO tbl_location_types (location_type_id, date_updated, description, location_type) VALUES (1, '2012-09-21 18:51:47.967181+02', 'Country or other nation state. For practical purposes territories such as England and Wales are considered countries (due to common usage)', 'Country');
INSERT INTO tbl_location_types (location_type_id, date_updated, description, location_type) VALUES (2, '2012-09-21 18:51:47.967181+02', 'Administrative units such as county, parish, län, socken. Country specific terms. Includes historical or non-active regions.', 'Sub-country administrative region');
INSERT INTO tbl_location_types (location_type_id, date_updated, description, location_type) VALUES (7, '2012-09-21 18:51:47.967181+02', 'Geographical areas which are not necessarily defined as single administrative units. E.g. part of a continent: Central Europe, Southern Scandinavia; Geomorphological regions: Eastern Alps; An island: Andoya, Holmön. The terms may coincide with administrative units.', 'Aggregate/non-admin geographical region');

INSERT INTO tbl_locations (location_id, location_name, location_type_id) VALUES (105, 'Ireland', 1);
INSERT INTO tbl_locations (location_id, location_name, location_type_id) VALUES (205, 'Sweden', 1);
INSERT INTO tbl_locations (location_id, location_name, location_type_id) VALUES (240, 'England', 1);
INSERT INTO tbl_locations (location_id, location_name, location_type_id) VALUES (590, 'Antrim', 2);
INSERT INTO tbl_locations (location_id, location_name, location_type_id) VALUES (747, 'Oxfordshire', 2);
INSERT INTO tbl_locations (location_id, location_name, location_type_id) VALUES (770, 'Santorini', 7);
INSERT INTO tbl_locations (location_id, location_name, location_type_id) VALUES (780, 'Skåne', 2);
INSERT INTO tbl_locations (location_id, location_name, location_type_id) VALUES (777, 'Greece', 1);

insert into tbl_sites (site_id, site_name, national_site_identifier, latitude_dd, longitude_dd, altitude, site_description)
values (1, 'Normal: prestored site', 'SU 498973', 51.672222, -1.2811111, 60, '15th-16th C well/pit with a few calcified remains of Porcellio dilatatus, cf. Fannia sp and Sphaeroceridae.
Not quantified.');
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id) values ('TSite', 'Normal: Prestored site', 'SITE000006', 'tbl_sites', 1);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (1, 1, 747);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (2, 1, 240);

insert into tbl_sites (site_id, site_name, national_site_identifier, latitude_dd, longitude_dd, altitude, site_description)
values (3, 'Update site', null, 51.67222, -1.281111, 60, 'Site information updated');
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values ('TSite', 'Update site', 'SITE000010', 'tbl_sites', 3);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (3, 3, 770);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (4, 3, 777);

insert into tbl_sites (site_id, site_name, national_site_identifier, latitude_dd, longitude_dd, altitude, site_description)
values (4, 'Name already exists', 'SU 123', null, null, null, 'Name clashes with existing non-imported site');
insert into tbl_site_locations (site_location_id, site_id, location_id) values (5, 4, 590);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (6, 4, 240);

insert into tbl_sites (site_id, site_name, national_site_identifier, latitude_dd, longitude_dd, altitude, site_description, date_updated)
values (5, 'Newer Sead changes', 'SU 321', null, null, 60, 'Site has been updated in sead database after import', '2016-01-01');
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id, change_date)
values ('TSite', 'Newer Sead changes', 'SITE000022', 'tbl_sites', 5, '2015-01-01');
insert into tbl_site_locations (site_location_id, site_id, location_id) values (7, 5, 105);

insert into tbl_sites (site_id, site_name, national_site_identifier, latitude_dd, longitude_dd, altitude, site_description)
values (6, 'name and ngr will change', 'SU 321', 0, 0, 0, 'Site changes name and national identifier');
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values ('TSite', 'name and ngr will change', 'SITE000025', 'tbl_sites', 6);

insert into tbl_sites (site_id, site_name, national_site_identifier, latitude_dd, longitude_dd, altitude, site_description)
values (7, 'Non-unique name', 'SU 321', 0, 0, 0, 'Name exists for multiple sites with same location, previously not from Bugs');
insert into tbl_site_locations (site_location_id, site_id, location_id) values (8, 7, 590);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (9, 7, 240);

insert into tbl_sites (site_id, site_name, national_site_identifier, latitude_dd, longitude_dd, altitude, site_description)
values (8, 'Non-unique name', 'SU 321', 0, 0, 0, 'Name exists for multiple sites with same location, previously not from Bugs');
insert into tbl_site_locations (site_location_id, site_id, location_id) values (10, 8, 590);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (11, 8, 240);