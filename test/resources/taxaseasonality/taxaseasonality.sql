INSERT INTO tbl_location_types (location_type_id, date_updated, description, location_type) VALUES (1, '2012-09-21 18:51:47.967181+02', 'Country or other nation state. For practical purposes territories such as England and Wales are considered countries (due to common usage)', 'Country');
insert into tbl_locations (location_id, location_name, location_type_id) values (1, 'United Kingdom', 1);
insert into tbl_locations (location_id, location_name, location_type_id) values (2, 'Sweden', 1);
insert into tbl_locations (location_id, location_name, location_type_id) values (3, 'Germany', 1);
insert into tbl_locations (location_id, location_name, location_type_id) values (4, 'Egypt', 1);
insert into tbl_locations (location_id, location_name, location_type_id) values (5, 'International', 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
  values (1, 'Country', '', 'UK', 'tbl_locations', 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
  values (2, 'Country', '', 'Swe', 'tbl_locations', 2);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
  values (3, 'Country', '', 'Ger', 'tbl_locations', 3);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
  values (4, 'Country', '', 'Eg', 'tbl_locations', 4);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
  values (5, 'Country', '', 'Int', 'tbl_locations', 5);

insert into tbl_taxa_tree_orders (order_id, order_name) values (1, 'species order');
insert into tbl_taxa_tree_families (family_id, family_name, order_id) values (1, 'species family', 1);
insert into tbl_taxa_tree_genera (genus_id, genus_name, family_id) values (1, 'species genus', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (1, 'species 1', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (2, 'species 2', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (3, 'species 3', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (4, 'species 4', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (5, 'species 5', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (6, 'species 6', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (7, 'species 7', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (8, 'species 8', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (9, 'species 9', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (10, 'species 10', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (11, 'species 11', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (12, 'species 12', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (13, 'species 13', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (14, 'species 14', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (15, 'species 15', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (16, 'species 16', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (17, 'species 17', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (18, 'species 18', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (19, 'species 19', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (20, 'species 20', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (21, 'species 21', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (22, 'species 22', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (23, 'species 23', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (24, 'species 24', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (25, 'species 25', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (26, 'species 26', 1);

insert into tbl_taxonomic_order_systems (taxonomic_order_system_id, system_name) values (1, 'BugsCEP taxonomic order');
insert into tbl_taxonomic_order (taxonomic_order_id, taxonomic_order_system_id, taxon_id, taxonomic_code) values (1, 1, 1, 1.0000001);
insert into tbl_taxonomic_order (taxonomic_order_id, taxonomic_order_system_id, taxon_id, taxonomic_code) values (2, 1, 2, 1.0000002);
insert into tbl_taxonomic_order (taxonomic_order_id, taxonomic_order_system_id, taxon_id, taxonomic_code) values (3, 1, 3, 1.0000003);
insert into tbl_taxonomic_order (taxonomic_order_id, taxonomic_order_system_id, taxon_id, taxonomic_code) values (4, 1, 4, 1.0000004);
insert into tbl_taxonomic_order (taxonomic_order_id, taxonomic_order_system_id, taxon_id, taxonomic_code) values (5, 1, 5, 1.0000005);
insert into tbl_taxonomic_order (taxonomic_order_id, taxonomic_order_system_id, taxon_id, taxonomic_code) values (6, 1, 6, 1.0000006);
insert into tbl_taxonomic_order (taxonomic_order_id, taxonomic_order_system_id, taxon_id, taxonomic_code) values (7, 1, 7, 1.0000007);
insert into tbl_taxonomic_order (taxonomic_order_id, taxonomic_order_system_id, taxon_id, taxonomic_code) values (8, 1, 8, 1.0000008);
insert into tbl_taxonomic_order (taxonomic_order_id, taxonomic_order_system_id, taxon_id, taxonomic_code) values (9, 1, 9, 1.0000009);
insert into tbl_taxonomic_order (taxonomic_order_id, taxonomic_order_system_id, taxon_id, taxonomic_code) values (10, 1, 10, 1.0000010);
insert into tbl_taxonomic_order (taxonomic_order_id, taxonomic_order_system_id, taxon_id, taxonomic_code) values (11, 1, 11, 1.0000011);
insert into tbl_taxonomic_order (taxonomic_order_id, taxonomic_order_system_id, taxon_id, taxonomic_code) values (12, 1, 12, 1.0000012);
insert into tbl_taxonomic_order (taxonomic_order_id, taxonomic_order_system_id, taxon_id, taxonomic_code) values (13, 1, 13, 1.0000013);
insert into tbl_taxonomic_order (taxonomic_order_id, taxonomic_order_system_id, taxon_id, taxonomic_code) values (14, 1, 14, 1.0000014);
insert into tbl_taxonomic_order (taxonomic_order_id, taxonomic_order_system_id, taxon_id, taxonomic_code) values (15, 1, 15, 1.0000015);
insert into tbl_taxonomic_order (taxonomic_order_id, taxonomic_order_system_id, taxon_id, taxonomic_code) values (16, 1, 16, 1.0000016);
insert into tbl_taxonomic_order (taxonomic_order_id, taxonomic_order_system_id, taxon_id, taxonomic_code) values (17, 1, 17, 1.0000017);
insert into tbl_taxonomic_order (taxonomic_order_id, taxonomic_order_system_id, taxon_id, taxonomic_code) values (18, 1, 18, 1.0000018);
insert into tbl_taxonomic_order (taxonomic_order_id, taxonomic_order_system_id, taxon_id, taxonomic_code) values (19, 1, 19, 1.0000019);
insert into tbl_taxonomic_order (taxonomic_order_id, taxonomic_order_system_id, taxon_id, taxonomic_code) values (20, 1, 20, 1.0000020);
insert into tbl_taxonomic_order (taxonomic_order_id, taxonomic_order_system_id, taxon_id, taxonomic_code) values (21, 1, 21, 1.0000021);
insert into tbl_taxonomic_order (taxonomic_order_id, taxonomic_order_system_id, taxon_id, taxonomic_code) values (22, 1, 22, 1.0000022);
insert into tbl_taxonomic_order (taxonomic_order_id, taxonomic_order_system_id, taxon_id, taxonomic_code) values (23, 1, 23, 1.0000023);
insert into tbl_taxonomic_order (taxonomic_order_id, taxonomic_order_system_id, taxon_id, taxonomic_code) values (24, 1, 24, 1.0000024);
insert into tbl_taxonomic_order (taxonomic_order_id, taxonomic_order_system_id, taxon_id, taxonomic_code) values (25, 1, 25, 1.0000025);
insert into tbl_taxonomic_order (taxonomic_order_id, taxonomic_order_system_id, taxon_id, taxonomic_code) values (26, 1, 26, 1.0000026);

insert into tbl_activity_types (activity_type_id, activity_type) values (1, 'Adult active');

insert into tbl_season_types (season_type_id, season_type) values (1, 'Month');
insert into tbl_seasons (season_id, season_name, season_type_id) values (1, 'January', 1);
insert into tbl_seasons (season_id, season_name, season_type_id) values (2, 'February', 1);
insert into tbl_seasons (season_id, season_name, season_type_id) values (3, 'March', 1);
insert into tbl_seasons (season_id, season_name, season_type_id) values (4, 'April', 1);
insert into tbl_seasons (season_id, season_name, season_type_id) values (5, 'May', 1);
insert into tbl_seasons (season_id, season_name, season_type_id) values (6, 'June', 1);
insert into tbl_seasons (season_id, season_name, season_type_id) values (7, 'July', 1);
insert into tbl_seasons (season_id, season_name, season_type_id) values (8, 'August', 1);
insert into tbl_seasons (season_id, season_name, season_type_id) values (9, 'September', 1);
insert into tbl_seasons (season_id, season_name, season_type_id) values (10, 'October', 1);
insert into tbl_seasons (season_id, season_name, season_type_id) values (11, 'November', 1);
insert into tbl_seasons (season_id, season_name, season_type_id) values (12, 'December', 1);

insert into tbl_taxa_seasonality (seasonality_id, activity_type_id, season_id, taxon_id, location_id)
    values (1, 1, 1, 23, 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values (6, 'TSeasonActiveAdult', '{1.0000023JaUK}', '1.0000023', 'tbl_taxa_seasonality', 1);

insert into tbl_taxa_seasonality (seasonality_id, activity_type_id, season_id, taxon_id, location_id, date_updated)
values (2, 1, 1, 24, 1, '2016-01-01');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id, change_date)
values (7, 'TSeasonActiveAdult', '{1.0000024JaUK}', '1.0000024', 'tbl_taxa_seasonality', 2, '2015-01-01');

insert into tbl_taxa_seasonality (seasonality_id, activity_type_id, season_id, taxon_id, location_id)
values (3, 1, 1, 25, 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (8, 'TSeasonActiveAdult', '{1.0000025JaUK}', '1.0000025', 'tbl_taxa_seasonality', 3);

insert into tbl_taxa_seasonality (seasonality_id, activity_type_id, season_id, taxon_id, location_id)
values (4, 1, 2, 26, 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (9, 'TSeasonActiveAdult', '{1.0000026FeUK}', '1.0000026', 'tbl_taxa_seasonality', 4);
insert into tbl_taxa_seasonality (seasonality_id, activity_type_id, season_id, taxon_id, location_id)
values (5, 1, 3, 26, 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (10, 'TSeasonActiveAdult', '{1.0000026MrUK}', '1.0000026', 'tbl_taxa_seasonality', 5);
insert into tbl_taxa_seasonality (seasonality_id, activity_type_id, season_id, taxon_id, location_id)
values (6, 1, 4, 26, 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (11, 'TSeasonActiveAdult', '{1.0000026ApUK}', '1.0000026', 'tbl_taxa_seasonality', 6);