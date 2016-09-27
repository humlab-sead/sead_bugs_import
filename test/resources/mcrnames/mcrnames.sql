insert into tbl_taxa_tree_authors (author_id, author_name) values (1, 'L.');
insert into tbl_taxa_tree_orders (order_id, order_name) values(1, 'ORDER PENDING CLASSIFICATION');
insert into tbl_taxa_tree_families (family_id, family_name, order_id) values (1, 'CARABIDAE', 1);
insert into tbl_taxa_tree_genera (genus_id, genus_name, family_id) values (1, 'Cicindela', 1);
insert into tbl_taxa_tree_genera (genus_id, genus_name, family_id) values (2, 'Carabus', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id, author_id) values (1, 'sylvatica', 1, 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id, author_id) values (2, 'clathratus', 2, 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id, author_id) values (3, 'test', 1, 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id, author_id) values (4, 'test 2', 1, 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id, author_id) values (5, 'test 3', 1, 1);
insert into tbl_taxonomic_order_systems (taxonomic_order_system_id, system_name) values (1, 'BugsCEP taxonomic order');
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (1, 1, 1.001002, 1);
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (2, 2, 1.0040140, 1);
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (3, 3, 1.0000000, 1);
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (4, 4, 2.0000000, 1);
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (5, 5, 3.0000000, 1);

insert into tbl_mcr_names (taxon_id, comparison_notes, mcr_name_trim, mcr_number, mcr_species_name)
 values (1, 'Found similarity', 'CicindelasylvaticaL.', 273, 'Cicindela sylvatica L.');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
 values (1, 'TMCRNames', '{1.001002,273,Cicindela sylvatica L.,Found similarity}', '1.001002', 'tbl_mcr_names', 1);

insert into tbl_mcr_names (taxon_id, comparison_notes, mcr_name_trim, mcr_number, mcr_species_name)
 values (3, 'do not import', 'Matchwithouttraces', 1, 'Match without traces');

insert into tbl_mcr_names (taxon_id, comparison_notes, mcr_name_trim, mcr_number, mcr_species_name)
 values (4, 'import change', 'Updatethis', 2, 'Update this');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (2, 'TMCRNames', '{2.0,2,Update this,import change}', '2.0', 'tbl_mcr_names', 4);


insert into tbl_mcr_names (taxon_id, comparison_notes, mcr_name_trim, mcr_number, mcr_species_name, date_updated)
 values (5, 'import?', 'Seaddatachanged', 3, 'Sead data changed', '2016-01-01');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id, change_date)
 values (3, 'TMCRNames', '{3.0,3,Sead data changed,import?}', '3.0', 'tbl_mcr_names', 5, '2015-01-01');
