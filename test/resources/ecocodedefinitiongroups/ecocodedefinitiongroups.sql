insert into tbl_ecocode_systems (ecocode_system_id, name) values (1, 'Koch System');

insert into tbl_ecocode_groups (ecocode_group_id, ecocode_system_id, abbreviation, name) values (1, 1, 'Ext', 'Existing group');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values (1, 'TEcoDefGroups', '{Ext,Existing group}', 'Ext', 'tbl_ecocode_groups', 1);

insert into tbl_ecocode_groups (ecocode_group_id, ecocode_system_id, abbreviation, name) values (2, 1, 'Upd', 'Group to be updated');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values (2, 'TEcoDefGroups', '{Upd,Group to be updated}', 'Upd', 'tbl_ecocode_groups', 2);




