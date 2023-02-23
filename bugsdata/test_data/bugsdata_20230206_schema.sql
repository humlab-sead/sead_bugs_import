-- ----------------------------------------------------------
-- MDB Tools - A library for reading MS Access database files
-- Copyright (C) 2000-2011 Brian Bruns and others.
-- Files in libmdb are licensed under LGPL and the utilities under
-- the GPL, see COPYING.LIB and COPYING files respectively.
-- Check out http://mdbtools.sourceforge.net
-- ----------------------------------------------------------

SET client_encoding = 'UTF-8';

CREATE TABLE "BuildVersionBugsdata"
 (
	"BuildCounter"			SERIAL, 
	"Component"			VARCHAR (20), 
	"DevLevel"			VARCHAR (20), 
	"Version"			VARCHAR (20), 
	"Build"			VARCHAR (20), 
	"Implemented"			TIMESTAMP WITHOUT TIME ZONE, 
	"VersionChanges"			TEXT
);
COMMENT ON COLUMN "BuildVersionBugsdata"."Component" IS 'CEP, MCR, Stats';
COMMENT ON COLUMN "BuildVersionBugsdata"."DevLevel" IS 'Prototype;Alpha;Beta;Release';
COMMENT ON COLUMN "BuildVersionBugsdata"."Version" IS 'Version';
COMMENT ON COLUMN "BuildVersionBugsdata"."Build" IS 'Sub-version build - fine definition of version';
COMMENT ON COLUMN "BuildVersionBugsdata"."Implemented" IS 'Date of implementation for this build';
COMMENT ON COLUMN "BuildVersionBugsdata"."VersionChanges" IS 'Significant improvement over previous version';
COMMENT ON TABLE "BuildVersionBugsdata" IS 'version changes info for bugsdata.mdb only';

-- CREATE INDEXES ...
CREATE INDEX "BuildVersionBugsdata_Component_idx" ON "BuildVersionBugsdata" ("Component");
ALTER TABLE "BuildVersionBugsdata" ADD CONSTRAINT "BuildVersionBugsdata_pkey" PRIMARY KEY ("BuildCounter");

CREATE TABLE "INDEX"
 (
	"CODE" VARCHAR(20), 
	"FAMILY"			VARCHAR (100), 
	"GENUS"			VARCHAR (100), 
	"SPECIES"			VARCHAR (70), 
	"AUTHORITY"			VARCHAR (70)
);

-- CREATE INDEXES ...
CREATE INDEX "INDEX_IAuthority_idx" ON "INDEX" ("AUTHORITY");
CREATE INDEX "INDEX_IFamily_idx" ON "INDEX" ("FAMILY");
CREATE INDEX "INDEX_IGenus_idx" ON "INDEX" ("GENUS");
CREATE INDEX "INDEX_ISpecies_idx" ON "INDEX" ("SPECIES");
ALTER TABLE "INDEX" ADD CONSTRAINT "INDEX_pkey" PRIMARY KEY ("CODE");

CREATE TABLE "TAttributes"
 (
	"CODE" VARCHAR(20), 
	"AttribType"			VARCHAR (100), 
	"AttribMeasure"			VARCHAR (40), 
	"Value"			REAL, 
	"AttribUnits"			VARCHAR (20)
);
COMMENT ON COLUMN "TAttributes"."AttribType" IS 'eg. length;width';
COMMENT ON COLUMN "TAttributes"."AttribMeasure" IS 'eg. min;max;mean';
COMMENT ON COLUMN "TAttributes"."Value" IS 'eg. 5;7';
COMMENT ON COLUMN "TAttributes"."AttribUnits" IS 'eg. mm;cm';

-- CREATE INDEXES ...
CREATE INDEX "TAttributes_PrimaryKey_idx" ON "TAttributes" ("CODE");

CREATE TABLE "TBiblio"
 (
	"REFERENCE"			VARCHAR (120), 
	"AUTHOR"			VARCHAR (510), 
	"TITLE"			TEXT, 
	"Notes"			TEXT
);

-- CREATE INDEXES ...
ALTER TABLE "TBiblio" ADD CONSTRAINT "TBiblio_pkey" PRIMARY KEY ("REFERENCE");
CREATE INDEX "TBiblio_REFERENCE_idx" ON "TBiblio" ("REFERENCE");

CREATE TABLE "TbirmBEETLEdat"
 (
	"Field1"			VARCHAR (510), 
	"Field2"			VARCHAR (510), 
	"Field3"			VARCHAR (510), 
	"Field4"			VARCHAR (510), 
	"Field5"			VARCHAR (510), 
	"Field6"			VARCHAR (510), 
	"Field7"			VARCHAR (510), 
	"Field8"			VARCHAR (510), 
	"Field9"			VARCHAR (510), 
	"Field10"			VARCHAR (510), 
	"Field11"			VARCHAR (510), 
	"Field12"			VARCHAR (510), 
	"Field13"			VARCHAR (510), 
	"Field14"			VARCHAR (510), 
	"Field15"			VARCHAR (510), 
	"Field16"			VARCHAR (510), 
	"Field17"			VARCHAR (510), 
	"Field18"			VARCHAR (510), 
	"Field19"			VARCHAR (510), 
	"Field20"			VARCHAR (510), 
	"Field21"			VARCHAR (510), 
	"Field22"			VARCHAR (510), 
	"Field23"			VARCHAR (510), 
	"Field24"			VARCHAR (510), 
	"Field25"			VARCHAR (510), 
	"Field26"			VARCHAR (510), 
	"Field27"			VARCHAR (510), 
	"Field28"			VARCHAR (510), 
	"Field29"			VARCHAR (510), 
	"Field30"			VARCHAR (510), 
	"Field31"			VARCHAR (510), 
	"Field32"			VARCHAR (510), 
	"Field33"			VARCHAR (510), 
	"Field34"			VARCHAR (510), 
	"Field35"			VARCHAR (510), 
	"Field36"			VARCHAR (510), 
	"Field37"			VARCHAR (510), 
	"Field38"			VARCHAR (510), 
	"Field39"			VARCHAR (510), 
	"Field40"			VARCHAR (510), 
	"Field41"			VARCHAR (510), 
	"Field42"			VARCHAR (510), 
	"Field43"			VARCHAR (510), 
	"Field44"			VARCHAR (510), 
	"Field45"			VARCHAR (510), 
	"Field46"			VARCHAR (510), 
	"Field47"			VARCHAR (510), 
	"Field48"			VARCHAR (510), 
	"Field49"			VARCHAR (510), 
	"Field50"			VARCHAR (510), 
	"Field51"			VARCHAR (510), 
	"Field52"			VARCHAR (510), 
	"Field53"			VARCHAR (510), 
	"Field54"			VARCHAR (510), 
	"Field55"			VARCHAR (510), 
	"Field56"			VARCHAR (510), 
	"Field57"			VARCHAR (510), 
	"Field58"			VARCHAR (510), 
	"Field59"			VARCHAR (510), 
	"Field60"			VARCHAR (510), 
	"MCRRow"			INTEGER, 
	"CODE" VARCHAR(20)
);
COMMENT ON TABLE "TbirmBEETLEdat" IS 'MCR Envelope data';

