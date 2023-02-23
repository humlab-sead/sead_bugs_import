/*
SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;
CREATE DATABASE bugsdata_SAMP000546 WITH TEMPLATE = template0 ENCODING = 'UTF8';
\connect bugsdata_SAMP000546
*/

create schema sample_SAMP000546;

SET search_path = sample_SAMP000546;

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

CREATE TABLE "BuildVersionBugsdata" (
    "BuildCounter" integer NOT NULL,
    "Component" character varying(20),
    "DevLevel" character varying(20),
    "Version" character varying(20),
    "Build" character varying(20),
    "Implemented" timestamp without time zone,
    "VersionChanges" text
);

CREATE SEQUENCE "BuildVersionBugsdata_BuildCounter_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE "BuildVersionBugsdata_BuildCounter_seq" OWNED BY "BuildVersionBugsdata"."BuildCounter";

CREATE TABLE "INDEX" (
    "CODE" character varying(20) NOT NULL,
    "FAMILY" character varying(100),
    "GENUS" character varying(100),
    "SPECIES" character varying(70),
    "AUTHORITY" character varying(70)
);

CREATE TABLE "TAttributes" (
    "CODE" character varying(20),
    "AttribType" character varying(100),
    "AttribMeasure" character varying(40),
    "Value" real,
    "AttribUnits" character varying(20),
    "AttributeCODE" integer NOT NULL
);

CREATE SEQUENCE "TAttributes_AttributeCODE_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE "TAttributes_AttributeCODE_seq" OWNED BY "TAttributes"."AttributeCODE";

CREATE TABLE "TBiblio" (
    "REFERENCE" character varying(120) NOT NULL,
    "AUTHOR" character varying(510),
    "TITLE" text,
    "Notes" text
);

CREATE TABLE "TBiology" (
    "CODE" character varying(20),
    "Ref" character varying(120),
    "Data" text,
    "BiologyCODE" integer NOT NULL
);

CREATE SEQUENCE "TBiology_BiologyCODE_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE "TBiology_BiologyCODE_seq" OWNED BY "TBiology"."BiologyCODE";

CREATE TABLE "TCountry" (
    "CountryCode" character varying(6) NOT NULL,
    "Country" character varying(100)
);

CREATE TABLE "TCountsheet" (
    "CountsheetCODE" character varying(20) NOT NULL,
    "CountsheetName" character varying(200),
    "SiteCODE" character varying(20),
    "SheetContext" character varying(50),
    "SheetType" character varying(50)
);

CREATE TABLE "TDatesCalendar" (
    "SampleCODE" character varying(20),
    "Uncertainty" character varying(20),
    "CalendarCODE" character varying(20) NOT NULL,
    "Date" integer,
    "BCADBP" character varying(20),
    "DatingMethod" character varying(100),
    "Notes" text
);

CREATE TABLE "TDatesMethods" (
    "Abbrev" character varying(100) NOT NULL,
    "Method" character varying(100),
    "Type" character varying(100),
    "SortOrder" integer
);

CREATE TABLE "TDatesPeriod" (
    "PeriodDateCODE" character varying(20) NOT NULL,
    "SampleCODE" character varying(20),
    "Uncertainty" character varying(20),
    "PeriodCODE" character varying(40),
    "DatingMethod" character varying(100),
    "Notes" text
);

CREATE TABLE "TDatesRadio" (
    "DateCODE" character varying(20) NOT NULL,
    "SampleCODE" character varying(20),
    "LabNr" character varying(40),
    "Uncertainty" character varying(20),
    "Date" integer,
    "AgeErrorOrPlusError" integer,
    "AgeErrorMinus" integer,
    "DatingMethod" character varying(30),
    "MaterialType" character varying(100),
    "LabID" character varying(20),
    "Notes" text
);

CREATE TABLE "TDatesTypes" (
    "Type" character varying(100) NOT NULL
);

CREATE TABLE "TDistrib" (
    "CODE" character varying(20),
    "Ref" character varying(120),
    "Data" text,
    "DistribCODE" integer NOT NULL
);

CREATE SEQUENCE "TDistrib_DistribCODE_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE "TDistrib_DistribCODE_seq" OWNED BY "TDistrib"."DistribCODE";

CREATE TABLE "TEcoBugs" (
    "CODE" character varying(20) NOT NULL,
    "BugsEcoCODE" character varying(14)
);

CREATE TABLE "TEcoDefBugs" (
    "SortOrder" integer NOT NULL,
    "BugsEcoCODE" character varying(14) NOT NULL,
    "Definition" character varying(100),
    "Notes" text,
    "EcoLabel" character varying(100)
);

CREATE TABLE "TEcoDefGroups" (
    "EcoGroupCode" character varying(50) NOT NULL,
    "EcoName" character varying(100)
);

CREATE TABLE "TEcoDefKoch" (
    "BugsKochCode" character varying(10) NOT NULL,
    "KochCode" character varying(4),
    "FullName" character varying(100),
    "KochGroup" character varying(50),
    "Description" character varying(510),
    "Notes" text
);

CREATE TABLE "TEcoKoch" (
    "CODE" character varying(20) NOT NULL,
    "BugsKochCode" character varying(10)
);

CREATE TABLE "TFossil" (
    "FossilBugsCODE" character varying(20) NOT NULL,
    "CODE" character varying(20),
    "SampleCODE" character varying(20),
    "Abundance" integer
);

CREATE TABLE "TFossilUncertainty" (
    "FossilBugsCODE" character varying(20) NOT NULL,
    "Uncertainty" character varying(510)
);

CREATE TABLE "TINDEXReplacements" (
    "CODE" character varying(20),
    "ReplacedName" character varying(200) NOT NULL,
    "SiteCODE" character varying(20)
);

