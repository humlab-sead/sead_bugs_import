insert into tbl_ecocode_systems (ecocode_system_id, name) values (1, 'Bugs System');
insert into tbl_ecocode_groups (ecocode_group_id, abbreviation, ecocode_system_id) values (1, 'Bugs group', 1);

insert into tbl_ecocode_definitions (ecocode_definition_id, ecocode_group_id, abbreviation, name, definition, notes) values
  (1, 1, 'Exists', 'Exists', 'Item exists', 'with notes');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
  values (1, 'TEcoDefBugs', '', 'Exists', 'tbl_ecocode_definitions', 1);

insert into tbl_ecocode_definitions (ecocode_definition_id, ecocode_group_id, abbreviation, name, definition, notes) values
  (2, 1, 'Upd', 'Updated', 'Item to be updated', 'with notes');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (2, 'TEcoDefBugs', '', 'Upd', 'tbl_ecocode_definitions', 2);

insert into tbl_ecocode_definitions (ecocode_definition_id, ecocode_group_id, abbreviation, name, definition, notes, date_updated) values
  (3, 1, 'UpdE', 'Fail update', 'This value should not be updated', 'with notes', '2016-01-01');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id, change_date)
   values (3, 'TEcoDefBugs', '', 'UpdE', 'tbl_ecocode_definitions', 3, '2015-01-01');

