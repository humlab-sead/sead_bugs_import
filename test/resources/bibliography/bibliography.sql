insert into tbl_biblio (biblio_id, bugs_author, bugs_reference, bugs_title)
    values (1, 'Exists, A.B (2000)', 'Exists 2000', 'An existing bibliography non-edited');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values (1, 'TBiblio', '{}', 'Exists 2000', 'tbl_biblio', 1);

insert into tbl_biblio (biblio_id, bugs_author, bugs_reference, bugs_title, date_updated)
    values (2, 'Exists, A.B (2001)', 'Exists 2001', 'A changed entry', '2016-01-01');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id, change_date)
    values (2, 'TBiblio', '{}', 'Exists 2001', 'tbl_biblio', 2, '2015-01-01');

insert into tbl_biblio (biblio_id, bugs_author, bugs_reference, bugs_title)
values (3, 'Exists, A.B (2012)', 'Exists 2012', 'A preinstalled bugs reference');