CREATE TABLE "TKeys" (
    "CODE" character varying(20),
    "Ref" character varying(120),
    "Data" text,
    "KeyCODE" integer NOT NULL
);

CREATE SEQUENCE "TKeys_KeyCODE_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE "TKeys_KeyCODE_seq" OWNED BY "TKeys"."KeyCODE";

CREATE TABLE "TLab" (
    "LabID" character varying(20) NOT NULL,
    "Labname" character varying(200),
    "Address" character varying(510),
    "Country" character varying(60),
    "Telephone" character varying(100),
    "Website" text,
    email text
);

CREATE TABLE "TLookupCountsheetContext" (
    "SheetContext" character varying(50) NOT NULL,
    "SortOrder" integer
);

CREATE TABLE "TLookupCountsheetTypes" (
    "CountsheetType" character varying(50) NOT NULL,
    "SortOrder" integer
);

CREATE TABLE "TLookupMonths" (
    "SeasonCode" character varying(6) NOT NULL,
    "SeasonName" character varying(100),
    "SortOrder" integer
);

CREATE TABLE "TLookupUncertaintyType" (
    "Uncertainty" character varying(30) NOT NULL,
    "SortOrder" integer
);

CREATE TABLE "TMCRNames" (
    "MCRNameTrim" character varying(160),
    "CompareStatus" character varying(510),
    "CODE" character varying(20) NOT NULL,
    "tempCODE" double precision,
    "MCRNumber" integer,
    "MCRName" character varying(400)
);

CREATE TABLE "TMCRSummaryData" (
    "CODE" character varying(20) NOT NULL,
    "TMaxLo" integer,
    "TMaxHi" integer,
    "TMinLo" integer,
    "TMinHi" integer,
    "TRangeLo" integer,
    "TRangeHi" integer,
    "COGMidTMax" integer,
    "COGMidTRange" integer
);

CREATE TABLE "TPeriods" (
    "PeriodCODE" character varying(40) NOT NULL,
    "PeriodName" character varying(56),
    "PeriodType" character varying(100),
    "PeriodDesc" character varying(510),
    "PeriodRef" character varying(100),
    "PeriodGeog" character varying(200),
    "Begin" integer,
    "BeginBCAD" character varying(8),
    "End" integer,
    "EndBCAD" character varying(8),
    "YearsType" character varying(26)
);

CREATE TABLE "TRDB" (
    "CODE" character varying(20),
    "CountryCode" character varying(6),
    "RDBCode" integer,
    "RDBCODE" integer NOT NULL
);

CREATE TABLE "TRDBCodes" (
    "RDBCode" integer NOT NULL,
    "Category" character varying(8),
    "RDBDefinition" character varying(400),
    "RDBSystemCode" integer
);

CREATE SEQUENCE "TRDBCodes_RDBCode_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE "TRDBCodes_RDBCode_seq" OWNED BY "TRDBCodes"."RDBCode";

CREATE TABLE "TRDBSystems" (
    "RDBSystemCode" integer NOT NULL,
    "RDBSystem" character varying(20),
    "RDBVersion" character varying(20),
    "RDBSystemDate" integer,
    "RDBFirstPublished" integer,
    "Ref" character varying(120),
    "CountryCode" character varying(6)
);

CREATE SEQUENCE "TRDBSystems_RDBSystemCode_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE "TRDBSystems_RDBSystemCode_seq" OWNED BY "TRDBSystems"."RDBSystemCode";

CREATE SEQUENCE "TRDB_RDBCODE_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE "TRDB_RDBCODE_seq" OWNED BY "TRDB"."RDBCODE";

CREATE TABLE "TSample" (
    "SampleCODE" character varying(20) NOT NULL,
    "SiteCODE" character varying(20),
    "X" character varying(100),
    "Y" character varying(100),
    "ZorDepthTop" double precision,
    "ZorDepthBot" double precision,
    "RefNrContext" character varying(100),
    "CountsheetCODE" character varying(20)
);

CREATE TABLE "TSeasonActiveAdult" (
    "CODE" character varying(20),
    "HSeason" character varying(6),
    "CountryCode" character varying(6)
);

CREATE TABLE "TSite" (
    "SiteCODE" character varying(20) NOT NULL,
    "SiteName" character varying(100),
    "Region" character varying(80),
    "Country" character varying(40),
    "NGR" character varying(20),
    "LatDD" real,
    "LongDD" real,
    "Alt" real,
    "IDBy" character varying(510),
    "Interp" character varying(510),
    "Specimens" character varying(510)
);

CREATE TABLE "TSiteOtherProxies" (
    "OtherProxyID" integer NOT NULL,
    "SiteCODE" character varying(20),
    "HasPollen" smallint,
    "HasPlantMacro" smallint,
    "HasDiatoms" smallint,
    "HasChironomids" smallint,
    "HasSoilChemistry" smallint,
    "HasIsotopes" smallint,
    "HasAnimalBones" smallint,
    "HasArchaeology" smallint,
    "HasMolluscs" smallint
);

CREATE SEQUENCE "TSiteOtherProxies_OtherProxyID_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE "TSiteOtherProxies_OtherProxyID_seq" OWNED BY "TSiteOtherProxies"."OtherProxyID";

CREATE TABLE "TSiteRef" (
    "SiteCODE" character varying(20),
    "Ref" character varying(120),
    "SiteRefCODE" integer NOT NULL
);

CREATE SEQUENCE "TSiteRef_SiteRefCODE_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE "TSiteRef_SiteRefCODE_seq" OWNED BY "TSiteRef"."SiteRefCODE";

