/*
"{" +
                code + ',' +
                name + ',' +
                region + ',' +
                country + ',' +
                ngr + ',' +
                latDD + ',' +
                longDD + ',' +
                alt + ',' +
                iDBy + ',' +
                interp + ',' +
                specimens + '}'
 */

insert into tbl_sample_group_sampling_contexts (sampling_context_id, sampling_context)
values (1, 'Archaeological site');
insert into tbl_method_groups (method_group_id , group_name, description)
values (1, 'Sampling', 'Sampling');
insert into tbl_methods (method_id, method_group_id, method_name, description)
values (1, 1, 'Temporary record', 'Temporary record');
insert into tbl_methods (method_id, method_group_id, method_name, description)
values (2, 1, 'Presence/Absence', 'Presence/Absence');
insert into tbl_method_groups (method_group_id , group_name, description)
values (2, 'Palaeobiological', 'Palaeobiological');
insert into tbl_methods (method_id, method_group_id, method_name, method_abbrev_or_alt_name)
values (3, 2, 'Palaeoentomology', 'Fossil insect');
insert into tbl_alt_ref_types (alt_ref_type_id, alt_ref_type) values (1, 'Other alternative sample name');
insert into tbl_sample_types (sample_type_id, type_name) values (1, 'Unspecified');
insert into tbl_data_type_groups (data_type_group_id, data_type_group_name) values (1, 'Presence only');
insert into tbl_data_types (data_type_id, data_type_group_id, data_type_name) values (1, 1, 'Presence/Absence');
insert into tbl_dataset_masters (master_set_id, master_name) values (1, 'Bugs database');
insert into tbl_contact_types (contact_type_id, contact_type_name) values (1, 'Identifications by');
insert into tbl_contact_types (contact_type_id, contact_type_name) values (2, 'Specimen repository');

insert into tbl_taxa_tree_orders (order_id, order_name) values(1, 'ORDER PENDING CLASSIFICATION');
insert into tbl_taxa_tree_families (family_id, family_name, order_id) values (1, 'Family', 1);
insert into tbl_taxa_tree_genera (genus_id, genus_name, family_id) values (1, 'Genus', 1);
insert into tbl_taxa_tree_master (taxon_id, species, genus_id) values (1, 'species', 1);
insert into tbl_taxonomic_order_systems (taxonomic_order_system_id, system_name) values (1, 'BugsCEP taxonomic order');
insert into tbl_taxonomic_order (taxonomic_order_id, taxon_id, taxonomic_code, taxonomic_order_system_id) values (1, 1, 1.0000000, 1);

insert into tbl_sites (site_id, site_name) values (1, 'No contacts');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (1, 'TSite', '{SITE000001,No contacts,,,,,,,,interp,}', 'SITE000001', 'tbl_sites', 1);
insert into tbl_sites (site_id, site_name) values (2, 'Two contacts');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (2, 'TSite', '{SITE000002,Two contacts,,,,,,,Archer,interp,University of Carpatia}', 'SITE000002', 'tbl_sites', 2);
insert into tbl_sites (site_id, site_name) values (3, 'One contact exists');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (3, 'TSite', '{SITE000003,One contact exists,,,,,,,A new person,interp,University of Ottawa}', 'SITE000003', 'tbl_sites', 3);
insert into tbl_sites (site_id, site_name) values (4, 'Two contacts exist');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (4, 'TSite', '{SITE000004,Two contacts exist,,,,,,,First;Second,interp,University of Bolivia}', 'SITE000004', 'tbl_sites', 4);
insert into tbl_sites (site_id, site_name) values (5, 'Contact should only be created once');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (5, 'TSite', '{SITE000005,Contact should only be created once,,,,,,,,interp,University of Carpatia}', 'SITE000005', 'tbl_sites', 5);

