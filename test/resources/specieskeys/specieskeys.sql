insert into tbl_biblio (biblio_id, bugs_author, bugs_reference, bugs_title) values (1,'Skidmore (unpubl.)','Skidmore unpubl.','Keys to the British Beetle (Coleoptera) Fauna.');
insert into tbl_taxa_tree_orders (order_id, order_name) values(1, 'ORDER PENDING CLASSIFICATION');
insert into tbl_taxa_tree_families (family_id, family_name, order_id) values (1, 'CARABIDAE', 1);
insert into tbl_taxa_tree_genera (genus_id, genus_name, family_id) values (1, 'Carabidae', 1);
insert into tbl_taxa_tree_genera (genus_id, genus_name, family_id) values (2, 'Cicindela', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (1, 'indet.', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (2, 'sp.', 2);
insert into tbl_taxonomic_order_systems (taxonomic_order_system_id, system_name) values (1, 'BugsCEP taxonomic order');
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (1, 1, 1.0010001, 1);
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (2, 2, 1.0010122, 1);

insert into tbl_text_identification_keys (key_id, biblio_id, key_text, taxon_id) values (1, 1, 'Key to Subfamilies of Carabidae', 1);