CREATE TABLE "TSpeciesAssociations" (
    "SpeciesAssociationID" integer NOT NULL,
    "CODE" character varying(20),
    "AssociatedSpeciesCODE" character varying,
    "AssociationType" character varying(100),
    "Ref" character varying(120)
);

CREATE SEQUENCE "TSpeciesAssociations_SpeciesAssociationID_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE "TSpeciesAssociations_SpeciesAssociationID_seq" OWNED BY "TSpeciesAssociations"."SpeciesAssociationID";

CREATE TABLE "TSynonym" (
    "CODE" character varying(20) NOT NULL,
    "SynGenus" character varying(100),
    "SynSpecies" character varying(100),
    "SynAuthority" character varying(100),
    "Ref" character varying(120),
    "Notes" character varying(510),
    "SynonymCODE" integer NOT NULL
);

CREATE TABLE "TSynonymNotes" (
    "CODE" character varying(20) NOT NULL,
    "Notes" character varying(510),
    "SynonymNoteCODE" integer NOT NULL
);

CREATE SEQUENCE "TSynonymNotes_SynonymNoteCODE_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE "TSynonymNotes_SynonymNoteCODE_seq" OWNED BY "TSynonymNotes"."SynonymNoteCODE";

CREATE SEQUENCE "TSynonym_SynonymCODE_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE "TSynonym_SynonymCODE_seq" OWNED BY "TSynonym"."SynonymCODE";

CREATE TABLE "TTaxoNotes" (
    "CODE" character varying(20),
    "Ref" character varying(120),
    "Data" text,
    "TaxoNoteCODE" integer NOT NULL
);

CREATE SEQUENCE "TTaxoNotes_TaxoNoteCODE_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE "TTaxoNotes_TaxoNoteCODE_seq" OWNED BY "TTaxoNotes"."TaxoNoteCODE";

CREATE TABLE "TbirmBEETLEdat" (
    "Field1" character varying(510),
    "Field2" character varying(510),
    "Field3" character varying(510),
    "Field4" character varying(510),
    "Field5" character varying(510),
    "Field6" character varying(510),
    "Field7" character varying(510),
    "Field8" character varying(510),
    "Field9" character varying(510),
    "Field10" character varying(510),
    "Field11" character varying(510),
    "Field12" character varying(510),
    "Field13" character varying(510),
    "Field14" character varying(510),
    "Field15" character varying(510),
    "Field16" character varying(510),
    "Field17" character varying(510),
    "Field18" character varying(510),
    "Field19" character varying(510),
    "Field20" character varying(510),
    "Field21" character varying(510),
    "Field22" character varying(510),
    "Field23" character varying(510),
    "Field24" character varying(510),
    "Field25" character varying(510),
    "Field26" character varying(510),
    "Field27" character varying(510),
    "Field28" character varying(510),
    "Field29" character varying(510),
    "Field30" character varying(510),
    "Field31" character varying(510),
    "Field32" character varying(510),
    "Field33" character varying(510),
    "Field34" character varying(510),
    "Field35" character varying(510),
    "Field36" character varying(510),
    "Field37" character varying(510),
    "Field38" character varying(510),
    "Field39" character varying(510),
    "Field40" character varying(510),
    "Field41" character varying(510),
    "Field42" character varying(510),
    "Field43" character varying(510),
    "Field44" character varying(510),
    "Field45" character varying(510),
    "Field46" character varying(510),
    "Field47" character varying(510),
    "Field48" character varying(510),
    "Field49" character varying(510),
    "Field50" character varying(510),
    "Field51" character varying(510),
    "Field52" character varying(510),
    "Field53" character varying(510),
    "Field54" character varying(510),
    "Field55" character varying(510),
    "Field56" character varying(510),
    "Field57" character varying(510),
    "Field58" character varying(510),
    "Field59" character varying(510),
    "Field60" character varying(510),
    "MCRRow" integer,
    "CODE" character varying(20)
);

CREATE UNLOGGED TABLE jailer_config (
    jversion character varying(20),
    jkey character varying(200),
    jvalue character varying(200)
);

CREATE UNLOGGED TABLE jailer_dependency (
    r_entitygraph integer NOT NULL,
    assoc integer NOT NULL,
    depend_id integer NOT NULL,
    traversed integer,
    from_type integer NOT NULL,
    to_type integer NOT NULL,
    from_pk tid,
    from_pk0 character varying(120),
    from_pk1 integer,
    to_pk tid,
    to_pk0 character varying(120),
    to_pk1 integer
);

CREATE UNLOGGED TABLE jailer_entity (
    r_entitygraph integer NOT NULL,
    pk tid,
    pk0 character varying(120),
    pk1 integer,
    birthday integer NOT NULL,
    type integer NOT NULL,
    orig_birthday integer,
    association integer
);

CREATE UNLOGGED TABLE jailer_graph (
    id integer NOT NULL,
    age integer NOT NULL
);

CREATE UNLOGGED TABLE jailer_set (
    set_id integer NOT NULL,
    type integer NOT NULL,
    pk tid,
    pk0 character varying(120),
    pk1 integer
);

CREATE UNLOGGED TABLE jailer_tmp (
    c1 integer,
    c2 integer
);

ALTER TABLE ONLY "BuildVersionBugsdata" ALTER COLUMN "BuildCounter" SET DEFAULT nextval('"BuildVersionBugsdata_BuildCounter_seq"'::regclass);

ALTER TABLE ONLY "TAttributes" ALTER COLUMN "AttributeCODE" SET DEFAULT nextval('"TAttributes_AttributeCODE_seq"'::regclass);

ALTER TABLE ONLY "TBiology" ALTER COLUMN "BiologyCODE" SET DEFAULT nextval('"TBiology_BiologyCODE_seq"'::regclass);

