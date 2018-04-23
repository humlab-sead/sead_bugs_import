select 'tbl_sites' as t, site_name as n, count(site_name) as c from tbl_sites group by site_name having count(site_name) > 1
union
select 'tbl_biblio' as t, bugs_reference as n, count(bugs_reference) as c from tbl_biblio group by bugs_reference having count(bugs_reference) > 1
union
select 'tbl_contacts first_name' as t, first_name as n, count(first_name) as c from tbl_contacts group by first_name having count(first_name) > 1
union
select 'tbl_contacts last_name' as t, last_name as n, count(last_name) as c from tbl_contacts group by last_name having count(last_name) > 1
union
select 'tbl_locations country' as t, location_name as n, count(location_name) as c from tbl_locations 
	where location_type_id = 1
	group by location_name having count(location_name) > 1
union
select 'tbl_locations uncategorized' as t, location_name as n, count(location_name) as c from tbl_locations 
	where location_type_id = 20
	group by location_name having count(location_name) > 1
union
select 'tbl_geochronology' as t, compressedTable.compressedColumns as n, count(compressedTable.compressedColumns)
	from (select 
	  coalesce(dating_lab_id, 0) ||
	  coalesce(lab_number, '')||
	  coalesce(age, 0) ||
	  coalesce(error_older, 0) ||
	  coalesce(error_younger, 0) ||
	  coalesce(delta_13c, 0) ||
	  coalesce(notes, '')||
	  coalesce(dating_uncertainty_id, 0) as compressedColumns
	  from tbl_geochronology) as compressedTable
	group by compressedTable.compressedColumns having count(compressedTable.compressedColumns) > 1
union
select 'tbl_analysis_entities unique sample - dataset combo' as t, compressedTable.compressedColumns as n, count(compressedTable.compressedColumns)
  from (
	 select physical_sample_id || '-' || dataset_id as compressedColumns
	 from tbl_analysis_entities
	) as compressedTable
  group by compressedTable.compressedColumns having count(compressedTable.compressedColumns) > 1
union
select 'tbl_physical_samples' as t, compressedTable.compressedColumns as n, count(compressedTable.compressedColumns)
	from (select 
		sample_group_id || '-' || 
		sample_name ||
		sample_type_id as compressedColumns
		from tbl_physical_samples
	 ) as compressedTable
	 group by compressedTable.compressedColumns having count(compressedTable.compressedColumns) > 1
union
select 'tbl_rdb' as t, compressedTable.compressedColumns as n, count(compressedTable.compressedColumns)
	from (select 
		location_id || '-' || 
		coalesce(rdb_code_id, 0) ||
		taxon_id as compressedColumns
		from tbl_rdb
	 ) as compressedTable
	 group by compressedTable.compressedColumns having count(compressedTable.compressedColumns) > 1
union
select 'tbl_rdb_codes' as t, rdb_category as n, count(rdb_category)
	from tbl_rdb_codes
	where tbl_rdb_codes is not null
	group by rdb_category having count(tbl_rdb_codes) > 1
union
select 'tbl_relative_ages' as t, abbreviation as n, count(abbreviation) as c from tbl_relative_ages group by abbreviation having count(abbreviation) > 1
union
select 'tbl_sample_dimensions' as t, compressedTable.compressedColumns as n, count(compressedTable.compressedColumns)
	from (select 
		physical_sample_id || '-' || 
		dimension_id || '-' ||
		method_id as compressedColumns
		from tbl_sample_dimensions
	 ) as compressedTable
	 group by compressedTable.compressedColumns having count(compressedTable.compressedColumns) > 1
union
select 'tbl_sample_groups' as t, compressedTable.compressedColumns as n, count(compressedTable.compressedColumns)
	from (select 
		site_id || '-' || 
		sample_group_name as compressedColumns
		from tbl_sample_groups
	 ) as compressedTable
	 group by compressedTable.compressedColumns having count(compressedTable.compressedColumns) > 1
union
select 'tbl_site_locations' as t, compressedTable.compressedColumns as n, count(compressedTable.compressedColumns)
	from (select 
		location_id || '-' || 
		site_id as compressedColumns
		from tbl_site_locations
	 ) as compressedTable
	 group by compressedTable.compressedColumns having count(compressedTable.compressedColumns) > 1
union
select 'tbl_site_references' as t, compressedTable.compressedColumns as n, count(compressedTable.compressedColumns)
	from (select 
		biblio_id || '-' || 
		site_id as compressedColumns
		from tbl_site_references
	 ) as compressedTable
	 group by compressedTable.compressedColumns having count(compressedTable.compressedColumns) > 1
