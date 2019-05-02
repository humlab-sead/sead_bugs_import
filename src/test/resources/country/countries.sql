insert into tbl_location_types (location_type_id, location_type) values (1, 'Country');
insert into tbl_location_types (location_type_id, location_type) values (2, 'Sub-country administrative region');

insert into tbl_locations (location_id, location_name, location_type_id) values (1, 'Egypt', 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (1, 'TCountry', '{Eg,Egypt}', 'Eg', 'tbl_locations', 1);

insert into tbl_locations (location_id, location_name, location_type_id) values (2, 'Germany',1);
insert into tbl_locations (location_id, location_name, location_type_id) values (5, 'Andorra',1);