ALTER TABLE ONLY "TDistrib" ALTER COLUMN "DistribCODE" SET DEFAULT nextval('"TDistrib_DistribCODE_seq"'::regclass);

ALTER TABLE ONLY "TKeys" ALTER COLUMN "KeyCODE" SET DEFAULT nextval('"TKeys_KeyCODE_seq"'::regclass);

ALTER TABLE ONLY "TRDB" ALTER COLUMN "RDBCODE" SET DEFAULT nextval('"TRDB_RDBCODE_seq"'::regclass);

ALTER TABLE ONLY "TRDBCodes" ALTER COLUMN "RDBCode" SET DEFAULT nextval('"TRDBCodes_RDBCode_seq"'::regclass);

ALTER TABLE ONLY "TRDBSystems" ALTER COLUMN "RDBSystemCode" SET DEFAULT nextval('"TRDBSystems_RDBSystemCode_seq"'::regclass);

ALTER TABLE ONLY "TSiteOtherProxies" ALTER COLUMN "OtherProxyID" SET DEFAULT nextval('"TSiteOtherProxies_OtherProxyID_seq"'::regclass);

ALTER TABLE ONLY "TSiteRef" ALTER COLUMN "SiteRefCODE" SET DEFAULT nextval('"TSiteRef_SiteRefCODE_seq"'::regclass);

ALTER TABLE ONLY "TSpeciesAssociations" ALTER COLUMN "SpeciesAssociationID" SET DEFAULT nextval('"TSpeciesAssociations_SpeciesAssociationID_seq"'::regclass);

ALTER TABLE ONLY "TSynonym" ALTER COLUMN "SynonymCODE" SET DEFAULT nextval('"TSynonym_SynonymCODE_seq"'::regclass);

ALTER TABLE ONLY "TSynonymNotes" ALTER COLUMN "SynonymNoteCODE" SET DEFAULT nextval('"TSynonymNotes_SynonymNoteCODE_seq"'::regclass);

ALTER TABLE ONLY "TTaxoNotes" ALTER COLUMN "TaxoNoteCODE" SET DEFAULT nextval('"TTaxoNotes_TaxoNoteCODE_seq"'::regclass);

ALTER TABLE ONLY "BuildVersionBugsdata"
    ADD CONSTRAINT "BuildVersionBugsdata_pkey" PRIMARY KEY ("BuildCounter");

ALTER TABLE ONLY "INDEX"
    ADD CONSTRAINT "INDEX_pkey" PRIMARY KEY ("CODE");

ALTER TABLE ONLY "TAttributes"
    ADD CONSTRAINT "TAttributes_pkey" PRIMARY KEY ("AttributeCODE");

ALTER TABLE ONLY "TBiblio"
    ADD CONSTRAINT "TBiblio_pkey" PRIMARY KEY ("REFERENCE");

ALTER TABLE ONLY "TBiology"
    ADD CONSTRAINT "TBiology_pkey" PRIMARY KEY ("BiologyCODE");

ALTER TABLE ONLY "TCountry"
    ADD CONSTRAINT "TCountry_pkey" PRIMARY KEY ("CountryCode");

ALTER TABLE ONLY "TCountsheet"
    ADD CONSTRAINT "TCountsheet_pkey" PRIMARY KEY ("CountsheetCODE");

ALTER TABLE ONLY "TDatesCalendar"
    ADD CONSTRAINT "TDatesCalendar_pkey" PRIMARY KEY ("CalendarCODE");

ALTER TABLE ONLY "TDatesMethods"
    ADD CONSTRAINT "TDatesMethods_pkey" PRIMARY KEY ("Abbrev");

ALTER TABLE ONLY "TDatesPeriod"
    ADD CONSTRAINT "TDatesPeriod_pkey" PRIMARY KEY ("PeriodDateCODE");

ALTER TABLE ONLY "TDatesRadio"
    ADD CONSTRAINT "TDatesRadio_pkey" PRIMARY KEY ("DateCODE");

ALTER TABLE ONLY "TDatesTypes"
    ADD CONSTRAINT "TDatesTypes_pkey" PRIMARY KEY ("Type");

ALTER TABLE ONLY "TDistrib"
    ADD CONSTRAINT "TDistrib_pkey" PRIMARY KEY ("DistribCODE");

ALTER TABLE ONLY "TEcoDefBugs"
    ADD CONSTRAINT "TEcoDefBugs_pkey" PRIMARY KEY ("BugsEcoCODE");

ALTER TABLE ONLY "TEcoDefGroups"
    ADD CONSTRAINT "TEcoDefGroups_pkey" PRIMARY KEY ("EcoGroupCode");

ALTER TABLE ONLY "TEcoDefKoch"
    ADD CONSTRAINT "TEcoDefKoch_pkey" PRIMARY KEY ("BugsKochCode");

ALTER TABLE ONLY "TFossilUncertainty"
    ADD CONSTRAINT "TFossilUncertainty_pkey" PRIMARY KEY ("FossilBugsCODE");

ALTER TABLE ONLY "TFossil"
    ADD CONSTRAINT "TFossil_pkey" PRIMARY KEY ("FossilBugsCODE");

ALTER TABLE ONLY "TINDEXReplacements"
    ADD CONSTRAINT "TINDEXReplacements_pkey" PRIMARY KEY ("ReplacedName");

ALTER TABLE ONLY "TKeys"
    ADD CONSTRAINT "TKeys_pkey" PRIMARY KEY ("KeyCODE");

ALTER TABLE ONLY "TLab"
    ADD CONSTRAINT "TLab_pkey" PRIMARY KEY ("LabID");

ALTER TABLE ONLY "TLookupCountsheetContext"
    ADD CONSTRAINT "TLookupCountsheetContext_pkey" PRIMARY KEY ("SheetContext");

