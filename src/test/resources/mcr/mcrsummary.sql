insert into tbl_taxa_tree_authors (author_id, author_name) values (1, 'L.');
insert into tbl_taxa_tree_orders (order_id, order_name) values(1, 'ORDER PENDING CLASSIFICATION');
insert into tbl_taxa_tree_families (family_id, family_name, order_id) values (1, 'CARABIDAE', 1);
insert into tbl_taxa_tree_genera (genus_id, genus_name, family_id) values (2, 'Cicindela', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id, author_id) values (2, 'sylvatica', 2, 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id, author_id) values (3, 'campestris', 2, 1);
insert into tbl_taxonomic_order_systems (taxonomic_order_system_id, system_name) values (1, 'BugsCEP taxonomic order');
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (2, 2, 1.0010020, 1);
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (3, 3, 1.0010070, 1);

insert into tbl_mcr_summary_data (mcr_summary_data_id, taxon_id, tmax_lo, tmax_hi, tmin_lo, tmin_hi, trange_lo, trange_hi, cog_mid_tmax, cog_mid_trange) 
	values (1, 2, 14, 29, -29, 7, 12, 49, 22,30);