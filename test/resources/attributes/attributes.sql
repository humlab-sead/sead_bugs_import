insert into tbl_taxa_tree_orders (order_id, order_name) values(1, 'ORDER PENDING CLASSIFICATION');
insert into tbl_taxa_tree_families (family_id, family_name, order_id) values (1, 'Test', 1);
insert into tbl_taxa_tree_genera (genus_id, genus_name, family_id) values (1, 'Test', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (1, 'test', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (2, 'test 2', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (3, 'test 3', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (4, 'test 4', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (5, 'test 5', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (6, 'test 6', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (7, 'test 7', 1);
insert into tbl_taxonomic_order_systems (taxonomic_order_system_id, system_name) values (1, 'BugsCEP taxonomic order');
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (1, 1, 1.0000000, 1);
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (2, 2, 2.0000000, 1);
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (3, 3, 3.0000000, 1);
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (4, 4, 4.0000000, 1);
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (5, 5, 5.0000000, 1);
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (6, 6, 6.0000000, 1);
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (7, 7, 7.0000000, 1);

insert into tbl_taxa_measured_attributes (measured_attribute_id, taxon_id, attribute_type, attribute_measure, data, attribute_units)
    values (1, 1, 'Stored', 'Max', 1, 'mm');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
 values (1, 'TAttributes', '{1.0,Stored,Max,1,mmm}', '1.0', 'tbl_taxa_measured_attributes', 1);
insert into tbl_taxa_measured_attributes (measured_attribute_id, taxon_id, attribute_type, attribute_measure, data, attribute_units)
 values (2, 1, 'Stored', 'Min', 1, 'mm');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
 values (2, 'TAttributes', '{1.0,Stored,Min,1,mmm}', '1.0', 'tbl_taxa_measured_attributes', 2);

insert into tbl_taxa_measured_attributes (measured_attribute_id, taxon_id, attribute_type, attribute_measure, data, attribute_units)
values (3, 2, 'Stored', 'Max', 1, 'mm');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (3, 'TAttributes', '{2.0,Stored,Max,1,mmm}', '2.0', 'tbl_taxa_measured_attributes', 3);

insert into tbl_taxa_measured_attributes (measured_attribute_id, taxon_id, attribute_type, attribute_measure, data, attribute_units)
values (4, 4, 'Stored w/o trace', 'Max', 1, 'mm');
insert into tbl_taxa_measured_attributes (measured_attribute_id, taxon_id, attribute_type, attribute_measure, data, attribute_units)
values (5, 4, 'Stored w/o trace', 'Min', 1, 'mm');

insert into tbl_taxa_measured_attributes (measured_attribute_id, taxon_id, attribute_type, attribute_measure, data, attribute_units)
values (6, 5, 'Changed', 'Max', 1, 'mm');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (4, 'TAttributes', '{5.0,Changed,Max,1,mmm}', '5.0', 'tbl_taxa_measured_attributes', 6);
insert into tbl_taxa_measured_attributes (measured_attribute_id, taxon_id, attribute_type, attribute_measure, data, attribute_units)
values (7, 5, 'Changed', 'Min', 1, 'mm');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (5, 'TAttributes', '{5.0,Changed,Min,1,mmm}', '5.0', 'tbl_taxa_measured_attributes', 7);

insert into tbl_taxa_measured_attributes (measured_attribute_id, taxon_id, attribute_type, attribute_measure, data, attribute_units)
values (8, 6, 'Stored', 'Max', 1, 'mm');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (6, 'TAttributes', '{6.0,Changed,Max,1,mmm}', '6.0', 'tbl_taxa_measured_attributes', 8);
insert into tbl_taxa_measured_attributes (measured_attribute_id, taxon_id, attribute_type, attribute_measure, data, attribute_units)
values (9, 6, 'Changed', 'Min', 1, 'mm');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (7, 'TAttributes', '{6.0,Changed,Min,1,mmm}', '6.0', 'tbl_taxa_measured_attributes', 9);

insert into tbl_taxa_measured_attributes (measured_attribute_id, taxon_id, attribute_type, attribute_measure, data, attribute_units)
values (10, 7, 'Changed after import', 'Max', 1, 'mm');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (8, 'TAttributes', '{7.0,Changed after import,Max,1,mmm}', '7.0', 'tbl_taxa_measured_attributes', 10);
insert into tbl_taxa_measured_attributes (measured_attribute_id, taxon_id, attribute_type, attribute_measure, data, attribute_units, date_updated)
values (11, 7, 'Stored', 'Min', 1, 'mm', '2016-01-01');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id, change_date)
values (9, 'TAttributes', '{7.0,Stored,Min,1,mmm}', '7.0', 'tbl_taxa_measured_attributes', 11, '2015-01-01');