ALTER TABLE ONLY "TLookupCountsheetTypes"
    ADD CONSTRAINT "TLookupCountsheetTypes_pkey" PRIMARY KEY ("CountsheetType");

ALTER TABLE ONLY "TLookupMonths"
    ADD CONSTRAINT "TLookupMonths_pkey" PRIMARY KEY ("SeasonCode");

ALTER TABLE ONLY "TLookupUncertaintyType"
    ADD CONSTRAINT "TLookupUncertaintyType_pkey" PRIMARY KEY ("Uncertainty");

ALTER TABLE ONLY "TMCRNames"
    ADD CONSTRAINT "TMCRNames_pkey" PRIMARY KEY ("CODE");

ALTER TABLE ONLY "TMCRSummaryData"
    ADD CONSTRAINT "TMCRSummaryData_pkey" PRIMARY KEY ("CODE");

ALTER TABLE ONLY "TPeriods"
    ADD CONSTRAINT "TPeriods_pkey" PRIMARY KEY ("PeriodCODE");

ALTER TABLE ONLY "TRDBCodes"
    ADD CONSTRAINT "TRDBCodes_pkey" PRIMARY KEY ("RDBCode");

ALTER TABLE ONLY "TRDBSystems"
    ADD CONSTRAINT "TRDBSystems_pkey" PRIMARY KEY ("RDBSystemCode");

ALTER TABLE ONLY "TRDB"
    ADD CONSTRAINT "TRDB_pkey" PRIMARY KEY ("RDBCODE");

ALTER TABLE ONLY "TSample"
    ADD CONSTRAINT "TSample_pkey" PRIMARY KEY ("SampleCODE");

ALTER TABLE ONLY "TSiteOtherProxies"
    ADD CONSTRAINT "TSiteOtherProxies_pkey" PRIMARY KEY ("OtherProxyID");

ALTER TABLE ONLY "TSiteRef"
    ADD CONSTRAINT "TSiteRef_pkey" PRIMARY KEY ("SiteRefCODE");

ALTER TABLE ONLY "TSite"
    ADD CONSTRAINT "TSite_pkey" PRIMARY KEY ("SiteCODE");

ALTER TABLE ONLY "TSpeciesAssociations"
    ADD CONSTRAINT "TSpeciesAssociations_pkey" PRIMARY KEY ("SpeciesAssociationID");

ALTER TABLE ONLY "TSynonymNotes"
    ADD CONSTRAINT "TSynonymNotes_pkey" PRIMARY KEY ("SynonymNoteCODE");

ALTER TABLE ONLY "TSynonym"
    ADD CONSTRAINT "TSynonym_pkey" PRIMARY KEY ("SynonymCODE");

ALTER TABLE ONLY "TTaxoNotes"
    ADD CONSTRAINT "TTaxoNotes_pkey" PRIMARY KEY ("TaxoNoteCODE");

CREATE INDEX "BuildVersionBugsdata_Component_idx" ON "BuildVersionBugsdata" USING btree ("Component");

CREATE INDEX "INDEX_IAuthority_idx" ON "INDEX" USING btree ("AUTHORITY");

CREATE INDEX "INDEX_IFamily_idx" ON "INDEX" USING btree ("FAMILY");

CREATE INDEX "INDEX_IGenus_idx" ON "INDEX" USING btree ("GENUS");

CREATE INDEX "INDEX_ISpecies_idx" ON "INDEX" USING btree ("SPECIES");

CREATE INDEX "TAttributes_PrimaryKey_idx" ON "TAttributes" USING btree ("CODE");

CREATE INDEX "TBiblio_REFERENCE_idx" ON "TBiblio" USING btree ("REFERENCE");

CREATE INDEX "TBiology_PrimaryKey_idx" ON "TBiology" USING btree ("CODE");

CREATE INDEX "TBiology_RefKey_idx" ON "TBiology" USING btree ("Ref");

CREATE INDEX "TCountry_CountryCode_idx" ON "TCountry" USING btree ("CountryCode");

CREATE INDEX "TCountsheet_ICountsheetName_idx" ON "TCountsheet" USING btree ("CountsheetName");

CREATE INDEX "TCountsheet_ISiteCODE_idx" ON "TCountsheet" USING btree ("SiteCODE");

CREATE INDEX "TCountsheet_SheetContext_idx" ON "TCountsheet" USING btree ("SheetContext");

CREATE INDEX "TCountsheet_SheetType_idx" ON "TCountsheet" USING btree ("SheetType");

CREATE INDEX "TDatesCalendar_Date_idx" ON "TDatesCalendar" USING btree ("Date");

CREATE INDEX "TDatesCalendar_TDatesCalenderSampleCODE_idx" ON "TDatesCalendar" USING btree ("SampleCODE");

CREATE INDEX "TDatesMethods_IdxSortOrder_idx" ON "TDatesMethods" USING btree ("SortOrder");

CREATE INDEX "TDatesPeriod_IndexPeriod_idx" ON "TDatesPeriod" USING btree ("PeriodCODE");

CREATE INDEX "TDatesPeriod_TDatesPeriodSampleCODE_idx" ON "TDatesPeriod" USING btree ("SampleCODE");

CREATE INDEX "TDatesRadio_IndexAge_idx" ON "TDatesRadio" USING btree ("Date");

CREATE INDEX "TDatesRadio_IndexMethod_idx" ON "TDatesRadio" USING btree ("DatingMethod");

CREATE INDEX "TDatesRadio_LabNr_idx" ON "TDatesRadio" USING btree ("LabNr");

CREATE INDEX "TDatesRadio_SampleCODE_idx" ON "TDatesRadio" USING btree ("SampleCODE");

