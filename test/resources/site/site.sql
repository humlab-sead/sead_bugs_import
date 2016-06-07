insert into tbl_sites (site_id, site_name, national_site_identifier, latitude_dd, longitude_dd, altitude, site_description)
    values (1, 'Abingdon: Stert Street', 'SU 498973', 51.67222, -1.281111, 60, '15th-16th C well/pit with a few calcified remains of Porcellio dilatatus, cf. Fannia sp and Sphaeroceridae.
Not quantified.');
insert into tbl_sites (site_id, site_name, national_site_identifier, latitude_dd, longitude_dd, altitude, site_description)
    values (2, 'Agerod V', NULL , 55.9341, 13.4002, 39, 'Ageröd V. Specimens hand picked by the excavators of a mesolithic midden.');

insert into tbl_site_locations (site_id, location_id) values (1, 747);
insert into tbl_site_locations (site_id, location_id) values (1, 240);
insert into tbl_site_locations (site_id, location_id) values (2, 780);
insert into tbl_site_locations (site_id, location_id) values (2, 205);