-- CREATE INDEXES ...
CREATE INDEX "TbirmBEETLEdat_ICODE_idx" ON "TbirmBEETLEdat" ("CODE");
CREATE INDEX "TbirmBEETLEdat_IMCRRow_idx" ON "TbirmBEETLEdat" ("MCRRow");

CREATE TABLE "TCountry"
 (
	"CountryCode"			VARCHAR (6), 
	"Country"			VARCHAR (100)
);

-- CREATE INDEXES ...
CREATE INDEX "TCountry_CountryCode_idx" ON "TCountry" ("CountryCode");
ALTER TABLE "TCountry" ADD CONSTRAINT "TCountry_pkey" PRIMARY KEY ("CountryCode");

CREATE TABLE "TCountsheet"
 (
	"CountsheetCODE"			VARCHAR (20), 
	"CountsheetName"			VARCHAR (200), 
	"SiteCODE"			VARCHAR (20), 
	"SheetContext"			VARCHAR (50), 
	"SheetType"			VARCHAR (50)
);
COMMENT ON COLUMN "TCountsheet"."CountsheetCODE" IS 'COUNnnnnnn BugsCEP unique code for countsheet - allows identification of samples and fossils to sheet';
COMMENT ON COLUMN "TCountsheet"."SheetContext" IS 'Archaeological contexts;Stratigraphic sequence;Pitfall traps;Other Modern;Other Palaeo';
COMMENT ON COLUMN "TCountsheet"."SheetType" IS 'Abundances;Presence/Absence;Partial abundances;Other';

-- CREATE INDEXES ...
CREATE INDEX "TCountsheet_ICountsheetName_idx" ON "TCountsheet" ("CountsheetName");
CREATE INDEX "TCountsheet_ISiteCODE_idx" ON "TCountsheet" ("SiteCODE");
ALTER TABLE "TCountsheet" ADD CONSTRAINT "TCountsheet_pkey" PRIMARY KEY ("CountsheetCODE");
CREATE INDEX "TCountsheet_SheetContext_idx" ON "TCountsheet" ("SheetContext");
CREATE INDEX "TCountsheet_SheetType_idx" ON "TCountsheet" ("SheetType");

CREATE TABLE "TDatesCalendar"
 (
	"SampleCODE"			VARCHAR (20), 
	"Uncertainty"			VARCHAR (20), 
	"CalendarCODE"			VARCHAR (20), 
	"Date"			INTEGER, 
	"BCADBP"			VARCHAR (20), 
	"DatingMethod"			VARCHAR (100), 
	"Notes"			TEXT
);
COMMENT ON COLUMN "TDatesCalendar"."Uncertainty" IS 'eg. ca.;?';
COMMENT ON COLUMN "TDatesCalendar"."CalendarCODE" IS 'link to date in TDatesCalendarData';
COMMENT ON COLUMN "TDatesCalendar"."Date" IS 'date, integer, eg. 100 or 1000';
COMMENT ON COLUMN "TDatesCalendar"."DatingMethod" IS 'eg. Stratigraphic;Artifact typology etc.';

-- CREATE INDEXES ...
CREATE INDEX "TDatesCalendar_Date_idx" ON "TDatesCalendar" ("Date");
ALTER TABLE "TDatesCalendar" ADD CONSTRAINT "TDatesCalendar_pkey" PRIMARY KEY ("CalendarCODE");
CREATE INDEX "TDatesCalendar_TDatesCalenderSampleCODE_idx" ON "TDatesCalendar" ("SampleCODE");

CREATE TABLE "TDatesMethods"
 (
	"Abbrev"			VARCHAR (100), 
	"Method"			VARCHAR (100), 
	"Type"			VARCHAR (100), 
	"SortOrder"			INTEGER
);
COMMENT ON COLUMN "TDatesMethods"."Abbrev" IS 'Relates to DatingMethod fields in TDates... tables';
COMMENT ON COLUMN "TDatesMethods"."Method" IS 'C14,K/Ar,U-Series,Lichen, Stratigraphic...';
COMMENT ON COLUMN "TDatesMethods"."Type" IS 'Radiometric, Calender, Period';

-- CREATE INDEXES ...
CREATE INDEX "TDatesMethods_IdxSortOrder_idx" ON "TDatesMethods" ("SortOrder");
ALTER TABLE "TDatesMethods" ADD CONSTRAINT "TDatesMethods_pkey" PRIMARY KEY ("Abbrev");