CREATE INDEX "TDatesRadio_TDatesRadioLabID_idx" ON "TDatesRadio" USING btree ("LabID");

CREATE INDEX "TDistrib_PrimaryKey_idx" ON "TDistrib" USING btree ("CODE");

CREATE INDEX "TDistrib_RefKey_idx" ON "TDistrib" USING btree ("Ref");

CREATE INDEX "TEcoBugs_IdxCODE_idx" ON "TEcoBugs" USING btree ("CODE");

CREATE INDEX "TEcoBugs_IdxEcoCODE_idx" ON "TEcoBugs" USING btree ("BugsEcoCODE");

CREATE INDEX "TEcoDefBugs_IdxSortOrder_idx" ON "TEcoDefBugs" USING btree ("SortOrder");

CREATE INDEX "TEcoDefKoch_BugsKochCode_idx" ON "TEcoDefKoch" USING btree ("BugsKochCode");

CREATE INDEX "TEcoDefKoch_KochCode_idx" ON "TEcoDefKoch" USING btree ("KochCode");

CREATE INDEX "TEcoKoch_CODE_idx" ON "TEcoKoch" USING btree ("CODE");

CREATE INDEX "TEcoKoch_RobinsonCODE_idx" ON "TEcoKoch" USING btree ("BugsKochCode");

CREATE INDEX "TFossilUncertainty_FossilBugsCODE_idx" ON "TFossilUncertainty" USING btree ("FossilBugsCODE");

CREATE INDEX "TFossil_CODE_idx" ON "TFossil" USING btree ("CODE");

CREATE INDEX "TFossil_FossilBugsCODE_idx" ON "TFossil" USING btree ("FossilBugsCODE");

CREATE INDEX "TFossil_SampleCODE_idx" ON "TFossil" USING btree ("SampleCODE");

CREATE INDEX "TINDEXReplacements_CODE_idx" ON "TINDEXReplacements" USING btree ("CODE");

CREATE INDEX "TINDEXReplacements_SiteCODE_idx" ON "TINDEXReplacements" USING btree ("SiteCODE");

CREATE INDEX "TKeys_PrimaryKey_idx" ON "TKeys" USING btree ("CODE");

CREATE INDEX "TKeys_RefKey_idx" ON "TKeys" USING btree ("Ref");

CREATE INDEX "TLab_IndexLabName_idx" ON "TLab" USING btree ("Labname");

CREATE INDEX "TLab_Indexemail_idx" ON "TLab" USING btree (email);

CREATE INDEX "TLab_Indexwebsite_idx" ON "TLab" USING btree ("Website");

CREATE INDEX "TLab_LabID_idx" ON "TLab" USING btree ("LabID");

CREATE INDEX "TLookupCountsheetContext_SortOrder_idx" ON "TLookupCountsheetContext" USING btree ("SortOrder");

CREATE INDEX "TLookupCountsheetTypes_SortOrder_idx" ON "TLookupCountsheetTypes" USING btree ("SortOrder");

CREATE INDEX "TLookupMonths_SortOrder_idx" ON "TLookupMonths" USING btree ("SortOrder");

CREATE INDEX "TLookupUncertaintyType_SortOrder_idx" ON "TLookupUncertaintyType" USING btree ("SortOrder");

CREATE INDEX "TMCRNames_tempCODE_idx" ON "TMCRNames" USING btree ("tempCODE");

CREATE INDEX "TPeriods_IndexBegin_idx" ON "TPeriods" USING btree ("Begin");

CREATE INDEX "TPeriods_IndexEnd_idx" ON "TPeriods" USING btree ("End");

CREATE INDEX "TRDBCodes_IndexSystem_idx" ON "TRDBCodes" USING btree ("RDBSystemCode");

CREATE INDEX "TRDBCodes_RDBCode_idx" ON "TRDBCodes" USING btree ("RDBCode");

CREATE INDEX "TRDBSystems_CountryCode_idx" ON "TRDBSystems" USING btree ("CountryCode");

CREATE INDEX "TRDB_CountryCode_idx" ON "TRDB" USING btree ("CountryCode");

CREATE INDEX "TRDB_PrimaryKey_idx" ON "TRDB" USING btree ("CODE");

CREATE INDEX "TRDB_TRDBRDBCode_idx" ON "TRDB" USING btree ("RDBCode");

CREATE INDEX "TSample_CountsheetCODE_idx" ON "TSample" USING btree ("CountsheetCODE");

CREATE INDEX "TSample_SiteCODE_idx" ON "TSample" USING btree ("SiteCODE");

CREATE INDEX "TSeasonActiveAdult_CODE_idx" ON "TSeasonActiveAdult" USING btree ("CODE");

CREATE INDEX "TSeasonActiveAdult_CountryID_idx" ON "TSeasonActiveAdult" USING btree ("CountryCode");

CREATE INDEX "TSeasonActiveAdult_HSeason_idx" ON "TSeasonActiveAdult" USING btree ("HSeason");

CREATE INDEX "TSiteOtherProxies_OtherProxyID_idx" ON "TSiteOtherProxies" USING btree ("OtherProxyID");

CREATE INDEX "TSiteOtherProxies_TSiteRefSiteCODE_idx" ON "TSiteOtherProxies" USING btree ("SiteCODE");

CREATE INDEX "TSiteRef_TSiteRefSiteCODE_idx" ON "TSiteRef" USING btree ("SiteCODE");

CREATE INDEX "TSite_IDBy_idx" ON "TSite" USING btree ("IDBy");

CREATE INDEX "TSite_SiteCODE_idx" ON "TSite" USING btree ("SiteCODE");

CREATE INDEX "TSite_SiteName_idx" ON "TSite" USING btree ("SiteName");

