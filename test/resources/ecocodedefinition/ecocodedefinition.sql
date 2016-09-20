insert into tbl_ecocode_systems (ecocode_system_id, name) values (1, 'Koch System');
insert into tbl_ecocode_groups (ecocode_group_id, label, ecocode_system_id) values (1, 'Koch group', 1);

insert into tbl_ecocode_definitions (ecocode_definition_id, ecocode_group_id, abbreviation, label, definition, notes) values
  (1, 1, 'Ecoab', 'arboricolous', 'in trees.', 'imported previously');
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
  values ('TEcoDefKoch', '{Ecoab,ab,arboricolous,Eco,in trees.,}', 'Ecoab', 'tbl_ecocode_definitions', 1);

insert into tbl_ecocode_definitions (ecocode_definition_id, ecocode_group_id, abbreviation, label, definition, notes) values
  (2, 1, 'Ecoag', 'agaricolous', 'agarics', 'added without import');

insert into tbl_ecocode_definitions (ecocode_definition_id, ecocode_group_id, abbreviation, label, definition, notes, date_updated) values
  (3, 1, 'Ecoak', 'akrodendric', 'in tree tops', 'to be updated', '2015-01-01');
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id, change_date)
   values ('TEcoDefKoch', '{Ecoak,ak,akrodendric,Eco,in tree tops,}', 'Ecoak', 'tbl_ecocode_definitions', 3, '2015-01-01');


insert into tbl_ecocode_definitions (ecocode_definition_id, ecocode_group_id, abbreviation, label, definition, date_updated, notes) values
  (4, 1, 'Ecoap', 'arundicolous', 'in reeds.', '2016-01-01', 'sead data updated after initial import');
insert into bugs_import.bugs_trace (bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id, change_date)
  values ('TEcoDefKoch', '{Ecoap,ap,arundicolous,Eco,in reeds.,}', 'Ecoap', 'tbl_ecocode_definitions', 4, '2015-01-01');


