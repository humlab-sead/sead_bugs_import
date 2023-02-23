ALTER TABLE "public"."TCountsheet" ADD CONSTRAINT "fk_TCountsheet_TLookupCountsheetContext_1" FOREIGN KEY ("SheetContext") REFERENCES "public"."TLookupCountsheetContext" ("SheetContext");

Update "TCountsheet" Set "SheetType" = 'Abundances' Where "SheetType" = 'Abundance';
Update "TCountsheet" Set "SheetType" = 'Partial abundances' Where "SheetType" = 'Partial Abundance';
ALTER TABLE "public"."TCountsheet" ADD CONSTRAINT "fk_TCountsheet_TLookupCountsheetTypes_1" FOREIGN KEY ("SheetType") REFERENCES "public"."TLookupCountsheetTypes" ("CountsheetType");

-- Missing in TDatesMethods: null, '' and Calendar date by unknown method
Update "public"."TDatesCalendar" set "DatingMethod" = NULL where "DatingMethod" in (' ', 'Calendar date by unknown method');
ALTER TABLE "public"."TDatesCalendar" ADD CONSTRAINT "fk_TDatesCalendar_TDatesMethods_1" FOREIGN KEY ("DatingMethod") REFERENCES "public"."TDatesMethods" ("Abbrev");

ALTER TABLE "public"."TDatesPeriod" ADD CONSTRAINT "fk_TDatesPeriod_TPeriods_1" FOREIGN KEY ("PeriodCODE") REFERENCES "public"."TPeriods" ("PeriodCODE");

update "TDatesPeriod" set "DatingMethod" = NULL where "DatingMethod" = ' ';  
ALTER TABLE "public"."TDatesPeriod" ADD CONSTRAINT "fk_TDatesPeriod_TDatesMethods_1" FOREIGN KEY ("DatingMethod") REFERENCES "public"."TDatesMethods" ("Abbrev");

update "TDatesRadio" set "LabID" = NULL where "LabID" in ('Suerc', 'Birmingham');  
ALTER TABLE "public"."TDatesRadio" ADD CONSTRAINT "fk_TDatesRadio_TLab_1" FOREIGN KEY ("LabID") REFERENCES "public"."TLab" ("LabID") ON DELETE CASCADE;

ALTER TABLE "public"."TDatesRadio" ADD CONSTRAINT "fk_TDatesRadio_TDatesMethods_1" FOREIGN KEY ("DatingMethod") REFERENCES "public"."TDatesMethods" ("Abbrev");


ALTER TABLE "public"."TKeys" ADD CONSTRAINT "fk_TKeys_TBiblio_1" FOREIGN KEY ("Ref") REFERENCES "public"."TBiblio" ("REFERENCE");

ALTER TABLE "public"."TRDB" ADD CONSTRAINT "fk_TRDB_TRDBCodes_1" FOREIGN KEY ("RDBCode") REFERENCES "public"."TRDBCodes" ("RDBCode");

insert into "TRDBSystems" ("RDBSystemCode", "RDBSystem") values (1, 'UKRDB');
insert into "TRDBSystems" ("RDBSystemCode", "RDBSystem") values (2, 'IUCN');
insert into "TRDBSystems" ("RDBSystemCode", "RDBSystem") values (3, 'IUCN');
ALTER TABLE "public"."TRDBCodes" ADD CONSTRAINT "fk_TRDBCodes_TRDBSystems_1" FOREIGN KEY ("RDBSystemCode") REFERENCES "public"."TRDBSystems" ("RDBSystemCode");

ALTER TABLE "public"."TSample" ADD CONSTRAINT "fk_TSample_TCountsheet_1" FOREIGN KEY ("CountsheetCODE") REFERENCES "public"."TCountsheet" ("CountsheetCODE");

Update "TSpeciesAssociations"
  Set "Ref" = NULL
Where "Ref" is not NULL
  and "Ref" not in (Select "REFERENCE" from "TBiblio")
ALTER TABLE "public"."TSpeciesAssociations" ADD CONSTRAINT "fk_TSpeciesAssociations_TBiblio_1" FOREIGN KEY ("Ref") REFERENCES "public"."TBiblio" ("REFERENCE");

ALTER TABLE "TSpeciesAssociations" ALTER COLUMN "AssociatedSpeciesCODE" TYPE varchar;

with codes as (
    Select distinct S."AssociatedSpeciesCODE",  T."CODE"
    From "TSpeciesAssociations" S
    left join "INDEX" T on ABS(T."CODE"::float - S."AssociatedSpeciesCODE"::float) < 0.0000000001
) update "TSpeciesAssociations"
    set "AssociatedSpeciesCODE" = codes."CODE"
  from codes
  where codes."AssociatedSpeciesCODE" = "TSpeciesAssociations"."AssociatedSpeciesCODE" 
ALTER TABLE "public"."TSpeciesAssociations" ADD CONSTRAINT "fk_TSpeciesAssociations_INDEX_1" FOREIGN KEY ("AssociatedSpeciesCODE") REFERENCES "public"."INDEX" ("CODE");

-- Missing values: NULL, 'Guéorguiev & Guéorguiev 1995', 'Aalto et al (1984)'
Update "public"."TSynonym" set "Ref" = NULL where "Ref" in ('Guéorguiev & Guéorguiev 1995', 'Aalto et al (1984)')
ALTER TABLE "public"."TSynonym" ADD CONSTRAINT "fk_TSynonym_TBiblio_1" FOREIGN KEY ("Ref") REFERENCES "public"."TBiblio" ("REFERENCE");

-- No primary key:
-- ALTER TABLE "public"."TSynonymNotes" ADD CONSTRAINT "fk_TSynonymNotes_TSynonym_1" FOREIGN KEY ("CODE") REFERENCES "public"."TSynonym" ("CODE");
-- ALTER TABLE "public"."TDatesRadio" ADD CONSTRAINT "fk_TDatesRadio_TLookupUncertaintyType_1" FOREIGN KEY ("Uncertainty") REFERENCES "public"."TLookupUncertaintyType" ("Uncertainty");
-- ALTER TABLE "public"."TINDEXReplacements" ADD CONSTRAINT "fk_TINDEXReplacements_TSite_1" FOREIGN KEY ("SiteCODE") REFERENCES "public"."TSite" ("SiteCODE");
-- ALTER TABLE "public"."TINDEXReplacements" ADD CONSTRAINT "fk_TINDEXReplacements_INDEX_1" FOREIGN KEY ("CODE") REFERENCES "public"."INDEX" ("CODE");


ALTER TABLE "TBiology" ADD COLUMN "BiologyCODE" SERIAL PRIMARY KEY;
ALTER TABLE "TAttributes" ADD COLUMN "AttributeCODE" SERIAL PRIMARY KEY;
ALTER TABLE "TDistrib" ADD COLUMN "DistribCODE" SERIAL PRIMARY KEY;
ALTER TABLE "TKeys" ADD COLUMN "KeyCODE" SERIAL PRIMARY KEY;
ALTER TABLE "TRDB" ADD COLUMN "RDBCODE" SERIAL PRIMARY KEY;
ALTER TABLE "TSiteRef" ADD COLUMN "SiteRefCODE" SERIAL PRIMARY KEY;
ALTER TABLE "TSynonym" ADD COLUMN "SynonymCODE" SERIAL PRIMARY KEY;
ALTER TABLE "TSynonymNotes" ADD COLUMN "SynonymNoteCODE" SERIAL PRIMARY KEY;
ALTER TABLE "TTaxoNotes" ADD COLUMN "TaxoNoteCODE" SERIAL PRIMARY KEY;