CREATE TABLE "TDatesRadio"
 (
	"DateCODE"			VARCHAR (20), 
	"SampleCODE"			VARCHAR (20), 
	"LabNr"			VARCHAR (40), 
	"Uncertainty"			VARCHAR (20), 
	"Date"			INTEGER, 
	"AgeErrorOrPlusError"			INTEGER, 
	"AgeErrorMinus"			INTEGER, 
	"DatingMethod"			VARCHAR (30), 
	"MaterialType"			VARCHAR (100), 
	"LabID"			VARCHAR (20), 
	"Notes"			TEXT
);
COMMENT ON COLUMN "TDatesRadio"."LabNr" IS 'Lab''s identifier for sample';
COMMENT ON COLUMN "TDatesRadio"."Date" IS 'Age in radiocarbon years';
COMMENT ON COLUMN "TDatesRadio"."AgeErrorOrPlusError" IS '+/- OR Plus error for some old dates';
COMMENT ON COLUMN "TDatesRadio"."AgeErrorMinus" IS 'Minus error for some old dates';
COMMENT ON COLUMN "TDatesRadio"."DatingMethod" IS 'eg. C14;K/Ar;U-series;Tl;Lichen;Dendro;Varve from list...';
COMMENT ON COLUMN "TDatesRadio"."MaterialType" IS 'eg. Seed;Insect;Wood;Charcoal...';
COMMENT ON COLUMN "TDatesRadio"."LabID" IS 'Identifier for analysis lab from list...';
COMMENT ON TABLE "TDatesRadio" IS 'CEP Dates Radiometric';

-- CREATE INDEXES ...
CREATE INDEX "TDatesRadio_IndexAge_idx" ON "TDatesRadio" ("Date");
CREATE INDEX "TDatesRadio_IndexMethod_idx" ON "TDatesRadio" ("DatingMethod");
CREATE INDEX "TDatesRadio_LabNr_idx" ON "TDatesRadio" ("LabNr");
ALTER TABLE "TDatesRadio" ADD CONSTRAINT "TDatesRadio_pkey" PRIMARY KEY ("DateCODE");
CREATE INDEX "TDatesRadio_SampleCODE_idx" ON "TDatesRadio" ("SampleCODE");
CREATE INDEX "TDatesRadio_TDatesRadioLabID_idx" ON "TDatesRadio" ("LabID");

CREATE TABLE "TDatesTypes"
 (
	"Type"			VARCHAR (100)
);
COMMENT ON COLUMN "TDatesTypes"."Type" IS 'Radiometric, Calender, Period';

-- CREATE INDEXES ...
ALTER TABLE "TDatesTypes" ADD CONSTRAINT "TDatesTypes_pkey" PRIMARY KEY ("Type");

CREATE TABLE "TDistrib"
 (
	"CODE" VARCHAR(20), 
	"Ref"			VARCHAR (120), 
	"Data"			TEXT
);

-- CREATE INDEXES ...
CREATE INDEX "TDistrib_PrimaryKey_idx" ON "TDistrib" ("CODE");
CREATE INDEX "TDistrib_RefKey_idx" ON "TDistrib" ("Ref");

CREATE TABLE "TEcoBugs"
 (
	"CODE" VARCHAR(20) NOT NULL, 
	"BugsEcoCODE"			VARCHAR (14)
);
COMMENT ON TABLE "TEcoBugs" IS 'Stats BugsEcoCodes for species';

-- CREATE INDEXES ...
CREATE INDEX "TEcoBugs_IdxCODE_idx" ON "TEcoBugs" ("CODE");
CREATE INDEX "TEcoBugs_IdxEcoCODE_idx" ON "TEcoBugs" ("BugsEcoCODE");

CREATE TABLE "TEcoDefBugs"
 (
	"SortOrder"			INTEGER NOT NULL, 
	"BugsEcoCODE"			VARCHAR (14), 
	"Definition"			VARCHAR (100), 
	"Notes"			TEXT, 
	"EcoLabel"			VARCHAR (100)
);
COMMENT ON COLUMN "TEcoDefBugs"."EcoLabel" IS 'Abreviated label for graph titles';
COMMENT ON TABLE "TEcoDefBugs" IS 'Stats BugsEcocode definitions';

-- CREATE INDEXES ...
CREATE INDEX "TEcoDefBugs_IdxSortOrder_idx" ON "TEcoDefBugs" ("SortOrder");
ALTER TABLE "TEcoDefBugs" ADD CONSTRAINT "TEcoDefBugs_pkey" PRIMARY KEY ("BugsEcoCODE");

CREATE TABLE "TEcoDefGroups"
 (
	"EcoGroupCode"			VARCHAR (50), 
	"EcoName"			VARCHAR (100)
);
COMMENT ON TABLE "TEcoDefGroups" IS 'Stats KochEcocode groups';

-- CREATE INDEXES ...
ALTER TABLE "TEcoDefGroups" ADD CONSTRAINT "TEcoDefGroups_pkey" PRIMARY KEY ("EcoGroupCode");

CREATE TABLE "TEcoKoch"
 (
	"CODE" VARCHAR(20) NOT NULL, 
	"BugsKochCode"			VARCHAR (10)
);
COMMENT ON TABLE "TEcoKoch" IS 'Stats Koch EcoCodes for species';

-- CREATE INDEXES ...
CREATE INDEX "TEcoKoch_CODE_idx" ON "TEcoKoch" ("CODE");
CREATE INDEX "TEcoKoch_RobinsonCODE_idx" ON "TEcoKoch" ("BugsKochCode");

CREATE TABLE "TFossil"
 (
	"FossilBugsCODE"			VARCHAR (20), 
	"CODE" VARCHAR(20), 
	"SampleCODE"			VARCHAR (20), 
	"Abundance"			INTEGER
);
COMMENT ON COLUMN "TFossil"."Abundance" IS 'abundance from countsheet... NO LONGER TEXT (prev. keep as text for now incase of presence/absence text data (eventually replace these with "1" for presence))';

