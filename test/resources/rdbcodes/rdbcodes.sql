insert into tbl_location_types (location_type_id, location_type) values (1, 'Country');
insert into tbl_locations (location_id, location_name, location_type_id) values (1, 'United kingdom', 1);
insert into tbl_locations (location_id, location_name, location_type_id) values (2, 'Sweden', 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (1, 'Country', '', 'UK', 'tbl_locations', 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (2, 'Country', '', 'Swe', 'tbl_locations', 2);
insert into tbl_biblio (biblio_id, bugs_author, bugs_reference, bugs_title) values (1, 'Hyman, P.S. (1992)', 'Hyman 1992', 'A review of the scarce and threatened Coleoptera of Great Britain, Part 1 (Revised & updated by M.S.Parsons). UK Joint Nature Conservation Committee, Peterborough.');

INSERT INTO tbl_rdb_systems (rdb_system_id, biblio_id, location_id, rdb_system) VALUES (1, 1, 1, 'Test 1');
INSERT INTO tbl_rdb_systems (rdb_system_id, biblio_id, location_id, rdb_system) VALUES (2, 1, 2, 'Test 2');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
  values (3, 'TRDBSystems', '{1,Test 1,null,null,null,Hyman 1992,UK}', '1', 'tbl_rdb_systems', 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (4, 'TRDBSystems', '{1,Test 2,null,null,null,Hyman 1992,UK}', '2', 'tbl_rdb_systems', 2);

insert into tbl_rdb_codes (rdb_code_id, rdb_category, rdb_definition, rdb_system_id)
    values (1, 'AB', 'already stored w. trace', 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
 values (5, 'TRDBCodes', '{2,AB,already stored w. trace,1}', '2', 'tbl_rdb_codes', 1);

insert into tbl_rdb_codes (rdb_code_id, rdb_category, rdb_definition, rdb_system_id)
 values (2, 'AC', 'already added w/o import', 1);

insert into tbl_rdb_codes (rdb_code_id, rdb_category, rdb_definition, rdb_system_id)
 values (3, 'AD', 'update', 2);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (6, 'TRDBCodes', '{4,AD,update,2}', '4', 'tbl_rdb_codes', 3);

insert into tbl_rdb_codes (rdb_code_id, rdb_category, rdb_definition, rdb_system_id, date_updated)
values (4, 'E2', 'sead changes made', 1, '2016-01-01');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id, change_date)
values (7, 'TRDBCodes', '{7,E2,error sead changes,1}', '7', 'tbl_rdb_codes', 4, '2015-01-01');