insert into tbl_sample_groups (sample_group_id, site_id, sampling_context_id, method_id, sample_group_name)
values (1, 1, 1, 1, 'Sheet for site 1');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (6, 'TCountsheet', '{COUN000001,Sheet for site 1,SITE000001,Archaeological contexts,Presence/Absence}', 'COUN000001', 'tbl_sample_groups', 1);
insert into tbl_sample_groups (sample_group_id, site_id, sampling_context_id, method_id, sample_group_name)
values (2, 2, 1, 1, 'Sheet for site 2');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (7, 'TCountsheet', '{COUN000002,Sheet for site 2,SITE000002,Archaeological contexts,Presence/Absence}', 'COUN000002', 'tbl_sample_groups', 2);
insert into tbl_sample_groups (sample_group_id, site_id, sampling_context_id, method_id, sample_group_name)
values (3, 3, 1, 1, 'Sheet for site 3');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (8, 'TCountsheet', '{COUN000003,Sheet for site 3,SITE000003,Archaeological contexts,Presence/Absence}', 'COUN000003', 'tbl_sample_groups', 3);
insert into tbl_sample_groups (sample_group_id, site_id, sampling_context_id, method_id, sample_group_name)
values (4, 4, 1, 1, 'Sheet for site 4');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (9, 'TCountsheet', '{COUN000004,Sheet for site 4,SITE000004,Archaeological contexts,Presence/Absence}', 'COUN000004', 'tbl_sample_groups', 4);
insert into tbl_sample_groups (sample_group_id, site_id, sampling_context_id, method_id, sample_group_name)
values (5, 5, 1, 1, 'Sheet for site 5');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (10, 'TCountsheet', '{COUN000005,Sheet for site 5,SITE000005,Archaeological contexts,Presence/Absence}', 'COUN000005', 'tbl_sample_groups', 5);

insert into tbl_physical_samples (physical_sample_id, sample_group_id, alt_ref_type_id, sample_type_id, sample_name)
values (1, 1, 1, 1, 'Sample for sample group 1');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (11, 'TSample', '', 'SAMP000001', 'tbl_physical_samples', 1);
insert into tbl_physical_samples (physical_sample_id, sample_group_id, alt_ref_type_id, sample_type_id, sample_name)
values (2, 2, 1, 1, 'Sample for sample group 2');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (12, 'TSample', '', 'SAMP000002', 'tbl_physical_samples', 2);
insert into tbl_physical_samples (physical_sample_id, sample_group_id, alt_ref_type_id, sample_type_id, sample_name)
values (3, 3, 1, 1, 'Sample for sample group 3');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (13, 'TSample', '', 'SAMP000003', 'tbl_physical_samples', 3);
insert into tbl_physical_samples (physical_sample_id, sample_group_id, alt_ref_type_id, sample_type_id, sample_name)
values (4, 4, 1, 1, 'Sample for sample group 4');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (14, 'TSample', '', 'SAMP000004', 'tbl_physical_samples', 4);
insert into tbl_physical_samples (physical_sample_id, sample_group_id, alt_ref_type_id, sample_type_id, sample_name)
values (5, 5, 1, 1, 'Sample for sample group 5');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (15, 'TSample', '', 'SAMP000005', 'tbl_physical_samples', 5);

