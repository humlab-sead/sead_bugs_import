insert into tbl_taxa_tree_authors (author_id, author_name) values (1, 'L.');
insert into tbl_taxa_tree_authors (author_id, author_name) values (2, 'A stored authority');

insert into tbl_taxa_tree_orders (order_id, order_name) values(1, 'ORDER PENDING CLASSIFICATION');
insert into tbl_taxa_tree_families (family_id, family_name, order_id) values (1, 'CARABIDAE', 1);
insert into tbl_taxa_tree_genera (genus_id, genus_name, family_id) values (1, 'Off genus', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id, author_id) values (1, 'species 1', 1, 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id, author_id) values (2, 'species 2', 1, 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id, author_id) values (3, 'species 3', 1, 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id, author_id) values (4, 'syn species', 1, 2);

insert into tbl_taxonomic_order_systems (taxonomic_order_system_id, system_name) values (1, 'BugsCEP taxonomic order');
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (1, 1, 1.0000000, 1);
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (2, 2, 2.0000000, 1);
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (3, 3, 3.0000000, 1);

insert into tbl_biblio (biblio_id, bugs_author, bugs_reference, bugs_title) values (1, 'Ref 1', 'Ref 1', 'Ref 1');
--insert into tbl_biblio (biblio_id, bugs_author, bugs_reference, bugs_title) values (2, 'Ref 3', 'Ref 3', 'Ref 3');

insert into tbl_species_association_types (association_type_id, association_type_name) values (1, 'synonym of');

insert into tbl_species_associations (species_association_id, taxon_id, associated_taxon_id, association_type_id, biblio_id)
    values (1, 4, 1, 1, 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
  values (1, 'TSynonym', '{1.0,Off genus,syn species,A stored authority,Ref 1,exists}', null, 'tbl_species_associations', 1);