-- CREATE INDEXES ...
CREATE INDEX "TFossil_CODE_idx" ON "TFossil" ("CODE");
CREATE INDEX "TFossil_FossilBugsCODE_idx" ON "TFossil" ("FossilBugsCODE");
ALTER TABLE "TFossil" ADD CONSTRAINT "TFossil_pkey" PRIMARY KEY ("FossilBugsCODE");
CREATE INDEX "TFossil_SampleCODE_idx" ON "TFossil" ("SampleCODE");

CREATE TABLE "TFossilUncertainty"
 (
	"FossilBugsCODE"			VARCHAR (20), 
	"Uncertainty"			VARCHAR (510)
);
COMMENT ON COLUMN "TFossilUncertainty"."Uncertainty" IS 'e.g. May be Sp A, uncertain id.';

-- CREATE INDEXES ...
CREATE INDEX "TFossilUncertainty_FossilBugsCODE_idx" ON "TFossilUncertainty" ("FossilBugsCODE");
ALTER TABLE "TFossilUncertainty" ADD CONSTRAINT "TFossilUncertainty_pkey" PRIMARY KEY ("FossilBugsCODE");

CREATE TABLE "TINDEXReplacements"
 (
	"CODE" VARCHAR(20), 
	"ReplacedName"			VARCHAR (200), 
	"SiteCODE"			VARCHAR (20)
);
COMMENT ON COLUMN "TINDEXReplacements"."CODE" IS 'Species to replace the XLS name with';
COMMENT ON COLUMN "TINDEXReplacements"."ReplacedName" IS 'Species name from XLS file';
COMMENT ON COLUMN "TINDEXReplacements"."SiteCODE" IS 'Site where the replacement was first made';
COMMENT ON TABLE "TINDEXReplacements" IS 'All logs manual replacements when converting species lists';

-- CREATE INDEXES ...
CREATE INDEX "TINDEXReplacements_CODE_idx" ON "TINDEXReplacements" ("CODE");
ALTER TABLE "TINDEXReplacements" ADD CONSTRAINT "TINDEXReplacements_pkey" PRIMARY KEY ("ReplacedName");
CREATE INDEX "TINDEXReplacements_SiteCODE_idx" ON "TINDEXReplacements" ("SiteCODE");

CREATE TABLE "TKeys"
 (
	"CODE" VARCHAR(20), 
	"Ref"			VARCHAR (120), 
	"Data"			TEXT
);

-- CREATE INDEXES ...
CREATE INDEX "TKeys_PrimaryKey_idx" ON "TKeys" ("CODE");
CREATE INDEX "TKeys_RefKey_idx" ON "TKeys" ("Ref");

CREATE TABLE "TLab"
 (
	"LabID"			VARCHAR (20) NOT NULL, 
	"Labname"			VARCHAR (200), 
	"Address"			VARCHAR (510), 
	"Country"			VARCHAR (60), 
	"Telephone"			VARCHAR (100), 
	"Website"			TEXT, 
	"email"			TEXT
);
COMMENT ON COLUMN "TLab"."LabID" IS 'Official LabID - * indicates that lab is no longer operating, data from http://www.radiocarbon.org/Info/labcodes.html';
COMMENT ON COLUMN "TLab"."Labname" IS 'Note that LabID altered for LU (St. Petersberg) due to clash with Lu Lund';
COMMENT ON TABLE "TLab" IS 'CEP Dates Radiometric analysis labs';

-- CREATE INDEXES ...
CREATE INDEX "TLab_Indexemail_idx" ON "TLab" ("email");
CREATE INDEX "TLab_IndexLabName_idx" ON "TLab" ("Labname");
CREATE INDEX "TLab_Indexwebsite_idx" ON "TLab" ("Website");
CREATE INDEX "TLab_LabID_idx" ON "TLab" ("LabID");
ALTER TABLE "TLab" ADD CONSTRAINT "TLab_pkey" PRIMARY KEY ("LabID");

CREATE TABLE "TLookupCountsheetContext"
 (
	"SheetContext"			VARCHAR (50), 
	"SortOrder"			INTEGER
);

-- CREATE INDEXES ...
ALTER TABLE "TLookupCountsheetContext" ADD CONSTRAINT "TLookupCountsheetContext_pkey" PRIMARY KEY ("SheetContext");
CREATE INDEX "TLookupCountsheetContext_SortOrder_idx" ON "TLookupCountsheetContext" ("SortOrder");

CREATE TABLE "TLookupMonths"
 (
	"SeasonCode"			VARCHAR (6), 
	"SeasonName"			VARCHAR (100), 
	"SortOrder"			INTEGER
);

-- CREATE INDEXES ...
ALTER TABLE "TLookupMonths" ADD CONSTRAINT "TLookupMonths_pkey" PRIMARY KEY ("SeasonCode");
CREATE INDEX "TLookupMonths_SortOrder_idx" ON "TLookupMonths" ("SortOrder");

CREATE TABLE "TLookupUncertaintyType"
 (
	"Uncertainty"			VARCHAR (30), 
	"SortOrder"			INTEGER
);

-- CREATE INDEXES ...
ALTER TABLE "TLookupUncertaintyType" ADD CONSTRAINT "TLookupUncertaintyType_pkey" PRIMARY KEY ("Uncertainty");
CREATE INDEX "TLookupUncertaintyType_SortOrder_idx" ON "TLookupUncertaintyType" ("SortOrder");

CREATE TABLE "TMCRNames"
 (
	"MCRNameTrim"			VARCHAR (160), 
	"CompareStatus"			VARCHAR (510), 
	"CODE" VARCHAR(20) NOT NULL, 
	"tempCODE"			DOUBLE PRECISION, 
	"MCRNumber"			INTEGER, 
	"MCRName"			VARCHAR (400)
);
COMMENT ON TABLE "TMCRNames" IS 'MCR species index with MCRBirm names &  number for comparison';

