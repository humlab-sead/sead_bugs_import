insert into tbl_taxonomic_order_systems (taxonomic_order_system_id, system_name) values (1, 'BugsCEP taxonomic order');
insert into tbl_taxa_tree_orders (order_id, order_name) values(1, 'ORDER PENDING CLASSIFICATION');
insert into tbl_taxa_tree_authors (author_id, author_name) values (1, 'Author');

insert into tbl_taxa_tree_families (family_id, family_name, order_id) values (1, 'Family', 1);
insert into tbl_taxa_tree_genera (genus_id, genus_name, family_id) values (1, 'Genus', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (1, 'indet.', 1);
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (1, 1, 1.0, 1);

insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (2, 'createTaxonomicOrder', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id, author_id) values (3, 'createTaxonomicOrder', 1, 1);

insert into tbl_taxa_tree_families (family_id, family_name, order_id) values (2, 'No data', 1);
insert into tbl_taxa_tree_genera (genus_id, genus_name, family_id) values (2, 'No data', 2);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (4, 'No data', 2);
