insert into tbl_taxa_tree_orders (order_id, order_name) values(1, 'ORDER PENDING CLASSIFICATION');
insert into tbl_taxa_tree_families (family_id, family_name, order_id) values (1, 'Family', 1);
insert into tbl_taxa_tree_genera (genus_id, genus_name, family_id) values (1, 'Genus', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (1, 'sp 1', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (2, 'sp 2', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (3, 'sp 3', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (5, 'sp 5', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (6, 'sp 6', 1);
insert into tbl_taxonomic_order_systems (taxonomic_order_system_id, system_name) values (1, 'BugsCEP taxonomic order');
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (1, 1, 1.0, 1);
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (2, 2, 2.0, 1);
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (3, 3, 3.0, 1);
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (5, 5, 5.0, 1);
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (6, 6, 6.0, 1);

insert into tbl_biblio (biblio_id, bugs_reference) values (1, 'Ref 1');
insert into tbl_biblio (biblio_id, bugs_reference) values (2, 'Ref 2');

insert into tbl_text_biology(biology_id,biblio_id,biology_text,taxon_id) values (1,1,'Note exists',1);
insert into bugs_import.bugs_trace(bugs_trace_id, bugs_table, bugs_data, sead_table, sead_reference_id)
    values (1, 'TBiology', '{1.0,Ref 1,Note exists}', 'tbl_taxonomic_biology', 1);