CREATE INDEX "TSpeciesAssociations_AssociatedSpeciesCODE_idx" ON "TSpeciesAssociations" USING btree ("AssociatedSpeciesCODE");

CREATE INDEX "TSpeciesAssociations_AssociationType_idx" ON "TSpeciesAssociations" USING btree ("AssociationType");

CREATE INDEX "TSpeciesAssociations_CODE_idx" ON "TSpeciesAssociations" USING btree ("CODE");

CREATE INDEX "TSpeciesAssociations_RefKey_idx" ON "TSpeciesAssociations" USING btree ("Ref");

CREATE INDEX "TSpeciesAssociations_SpeciesAssociationID_idx" ON "TSpeciesAssociations" USING btree ("SpeciesAssociationID");

CREATE INDEX "TSynonymNotes_CODE_idx" ON "TSynonymNotes" USING btree ("CODE");

CREATE INDEX "TSynonymNotes_Ref_idx" ON "TSynonymNotes" USING btree ("Notes");

CREATE INDEX "TSynonym_CODE_idx" ON "TSynonym" USING btree ("CODE");

CREATE INDEX "TSynonym_Ref_idx" ON "TSynonym" USING btree ("Ref");

CREATE INDEX "TTaxoNotes_PrimaryKey_idx" ON "TTaxoNotes" USING btree ("CODE");

CREATE INDEX "TTaxoNotes_RefKey_idx" ON "TTaxoNotes" USING btree ("Ref");

CREATE INDEX "TbirmBEETLEdat_ICODE_idx" ON "TbirmBEETLEdat" USING btree ("CODE");

CREATE INDEX "TbirmBEETLEdat_IMCRRow_idx" ON "TbirmBEETLEdat" USING btree ("MCRRow");

CREATE INDEX jlr_dep_from1 ON jailer_dependency USING btree (r_entitygraph, assoc, from_pk, from_pk0, from_pk1);

CREATE INDEX jlr_dep_to1 ON jailer_dependency USING btree (r_entitygraph, to_pk, to_pk0, to_pk1);

CREATE INDEX jlr_enty_brthdy ON jailer_entity USING btree (r_entitygraph, type, birthday);

CREATE INDEX jlr_enty_upk1 ON jailer_entity USING btree (r_entitygraph, pk, pk0, pk1, type, birthday);

CREATE INDEX jlr_pk_set1 ON jailer_set USING btree (set_id, pk, pk0, pk1, type);

ALTER TABLE ONLY "TAttributes"
    ADD CONSTRAINT "TAttributes_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TBiology"
    ADD CONSTRAINT "TBiology_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TBiology"
    ADD CONSTRAINT "TBiology_Ref_fk" FOREIGN KEY ("Ref") REFERENCES "TBiblio"("REFERENCE") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TCountsheet"
    ADD CONSTRAINT "TCountsheet_SiteCODE_fk" FOREIGN KEY ("SiteCODE") REFERENCES "TSite"("SiteCODE") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TDatesCalendar"
    ADD CONSTRAINT "TDatesCalendar_SampleCODE_fk" FOREIGN KEY ("SampleCODE") REFERENCES "TSample"("SampleCODE") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TDatesPeriod"
    ADD CONSTRAINT "TDatesPeriod_SampleCODE_fk" FOREIGN KEY ("SampleCODE") REFERENCES "TSample"("SampleCODE") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TDatesRadio"
    ADD CONSTRAINT "TDatesRadio_SampleCODE_fk" FOREIGN KEY ("SampleCODE") REFERENCES "TSample"("SampleCODE") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TDistrib"
    ADD CONSTRAINT "TDistrib_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TDistrib"
    ADD CONSTRAINT "TDistrib_Ref_fk" FOREIGN KEY ("Ref") REFERENCES "TBiblio"("REFERENCE") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TEcoBugs"
    ADD CONSTRAINT "TEcoBugs_BugsEcoCODE_fk" FOREIGN KEY ("BugsEcoCODE") REFERENCES "TEcoDefBugs"("BugsEcoCODE") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TEcoBugs"
    ADD CONSTRAINT "TEcoBugs_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TEcoDefKoch"
    ADD CONSTRAINT "TEcoDefKoch_KochGroup_fk" FOREIGN KEY ("KochGroup") REFERENCES "TEcoDefGroups"("EcoGroupCode") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TEcoKoch"
    ADD CONSTRAINT "TEcoKoch_BugsKochCode_fk" FOREIGN KEY ("BugsKochCode") REFERENCES "TEcoDefKoch"("BugsKochCode") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TEcoKoch"
    ADD CONSTRAINT "TEcoKoch_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TFossilUncertainty"
    ADD CONSTRAINT "TFossilUncertainty_FossilBugsCODE_fk" FOREIGN KEY ("FossilBugsCODE") REFERENCES "TFossil"("FossilBugsCODE");

ALTER TABLE ONLY "TFossil"
    ADD CONSTRAINT "TFossil_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TFossil"
    ADD CONSTRAINT "TFossil_SampleCODE_fk" FOREIGN KEY ("SampleCODE") REFERENCES "TSample"("SampleCODE") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TKeys"
    ADD CONSTRAINT "TKeys_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TMCRNames"
    ADD CONSTRAINT "TMCRNames_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE;

ALTER TABLE ONLY "TMCRSummaryData"
    ADD CONSTRAINT "TMCRSummaryData_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE;

