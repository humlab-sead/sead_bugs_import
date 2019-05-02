create schema bugs_import;
create table bugs_import.bugs_trace (
  bugs_trace_id serial not null,
  bugs_table varchar(100) not null,
  bugs_data varchar,
  bugs_identifier varchar,
  sead_table varchar(255) not null,
  sead_reference_id integer not null,
  change_date timestamp with time zone DEFAULT now(),
  manipulation_type varchar(50),
  constraint pk_bugs_trace_bugs_trace_id primary key(bugs_trace_id)
);

create table bugs_import.bugs_errors (
  bugs_error_id serial not null,
  bugs_table varchar(100) not null,
  bugs_data varchar,
  bugs_identifier varchar,
  message text,
  change_date timestamp with time zone DEFAULT now(),
  constraint pk_bugs_import_bugs_error_id primary key(bugs_error_id)
);

create table bugs_import.id_based_translations (
  id_based_translation_id serial not null,
  bugs_definition text,
  bugs_table varchar(50),
  target_column varchar(50),
  replacement_value text,
  constraint pk_id_based_translation primary key(id_based_translation_id)
);

create table bugs_import.bugs_type_translations (
  type_translation_id serial not null,
  bugs_table varchar(50),
  bugs_column varchar(50),
  triggering_column_value text,
  target_column  varchar(50),
  replacement_value text,
  constraint pk_type_translation_id primary key (type_translation_id)
);