insert into tbl_taxa_tree_authors (author_id, author_name) values (1, 'L.');
insert into tbl_taxa_tree_orders (order_id, order_name) values(1, 'ORDER PENDING CLASSIFICATION');
insert into tbl_taxa_tree_families (family_id, family_name, order_id) values (1, 'CARABIDAE', 1);
insert into tbl_taxa_tree_genera (genus_id, genus_name, family_id) values (1, 'Cicindela', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id, author_id) values (1, 'sylvatica', 1, 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id, author_id) values (2, 'clathratus', 1, 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id, author_id) values (3, 'species 3', 1, 1);
insert into tbl_taxonomic_order_systems (taxonomic_order_system_id, system_name) values (1, 'BugsCEP taxonomic order');
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (1, 1, 1.0000000, 1);
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (2, 2, 2.0000000, 1);
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (3, 3, 3.0000000, 1);

insert into tbl_biblio (biblio_id, authors, bugs_reference, title) values (1, 'Ref 1', 'Ref 1', 'Ref 1');
insert into tbl_biblio (biblio_id, authors, bugs_reference, title) values (2, 'Ref 3', 'Ref 3', 'Ref 3');

insert into tbl_species_association_types (association_type_id, association_type_name) values (1, 'parasitizes');
insert into tbl_species_association_types (association_type_id, association_type_name) values (2, 'is associated with');

insert into tbl_species_associations (species_association_id, taxon_id, associated_taxon_id, association_type_id)
    values (1, 1, 2, 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
  values (1, 'TSpeciesAssociations', null, '1', 'tbl_species_associations', 1);

insert into tbl_species_associations (species_association_id, taxon_id, associated_taxon_id, association_type_id, biblio_id)
  values (2, 1, 2, 2, 2);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
  values (2, 'TSpeciesAssociations', null, '11', 'tbl_species_associations', 2);

insert into tbl_species_associations (species_association_id, taxon_id, associated_taxon_id, association_type_id, biblio_id, date_updated)
  values (3, 1, 2, 1, 2, '2016-01-01');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id, change_date)
  values (3, 'TSpeciesAssociations', null, '12', 'tbl_species_associations', 3, '2015-01-01');