-- CREATE INDEXES ...
ALTER TABLE "TMCRNames" ADD CONSTRAINT "TMCRNames_pkey" PRIMARY KEY ("CODE");
CREATE INDEX "TMCRNames_tempCODE_idx" ON "TMCRNames" ("tempCODE");

CREATE TABLE "TMCRSummaryData"
 (
	"CODE" VARCHAR(20), 
	"TMaxLo"			INTEGER, 
	"TMaxHi"			INTEGER, 
	"TMinLo"			INTEGER, 
	"TMinHi"			INTEGER, 
	"TRangeLo"			INTEGER, 
	"TRangeHi"			INTEGER, 
	"COGMidTMax"			INTEGER, 
	"COGMidTRange"			INTEGER
);
COMMENT ON TABLE "TMCRSummaryData" IS 'MCR data summaries - max ranges used in prediction';

-- CREATE INDEXES ...
ALTER TABLE "TMCRSummaryData" ADD CONSTRAINT "TMCRSummaryData_pkey" PRIMARY KEY ("CODE");

CREATE TABLE "TPeriods"
 (
	"PeriodCODE"			VARCHAR (40), 
	"PeriodName"			VARCHAR (56), 
	"PeriodType"			VARCHAR (100), 
	"PeriodDesc"			VARCHAR (510), 
	"PeriodRef"			VARCHAR (100), 
	"PeriodGeog"			VARCHAR (200), 
	"Begin"			INTEGER, 
	"BeginBCAD"			VARCHAR (8), 
	"End"			INTEGER, 
	"EndBCAD"			VARCHAR (8), 
	"YearsType"			VARCHAR (26)
);
COMMENT ON COLUMN "TPeriods"."PeriodCODE" IS 'Abbreviation (CODE) in Bugs';
COMMENT ON COLUMN "TPeriods"."PeriodName" IS 'Name within type group eg. 5a for OIS-5a';
COMMENT ON COLUMN "TPeriods"."PeriodType" IS 'Type of period definition - eg Oxygen Isotope, Glacial/Interglacial/Stadial... NOTE: Check Bugs periods, and replace globally with correct/standardised through db';
COMMENT ON COLUMN "TPeriods"."PeriodDesc" IS 'Description, including extra definition info';
COMMENT ON COLUMN "TPeriods"."PeriodRef" IS 'Reference for period''s chronological definition';
COMMENT ON COLUMN "TPeriods"."PeriodGeog" IS 'Geographical scope of period name';
COMMENT ON COLUMN "TPeriods"."Begin" IS 'Start of period (oldest)';
COMMENT ON COLUMN "TPeriods"."BeginBCAD" IS '& BP';
COMMENT ON COLUMN "TPeriods"."End" IS 'End of period (most recent)';
COMMENT ON COLUMN "TPeriods"."YearsType" IS 'Time scale used (C14;Calendar;Calibrated - give curve name in PeriodDesc)';
COMMENT ON TABLE "TPeriods" IS 'CEP Dates Periods mater list';

-- CREATE INDEXES ...
CREATE INDEX "TPeriods_IndexBegin_idx" ON "TPeriods" ("Begin");
CREATE INDEX "TPeriods_IndexEnd_idx" ON "TPeriods" ("End");
ALTER TABLE "TPeriods" ADD CONSTRAINT "TPeriods_pkey" PRIMARY KEY ("PeriodCODE");

CREATE TABLE "TRDB"
 (
	"CODE" VARCHAR(20), 
	"CountryCode"			VARCHAR (6), 
	"RDBCode"			INTEGER
);

-- CREATE INDEXES ...
CREATE INDEX "TRDB_CountryCode_idx" ON "TRDB" ("CountryCode");
CREATE INDEX "TRDB_PrimaryKey_idx" ON "TRDB" ("CODE");
CREATE INDEX "TRDB_TRDBRDBCode_idx" ON "TRDB" ("RDBCode");

CREATE TABLE "TRDBCodes"
 (
	"RDBCode"			SERIAL, 
	"Category"			VARCHAR (8), 
	"RDBDefinition"			VARCHAR (400), 
	"RDBSystemCode"			INTEGER
);
COMMENT ON COLUMN "TRDBCodes"."RDBDefinition" IS 'full name and explanation';

-- CREATE INDEXES ...
CREATE INDEX "TRDBCodes_IndexSystem_idx" ON "TRDBCodes" ("RDBSystemCode");
ALTER TABLE "TRDBCodes" ADD CONSTRAINT "TRDBCodes_pkey" PRIMARY KEY ("RDBCode");
CREATE INDEX "TRDBCodes_RDBCode_idx" ON "TRDBCodes" ("RDBCode");

CREATE TABLE "TRDBSystems"
 (
	"RDBSystemCode"			SERIAL, 
	"RDBSystem"			VARCHAR (20), 
	"RDBVersion"			VARCHAR (20), 
	"RDBSystemDate"			INTEGER, 
	"RDBFirstPublished"			INTEGER, 
	"Ref"			VARCHAR (120), 
	"CountryCode"			VARCHAR (6)
);

-- CREATE INDEXES ...
CREATE INDEX "TRDBSystems_CountryCode_idx" ON "TRDBSystems" ("CountryCode");
ALTER TABLE "TRDBSystems" ADD CONSTRAINT "TRDBSystems_pkey" PRIMARY KEY ("RDBSystemCode");

CREATE TABLE "TSeasonActiveAdult"
 (
	"CODE" VARCHAR(20), 
	"HSeason"			VARCHAR (6), 
	"CountryCode"			VARCHAR (6)
);

-- CREATE INDEXES ...
CREATE INDEX "TSeasonActiveAdult_CODE_idx" ON "TSeasonActiveAdult" ("CODE");
CREATE INDEX "TSeasonActiveAdult_CountryID_idx" ON "TSeasonActiveAdult" ("CountryCode");
CREATE INDEX "TSeasonActiveAdult_HSeason_idx" ON "TSeasonActiveAdult" ("HSeason");

