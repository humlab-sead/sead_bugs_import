insert into tbl_biblio (biblio_id, bugs_author, bugs_reference, bugs_title) values (1, 'Hyman, P.S. (1992)', 'Hyman 1992', 'A review of the scarce and threatened Coleoptera of Great Britain, Part 1 (Revised & updated by M.S.Parsons). UK Joint Nature Conservation Committee, Peterborough.');
insert into tbl_biblio (biblio_id, bugs_author, bugs_reference, bugs_title) values (2, 'IUCN (2001)', 'IUCN 2001', 'IUCN Red List Categories and Criteria: Version 3.1. IUCN Species Survival Commission. IUCN, Gland, Switzerland and Cambridge, U.K. ii + 30pp.');
insert into tbl_biblio (biblio_id, bugs_author, bugs_reference, bugs_title) values (3, 'Gärdenfors, U. (2000)', 'Gärdenfors 2000', 'Hur rödlistas arter? Manualer och riktlinjer [How to red-list species. Manual and guidelines]. ArtDatabanken, SLU, Uppsala [In Swedish, English Summary].');
insert into tbl_biblio (biblio_id, bugs_author, bugs_reference, bugs_title) values (4, 'Test (2000)', 'Test 2000', 'Test bibliographic material');
insert into tbl_biblio (biblio_id, bugs_author, bugs_reference, bugs_title) values (5, 'Updated (1894)', 'Updated 1894', 'Update test bibliographic material');
insert into tbl_biblio (biblio_id, bugs_author, bugs_reference, bugs_title) values (6, 'Error no system', 'Error no system', 'This is just an error message');
insert into tbl_biblio (biblio_id, bugs_author, bugs_reference, bugs_title) values (7, 'Error failed country', 'Error country does not exists', 'No country exist for code');

insert into tbl_location_types (location_type_id, location_type) values (1, 'Country');
insert into tbl_locations (location_id, location_name, location_type_id) values (1, 'United kingdom', 1);
insert into tbl_locations (location_id, location_name, location_type_id) values (2, 'International', 1);
insert into tbl_locations (location_id, location_name, location_type_id) values (3, 'Sweden', 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (8, 'TCountry', '', 'UK', 'tbl_locations', 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (10, 'TCountry', '', 'Swe', 'tbl_locations', 3);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (12, 'TCountry', '', 'Int', 'tbl_locations', 2);

INSERT INTO tbl_rdb_systems (rdb_system_id, biblio_id, location_id, rdb_first_published, rdb_system,rdb_system_date, rdb_version)
  VALUES (1, 1, 1, null, 'UKRDB',null, null);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (1, 'TRDBSystems', '{1,UKRDB,null,null,null,Hyman 1992,UK}', '1', 'tbl_rdb_systems', 1);

INSERT INTO tbl_rdb_systems (rdb_system_id, biblio_id, location_id, rdb_first_published, rdb_system,rdb_system_date, rdb_version, date_updated)
  VALUES (2, 5, 2, 1894, 'TestUpdate', 1999, '3.1', '2016-01-01');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id, change_date)
values (2, 'TRDBSystems', '{5,TestUpdate,3.1,1999,1894,Updated 1894,Int}', '5', 'tbl_rdb_systems', 2, '2016-01-01');

INSERT INTO tbl_rdb_systems (rdb_system_id, biblio_id, location_id, rdb_first_published, rdb_system,rdb_system_date, rdb_version, date_updated)
VALUES (3, 3, 3, 2000, 'OldUpdate', 2000, '2', '2016-01-01');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id, change_date)
values (3, 'TRDBSystems', '{9,OldUpdate,1,2000,2000,Gärdenfors 2000,Swe}', '9', 'tbl_rdb_systems', 3, '2015-01-01');



