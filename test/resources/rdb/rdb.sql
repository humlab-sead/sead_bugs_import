insert into tbl_location_types (location_type_id, location_type) values (1, 'Country');
insert into tbl_locations (location_id, location_name, location_type_id) values (1, 'United kingdom', 1);
insert into tbl_locations (location_id, location_name, location_type_id) values (2, 'Sweden', 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (1, 'Country', '', 'UK', 'tbl_locations', 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (2, 'Country', '', 'Swe', 'tbl_locations', 2);
insert into tbl_biblio (biblio_id, bugs_author, bugs_reference, bugs_title) values (1, 'Hyman, P.S. (1992)', 'Hyman 1992', 'A review of the scarce and threatened Coleoptera of Great Britain, Part 1 (Revised & updated by M.S.Parsons). UK Joint Nature Conservation Committee, Peterborough.');

INSERT INTO tbl_rdb_systems (rdb_system_id, biblio_id, location_id, rdb_system) VALUES (1, 1, 1, 'Test 1');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (3, 'TRDBSystems', '{1,Test 1,null,null,null,Hyman 1992,UK}', '1', 'tbl_rdb_systems', 1);

insert into tbl_rdb_codes (rdb_code_id, rdb_category, rdb_definition, rdb_system_id)
values (1, 'A', 'normal insert', 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (4, 'TRDBCodes', '{1,A,normal insert,1}', '1', 'tbl_rdb_codes', 1);

insert into tbl_rdb_codes (rdb_code_id, rdb_category, rdb_definition, rdb_system_id)
values (2, 'AB', 'already stored w. trace', 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (5, 'TRDBCodes', '{2,AB,already stored w. trace,1}', '2', 'tbl_rdb_codes', 2);

insert into tbl_taxa_tree_orders (order_id, order_name) values(1, 'ORDER PENDING CLASSIFICATION');
insert into tbl_taxa_tree_families (family_id, family_name, order_id) values (1, 'CARABIDAE', 1);
insert into tbl_taxa_tree_genera (genus_id, genus_name, family_id) values (1, 'Carabidae', 1);
insert into tbl_taxa_tree_genera (genus_id, genus_name, family_id) values (2, 'Cicindela', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (1, 'indet.', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (2, 'sp.', 2);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (3, 'spp.', 2);
insert into tbl_taxonomic_order_systems (taxonomic_order_system_id, system_name) values (1, 'BugsCEP taxonomic order');
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (1, 1, 1.0010001, 1);
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (2, 2, 1.0010122, 1);
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (3, 3, 1.0010123, 1);

insert into tbl_rdb (rdb_id, location_id, rdb_code_id, taxon_id) values (1, 1, 1, 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values (6, 'TRDB', '{1.0010001,UK,1}', '1.0010001', 'tbl_rdb', 1);
--insert into tbl_rdb (rdb_id, location_id, rdb_code_id, taxon_id) values (2, 2, 1, 1);
--insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
--values (7, 'TRDB', '{1.0010001,Swe,1}', '1.0010001', 'tbl_rdb', 2);

insert into tbl_rdb (rdb_id, location_id, rdb_code_id, taxon_id) values (4, 2, 2, 2);

insert into tbl_rdb (rdb_id, location_id, rdb_code_id, taxon_id, date_updated) values (3, 1, 1, 3, '2016-01-01');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id, change_date)
     values (8, 'TRDB', '{1.0010123,UK,1}', '1.0010123', 'tbl_rdb', 3, '2015-01-01');

