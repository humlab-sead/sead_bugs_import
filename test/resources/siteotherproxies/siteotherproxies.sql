
INSERT INTO tbl_location_types (location_type_id, date_updated, description, location_type) VALUES (1, '2012-09-21 18:51:47.967181+02', 'Country or other nation state. For practical purposes territories such as England and Wales are considered countries (due to common usage)', 'Country');
INSERT INTO tbl_location_types (location_type_id, date_updated, description, location_type) VALUES (2, '2012-09-21 18:51:47.967181+02', 'Administrative units such as county, parish, län, socken. Country specific terms. Includes historical or non-active regions.', 'Sub-country administrative region');
INSERT INTO tbl_location_types (location_type_id, date_updated, description, location_type) VALUES (7, '2012-09-21 18:51:47.967181+02', 'Geographical areas which are not necessarily defined as single administrative units. E.g. part of a continent: Central Europe, Southern Scandinavia; Geomorphological regions: Eastern Alps; An island: Andoya, Holmön. The terms may coincide with administrative units.', 'Aggregate/non-admin geographical region');

INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (58, 'Greece', 1, NULL, NULL, '2012-09-21 18:51:47.967181+02');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (240, 'England', 1, NULL, NULL, '2012-09-21 18:51:47.967181+02');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (747, 'Oxfordshire', 2, NULL, NULL, '2012-09-21 18:51:47.967181+02');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (770, 'Santorini', 7, NULL, NULL, '2012-09-21 18:51:47.967181+02');

insert into tbl_sites (site_id, site_name) values (1, 'Akrotiri');
insert into tbl_sites (site_id, site_name, national_site_identifier, latitude_dd, longitude_dd, altitude, site_description)
values (2, 'Abingdon: Stert Street', 'SU 498973', 51.67222, -1.281111, 60, '15th-16th C well/pit with a few calcified remains of Porcellio dilatatus, cf. Fannia sp and Sphaeroceridae.
Not quantified.');
insert into tbl_site_locations (site_location_id, site_id, location_id) values (1, 2, 747);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (2, 2, 240);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (3, 1, 770);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (4, 1, 58);

insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id) values
  ('TSite', 'SITE000006Abingdon: Stert Street', 'SITE000006', 'tbl_sites', 2);
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id) values
  ('TSite', 'SITE000010Akrotiri', 'SITE000010', 'tbl_sites', 1);

insert into tbl_record_types (record_type_id, record_type_name, record_type_description) values (1,'Insects & similar','Insect and other arthrapod taxa, allong with other remains commonly extracted when analysing insect samples.');
insert into tbl_record_types (record_type_id, record_type_name, record_type_description) values (2,'Plants & pollen','Plants taxa and their pollen.');
insert into tbl_record_types (record_type_id, record_type_name, record_type_description) values (3,'Non-pollen palynomorphs','(Fossil) remains often extracted in association with pollen analyses.');
insert into tbl_record_types (record_type_id, record_type_name, record_type_description) values (4,'Molluscs','Snails and shellfish, terrestrial or aquatic.');
insert into tbl_record_types (record_type_id, record_type_name, record_type_description) values (5,'Diatoms','A major group of eukaryotic algae encased in a silica based cell wall.');
insert into tbl_record_types (record_type_id, record_type_name, record_type_description) values (6,'Ostracods','A class of the Crustacea, mainly microscopic aquatic organisms encased in a bivalve like shell.');
insert into tbl_record_types (record_type_id, record_type_name, record_type_description) values (7,'Chironomids','Non-biting midges, a Diptera family commonly treated as a separate proxy in palaeoecology, where the larval head capsules are preserved in sediments.');
insert into tbl_record_types (record_type_id, record_type_name, record_type_description) values (8,'Cladocera','Water fleas, an order of the class Brachiopoda, small aquatic crustaceans, of which Daphnia is the most commonly useful genus. ');
insert into tbl_record_types (record_type_id, record_type_name, record_type_description) values (9,'Charcoal','Carbonised remains of organic matter, the product of incomplete burning or heating.');
insert into tbl_record_types (record_type_id, record_type_name, record_type_description) values (10,'Non-biological taxa','Records for taxon based chemical/physical properties. Provides future support for chemical taxa not yet implemented.');
insert into tbl_record_types (record_type_id, record_type_name, record_type_description) values (11,'Subjective metadata','Subject descriptions of properties');
insert into tbl_record_types (record_type_id, record_type_name, record_type_description) values (12,'Soil chemistry/properties','Soil chemistry/property data, such as magnetic susceptibility, pH etc. Does not include ambient field measurement such as field based metal detector, MS-loop, fluxgate magnetaometer.');
insert into tbl_record_types (record_type_id, record_type_name, record_type_description) values (13,'Isotopes','Isotpic data of any nature');
insert into tbl_record_types (record_type_id, record_type_name, record_type_description) values (14,'Animal bones','Animal bone data available, external to SEAD. Note: human osteaology has own category.');
insert into tbl_record_types (record_type_id, record_type_name, record_type_description) values (15,'Human bones','Human osteology data available outside of SEAD.');
insert into tbl_record_types (record_type_id, record_type_name, record_type_description) values (16,'Other archaeology','More archaeology data is available through sources listed.');
insert into tbl_record_types (record_type_id, record_type_name, record_type_description) values (17,'External pollen data','Pollen data available through other sources, i.e. not stored in SEAD');
insert into tbl_record_types (record_type_id, record_type_name, record_type_description) values (18,'External plant macro data','Plant macrofossil data available through other sources, i.e. not stored in SEAD');
insert into tbl_record_types (record_type_id, record_type_name, record_type_description) values (19,'Dating','General dating data, includes geochronologies and tephra dates');

insert into tbl_site_other_records (site_other_records_id, site_id, record_type_id) values (1,1,18);
insert into tbl_site_other_records (site_other_records_id, site_id, record_type_id) values (2,1,14);
insert into tbl_site_other_records (site_other_records_id, site_id, record_type_id) values (3,1,16);
insert into tbl_site_other_records (site_other_records_id, site_id, record_type_id) values (4,1,4);
insert into tbl_site_other_records (site_other_records_id, site_id, record_type_id) values (7,1,13);
insert into tbl_site_other_records (site_other_records_id, site_id, record_type_id) values (5,2,18);
insert into tbl_site_other_records (site_other_records_id, site_id, record_type_id) values (6,2,16);