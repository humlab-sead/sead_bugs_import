insert into tbl_taxa_tree_authors (author_id, author_name) values (1, 'L.');
insert into tbl_taxa_tree_authors (author_id, author_name) values (2, 'Dej.');
insert into tbl_taxa_tree_orders (order_id, order_name) values(1, 'ORDER PENDING CLASSIFICATION');
insert into tbl_taxa_tree_families (family_id, family_name, order_id) values (1, 'CARABIDAE', 1);
insert into tbl_taxa_tree_genera (genus_id, genus_name, family_id) values (2, 'Cicindela', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id, author_id) values (3, 'hybrida', 2, 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id, author_id) values (4, 'maritima', 2, 2);
insert into tbl_taxonomic_order_systems (taxonomic_order_system_id, system_name) values (1, 'BugsCEP taxonomic order');
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (3, 3, 1.0010050, 1);
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (4, 4, 1.0010060, 1);

insert into tbl_biblio (biblio_id, bugs_author, bugs_reference, bugs_title) values (2977,'Hodge, P.J. & Jones, R.A. (1995)','Hodge & Jones 1995','New British Beetles. Species not in Joy''s practical handbook. British Entomological and Natural History Society, Reading.');
insert into tbl_biblio (biblio_id, bugs_author, bugs_reference, bugs_title) values (3125,'Hurka, K. (1996)','Hurka 1996','Carabidae of the Czech and Slovak Republics. Kabourek, Zlin. (565pp.)');

insert into tbl_taxonomy_notes (taxonomy_notes_id, biblio_id, taxon_id, taxonomy_notes) values (1, 2977, 3, 'See Cicindela maritima also.');