CREATE TABLE "TSite"
 (
	"SiteCODE"			VARCHAR (20), 
	"SiteName"			VARCHAR (100), 
	"Region"			VARCHAR (80), 
	"Country"			VARCHAR (40), 
	"NGR"			VARCHAR (20), 
	"LatDD"			REAL, 
	"LongDD"			REAL, 
	"Alt"			REAL, 
	"IDBy"			VARCHAR (510), 
	"Interp"			VARCHAR (510), 
	"Specimens"			VARCHAR (510)
);
COMMENT ON COLUMN "TSite"."LatDD" IS 'single data type gives ca. 1m accuracy';
COMMENT ON COLUMN "TSite"."LongDD" IS '- is west + is east';

-- CREATE INDEXES ...
CREATE INDEX "TSite_IDBy_idx" ON "TSite" ("IDBy");
ALTER TABLE "TSite" ADD CONSTRAINT "TSite_pkey" PRIMARY KEY ("SiteCODE");
CREATE INDEX "TSite_SiteCODE_idx" ON "TSite" ("SiteCODE");
CREATE INDEX "TSite_SiteName_idx" ON "TSite" ("SiteName");

CREATE TABLE "TSiteOtherProxies"
 (
	"OtherProxyID"			SERIAL, 
	"SiteCODE"			VARCHAR (20), 
	"HasPollen"			SMALLINT, 
	"HasPlantMacro"			SMALLINT, 
	"HasDiatoms"			SMALLINT, 
	"HasChironomids"			SMALLINT, 
	"HasSoilChemistry"			SMALLINT, 
	"HasIsotopes"			SMALLINT, 
	"HasAnimalBones"			SMALLINT, 
	"HasArchaeology"			SMALLINT, 
	"HasMolluscs"			SMALLINT
);
COMMENT ON COLUMN "TSiteOtherProxies"."HasSoilChemistry" IS 'includes physical properties (i.e. Phosphates, LOI, MS, Ph...)';
COMMENT ON COLUMN "TSiteOtherProxies"."HasIsotopes" IS 'General, any isotope work';
COMMENT ON COLUMN "TSiteOtherProxies"."HasArchaeology" IS 'Either dating or wood analyses';

-- CREATE INDEXES ...
CREATE INDEX "TSiteOtherProxies_OtherProxyID_idx" ON "TSiteOtherProxies" ("OtherProxyID");
ALTER TABLE "TSiteOtherProxies" ADD CONSTRAINT "TSiteOtherProxies_pkey" PRIMARY KEY ("OtherProxyID");
CREATE INDEX "TSiteOtherProxies_TSiteRefSiteCODE_idx" ON "TSiteOtherProxies" ("SiteCODE");

CREATE TABLE "TSiteRef"
 (
	"SiteCODE"			VARCHAR (20), 
	"Ref"			VARCHAR (120)
);

-- CREATE INDEXES ...
CREATE INDEX "TSiteRef_TSiteRefSiteCODE_idx" ON "TSiteRef" ("SiteCODE");

CREATE TABLE "TSpeciesAssociations"
 (
	"SpeciesAssociationID"			SERIAL, 
	"CODE" VARCHAR(20), 
	"AssociatedSpeciesCODE"			DOUBLE PRECISION, 
	"AssociationType"			VARCHAR (100), 
	"Ref"			VARCHAR (120)
);

-- CREATE INDEXES ...
CREATE INDEX "TSpeciesAssociations_AssociatedSpeciesCODE_idx" ON "TSpeciesAssociations" ("AssociatedSpeciesCODE");
CREATE INDEX "TSpeciesAssociations_AssociationType_idx" ON "TSpeciesAssociations" ("AssociationType");
CREATE INDEX "TSpeciesAssociations_CODE_idx" ON "TSpeciesAssociations" ("CODE");
ALTER TABLE "TSpeciesAssociations" ADD CONSTRAINT "TSpeciesAssociations_pkey" PRIMARY KEY ("SpeciesAssociationID");
CREATE INDEX "TSpeciesAssociations_RefKey_idx" ON "TSpeciesAssociations" ("Ref");
CREATE INDEX "TSpeciesAssociations_SpeciesAssociationID_idx" ON "TSpeciesAssociations" ("SpeciesAssociationID");

CREATE TABLE "TSynonymNotes"
 (
	"CODE" VARCHAR(20) NOT NULL, 
	"Notes"			VARCHAR (510)
);
COMMENT ON TABLE "TSynonymNotes" IS 'not implemented - needs SynonymCODE system.\015\012use Notes fields in TSynonym for now';

-- CREATE INDEXES ...
CREATE INDEX "TSynonymNotes_CODE_idx" ON "TSynonymNotes" ("CODE");
CREATE INDEX "TSynonymNotes_Ref_idx" ON "TSynonymNotes" ("Notes");

CREATE TABLE "TTaxoNotes"
 (
	"CODE" VARCHAR(20), 
	"Ref"			VARCHAR (120), 
	"Data"			TEXT
);
COMMENT ON TABLE "TTaxoNotes" IS 'CEP holds taxonomic notes for species, incl. ref';

-- CREATE INDEXES ...
CREATE INDEX "TTaxoNotes_PrimaryKey_idx" ON "TTaxoNotes" ("CODE");
CREATE INDEX "TTaxoNotes_RefKey_idx" ON "TTaxoNotes" ("Ref");

CREATE TABLE "TBiology"
 (
	"CODE" VARCHAR(20), 
	"Ref"			VARCHAR (120), 
	"Data"			TEXT
);

-- CREATE INDEXES ...
CREATE INDEX "TBiology_PrimaryKey_idx" ON "TBiology" ("CODE");
CREATE INDEX "TBiology_RefKey_idx" ON "TBiology" ("Ref");

