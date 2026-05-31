select abbreviation
from tbl_relative_ages
group by abbreviation
order by abbreviation

period_code	dating_method	count
BS At	Pollen	8
BS Bo	Pollen	2
BS PB	GeolPer	1
BS SB	Pollen	3
BS SB	GeolPer	1
BS YD	GeolPer	13
ERom	ArchPer	2
LNeoNWEuro	null	1
LNeoNWEuro	ArchPer	3
LNeoSEEuro	ArchPer	1
MedGreen	HistPer	8
MedGreen	ArchPer	97
MedIce	ArchPer	261
MigScand	ArchPer	6
Neo	null	4
Neo	Dendro	8
Neo	ArchPer	176
RomIAScand	ArchPer	6


select *
from bugs_import.bugs_type_translations
where replacement_value like '%BS%'

select *
from public.tbl_relative_dates
join tbl_methods using (method_id)
where method_abbrev_or_alt_name = 'Pollen'

select *
from tbl_methods
where method_abbrev_or_alt_name = 'Pollen'

with error_values as (
    select *, string_to_array(translate(bugs_data, '{}', ''), ',') as data_array, bugs_data
    from bugs_import.bugs_errors
    where TRUE
      and message = 'No period found for code'
) select data_array[4] as period_code, data_array[5] as dating_method, count(*) as count
  from error_values
  group by data_array[4], data_array[5]
  order by 1, 2 desc
  
select *, string_to_array(translate(bugs_data, '{}', ''), ',') as data_array, bugs_data
from bugs_import.bugs_errors
where TRUE
  and message = 'No period found for code'
    and translated_compressed_data like '%BS %'
	
  