insert into tbl_taxa_tree_orders (order_id, order_name) values(1, 'Order');
insert into tbl_taxa_tree_families (family_id, family_name, order_id) values (1, 'Family', 1);
insert into tbl_taxa_tree_genera (genus_id, genus_name, family_id) values (1, 'Genus', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (1, 'species 1', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (2, 'species 2', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (3, 'species 3', 1);
insert into tbl_taxonomic_order_systems (taxonomic_order_system_id, system_name) values (1, 'BugsCEP taxonomic order');
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (1, 1, 1.0000000, 1);
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (2, 2, 2.0000000, 1);
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (3, 3, 3.0000000, 1);

insert into tbl_ecocode_systems (ecocode_system_id, name) values (1, 'Bugs System');
insert into tbl_ecocode_groups (ecocode_group_id, abbreviation, ecocode_system_id) values (1, 'Bugs group', 1);

insert into tbl_ecocode_definitions (ecocode_definition_id, ecocode_group_id, abbreviation, name, definition) values
  (1, 1, 'Def', 'Definition', 'A standard definition');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (1, 'TEcoDefBugs', '', 'Def', 'tbl_ecocode_definitions', 1);

insert into tbl_ecocode_definitions (ecocode_definition_id, ecocode_group_id, abbreviation, name, definition) values
  (2, 1, 'Def 2', 'Definition 2', 'Another definition');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (2, 'TEcoDefBugs', '', 'Def 2', 'tbl_ecocode_definitions', 2);

insert into tbl_ecocode_definitions (ecocode_definition_id, ecocode_group_id, abbreviation, name, definition) values
  (3, 1, 'Def 3', 'Definition 3', 'Lower case definition testing');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (3, 'TEcoDefBugs', '', 'Def 3', 'tbl_ecocode_definitions', 3);

insert into tbl_ecocodes (ecocode_id, taxon_id, ecocode_definition_id) values (1, 1, 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values (4, 'TEcoBugs', '{1.0,Def}', null, 'tbl_ecocodes', 1);

insert into tbl_ecocodes (ecocode_id, taxon_id, ecocode_definition_id) values (2, 3, 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values (5, 'TEcoBugs', '{3.0,Def}', null, 'tbl_ecocodes', 2);