CREATE TABLE "TDatesPeriod"
 (
	"PeriodDateCODE"			VARCHAR (20), 
	"SampleCODE"			VARCHAR (20), 
	"Uncertainty"			VARCHAR (20), 
	"PeriodCODE"			VARCHAR (40), 
	"DatingMethod"			VARCHAR (100), 
	"Notes"			TEXT
);
COMMENT ON COLUMN "TDatesPeriod"."PeriodDateCODE" IS 'Unique identifier for period dates records';
COMMENT ON COLUMN "TDatesPeriod"."Uncertainty" IS 'eg. ca.;?';
COMMENT ON COLUMN "TDatesPeriod"."PeriodCODE" IS 'eg. BA;Rom;H etc.';
COMMENT ON COLUMN "TDatesPeriod"."DatingMethod" IS 'eg. Stratigraphic;Artifact typology etc.';

-- CREATE INDEXES ...
CREATE INDEX "TDatesPeriod_IndexPeriod_idx" ON "TDatesPeriod" ("PeriodCODE");
ALTER TABLE "TDatesPeriod" ADD CONSTRAINT "TDatesPeriod_pkey" PRIMARY KEY ("PeriodDateCODE");
CREATE INDEX "TDatesPeriod_TDatesPeriodSampleCODE_idx" ON "TDatesPeriod" ("SampleCODE");

CREATE TABLE "TEcoDefKoch"
 (
	"BugsKochCode"			VARCHAR (10), 
	"KochCode"			VARCHAR (4), 
	"FullName"			VARCHAR (100), 
	"KochGroup"			VARCHAR (50), 
	"Description"			VARCHAR (510), 
	"Notes"			TEXT
);
COMMENT ON TABLE "TEcoDefKoch" IS 'Stats KochEcocode definitions';

-- CREATE INDEXES ...
CREATE INDEX "TEcoDefKoch_BugsKochCode_idx" ON "TEcoDefKoch" ("BugsKochCode");
CREATE INDEX "TEcoDefKoch_KochCode_idx" ON "TEcoDefKoch" ("KochCode");
ALTER TABLE "TEcoDefKoch" ADD CONSTRAINT "TEcoDefKoch_pkey" PRIMARY KEY ("BugsKochCode");

CREATE TABLE "TLookupCountsheetTypes"
 (
	"CountsheetType"			VARCHAR (50), 
	"SortOrder"			INTEGER
);

-- CREATE INDEXES ...
ALTER TABLE "TLookupCountsheetTypes" ADD CONSTRAINT "TLookupCountsheetTypes_pkey" PRIMARY KEY ("CountsheetType");
CREATE INDEX "TLookupCountsheetTypes_SortOrder_idx" ON "TLookupCountsheetTypes" ("SortOrder");

CREATE TABLE "TSample"
 (
	"SampleCODE"			VARCHAR (20), 
	"SiteCODE"			VARCHAR (20), 
	"X"			VARCHAR (100), 
	"Y"			VARCHAR (100), 
	"ZorDepthTop"			DOUBLE PRECISION, 
	"ZorDepthBot"			DOUBLE PRECISION, 
	"RefNrContext"			VARCHAR (100), 
	"CountsheetCODE"			VARCHAR (20)
);
COMMENT ON COLUMN "TSample"."ZorDepthTop" IS 'Top of sample in column/sequence';
COMMENT ON COLUMN "TSample"."ZorDepthBot" IS 'Bottom of sample in column/sequence';

-- CREATE INDEXES ...
CREATE INDEX "TSample_CountsheetCODE_idx" ON "TSample" ("CountsheetCODE");
ALTER TABLE "TSample" ADD CONSTRAINT "TSample_pkey" PRIMARY KEY ("SampleCODE");
CREATE INDEX "TSample_SiteCODE_idx" ON "TSample" ("SiteCODE");

CREATE TABLE "TSynonym"
 (
	"CODE" VARCHAR(20) NOT NULL, 
	"SynGenus"			VARCHAR (100), 
	"SynSpecies"			VARCHAR (100), 
	"SynAuthority"			VARCHAR (100), 
	"Ref"			VARCHAR (120), 
	"Notes"			VARCHAR (510)
);
COMMENT ON COLUMN "TSynonym"."Notes" IS 'Notes on synynomy - will eventually move to TSynonymNotes when SynonymCODE system set up';

-- CREATE INDEXES ...
CREATE INDEX "TSynonym_CODE_idx" ON "TSynonym" ("CODE");
CREATE INDEX "TSynonym_Ref_idx" ON "TSynonym" ("Ref");


