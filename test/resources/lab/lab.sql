insert into tbl_location_types (location_type_id, location_type) values (1, 'Country');
insert into tbl_locations (location_id, location_name, location_type_id) values (1, 'USA', 1);
insert into tbl_locations (location_id, location_name, location_type_id) values (2, 'Sweden', 1);
insert into tbl_locations (location_id, location_name, location_type_id) values (3, 'Switzerland', 1);

insert into tbl_dating_labs (dating_lab_id, international_lab_id, lab_name, country_id)
 values (1, 'A', 'Arizona', 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
 values (1, 'TLab', '{A,Arizona,USA}', 'A', 'tbl_dating_labs', 1);

insert into tbl_dating_labs (dating_lab_id, international_lab_id, lab_name, country_id)
 values (2, 'B', 'Bern', 3);

insert into tbl_dating_labs(dating_lab_id, international_lab_id, lab_name, country_id)
    values (3, 'Test 3', 'This should be changed', 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
  values (2, 'TLab', '{Test 3,This should be changed,USA}', 'Test 3', 'tbl_dating_labs', 3);

insert into tbl_dating_labs(dating_lab_id, international_lab_id, lab_name, country_id, date_updated)
values (4, 'Test 4', 'Updated country', 2, '2016-01-01');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id, change_date)
values (3, 'TLab', '{Test 4,Sead data changed,Sweden}', 'Test 4', 'tbl_dating_labs', 4, '2015-01-01');
