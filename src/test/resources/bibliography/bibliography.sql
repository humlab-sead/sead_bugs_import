insert into tbl_biblio (biblio_id, authors, bugs_reference, title, full_reference)
    values (1, 'Exists, A.B (2000)', 'Exists 2000', 'An existing bibliography non-edited', 'full ref');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id)
    values (1, 'TBiblio', '{}', 'Exists 2000', 'tbl_biblio', 1);

insert into tbl_biblio (biblio_id, authors, bugs_reference, title, full_reference, date_updated)
    values (2, 'Exists, A.B (2001)', 'Exists 2001', 'A changed entry', 'full ref', '2016-01-01');
insert into bugs_import.bugs_trace (bugs_trace_id, bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id, change_date)
    values (2, 'TBiblio', '{}', 'Exists 2001', 'tbl_biblio', 2, '2015-01-01');

insert into tbl_biblio (biblio_id, authors, bugs_reference, title, full_reference)
values (3, 'Exists, A.B (2012)', 'Exists 2012', 'A preinstalled bugs reference', 'full ref');