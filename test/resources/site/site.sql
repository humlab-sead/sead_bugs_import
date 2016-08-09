insert into tbl_sites (site_id, site_name, national_site_identifier, latitude_dd, longitude_dd, altitude, site_description)
    values (1, 'Abingdon: Stert Street', 'SU 498973', 51.67222, -1.281111, 60, '15th-16th C well/pit with a few calcified remains of Porcellio dilatatus, cf. Fannia sp and Sphaeroceridae.
Not quantified.');
insert into tbl_sites (site_id, site_name, national_site_identifier, latitude_dd, longitude_dd, altitude, site_description)
    values (2, 'Agerod V', NULL , 55.934105, 13.400202, 39, 'Ageröd V. Specimens hand picked by the excavators of a mesolithic midden.');
insert into tbl_sites (site_id, site_name, national_site_identifier, latitude_dd, longitude_dd, altitude, site_description)
    values (3, 'Aghnadarragh', 'NG 457642' , 54.596592, -6.255964, 30, 'Woody detritus peat overlying');


INSERT INTO tbl_location_types (location_type_id, date_updated, description, location_type) VALUES (1, '2012-09-21 18:51:47.967181+02', 'Country or other nation state. For practical purposes territories such as England and Wales are considered countries (due to common usage)', 'Country');
INSERT INTO tbl_location_types (location_type_id, date_updated, description, location_type) VALUES (2, '2012-09-21 18:51:47.967181+02', 'Administrative units such as county, parish, län, socken. Country specific terms. Includes historical or non-active regions.', 'Sub-country administrative region');
INSERT INTO tbl_location_types (location_type_id, date_updated, description, location_type) VALUES (7, '2012-09-21 18:51:47.967181+02', 'Geographical areas which are not necessarily defined as single administrative units. E.g. part of a continent: Central Europe, Southern Scandinavia; Geomorphological regions: Eastern Alps; An island: Andoya, Holmön. The terms may coincide with administrative units.', 'Aggregate/non-admin geographical region');

INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (58, 'Denmark', 1, NULL, NULL, '2012-09-21 18:51:47.967181+02');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (105, 'Ireland', 1, NULL, NULL, '2012-09-21 18:51:47.967181+02');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (162, 'Norway', 1, NULL, NULL, '2012-09-21 18:51:47.967181+02');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (205, 'Sweden', 1, NULL, NULL, '2012-09-21 18:51:47.967181+02');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (240, 'England', 1, NULL, NULL, '2012-09-21 18:51:47.967181+02');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (241, 'France', 1, NULL, NULL, '2012-09-21 18:51:47.967181+02');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (590, 'Antrim', 2, NULL, NULL, '2012-09-21 18:51:47.967181+02');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (707, 'Møn', 2, NULL, NULL, '2012-09-21 18:51:47.967181+02');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (747, 'Oxfordshire', 2, NULL, NULL, '2012-09-21 18:51:47.967181+02');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (770, 'Santorini', 7, NULL, NULL, '2012-09-21 18:51:47.967181+02');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (780, 'Skåne', 2, NULL, NULL, '2012-09-21 18:51:47.967181+02');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (819, 'Warwickshire', 2, NULL, NULL, '2012-09-21 18:51:47.967181+02');

insert into tbl_site_locations (site_location_id, site_id, location_id) values (1, 1, 747);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (2, 1, 240);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (3, 2, 780);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (4, 2, 205);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (5, 3, 590);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (6, 3, 105);