insert into tbl_datasets (dataset_id, master_set_id, method_id, data_type_id, dataset_name) values (1, 1, 3, 1, 'COUN000001');
insert into tbl_analysis_entities (analysis_entity_id, physical_sample_id, dataset_id) values (1, 1, 1);
insert into tbl_abundances (abundance_id, taxon_id, analysis_entity_id, abundance) values (1, 1, 1, 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (16, 'TFossil', '{FOSS000001,1.0,SAMP000001,1}', 'FOSS000001', 'tbl_datasets', 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (17, 'TFossil', '{FOSS000001,1.0,SAMP000001,1}', 'FOSS000001', 'tbl_analysis_entities', 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (18, 'TFossil', '{FOSS000001,1.0,SAMP000001,1}', 'FOSS000001', 'tbl_abundances', 1);

insert into tbl_datasets (dataset_id, master_set_id, method_id, data_type_id, dataset_name) values (2, 1, 3, 1, 'COUN000002');
insert into tbl_analysis_entities (analysis_entity_id, physical_sample_id, dataset_id) values (2, 2, 2);
insert into tbl_abundances (abundance_id, taxon_id, analysis_entity_id, abundance) values (2, 1, 2, 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (19, 'TFossil', '{FOSS000002,1.0,SAMP000002,1}', 'FOSS000002', 'tbl_datasets', 2);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (20, 'TFossil', '{FOSS000001,1.0,SAMP000001,1}', 'FOSS000001', 'tbl_analysis_entities', 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (21, 'TFossil', '{FOSS000001,1.0,SAMP000001,1}', 'FOSS000001', 'tbl_abundances', 1);

insert into tbl_datasets (dataset_id, master_set_id, method_id, data_type_id, dataset_name) values (3, 1, 3, 1, 'COUN000003');
insert into tbl_analysis_entities (analysis_entity_id, physical_sample_id, dataset_id) values (3, 3, 3);
insert into tbl_abundances (abundance_id, taxon_id, analysis_entity_id, abundance) values (3, 1, 3, 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (22, 'TFossil', '{FOSS000004,1.0,SAMP000004,1}', 'FOSS000003', 'tbl_datasets', 3);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (23, 'TFossil', '{FOSS000001,1.0,SAMP000001,1}', 'FOSS000001', 'tbl_analysis_entities', 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (24, 'TFossil', '{FOSS000001,1.0,SAMP000001,1}', 'FOSS000001', 'tbl_abundances', 1);
insert into tbl_contacts (contact_id, first_name) values (1, 'University of Ottawa');
insert into tbl_dataset_contacts (dataset_contact_id, contact_id, contact_type_id, dataset_id) values (1, 1, 2, 3);

insert into tbl_datasets (dataset_id, master_set_id, method_id, data_type_id, dataset_name) values (4, 1, 3, 1, 'COUN000004');
insert into tbl_analysis_entities (analysis_entity_id, physical_sample_id, dataset_id) values (4, 4, 4);
insert into tbl_abundances (abundance_id, taxon_id, analysis_entity_id, abundance) values (4, 1, 4, 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (25, 'TFossil', '{FOSS000004,1.0,SAMP000004,1}', 'FOSS000004', 'tbl_datasets', 4);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (26, 'TFossil', '{FOSS000001,1.0,SAMP000001,1}', 'FOSS000001', 'tbl_analysis_entities', 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (27, 'TFossil', '{FOSS000001,1.0,SAMP000001,1}', 'FOSS000001', 'tbl_abundances', 1);
insert into tbl_contacts (contact_id, first_name) values (2, 'University of Bolivia');
insert into tbl_dataset_contacts (dataset_contact_id, contact_id, contact_type_id, dataset_id) values (2, 2, 2, 4);
insert into tbl_contacts (contact_id, last_name) values (3, 'First');
insert into tbl_dataset_contacts (dataset_contact_id, contact_id, contact_type_id, dataset_id) values (3, 3, 2, 4);
insert into tbl_contacts (contact_id, last_name) values (4, 'Second');
insert into tbl_dataset_contacts (dataset_contact_id, contact_id, contact_type_id, dataset_id) values (3, 4, 2, 4);

insert into tbl_datasets (dataset_id, master_set_id, method_id, data_type_id, dataset_name) values (5, 1, 3, 1, 'COUN000005');
insert into tbl_analysis_entities (analysis_entity_id, physical_sample_id, dataset_id) values (5, 5, 5);
insert into tbl_abundances (abundance_id, taxon_id, analysis_entity_id, abundance) values (5, 1, 5, 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (28, 'TFossil', '{FOSS000005,1.0,SAMP000005,1}', 'FOSS000005', 'tbl_datasets', 5);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (29, 'TFossil', '{FOSS000001,1.0,SAMP000001,1}', 'FOSS000001', 'tbl_analysis_entities', 1);
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
values (30, 'TFossil', '{FOSS000001,1.0,SAMP000001,1}', 'FOSS000001', 'tbl_abundances', 1);

-- who do we deal with traces?
-- insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
-- values (10, 'TFossil', null, 'FOSI000004', 'tbl_dataset_contacts', 4);