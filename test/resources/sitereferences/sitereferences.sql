INSERT INTO tbl_location_types (location_type_id, date_updated, description, location_type) VALUES (1, '2012-09-21 18:51:47.967181+02', 'Country or other nation state. For practical purposes territories such as England and Wales are considered countries (due to common usage)', 'Country');
INSERT INTO tbl_location_types (location_type_id, date_updated, description, location_type) VALUES (2, '2012-09-21 18:51:47.967181+02', 'Administrative units such as county, parish, län, socken. Country specific terms. Includes historical or non-active regions.', 'Sub-country administrative region');
INSERT INTO tbl_location_types (location_type_id, date_updated, description, location_type) VALUES (7, '2012-09-21 18:51:47.967181+02', 'Geographical areas which are not necessarily defined as single administrative units. E.g. part of a continent: Central Europe, Southern Scandinavia; Geomorphological regions: Eastern Alps; An island: Andoya, Holmön. The terms may coincide with administrative units.', 'Aggregate/non-admin geographical region');

INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (58, 'Greece', 1, NULL, NULL, '2012-09-21 18:51:47.967181+02');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (240, 'England', 1, NULL, NULL, '2012-09-21 18:51:47.967181+02');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (747, 'Oxfordshire', 2, NULL, NULL, '2012-09-21 18:51:47.967181+02');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (770, 'Santorini', 7, NULL, NULL, '2012-09-21 18:51:47.967181+02');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (780, 'Skåne', 2, NULL, NULL, '2012-09-21 18:51:47.967181+02');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (205, 'Sweden', 1, NULL, NULL, '2012-09-21 18:51:47.967181+02');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (590, 'Antrim', 2, NULL, NULL, '2012-09-21 18:51:47.967181+02');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (105, 'Ireland', 1, NULL, NULL, '2012-09-21 18:51:47.967181+02');
INSERT INTO tbl_locations (location_id, location_name, location_type_id, default_lat_dd, default_long_dd, date_updated) VALUES (819, 'Warwickshire', 2, NULL, NULL, '2012-09-21 18:51:47.967181+02');

insert into tbl_sites (site_id, site_name, national_site_identifier, latitude_dd, longitude_dd, altitude, site_description)
  values (1, 'Abingdon: Stert Street', 'SU 498973', 51.67222, -1.281111, 60, '15th-16th C well/pit with a few calcified remains of Porcellio dilatatus, cf. Fannia sp and Sphaeroceridae.
Not quantified.');
insert into tbl_sites (site_id, site_name, national_site_identifier, latitude_dd, longitude_dd, altitude, site_description)
  values (2, 'Agerod V', NULL , 55.934105, 13.400202, 39, 'Ageröd V. Specimens hand picked by the excavators of a mesolithic midden.');
insert into tbl_sites (site_id, site_name, national_site_identifier, latitude_dd, longitude_dd, altitude, site_description)
    values (3, 'Akrotiri', null, 36.04623, 25.41966,	10, 'Panagiotakopulu	Largely pests of stored products, preserved by both charring and desiccation by tephra burying Late Bronze Age town, ca. 1600 BC.');
insert into tbl_sites (site_id, site_name, national_site_identifier, latitude_dd, longitude_dd, altitude, site_description)
    values (4, 'Alcester: Coulter''s Garage', 'SP 088575', 52.21556,	-1.8725,	43, 'Girling	Late Iron Age natural assemblage adjacent to Roman town.');

insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id) values
  ('TSite', 'SITE000006Abingdon: Stert Street', 'SITE000006', 'tbl_sites', 1);
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id) values
  ('TSite', 'SITE000008Agerod V', 'SITE000008', 'tbl_sites', 2);
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id) values
  ('TSite', 'SITE000010Akrotiri', 'SITE000010', 'tbl_sites', 3);
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id) values
  ('TSite', 'SITE000012Alcester: Coulter''s Garage', 'SITE000012', 'tbl_sites', 4);

insert into tbl_site_locations (site_location_id, site_id, location_id) values (1, 1, 747);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (2, 1, 240);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (3, 2, 780);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (4, 2, 205);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (5, 3, 58);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (6, 3, 770);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (7, 4, 240);
insert into tbl_site_locations (site_location_id, site_id, location_id) values (8, 4, 819);

insert into tbl_biblio (biblio_id, bugs_author, bugs_reference, bugs_title) values (1, 'Robinson, M. A.  (1979)','Robinson (1979b)','Calcified seeds and arthopods.  In M. Parrington, Excavations at Stert Street, Abingdon, Oxon.  Oxoniensia, 44, 23-24.');
insert into tbl_biblio (biblio_id, bugs_author, bugs_reference, bugs_title) values (3, 'Lemdahl, G. & Nilsson, A., (1982)','Lemdahl & Nilsson (1982)','Ett postglacialt subfossilfynd av dykarbaggen Cybister lateralimarginalis från Skane. Entomologisk Tidskrift, 103, 121-122.');
insert into tbl_biblio (biblio_id, bugs_author, bugs_reference, bugs_title) values (4, 'Lemdahl, G.  (1983)','Lemdahl (1983)','Beetle remains from the Refuse Layer of the Bog Site Ageröd V. Appendix V, In L. Larsson (ed.) Ageröd V, an inland bogsite from the Atlantic period.  Acta Archaeologica Lundensia, 8° ser., 12, 169-172.');
insert into tbl_biblio (biblio_id, bugs_author, bugs_reference, bugs_title) values (5, 'McCabe, A.M., Coope, G. R., Gennard, D. E. & Doughty, P. (1987)','McCabe et al. (1987)','Freshwater organic deposits and stratified sediments between Early and Late Midlandian (Devensian) till sheets at Aghnadarragh, County Antrim, Northern Ireland. Journal of Quaternary Science, 2, 11-34.');
insert into tbl_biblio (biblio_id, bugs_author, bugs_reference, bugs_title) values (6, 'Panagiotakopulu, E. (2000)','Panagiotakopulu (2000)','Archaeology and entomology in the eastern Mediterranean. Research into the history of insect synanthropy in Greece and Egypt. British Archaeological Reports International Series 836. Oxford.');
insert into tbl_biblio (biblio_id, bugs_author, bugs_reference, bugs_title) values (7, 'Panagiotakopulu, E. & Buckland, P.C. (1991)','Panagiotakopulu & Buckland (1991)','Insect pests of stored products from Late Bronze Age Santorini, Greece. Journal of stored Product Research, 27, 179-184.');
insert into tbl_biblio (biblio_id, bugs_author, bugs_reference, bugs_title) values (8, 'Panagiotakopulu, E., Buckland, P. C., Day, P., Doumas, C., Sarpali, A. & Skidmore, P. (1997)','Panagiotakopulu et al. (1997)','A lepidopterous cocoon from Thera and evidence for silk in the Aegean Bronze Age. Antiquity, 71, 420-429.');
insert into tbl_biblio (biblio_id, bugs_author, bugs_reference, bugs_title) values (9, 'Shotton, F. W. Osborne, P. J. & Greig, J. R. A.  (1977)','Shotton et al. (1977)','The fossil content of a Flandrian deposit at Alcester.  Proceedings of the Coventry and District Natural History and Scientific Society, 5, 19-32.');

insert into tbl_site_references (site_reference_id, site_id, biblio_id) values (1, 2, 3);
insert into tbl_site_references (site_reference_id, site_id, biblio_id) values (2, 2, 4);
insert into tbl_site_references (site_reference_id, site_id, biblio_id) values (3, 3, 7);
insert into tbl_site_references (site_reference_id, site_id, biblio_id) values (4, 3, 8);