union
select 'tbl_sites' as t, site_name as n, count(site_name) as c from tbl_sites group by site_name having count(site_name) > 1
union
select 'tbl_species_associations' as t, compressedTable.compressedColumns as n, count(compressedTable.compressedColumns)
	from (select 
		associated_taxon_id || '-' || 
		association_type_id || '-' ||
		taxon_id as compressedColumns
		from tbl_species_associations
	 ) as compressedTable
	 group by compressedTable.compressedColumns having count(compressedTable.compressedColumns) > 1
union
select 'tbl_taxa_common_names' as t, compressedTable.compressedColumns as n, count(compressedTable.compressedColumns)
	from (select 
		common_name || 
		language_id || '-' ||
		taxon_id as compressedColumns
		from tbl_taxa_common_names
	 ) as compressedTable
	 group by compressedTable.compressedColumns having count(compressedTable.compressedColumns) > 1
union
select 'tbl_taxa_measured_attributes' as t, compressedTable.compressedColumns as n, count(compressedTable.compressedColumns)
	from (select 
		coalesce(attribute_measure, '') || 
		coalesce(attribute_type, '') ||
		coalesce(attribute_units, '') ||
		data || '-' ||
		taxon_id as compressedColumns
		from tbl_taxa_measured_attributes
	 ) as compressedTable
	 group by compressedTable.compressedColumns having count(compressedTable.compressedColumns) > 1
union
select 'tbl_taxa_seasonality' as t, compressedTable.compressedColumns as n, count(compressedTable.compressedColumns)
	from (select 
		activity_type_id || '-' ||
		season_id || '-' ||
		location_id || '-' ||
		taxon_id as compressedColumns
		from tbl_taxa_seasonality
	 ) as compressedTable
	 group by compressedTable.compressedColumns having count(compressedTable.compressedColumns) > 1
union
select 'tbl_taxa_tree' as t, compressedTable.compressedColumns as n, count(compressedTable.compressedColumns)
	from (select 
		f.family_name || ' ' ||
		g.genus_name || ' ' ||
		s.species || ' ' ||
		coalesce(a.author_name, '') as compressedColumns
		from tbl_taxa_tree_master s
		 left join tbl_taxa_tree_genera g on s.genus_id = g.genus_id
		 left join tbl_taxa_tree_families f on g.family_id = f.family_id
		 left join tbl_taxa_tree_authors a on s.author_id = a.author_id
	 ) as compressedTable
	 group by compressedTable.compressedColumns having count(compressedTable.compressedColumns) > 1
union
select 'tbl_taxonomic_order' as t, compressedTable.compressedColumns as n, count(compressedTable.compressedColumns)
	from (select 
		taxonomic_order_system_id || '-' ||
		taxonomic_code as compressedColumns
		from tbl_taxonomic_order
	 ) as compressedTable
	 group by compressedTable.compressedColumns having count(compressedTable.compressedColumns) > 1
union
select 'tbl_taxonomy_notes' as t, compressedTable.compressedColumns as n, count(compressedTable.compressedColumns)
	from (select 
		coalesce(taxonomy_notes, 'NULL') || ' ' || 
		biblio_id || ' ' ||
		taxon_id as compressedColumns
		from tbl_taxonomy_notes
	 ) as compressedTable
	 group by compressedTable.compressedColumns having count(compressedTable.compressedColumns) > 1
union
select 'tbl_text_biology' as t, compressedTable.compressedColumns as n, count(compressedTable.compressedColumns)
	from (select 
		coalesce(biology_text, 'NULL') || ' ' || 
		biblio_id || ' ' ||
		taxon_id as compressedColumns
		from tbl_text_biology
	 ) as compressedTable
	 group by compressedTable.compressedColumns having count(compressedTable.compressedColumns) > 1
union
select 'tbl_text_distribution' as t, compressedTable.compressedColumns as n, count(compressedTable.compressedColumns)
	from (select 
		coalesce(distribution_text, 'NULL') || ' ' ||
		taxon_id as compressedColumns
		from tbl_text_distribution
	 ) as compressedTable
	 group by compressedTable.compressedColumns having count(compressedTable.compressedColumns) > 1
union
select 'tbl_text_identification_keys' as t, compressedTable.compressedColumns as n, count(compressedTable.compressedColumns)
	from (select 
		coalesce(key_text, 'NULL') || ' ' ||
		taxon_id as compressedColumns
		from tbl_text_identification_keys
	 ) as compressedTable
	 group by compressedTable.compressedColumns having count(compressedTable.compressedColumns) > 1
