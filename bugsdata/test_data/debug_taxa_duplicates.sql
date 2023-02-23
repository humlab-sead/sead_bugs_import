/*

DEBUG DATABASE: sead_production_201912

select author_name, genus_name, species, min(taxon_id), max(taxon_id), count(*)
from tbl_taxa_tree_master
join tbl_taxa_tree_genera using (genus_id)
join tbl_taxa_tree_authors using (author_id)
group by author_name, genus_name, species
having count(*) > 1
--limit 10


select *
from tbl_taxa_tree_master
join tbl_taxa_tree_genera using (genus_id)
left join tbl_taxa_tree_authors using (author_id)
where species = 'oopterus'
limit 10

select *
from tbl_taxa_tree_master
where species like 'op%'
order by species

-- DATABASE: bugsdata_20200219
select *
from "INDEX"
where "SPECIES" = 'riparius'
  and "GENUS" = 'Elaphrus'
  and "AUTHORITY" = '(L.)'
  -- and "CODE" = '1.0120030000000000'

*/

select *
from tbl_taxa_tree_master
join tbl_taxa_tree_genera using (genus_id)
left join tbl_taxa_tree_authors using (author_id)
where species = 'oopterus'
limit 10

SELECT *
FROM dblink('hostaddr=127.0.0.1 dbname=bugsdata user=humlab_admin password=Vua9VagZ', 'select * from "INDEX"')
 AS t1(
    "CODE" character varying(20),
    "FAMILY" character varying(100),
    "GENUS" character varying(100),
    "SPECIES" character varying(70),
    "AUTHORITY" character varying(70)
 )

-- SELECT pg_reload_conf();

select *
from bugs_import.bugs_trace
where bugs_table = 'INDEX'
  and sead_table = 'tbl_taxa_tree_master'
limit 10

/* Dublettposter från Bugs tillsammans med befintlig post i SEAD
   Dublettposter verkar komma in från TSynonyms (inte INDEX)
   Se kod i SynonymMapper
 */
with duplicates as (
	select distinct author_id, genus_id, species
	from tbl_taxa_tree_master
	group by author_id, genus_id, species
	having count(*) > 1
)
	select taxon_id, author_name, genus_name, species, bt.*
	from duplicates
	join tbl_taxa_tree_master using (author_id, genus_id, species)
	join tbl_taxa_tree_genera using (genus_id)
	join tbl_taxa_tree_authors using (author_id)
	left join bugs_import.bugs_trace as bt
	  on bt.sead_table = 'tbl_taxa_tree_master'
	 and bt.sead_reference_id = taxon_id
	 -- and bt.bugs_table = 'INDEX'
	order by taxon_id
-->

(L.)	Elaphrus	riparius

select *
from bugs_import.bugs_trace as bt
where sead_table = 'tbl_taxa_tree_master'
  and bugs_table = 'TSynonym'

select *
from bugs_import.bugs_trace as bt
where bugs_identifier like '1.012003%'

select *
from bugs_import.bugs_errors
where bugs_table = 'INDEX'
limit 10

select *
from tbl_taxa_tree_master
join bugs_import.bugs_trace
  on sead_table = 'tbl_taxa_tree_master'
 and sead_reference_id = taxon_id
 -- and manipulation_type = 'INSERT'
--> 7015

limit 10



select *
from tbl_taxa_tree_master
where date_updated > '2019-01-01'