-- CREATE Relationships ...
-- Relationship from "TDatesPeriod" ("DatingMethod") to "TDatesMethods"("Abbrev") does not enforce integrity.
ALTER TABLE "TSiteRef" ADD CONSTRAINT "TSiteRef_Ref_fk" FOREIGN KEY ("Ref") REFERENCES "TBiblio"("REFERENCE") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "TRDBSystems" ADD CONSTRAINT "TRDBSystems_Ref_fk" FOREIGN KEY ("Ref") REFERENCES "TBiblio"("REFERENCE") ON UPDATE CASCADE;
ALTER TABLE "TBiology" ADD CONSTRAINT "TBiology_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "TEcoKoch" ADD CONSTRAINT "TEcoKoch_BugsKochCode_fk" FOREIGN KEY ("BugsKochCode") REFERENCES "TEcoDefKoch"("BugsKochCode") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "TDatesCalendar" ADD CONSTRAINT "TDatesCalendar_SampleCODE_fk" FOREIGN KEY ("SampleCODE") REFERENCES "TSample"("SampleCODE") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "TKeys" ADD CONSTRAINT "TKeys_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;
-- Relationship from "TSeasonActiveAdult" ("HSeason") to "TLookupMonths"("SeasonCode") does not enforce integrity.
-- Relationship from "TDatesRadio" ("LabID") to "TLab"("LabID") does not enforce integrity.
ALTER TABLE "TSpeciesAssociations" ADD CONSTRAINT "TSpeciesAssociations_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;
-- Relationship from "TDatesRadio" ("DatingMethod") to "TDatesMethods"("Abbrev") does not enforce integrity.
ALTER TABLE "TSeasonActiveAdult" ADD CONSTRAINT "TSeasonActiveAdult_CountryCode_fk" FOREIGN KEY ("CountryCode") REFERENCES "TCountry"("CountryCode") ON UPDATE CASCADE ON DELETE CASCADE;
-- Relationship from "TDatesCalendar" ("DatingMethod") to "TDatesMethods"("Abbrev") does not enforce integrity.
ALTER TABLE "TPeriods" ADD CONSTRAINT "TPeriods_PeriodRef_fk" FOREIGN KEY ("PeriodRef") REFERENCES "TBiblio"("REFERENCE") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "TAttributes" ADD CONSTRAINT "TAttributes_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "TRDB" ADD CONSTRAINT "TRDB_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "TFossil" ADD CONSTRAINT "TFossil_SampleCODE_fk" FOREIGN KEY ("SampleCODE") REFERENCES "TSample"("SampleCODE") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "TEcoDefKoch" ADD CONSTRAINT "TEcoDefKoch_KochGroup_fk" FOREIGN KEY ("KochGroup") REFERENCES "TEcoDefGroups"("EcoGroupCode") ON UPDATE CASCADE ON DELETE CASCADE;
-- Relationship from "TRDBCodes" ("RDBSystemCode") to "TRDBSystems"("RDBSystemCode") does not enforce integrity.
-- Relationship from "TRDB" ("RDBCode") to "TRDBCodes"("RDBCode") does not enforce integrity.
-- Relationship from "TKeys" ("Ref") to "TBiblio"("REFERENCE") does not enforce integrity.
ALTER TABLE "TEcoKoch" ADD CONSTRAINT "TEcoKoch_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "TFossil" ADD CONSTRAINT "TFossil_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "TCountsheet" ADD CONSTRAINT "TCountsheet_SiteCODE_fk" FOREIGN KEY ("SiteCODE") REFERENCES "TSite"("SiteCODE") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "TMCRSummaryData" ADD CONSTRAINT "TMCRSummaryData_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE;
ALTER TABLE "TSiteRef" ADD CONSTRAINT "TSiteRef_SiteCODE_fk" FOREIGN KEY ("SiteCODE") REFERENCES "TSite"("SiteCODE") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "TSeasonActiveAdult" ADD CONSTRAINT "TSeasonActiveAdult_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "TFossilUncertainty" ADD CONSTRAINT "TFossilUncertainty_FossilBugsCODE_fk" FOREIGN KEY ("FossilBugsCODE") REFERENCES "TFossil"("FossilBugsCODE");
-- Relationship from "TSynonymNotes" ("CODE") to "TSynonym"("CODE") does not enforce integrity.
ALTER TABLE "TDatesPeriod" ADD CONSTRAINT "TDatesPeriod_SampleCODE_fk" FOREIGN KEY ("SampleCODE") REFERENCES "TSample"("SampleCODE") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "TBiology" ADD CONSTRAINT "TBiology_Ref_fk" FOREIGN KEY ("Ref") REFERENCES "TBiblio"("REFERENCE") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "TSiteOtherProxies" ADD CONSTRAINT "TSiteOtherProxies_SiteCODE_fk" FOREIGN KEY ("SiteCODE") REFERENCES "TSite"("SiteCODE") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "TMCRNames" ADD CONSTRAINT "TMCRNames_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE;
ALTER TABLE "TSample" ADD CONSTRAINT "TSample_SiteCODE_fk" FOREIGN KEY ("SiteCODE") REFERENCES "TSite"("SiteCODE") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "TEcoBugs" ADD CONSTRAINT "TEcoBugs_BugsEcoCODE_fk" FOREIGN KEY ("BugsEcoCODE") REFERENCES "TEcoDefBugs"("BugsEcoCODE") ON UPDATE CASCADE ON DELETE CASCADE;
-- Relationship from "TDatesPeriod" ("PeriodCODE") to "TPeriods"("PeriodCODE") does not enforce integrity.
ALTER TABLE "TDatesRadio" ADD CONSTRAINT "TDatesRadio_SampleCODE_fk" FOREIGN KEY ("SampleCODE") REFERENCES "TSample"("SampleCODE") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "TSynonym" ADD CONSTRAINT "TSynonym_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "TTaxoNotes" ADD CONSTRAINT "TTaxoNotes_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "TEcoBugs" ADD CONSTRAINT "TEcoBugs_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "TTaxoNotes" ADD CONSTRAINT "TTaxoNotes_Ref_fk" FOREIGN KEY ("Ref") REFERENCES "TBiblio"("REFERENCE") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "TRDBSystems" ADD CONSTRAINT "TRDBSystems_CountryCode_fk" FOREIGN KEY ("CountryCode") REFERENCES "TCountry"("CountryCode") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "TRDB" ADD CONSTRAINT "TRDB_CountryCode_fk" FOREIGN KEY ("CountryCode") REFERENCES "TCountry"("CountryCode") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "TbirmBEETLEdat" ADD CONSTRAINT "TbirmBEETLEdat_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE;
ALTER TABLE "TDistrib" ADD CONSTRAINT "TDistrib_CODE_fk" FOREIGN KEY ("CODE") REFERENCES "INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "TDistrib" ADD CONSTRAINT "TDistrib_Ref_fk" FOREIGN KEY ("Ref") REFERENCES "TBiblio"("REFERENCE") ON UPDATE CASCADE ON DELETE CASCADE;
-- Relationship from "TSample" ("CountsheetCODE") to "TCountsheet"("CountsheetCODE") does not enforce integrity.