ALTER TABLE ONLY "TPeriods"
    ADD CONSTRAINT "TPeriods_PeriodRef_fk" FOREIGN KEY ("PeriodRef") REFERENCES "TBiblio"("REFERENCE") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TRDBSystems"
    ADD CONSTRAINT "TRDBSystems_CountryCode_fk" FOREIGN KEY ("CountryCode") REFERENCES "TCountry"("CountryCode") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TRDBSystems"
    ADD CONSTRAINT "TRDBSystems_Ref_fk" FOREIGN KEY ("Ref") REFERENCES "TBiblio"("REFERENCE") ON UPDATE CASCADE;

ALTER TABLE ONLY "TRDB"
    ADD CONSTRAINT "TRDB_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TRDB"
    ADD CONSTRAINT "TRDB_CountryCode_fk" FOREIGN KEY ("CountryCode") REFERENCES "TCountry"("CountryCode") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TSample"
    ADD CONSTRAINT "TSample_SiteCODE_fk" FOREIGN KEY ("SiteCODE") REFERENCES "TSite"("SiteCODE") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TSeasonActiveAdult"
    ADD CONSTRAINT "TSeasonActiveAdult_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TSeasonActiveAdult"
    ADD CONSTRAINT "TSeasonActiveAdult_CountryCode_fk" FOREIGN KEY ("CountryCode") REFERENCES "TCountry"("CountryCode") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TSiteOtherProxies"
    ADD CONSTRAINT "TSiteOtherProxies_SiteCODE_fk" FOREIGN KEY ("SiteCODE") REFERENCES "TSite"("SiteCODE") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TSiteRef"
    ADD CONSTRAINT "TSiteRef_Ref_fk" FOREIGN KEY ("Ref") REFERENCES "TBiblio"("REFERENCE") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TSiteRef"
    ADD CONSTRAINT "TSiteRef_SiteCODE_fk" FOREIGN KEY ("SiteCODE") REFERENCES "TSite"("SiteCODE") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TSpeciesAssociations"
    ADD CONSTRAINT "TSpeciesAssociations_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TSynonym"
    ADD CONSTRAINT "TSynonym_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TTaxoNotes"
    ADD CONSTRAINT "TTaxoNotes_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TTaxoNotes"
    ADD CONSTRAINT "TTaxoNotes_Ref_fk" FOREIGN KEY ("Ref") REFERENCES "TBiblio"("REFERENCE") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "TbirmBEETLEdat"
    ADD CONSTRAINT "TbirmBEETLEdat_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE;

ALTER TABLE ONLY "TCountsheet"
    ADD CONSTRAINT "fk_TCountsheet_TLookupCountsheetContext_1" FOREIGN KEY ("SheetContext") REFERENCES "TLookupCountsheetContext"("SheetContext");

ALTER TABLE ONLY "TCountsheet"
    ADD CONSTRAINT "fk_TCountsheet_TLookupCountsheetTypes_1" FOREIGN KEY ("SheetType") REFERENCES "TLookupCountsheetTypes"("CountsheetType");

ALTER TABLE ONLY "TDatesCalendar"
    ADD CONSTRAINT "fk_TDatesCalendar_TDatesMethods_1" FOREIGN KEY ("DatingMethod") REFERENCES "TDatesMethods"("Abbrev");

ALTER TABLE ONLY "TDatesPeriod"
    ADD CONSTRAINT "fk_TDatesPeriod_TDatesMethods_1" FOREIGN KEY ("DatingMethod") REFERENCES "TDatesMethods"("Abbrev");

ALTER TABLE ONLY "TDatesPeriod"
    ADD CONSTRAINT "fk_TDatesPeriod_TPeriods_1" FOREIGN KEY ("PeriodCODE") REFERENCES "TPeriods"("PeriodCODE");

ALTER TABLE ONLY "TDatesRadio"
    ADD CONSTRAINT "fk_TDatesRadio_TDatesMethods_1" FOREIGN KEY ("DatingMethod") REFERENCES "TDatesMethods"("Abbrev");

ALTER TABLE ONLY "TDatesRadio"
    ADD CONSTRAINT "fk_TDatesRadio_TLab_1" FOREIGN KEY ("LabID") REFERENCES "TLab"("LabID") ON DELETE CASCADE;

ALTER TABLE ONLY "TKeys"
    ADD CONSTRAINT "fk_TKeys_TBiblio_1" FOREIGN KEY ("Ref") REFERENCES "TBiblio"("REFERENCE");

ALTER TABLE ONLY "TRDBCodes"
    ADD CONSTRAINT "fk_TRDBCodes_TRDBSystems_1" FOREIGN KEY ("RDBSystemCode") REFERENCES "TRDBSystems"("RDBSystemCode");

ALTER TABLE ONLY "TRDB"
    ADD CONSTRAINT "fk_TRDB_TRDBCodes_1" FOREIGN KEY ("RDBCode") REFERENCES "TRDBCodes"("RDBCode");

ALTER TABLE ONLY "TSample"
    ADD CONSTRAINT "fk_TSample_TCountsheet_1" FOREIGN KEY ("CountsheetCODE") REFERENCES "TCountsheet"("CountsheetCODE");

ALTER TABLE ONLY "TSpeciesAssociations"
    ADD CONSTRAINT "fk_TSpeciesAssociations_INDEX_1" FOREIGN KEY ("AssociatedSpeciesCODE") REFERENCES "INDEX"("CODE");

ALTER TABLE ONLY "TSpeciesAssociations"
    ADD CONSTRAINT "fk_TSpeciesAssociations_TBiblio_1" FOREIGN KEY ("Ref") REFERENCES "TBiblio"("REFERENCE");

ALTER TABLE ONLY "TSynonym"
    ADD CONSTRAINT "fk_TSynonym_TBiblio_1" FOREIGN KEY ("Ref") REFERENCES "TBiblio"("REFERENCE");
-- Completed on 2023-02-09 09:11:31

--
-- PostgreSQL database dump complete
--
