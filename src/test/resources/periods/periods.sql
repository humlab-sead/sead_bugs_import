insert into tbl_location_types (location_type_id, location_type) values (1, 'Country');
insert into tbl_location_types (location_type_id, location_type) values (2, 'Sub-country administrative region');
insert into tbl_locations (location_id, location_name, location_type_id) values (1, 'UK', 1);
insert into tbl_locations (location_id, location_name, location_type_id) values (2, 'Eastern Mediterranean',2);

insert into tbl_relative_age_types (relative_age_type_id, age_type) values (1, 'Geological');
insert into tbl_relative_age_types (relative_age_type_id, age_type) values (2, 'Archaeological');
insert into tbl_relative_age_types (relative_age_type_id, age_type) values (3, 'Other');

insert into tbl_relative_ages (relative_age_id, relative_age_type_id, relative_age_name, abbreviation, description, c14_age_older, c14_age_younger, location_id)
    values (1, 1, 'Existing', 'EXIST', 'Previously inserted', 478000, 423000, 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values (1, 'TPeriods', '{}', 'EXIST', 'tbl_relative_ages', 1);

insert into tbl_relative_ages (relative_age_id, relative_age_type_id, relative_age_name, abbreviation, description, c14_age_older, c14_age_younger, location_id, date_updated)
    values (2, 1, 'Sead data updated', 'U_NEWER_SEAD', 'This data has changed since import', 0, 1222, null, '2016-01-01');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id, change_date)
    values (2, 'TPeriods', '{}', 'U_NEWER_SEAD', 'tbl_relative_ages', 2, '2015-01-01');

insert into tbl_relative_ages (relative_age_id, relative_age_type_id, relative_age_name, abbreviation, description, c14_age_older, c14_age_younger, location_id)
  values (3, 2, 'Updated', 'UPDATE', 'Update', 0, 1222, 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
  values (3, 'TPeriods', '{}', 'UPDATE', 'tbl_relative_ages', 3);

