--
-- PostgreSQL database dump
--

-- Dumped from database version 11.2 (Debian 11.2-1.pgdg90+1)
-- Dumped by pg_dump version 11.17 (Debian 11.17-1.pgdg90+1)

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

DROP DATABASE IF EXISTS bugsdata_samp000546_extra;
--
-- Name: bugsdata_samp000546_extra; Type: DATABASE; Schema: -; Owner: -
--

CREATE DATABASE bugsdata_samp000546_extra WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'en_US.utf8' LC_CTYPE = 'en_US.utf8';


\connect bugsdata_samp000546_extra

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

SET default_with_oids = false;

--
-- Name: BuildVersionBugsdata; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."BuildVersionBugsdata" (
    "BuildCounter" integer NOT NULL,
    "Component" character varying(20),
    "DevLevel" character varying(20),
    "Version" character varying(20),
    "Build" character varying(20),
    "Implemented" timestamp without time zone,
    "VersionChanges" text
);


--
-- Name: TABLE "BuildVersionBugsdata"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public."BuildVersionBugsdata" IS 'version changes info for bugsdata.mdb only';


--
-- Name: COLUMN "BuildVersionBugsdata"."Component"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."BuildVersionBugsdata"."Component" IS 'CEP, MCR, Stats';


--
-- Name: COLUMN "BuildVersionBugsdata"."DevLevel"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."BuildVersionBugsdata"."DevLevel" IS 'Prototype;Alpha;Beta;Release';


--
-- Name: COLUMN "BuildVersionBugsdata"."Version"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."BuildVersionBugsdata"."Version" IS 'Version';


--
-- Name: COLUMN "BuildVersionBugsdata"."Build"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."BuildVersionBugsdata"."Build" IS 'Sub-version build - fine definition of version';


--
-- Name: COLUMN "BuildVersionBugsdata"."Implemented"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."BuildVersionBugsdata"."Implemented" IS 'Date of implementation for this build';


--
-- Name: COLUMN "BuildVersionBugsdata"."VersionChanges"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."BuildVersionBugsdata"."VersionChanges" IS 'Significant improvement over previous version';


--
-- Name: BuildVersionBugsdata_BuildCounter_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public."BuildVersionBugsdata_BuildCounter_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: BuildVersionBugsdata_BuildCounter_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public."BuildVersionBugsdata_BuildCounter_seq" OWNED BY public."BuildVersionBugsdata"."BuildCounter";


--
-- Name: INDEX; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."INDEX" (
    "CODE" character varying(20) NOT NULL,
    "FAMILY" character varying(100),
    "GENUS" character varying(100),
    "SPECIES" character varying(70),
    "AUTHORITY" character varying(70)
);


--
-- Name: TAttributes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TAttributes" (
    "CODE" character varying(20),
    "AttribType" character varying(100),
    "AttribMeasure" character varying(40),
    "Value" real,
    "AttribUnits" character varying(20)
);


--
-- Name: COLUMN "TAttributes"."AttribType"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TAttributes"."AttribType" IS 'eg. length;width';


--
-- Name: COLUMN "TAttributes"."AttribMeasure"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TAttributes"."AttribMeasure" IS 'eg. min;max;mean';


--
-- Name: COLUMN "TAttributes"."Value"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TAttributes"."Value" IS 'eg. 5;7';


--
-- Name: COLUMN "TAttributes"."AttribUnits"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TAttributes"."AttribUnits" IS 'eg. mm;cm';


--
-- Name: TBiblio; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TBiblio" (
    "REFERENCE" character varying(120) NOT NULL,
    "AUTHOR" character varying(510),
    "TITLE" text,
    "Notes" text
);


--
-- Name: TBiology; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TBiology" (
    "CODE" character varying(20),
    "Ref" character varying(120),
    "Data" text
);


--
-- Name: TCountry; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TCountry" (
    "CountryCode" character varying(6) NOT NULL,
    "Country" character varying(100)
);


--
-- Name: TCountsheet; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TCountsheet" (
    "CountsheetCODE" character varying(20) NOT NULL,
    "CountsheetName" character varying(200),
    "SiteCODE" character varying(20),
    "SheetContext" character varying(50),
    "SheetType" character varying(50)
);


--
-- Name: COLUMN "TCountsheet"."CountsheetCODE"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TCountsheet"."CountsheetCODE" IS 'COUNnnnnnn BugsCEP unique code for countsheet - allows identification of samples and fossils to sheet';


--
-- Name: COLUMN "TCountsheet"."SheetContext"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TCountsheet"."SheetContext" IS 'Archaeological contexts;Stratigraphic sequence;Pitfall traps;Other Modern;Other Palaeo';


--
-- Name: COLUMN "TCountsheet"."SheetType"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TCountsheet"."SheetType" IS 'Abundances;Presence/Absence;Partial abundances;Other';


--
-- Name: TDatesCalendar; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TDatesCalendar" (
    "SampleCODE" character varying(20),
    "Uncertainty" character varying(20),
    "CalendarCODE" character varying(20) NOT NULL,
    "Date" integer,
    "BCADBP" character varying(20),
    "DatingMethod" character varying(100),
    "Notes" text
);


--
-- Name: COLUMN "TDatesCalendar"."Uncertainty"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TDatesCalendar"."Uncertainty" IS 'eg. ca.;?';


--
-- Name: COLUMN "TDatesCalendar"."CalendarCODE"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TDatesCalendar"."CalendarCODE" IS 'link to date in TDatesCalendarData';


--
-- Name: COLUMN "TDatesCalendar"."Date"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TDatesCalendar"."Date" IS 'date, integer, eg. 100 or 1000';


--
-- Name: COLUMN "TDatesCalendar"."DatingMethod"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TDatesCalendar"."DatingMethod" IS 'eg. Stratigraphic;Artifact typology etc.';


--
-- Name: TDatesMethods; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TDatesMethods" (
    "Abbrev" character varying(100) NOT NULL,
    "Method" character varying(100),
    "Type" character varying(100),
    "SortOrder" integer
);


--
-- Name: COLUMN "TDatesMethods"."Abbrev"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TDatesMethods"."Abbrev" IS 'Relates to DatingMethod fields in TDates... tables';


--
-- Name: COLUMN "TDatesMethods"."Method"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TDatesMethods"."Method" IS 'C14,K/Ar,U-Series,Lichen, Stratigraphic...';


--
-- Name: COLUMN "TDatesMethods"."Type"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TDatesMethods"."Type" IS 'Radiometric, Calender, Period';


--
-- Name: TDatesPeriod; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TDatesPeriod" (
    "PeriodDateCODE" character varying(20) NOT NULL,
    "SampleCODE" character varying(20),
    "Uncertainty" character varying(20),
    "PeriodCODE" character varying(40),
    "DatingMethod" character varying(100),
    "Notes" text
);


--
-- Name: COLUMN "TDatesPeriod"."PeriodDateCODE"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TDatesPeriod"."PeriodDateCODE" IS 'Unique identifier for period dates records';


--
-- Name: COLUMN "TDatesPeriod"."Uncertainty"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TDatesPeriod"."Uncertainty" IS 'eg. ca.;?';


--
-- Name: COLUMN "TDatesPeriod"."PeriodCODE"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TDatesPeriod"."PeriodCODE" IS 'eg. BA;Rom;H etc.';


--
-- Name: COLUMN "TDatesPeriod"."DatingMethod"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TDatesPeriod"."DatingMethod" IS 'eg. Stratigraphic;Artifact typology etc.';


--
-- Name: TDatesRadio; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TDatesRadio" (
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


--
-- Name: TABLE "TDatesRadio"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public."TDatesRadio" IS 'CEP Dates Radiometric';


--
-- Name: COLUMN "TDatesRadio"."LabNr"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TDatesRadio"."LabNr" IS 'Lab''s identifier for sample';


--
-- Name: COLUMN "TDatesRadio"."Date"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TDatesRadio"."Date" IS 'Age in radiocarbon years';


--
-- Name: COLUMN "TDatesRadio"."AgeErrorOrPlusError"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TDatesRadio"."AgeErrorOrPlusError" IS '+/- OR Plus error for some old dates';


--
-- Name: COLUMN "TDatesRadio"."AgeErrorMinus"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TDatesRadio"."AgeErrorMinus" IS 'Minus error for some old dates';


--
-- Name: COLUMN "TDatesRadio"."DatingMethod"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TDatesRadio"."DatingMethod" IS 'eg. C14;K/Ar;U-series;Tl;Lichen;Dendro;Varve from list...';


--
-- Name: COLUMN "TDatesRadio"."MaterialType"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TDatesRadio"."MaterialType" IS 'eg. Seed;Insect;Wood;Charcoal...';


--
-- Name: COLUMN "TDatesRadio"."LabID"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TDatesRadio"."LabID" IS 'Identifier for analysis lab from list...';


--
-- Name: TDatesTypes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TDatesTypes" (
    "Type" character varying(100) NOT NULL
);


--
-- Name: COLUMN "TDatesTypes"."Type"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TDatesTypes"."Type" IS 'Radiometric, Calender, Period';


--
-- Name: TDistrib; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TDistrib" (
    "CODE" character varying(20),
    "Ref" character varying(120),
    "Data" text
);


--
-- Name: TEcoBugs; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TEcoBugs" (
    "CODE" character varying(20) NOT NULL,
    "BugsEcoCODE" character varying(14)
);


--
-- Name: TABLE "TEcoBugs"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public."TEcoBugs" IS 'Stats BugsEcoCodes for species';


--
-- Name: TEcoDefBugs; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TEcoDefBugs" (
    "SortOrder" integer NOT NULL,
    "BugsEcoCODE" character varying(14) NOT NULL,
    "Definition" character varying(100),
    "Notes" text,
    "EcoLabel" character varying(100)
);


--
-- Name: TABLE "TEcoDefBugs"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public."TEcoDefBugs" IS 'Stats BugsEcocode definitions';


--
-- Name: COLUMN "TEcoDefBugs"."EcoLabel"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TEcoDefBugs"."EcoLabel" IS 'Abreviated label for graph titles';


--
-- Name: TEcoDefGroups; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TEcoDefGroups" (
    "EcoGroupCode" character varying(50) NOT NULL,
    "EcoName" character varying(100)
);


--
-- Name: TABLE "TEcoDefGroups"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public."TEcoDefGroups" IS 'Stats KochEcocode groups';


--
-- Name: TEcoDefKoch; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TEcoDefKoch" (
    "BugsKochCode" character varying(10) NOT NULL,
    "KochCode" character varying(4),
    "FullName" character varying(100),
    "KochGroup" character varying(50),
    "Description" character varying(510),
    "Notes" text
);


--
-- Name: TABLE "TEcoDefKoch"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public."TEcoDefKoch" IS 'Stats KochEcocode definitions';


--
-- Name: TEcoKoch; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TEcoKoch" (
    "CODE" character varying(20) NOT NULL,
    "BugsKochCode" character varying(10)
);


--
-- Name: TABLE "TEcoKoch"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public."TEcoKoch" IS 'Stats Koch EcoCodes for species';


--
-- Name: TFossil; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TFossil" (
    "FossilBugsCODE" character varying(20) NOT NULL,
    "CODE" character varying(20),
    "SampleCODE" character varying(20),
    "Abundance" integer
);


--
-- Name: COLUMN "TFossil"."Abundance"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TFossil"."Abundance" IS 'abundance from countsheet... NO LONGER TEXT (prev. keep as text for now incase of presence/absence text data (eventually replace these with "1" for presence))';


--
-- Name: TFossilUncertainty; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TFossilUncertainty" (
    "FossilBugsCODE" character varying(20) NOT NULL,
    "Uncertainty" character varying(510)
);


--
-- Name: COLUMN "TFossilUncertainty"."Uncertainty"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TFossilUncertainty"."Uncertainty" IS 'e.g. May be Sp A, uncertain id.';


--
-- Name: TINDEXReplacements; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TINDEXReplacements" (
    "CODE" character varying(20),
    "ReplacedName" character varying(200) NOT NULL,
    "SiteCODE" character varying(20)
);


--
-- Name: TABLE "TINDEXReplacements"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public."TINDEXReplacements" IS 'All logs manual replacements when converting species lists';


--
-- Name: COLUMN "TINDEXReplacements"."CODE"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TINDEXReplacements"."CODE" IS 'Species to replace the XLS name with';


--
-- Name: COLUMN "TINDEXReplacements"."ReplacedName"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TINDEXReplacements"."ReplacedName" IS 'Species name from XLS file';


--
-- Name: COLUMN "TINDEXReplacements"."SiteCODE"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TINDEXReplacements"."SiteCODE" IS 'Site where the replacement was first made';


--
-- Name: TKeys; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TKeys" (
    "CODE" character varying(20),
    "Ref" character varying(120),
    "Data" text
);


--
-- Name: TLab; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TLab" (
    "LabID" character varying(20) NOT NULL,
    "Labname" character varying(200),
    "Address" character varying(510),
    "Country" character varying(60),
    "Telephone" character varying(100),
    "Website" text,
    email text
);


--
-- Name: TABLE "TLab"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public."TLab" IS 'CEP Dates Radiometric analysis labs';


--
-- Name: COLUMN "TLab"."LabID"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TLab"."LabID" IS 'Official LabID - * indicates that lab is no longer operating, data from http://www.radiocarbon.org/Info/labcodes.html';


--
-- Name: COLUMN "TLab"."Labname"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TLab"."Labname" IS 'Note that LabID altered for LU (St. Petersberg) due to clash with Lu Lund';


--
-- Name: TLookupCountsheetContext; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TLookupCountsheetContext" (
    "SheetContext" character varying(50) NOT NULL,
    "SortOrder" integer
);


--
-- Name: TLookupCountsheetTypes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TLookupCountsheetTypes" (
    "CountsheetType" character varying(50) NOT NULL,
    "SortOrder" integer
);


--
-- Name: TLookupMonths; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TLookupMonths" (
    "SeasonCode" character varying(6) NOT NULL,
    "SeasonName" character varying(100),
    "SortOrder" integer
);


--
-- Name: TLookupUncertaintyType; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TLookupUncertaintyType" (
    "Uncertainty" character varying(30) NOT NULL,
    "SortOrder" integer
);


--
-- Name: TMCRNames; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TMCRNames" (
    "MCRNameTrim" character varying(160),
    "CompareStatus" character varying(510),
    "CODE" character varying(20) NOT NULL,
    "tempCODE" double precision,
    "MCRNumber" integer,
    "MCRName" character varying(400)
);


--
-- Name: TABLE "TMCRNames"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public."TMCRNames" IS 'MCR species index with MCRBirm names &  number for comparison';


--
-- Name: TMCRSummaryData; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TMCRSummaryData" (
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


--
-- Name: TABLE "TMCRSummaryData"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public."TMCRSummaryData" IS 'MCR data summaries - max ranges used in prediction';


--
-- Name: TPeriods; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TPeriods" (
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


--
-- Name: TABLE "TPeriods"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public."TPeriods" IS 'CEP Dates Periods mater list';


--
-- Name: COLUMN "TPeriods"."PeriodCODE"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TPeriods"."PeriodCODE" IS 'Abbreviation (CODE) in Bugs';


--
-- Name: COLUMN "TPeriods"."PeriodName"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TPeriods"."PeriodName" IS 'Name within type group eg. 5a for OIS-5a';


--
-- Name: COLUMN "TPeriods"."PeriodType"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TPeriods"."PeriodType" IS 'Type of period definition - eg Oxygen Isotope, Glacial/Interglacial/Stadial... NOTE: Check Bugs periods, and replace globally with correct/standardised through db';


--
-- Name: COLUMN "TPeriods"."PeriodDesc"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TPeriods"."PeriodDesc" IS 'Description, including extra definition info';


--
-- Name: COLUMN "TPeriods"."PeriodRef"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TPeriods"."PeriodRef" IS 'Reference for period''s chronological definition';


--
-- Name: COLUMN "TPeriods"."PeriodGeog"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TPeriods"."PeriodGeog" IS 'Geographical scope of period name';


--
-- Name: COLUMN "TPeriods"."Begin"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TPeriods"."Begin" IS 'Start of period (oldest)';


--
-- Name: COLUMN "TPeriods"."BeginBCAD"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TPeriods"."BeginBCAD" IS '& BP';


--
-- Name: COLUMN "TPeriods"."End"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TPeriods"."End" IS 'End of period (most recent)';


--
-- Name: COLUMN "TPeriods"."YearsType"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TPeriods"."YearsType" IS 'Time scale used (C14;Calendar;Calibrated - give curve name in PeriodDesc)';


--
-- Name: TRDB; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TRDB" (
    "CODE" character varying(20),
    "CountryCode" character varying(6),
    "RDBCode" integer
);


--
-- Name: TRDBCodes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TRDBCodes" (
    "RDBCode" integer NOT NULL,
    "Category" character varying(8),
    "RDBDefinition" character varying(400),
    "RDBSystemCode" integer
);


--
-- Name: COLUMN "TRDBCodes"."RDBDefinition"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TRDBCodes"."RDBDefinition" IS 'full name and explanation';


--
-- Name: TRDBCodes_RDBCode_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public."TRDBCodes_RDBCode_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: TRDBCodes_RDBCode_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public."TRDBCodes_RDBCode_seq" OWNED BY public."TRDBCodes"."RDBCode";


--
-- Name: TRDBSystems; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TRDBSystems" (
    "RDBSystemCode" integer NOT NULL,
    "RDBSystem" character varying(20),
    "RDBVersion" character varying(20),
    "RDBSystemDate" integer,
    "RDBFirstPublished" integer,
    "Ref" character varying(120),
    "CountryCode" character varying(6)
);


--
-- Name: TRDBSystems_RDBSystemCode_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public."TRDBSystems_RDBSystemCode_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: TRDBSystems_RDBSystemCode_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public."TRDBSystems_RDBSystemCode_seq" OWNED BY public."TRDBSystems"."RDBSystemCode";


--
-- Name: TSample; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TSample" (
    "SampleCODE" character varying(20) NOT NULL,
    "SiteCODE" character varying(20),
    "X" character varying(100),
    "Y" character varying(100),
    "ZorDepthTop" double precision,
    "ZorDepthBot" double precision,
    "RefNrContext" character varying(100),
    "CountsheetCODE" character varying(20)
);


--
-- Name: COLUMN "TSample"."ZorDepthTop"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TSample"."ZorDepthTop" IS 'Top of sample in column/sequence';


--
-- Name: COLUMN "TSample"."ZorDepthBot"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TSample"."ZorDepthBot" IS 'Bottom of sample in column/sequence';


--
-- Name: TSeasonActiveAdult; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TSeasonActiveAdult" (
    "CODE" character varying(20),
    "HSeason" character varying(6),
    "CountryCode" character varying(6)
);


--
-- Name: TSite; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TSite" (
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


--
-- Name: COLUMN "TSite"."LatDD"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TSite"."LatDD" IS 'single data type gives ca. 1m accuracy';


--
-- Name: COLUMN "TSite"."LongDD"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TSite"."LongDD" IS '- is west + is east';


--
-- Name: TSiteOtherProxies; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TSiteOtherProxies" (
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


--
-- Name: COLUMN "TSiteOtherProxies"."HasSoilChemistry"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TSiteOtherProxies"."HasSoilChemistry" IS 'includes physical properties (i.e. Phosphates, LOI, MS, Ph...)';


--
-- Name: COLUMN "TSiteOtherProxies"."HasIsotopes"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TSiteOtherProxies"."HasIsotopes" IS 'General, any isotope work';


--
-- Name: COLUMN "TSiteOtherProxies"."HasArchaeology"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TSiteOtherProxies"."HasArchaeology" IS 'Either dating or wood analyses';


--
-- Name: TSiteOtherProxies_OtherProxyID_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public."TSiteOtherProxies_OtherProxyID_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: TSiteOtherProxies_OtherProxyID_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public."TSiteOtherProxies_OtherProxyID_seq" OWNED BY public."TSiteOtherProxies"."OtherProxyID";


--
-- Name: TSiteRef; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TSiteRef" (
    "SiteCODE" character varying(20),
    "Ref" character varying(120)
);


--
-- Name: TSpeciesAssociations; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TSpeciesAssociations" (
    "SpeciesAssociationID" integer NOT NULL,
    "CODE" character varying(20),
    "AssociatedSpeciesCODE" double precision,
    "AssociationType" character varying(100),
    "Ref" character varying(120)
);


--
-- Name: TSpeciesAssociations_SpeciesAssociationID_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public."TSpeciesAssociations_SpeciesAssociationID_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: TSpeciesAssociations_SpeciesAssociationID_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public."TSpeciesAssociations_SpeciesAssociationID_seq" OWNED BY public."TSpeciesAssociations"."SpeciesAssociationID";


--
-- Name: TSynonym; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TSynonym" (
    "CODE" character varying(20) NOT NULL,
    "SynGenus" character varying(100),
    "SynSpecies" character varying(100),
    "SynAuthority" character varying(100),
    "Ref" character varying(120),
    "Notes" character varying(510)
);


--
-- Name: COLUMN "TSynonym"."Notes"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public."TSynonym"."Notes" IS 'Notes on synynomy - will eventually move to TSynonymNotes when SynonymCODE system set up';


--
-- Name: TSynonymNotes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TSynonymNotes" (
    "CODE" character varying(20) NOT NULL,
    "Notes" character varying(510)
);


--
-- Name: TABLE "TSynonymNotes"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public."TSynonymNotes" IS 'not implemented - needs SynonymCODE system.\015\012use Notes fields in TSynonym for now';


--
-- Name: TTaxoNotes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TTaxoNotes" (
    "CODE" character varying(20),
    "Ref" character varying(120),
    "Data" text
);


--
-- Name: TABLE "TTaxoNotes"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public."TTaxoNotes" IS 'CEP holds taxonomic notes for species, incl. ref';


--
-- Name: TbirmBEETLEdat; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TbirmBEETLEdat" (
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


--
-- Name: TABLE "TbirmBEETLEdat"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public."TbirmBEETLEdat" IS 'MCR Envelope data';


--
-- Name: BuildVersionBugsdata BuildCounter; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."BuildVersionBugsdata" ALTER COLUMN "BuildCounter" SET DEFAULT nextval('public."BuildVersionBugsdata_BuildCounter_seq"'::regclass);


--
-- Name: TRDBCodes RDBCode; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TRDBCodes" ALTER COLUMN "RDBCode" SET DEFAULT nextval('public."TRDBCodes_RDBCode_seq"'::regclass);


--
-- Name: TRDBSystems RDBSystemCode; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TRDBSystems" ALTER COLUMN "RDBSystemCode" SET DEFAULT nextval('public."TRDBSystems_RDBSystemCode_seq"'::regclass);


--
-- Name: TSiteOtherProxies OtherProxyID; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TSiteOtherProxies" ALTER COLUMN "OtherProxyID" SET DEFAULT nextval('public."TSiteOtherProxies_OtherProxyID_seq"'::regclass);


--
-- Name: TSpeciesAssociations SpeciesAssociationID; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TSpeciesAssociations" ALTER COLUMN "SpeciesAssociationID" SET DEFAULT nextval('public."TSpeciesAssociations_SpeciesAssociationID_seq"'::regclass);


--
-- Data for Name: BuildVersionBugsdata; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."BuildVersionBugsdata" ("BuildCounter", "Component", "DevLevel", "Version", "Build", "Implemented", "VersionChanges") FROM stdin;
\.


--
-- Data for Name: INDEX; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."INDEX" ("CODE", "FAMILY", "GENUS", "SPECIES", "AUTHORITY") FROM stdin;
1.0120045000000000	CARABIDAE	Elaphrus	tuberculatus	Mäklin
1.0120030000000000	CARABIDAE	Elaphrus	riparius	(L.)
23.028001499999998	STAPHYLINIDAE	Eucnecosum	brachypterum (grp)	(Grav.)
93.015056999999999	CURCULIONIDAE	Otiorhynchus	nodosus	(Möll.)
40.002102000000001	SCIRTIDAE	Ptilodactyla	exotica	Chapin
\.


--
-- Data for Name: TAttributes; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TAttributes" ("CODE", "AttribType", "AttribMeasure", "Value", "AttribUnits") FROM stdin;
\.


--
-- Data for Name: TBiblio; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TBiblio" ("REFERENCE", "AUTHOR", "TITLE", "Notes") FROM stdin;
Bell & Walker (2005)	Bell, M. & Walker M.J.C. (1992)	Late Quaternary Environmental Change - Physical and Human Perspectives (Second Edition). Longman, Essex.	\N
Bohme 2005	Böhme, J. (2005)	Die Köfer Mitteleuropas. K. Katalog (Faunistiche Übersicht) (2nd ed.). Spektrum Academic, Munich.	(revised version of Lucht 1987)
Morris 2006	Morris, M. (2006)	Checklist of beetles of the British Isles, Curculionidae. <www.coleopterist.org.uk/curculionidae-list.htm>.	\N
Strand 1946	Strand, A. (1946)	Nord Norges Coleoptera. Tromsö Museums Arshefter, Naturhistorisk Avd. Nr. 34, 67(1). (629pp.)	\N
\.


--
-- Data for Name: TBiology; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TBiology" ("CODE", "Ref", "Data") FROM stdin;
\.


--
-- Data for Name: TCountry; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TCountry" ("CountryCode", "Country") FROM stdin;
\.


--
-- Data for Name: TCountsheet; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TCountsheet" ("CountsheetCODE", "CountsheetName", "SiteCODE", "SheetContext", "SheetType") FROM stdin;
COUN000144	Hakullsmosse_bugsdata.XLS	SITE000253	Stratigraphic sequence	Abundances
\.


--
-- Data for Name: TDatesCalendar; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TDatesCalendar" ("SampleCODE", "Uncertainty", "CalendarCODE", "Date", "BCADBP", "DatingMethod", "Notes") FROM stdin;
\.


--
-- Data for Name: TDatesMethods; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TDatesMethods" ("Abbrev", "Method", "Type", "SortOrder") FROM stdin;
GeolPer	Geological period	Period	2
\.


--
-- Data for Name: TDatesPeriod; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TDatesPeriod" ("PeriodDateCODE", "SampleCODE", "Uncertainty", "PeriodCODE", "DatingMethod", "Notes") FROM stdin;
PERI005175	SAMP000546	\N	LG	GeolPer	\N
\.


--
-- Data for Name: TDatesRadio; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TDatesRadio" ("DateCODE", "SampleCODE", "LabNr", "Uncertainty", "Date", "AgeErrorOrPlusError", "AgeErrorMinus", "DatingMethod", "MaterialType", "LabID", "Notes") FROM stdin;
\.


--
-- Data for Name: TDatesTypes; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TDatesTypes" ("Type") FROM stdin;
\.


--
-- Data for Name: TDistrib; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TDistrib" ("CODE", "Ref", "Data") FROM stdin;
\.


--
-- Data for Name: TEcoBugs; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TEcoBugs" ("CODE", "BugsEcoCODE") FROM stdin;
\.


--
-- Data for Name: TEcoDefBugs; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TEcoDefBugs" ("SortOrder", "BugsEcoCODE", "Definition", "Notes", "EcoLabel") FROM stdin;
\.


--
-- Data for Name: TEcoDefGroups; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TEcoDefGroups" ("EcoGroupCode", "EcoName") FROM stdin;
\.


--
-- Data for Name: TEcoDefKoch; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TEcoDefKoch" ("BugsKochCode", "KochCode", "FullName", "KochGroup", "Description", "Notes") FROM stdin;
\.


--
-- Data for Name: TEcoKoch; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TEcoKoch" ("CODE", "BugsKochCode") FROM stdin;
\.


--
-- Data for Name: TFossil; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TFossil" ("FossilBugsCODE", "CODE", "SampleCODE", "Abundance") FROM stdin;
FOSS014299	93.015056999999999	SAMP000546	3
FOSS144182	23.028001499999998	SAMP000546	30
\.


--
-- Data for Name: TFossilUncertainty; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TFossilUncertainty" ("FossilBugsCODE", "Uncertainty") FROM stdin;
\.


--
-- Data for Name: TINDEXReplacements; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TINDEXReplacements" ("CODE", "ReplacedName", "SiteCODE") FROM stdin;
\.


--
-- Data for Name: TKeys; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TKeys" ("CODE", "Ref", "Data") FROM stdin;
\.


--
-- Data for Name: TLab; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TLab" ("LabID", "Labname", "Address", "Country", "Telephone", "Website", email) FROM stdin;
\.


--
-- Data for Name: TLookupCountsheetContext; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TLookupCountsheetContext" ("SheetContext", "SortOrder") FROM stdin;
Stratigraphic sequence	3
\.


--
-- Data for Name: TLookupCountsheetTypes; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TLookupCountsheetTypes" ("CountsheetType", "SortOrder") FROM stdin;
Abundances	1
\.


--
-- Data for Name: TLookupMonths; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TLookupMonths" ("SeasonCode", "SeasonName", "SortOrder") FROM stdin;
\.


--
-- Data for Name: TLookupUncertaintyType; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TLookupUncertaintyType" ("Uncertainty", "SortOrder") FROM stdin;
\.


--
-- Data for Name: TMCRNames; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TMCRNames" ("MCRNameTrim", "CompareStatus", "CODE", "tempCODE", "MCRNumber", "MCRName") FROM stdin;
\.


--
-- Data for Name: TMCRSummaryData; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TMCRSummaryData" ("CODE", "TMaxLo", "TMaxHi", "TMinLo", "TMinHi", "TRangeLo", "TRangeHi", "COGMidTMax", "COGMidTRange") FROM stdin;
\.


--
-- Data for Name: TPeriods; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TPeriods" ("PeriodCODE", "PeriodName", "PeriodType", "PeriodDesc", "PeriodRef", "PeriodGeog", "Begin", "BeginBCAD", "End", "EndBCAD", "YearsType") FROM stdin;
LG	Lateglacial	Geological	Cold period after the last Glaciation. Pollen Zones I-III	Bell & Walker (2005)	Europe	13500	BP	10000	BP	C14
\.


--
-- Data for Name: TRDB; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TRDB" ("CODE", "CountryCode", "RDBCode") FROM stdin;
\.


--
-- Data for Name: TRDBCodes; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TRDBCodes" ("RDBCode", "Category", "RDBDefinition", "RDBSystemCode") FROM stdin;
\.


--
-- Data for Name: TRDBSystems; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TRDBSystems" ("RDBSystemCode", "RDBSystem", "RDBVersion", "RDBSystemDate", "RDBFirstPublished", "Ref", "CountryCode") FROM stdin;
\.


--
-- Data for Name: TSample; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TSample" ("SampleCODE", "SiteCODE", "X", "Y", "ZorDepthTop", "ZorDepthBot", "RefNrContext", "CountsheetCODE") FROM stdin;
SAMP000546	SITE000253	\N	\N	\N	\N	B8:6/6	COUN000144
\.


--
-- Data for Name: TSeasonActiveAdult; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TSeasonActiveAdult" ("CODE", "HSeason", "CountryCode") FROM stdin;
\.


--
-- Data for Name: TSite; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TSite" ("SiteCODE", "SiteName", "Region", "Country", "NGR", "LatDD", "LongDD", "Alt", "IDBy", "Interp", "Specimens") FROM stdin;
SITE000253	Håkulls Mosse, Kullaberg	Skåne	Sweden	\N	56.2999992	12.5333338	125	Lemdahl	Kullen Peninsula, see also Björkeröds mosse.	\N
\.


--
-- Data for Name: TSiteOtherProxies; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TSiteOtherProxies" ("OtherProxyID", "SiteCODE", "HasPollen", "HasPlantMacro", "HasDiatoms", "HasChironomids", "HasSoilChemistry", "HasIsotopes", "HasAnimalBones", "HasArchaeology", "HasMolluscs") FROM stdin;
1	SITE000253	1	0	0	0	0	0	0	0	0
\.


--
-- Data for Name: TSiteRef; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TSiteRef" ("SiteCODE", "Ref") FROM stdin;
\.


--
-- Data for Name: TSpeciesAssociations; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TSpeciesAssociations" ("SpeciesAssociationID", "CODE", "AssociatedSpeciesCODE", "AssociationType", "Ref") FROM stdin;
\.


--
-- Data for Name: TSynonym; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TSynonym" ("CODE", "SynGenus", "SynSpecies", "SynAuthority", "Ref", "Notes") FROM stdin;
93.015056999999999	Otiorhynchus	dubius	(Ström.)	Bohme 2005	\N
93.015056999999999	Otiorhynchus	maurus	(Gyllenhal) non (Marsham)	Morris 2006	\N
23.028001499999998	Arpedium	\N	\N	Strand 1946	\N
1.0120045000000000	Elaphrus	riparius	(L.)	Lindroth 1985	regards this as a synonym.
\.


--
-- Data for Name: TSynonymNotes; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TSynonymNotes" ("CODE", "Notes") FROM stdin;
\.


--
-- Data for Name: TTaxoNotes; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TTaxoNotes" ("CODE", "Ref", "Data") FROM stdin;
40.002102000000001	Bohme 2005	Genus not listed.
\.


--
-- Data for Name: TbirmBEETLEdat; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."TbirmBEETLEdat" ("Field1", "Field2", "Field3", "Field4", "Field5", "Field6", "Field7", "Field8", "Field9", "Field10", "Field11", "Field12", "Field13", "Field14", "Field15", "Field16", "Field17", "Field18", "Field19", "Field20", "Field21", "Field22", "Field23", "Field24", "Field25", "Field26", "Field27", "Field28", "Field29", "Field30", "Field31", "Field32", "Field33", "Field34", "Field35", "Field36", "Field37", "Field38", "Field39", "Field40", "Field41", "Field42", "Field43", "Field44", "Field45", "Field46", "Field47", "Field48", "Field49", "Field50", "Field51", "Field52", "Field53", "Field54", "Field55", "Field56", "Field57", "Field58", "Field59", "Field60", "MCRRow", "CODE") FROM stdin;
\.


--
-- Name: BuildVersionBugsdata_BuildCounter_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public."BuildVersionBugsdata_BuildCounter_seq"', 1, false);


--
-- Name: TRDBCodes_RDBCode_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public."TRDBCodes_RDBCode_seq"', 1, false);


--
-- Name: TRDBSystems_RDBSystemCode_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public."TRDBSystems_RDBSystemCode_seq"', 1, false);


--
-- Name: TSiteOtherProxies_OtherProxyID_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public."TSiteOtherProxies_OtherProxyID_seq"', 1, false);


--
-- Name: TSpeciesAssociations_SpeciesAssociationID_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public."TSpeciesAssociations_SpeciesAssociationID_seq"', 1, false);


--
-- Name: BuildVersionBugsdata BuildVersionBugsdata_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."BuildVersionBugsdata"
    ADD CONSTRAINT "BuildVersionBugsdata_pkey" PRIMARY KEY ("BuildCounter");


--
-- Name: INDEX INDEX_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."INDEX"
    ADD CONSTRAINT "INDEX_pkey" PRIMARY KEY ("CODE");


--
-- Name: TBiblio TBiblio_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TBiblio"
    ADD CONSTRAINT "TBiblio_pkey" PRIMARY KEY ("REFERENCE");


--
-- Name: TCountry TCountry_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TCountry"
    ADD CONSTRAINT "TCountry_pkey" PRIMARY KEY ("CountryCode");


--
-- Name: TCountsheet TCountsheet_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TCountsheet"
    ADD CONSTRAINT "TCountsheet_pkey" PRIMARY KEY ("CountsheetCODE");


--
-- Name: TDatesCalendar TDatesCalendar_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TDatesCalendar"
    ADD CONSTRAINT "TDatesCalendar_pkey" PRIMARY KEY ("CalendarCODE");


--
-- Name: TDatesMethods TDatesMethods_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TDatesMethods"
    ADD CONSTRAINT "TDatesMethods_pkey" PRIMARY KEY ("Abbrev");


--
-- Name: TDatesPeriod TDatesPeriod_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TDatesPeriod"
    ADD CONSTRAINT "TDatesPeriod_pkey" PRIMARY KEY ("PeriodDateCODE");


--
-- Name: TDatesRadio TDatesRadio_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TDatesRadio"
    ADD CONSTRAINT "TDatesRadio_pkey" PRIMARY KEY ("DateCODE");


--
-- Name: TDatesTypes TDatesTypes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TDatesTypes"
    ADD CONSTRAINT "TDatesTypes_pkey" PRIMARY KEY ("Type");


--
-- Name: TEcoDefBugs TEcoDefBugs_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TEcoDefBugs"
    ADD CONSTRAINT "TEcoDefBugs_pkey" PRIMARY KEY ("BugsEcoCODE");


--
-- Name: TEcoDefGroups TEcoDefGroups_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TEcoDefGroups"
    ADD CONSTRAINT "TEcoDefGroups_pkey" PRIMARY KEY ("EcoGroupCode");


--
-- Name: TEcoDefKoch TEcoDefKoch_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TEcoDefKoch"
    ADD CONSTRAINT "TEcoDefKoch_pkey" PRIMARY KEY ("BugsKochCode");


--
-- Name: TFossilUncertainty TFossilUncertainty_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TFossilUncertainty"
    ADD CONSTRAINT "TFossilUncertainty_pkey" PRIMARY KEY ("FossilBugsCODE");


--
-- Name: TFossil TFossil_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TFossil"
    ADD CONSTRAINT "TFossil_pkey" PRIMARY KEY ("FossilBugsCODE");


--
-- Name: TINDEXReplacements TINDEXReplacements_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TINDEXReplacements"
    ADD CONSTRAINT "TINDEXReplacements_pkey" PRIMARY KEY ("ReplacedName");


--
-- Name: TLab TLab_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TLab"
    ADD CONSTRAINT "TLab_pkey" PRIMARY KEY ("LabID");


--
-- Name: TLookupCountsheetContext TLookupCountsheetContext_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TLookupCountsheetContext"
    ADD CONSTRAINT "TLookupCountsheetContext_pkey" PRIMARY KEY ("SheetContext");


--
-- Name: TLookupCountsheetTypes TLookupCountsheetTypes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TLookupCountsheetTypes"
    ADD CONSTRAINT "TLookupCountsheetTypes_pkey" PRIMARY KEY ("CountsheetType");


--
-- Name: TLookupMonths TLookupMonths_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TLookupMonths"
    ADD CONSTRAINT "TLookupMonths_pkey" PRIMARY KEY ("SeasonCode");


--
-- Name: TLookupUncertaintyType TLookupUncertaintyType_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TLookupUncertaintyType"
    ADD CONSTRAINT "TLookupUncertaintyType_pkey" PRIMARY KEY ("Uncertainty");


--
-- Name: TMCRNames TMCRNames_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TMCRNames"
    ADD CONSTRAINT "TMCRNames_pkey" PRIMARY KEY ("CODE");


--
-- Name: TMCRSummaryData TMCRSummaryData_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TMCRSummaryData"
    ADD CONSTRAINT "TMCRSummaryData_pkey" PRIMARY KEY ("CODE");


--
-- Name: TPeriods TPeriods_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TPeriods"
    ADD CONSTRAINT "TPeriods_pkey" PRIMARY KEY ("PeriodCODE");


--
-- Name: TRDBCodes TRDBCodes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TRDBCodes"
    ADD CONSTRAINT "TRDBCodes_pkey" PRIMARY KEY ("RDBCode");


--
-- Name: TRDBSystems TRDBSystems_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TRDBSystems"
    ADD CONSTRAINT "TRDBSystems_pkey" PRIMARY KEY ("RDBSystemCode");


--
-- Name: TSample TSample_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TSample"
    ADD CONSTRAINT "TSample_pkey" PRIMARY KEY ("SampleCODE");


--
-- Name: TSiteOtherProxies TSiteOtherProxies_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TSiteOtherProxies"
    ADD CONSTRAINT "TSiteOtherProxies_pkey" PRIMARY KEY ("OtherProxyID");


--
-- Name: TSite TSite_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TSite"
    ADD CONSTRAINT "TSite_pkey" PRIMARY KEY ("SiteCODE");


--
-- Name: TSpeciesAssociations TSpeciesAssociations_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TSpeciesAssociations"
    ADD CONSTRAINT "TSpeciesAssociations_pkey" PRIMARY KEY ("SpeciesAssociationID");


--
-- Name: BuildVersionBugsdata_Component_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "BuildVersionBugsdata_Component_idx" ON public."BuildVersionBugsdata" USING btree ("Component");


--
-- Name: INDEX_IAuthority_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "INDEX_IAuthority_idx" ON public."INDEX" USING btree ("AUTHORITY");


--
-- Name: INDEX_IFamily_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "INDEX_IFamily_idx" ON public."INDEX" USING btree ("FAMILY");


--
-- Name: INDEX_IGenus_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "INDEX_IGenus_idx" ON public."INDEX" USING btree ("GENUS");


--
-- Name: INDEX_ISpecies_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "INDEX_ISpecies_idx" ON public."INDEX" USING btree ("SPECIES");


--
-- Name: TAttributes_PrimaryKey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TAttributes_PrimaryKey_idx" ON public."TAttributes" USING btree ("CODE");


--
-- Name: TBiblio_REFERENCE_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TBiblio_REFERENCE_idx" ON public."TBiblio" USING btree ("REFERENCE");


--
-- Name: TBiology_PrimaryKey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TBiology_PrimaryKey_idx" ON public."TBiology" USING btree ("CODE");


--
-- Name: TBiology_RefKey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TBiology_RefKey_idx" ON public."TBiology" USING btree ("Ref");


--
-- Name: TCountry_CountryCode_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TCountry_CountryCode_idx" ON public."TCountry" USING btree ("CountryCode");


--
-- Name: TCountsheet_ICountsheetName_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TCountsheet_ICountsheetName_idx" ON public."TCountsheet" USING btree ("CountsheetName");


--
-- Name: TCountsheet_ISiteCODE_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TCountsheet_ISiteCODE_idx" ON public."TCountsheet" USING btree ("SiteCODE");


--
-- Name: TCountsheet_SheetContext_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TCountsheet_SheetContext_idx" ON public."TCountsheet" USING btree ("SheetContext");


--
-- Name: TCountsheet_SheetType_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TCountsheet_SheetType_idx" ON public."TCountsheet" USING btree ("SheetType");


--
-- Name: TDatesCalendar_Date_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TDatesCalendar_Date_idx" ON public."TDatesCalendar" USING btree ("Date");


--
-- Name: TDatesCalendar_TDatesCalenderSampleCODE_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TDatesCalendar_TDatesCalenderSampleCODE_idx" ON public."TDatesCalendar" USING btree ("SampleCODE");


--
-- Name: TDatesMethods_IdxSortOrder_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TDatesMethods_IdxSortOrder_idx" ON public."TDatesMethods" USING btree ("SortOrder");


--
-- Name: TDatesPeriod_IndexPeriod_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TDatesPeriod_IndexPeriod_idx" ON public."TDatesPeriod" USING btree ("PeriodCODE");


--
-- Name: TDatesPeriod_TDatesPeriodSampleCODE_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TDatesPeriod_TDatesPeriodSampleCODE_idx" ON public."TDatesPeriod" USING btree ("SampleCODE");


--
-- Name: TDatesRadio_IndexAge_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TDatesRadio_IndexAge_idx" ON public."TDatesRadio" USING btree ("Date");


--
-- Name: TDatesRadio_IndexMethod_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TDatesRadio_IndexMethod_idx" ON public."TDatesRadio" USING btree ("DatingMethod");


--
-- Name: TDatesRadio_LabNr_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TDatesRadio_LabNr_idx" ON public."TDatesRadio" USING btree ("LabNr");


--
-- Name: TDatesRadio_SampleCODE_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TDatesRadio_SampleCODE_idx" ON public."TDatesRadio" USING btree ("SampleCODE");


--
-- Name: TDatesRadio_TDatesRadioLabID_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TDatesRadio_TDatesRadioLabID_idx" ON public."TDatesRadio" USING btree ("LabID");


--
-- Name: TDistrib_PrimaryKey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TDistrib_PrimaryKey_idx" ON public."TDistrib" USING btree ("CODE");


--
-- Name: TDistrib_RefKey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TDistrib_RefKey_idx" ON public."TDistrib" USING btree ("Ref");


--
-- Name: TEcoBugs_IdxCODE_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TEcoBugs_IdxCODE_idx" ON public."TEcoBugs" USING btree ("CODE");


--
-- Name: TEcoBugs_IdxEcoCODE_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TEcoBugs_IdxEcoCODE_idx" ON public."TEcoBugs" USING btree ("BugsEcoCODE");


--
-- Name: TEcoDefBugs_IdxSortOrder_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TEcoDefBugs_IdxSortOrder_idx" ON public."TEcoDefBugs" USING btree ("SortOrder");


--
-- Name: TEcoDefKoch_BugsKochCode_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TEcoDefKoch_BugsKochCode_idx" ON public."TEcoDefKoch" USING btree ("BugsKochCode");


--
-- Name: TEcoDefKoch_KochCode_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TEcoDefKoch_KochCode_idx" ON public."TEcoDefKoch" USING btree ("KochCode");


--
-- Name: TEcoKoch_CODE_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TEcoKoch_CODE_idx" ON public."TEcoKoch" USING btree ("CODE");


--
-- Name: TEcoKoch_RobinsonCODE_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TEcoKoch_RobinsonCODE_idx" ON public."TEcoKoch" USING btree ("BugsKochCode");


--
-- Name: TFossilUncertainty_FossilBugsCODE_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TFossilUncertainty_FossilBugsCODE_idx" ON public."TFossilUncertainty" USING btree ("FossilBugsCODE");


--
-- Name: TFossil_CODE_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TFossil_CODE_idx" ON public."TFossil" USING btree ("CODE");


--
-- Name: TFossil_FossilBugsCODE_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TFossil_FossilBugsCODE_idx" ON public."TFossil" USING btree ("FossilBugsCODE");


--
-- Name: TFossil_SampleCODE_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TFossil_SampleCODE_idx" ON public."TFossil" USING btree ("SampleCODE");


--
-- Name: TINDEXReplacements_CODE_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TINDEXReplacements_CODE_idx" ON public."TINDEXReplacements" USING btree ("CODE");


--
-- Name: TINDEXReplacements_SiteCODE_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TINDEXReplacements_SiteCODE_idx" ON public."TINDEXReplacements" USING btree ("SiteCODE");


--
-- Name: TKeys_PrimaryKey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TKeys_PrimaryKey_idx" ON public."TKeys" USING btree ("CODE");


--
-- Name: TKeys_RefKey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TKeys_RefKey_idx" ON public."TKeys" USING btree ("Ref");


--
-- Name: TLab_IndexLabName_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TLab_IndexLabName_idx" ON public."TLab" USING btree ("Labname");


--
-- Name: TLab_Indexemail_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TLab_Indexemail_idx" ON public."TLab" USING btree (email);


--
-- Name: TLab_Indexwebsite_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TLab_Indexwebsite_idx" ON public."TLab" USING btree ("Website");


--
-- Name: TLab_LabID_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TLab_LabID_idx" ON public."TLab" USING btree ("LabID");


--
-- Name: TLookupCountsheetContext_SortOrder_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TLookupCountsheetContext_SortOrder_idx" ON public."TLookupCountsheetContext" USING btree ("SortOrder");


--
-- Name: TLookupCountsheetTypes_SortOrder_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TLookupCountsheetTypes_SortOrder_idx" ON public."TLookupCountsheetTypes" USING btree ("SortOrder");


--
-- Name: TLookupMonths_SortOrder_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TLookupMonths_SortOrder_idx" ON public."TLookupMonths" USING btree ("SortOrder");


--
-- Name: TLookupUncertaintyType_SortOrder_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TLookupUncertaintyType_SortOrder_idx" ON public."TLookupUncertaintyType" USING btree ("SortOrder");


--
-- Name: TMCRNames_tempCODE_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TMCRNames_tempCODE_idx" ON public."TMCRNames" USING btree ("tempCODE");


--
-- Name: TPeriods_IndexBegin_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TPeriods_IndexBegin_idx" ON public."TPeriods" USING btree ("Begin");


--
-- Name: TPeriods_IndexEnd_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TPeriods_IndexEnd_idx" ON public."TPeriods" USING btree ("End");


--
-- Name: TRDBCodes_IndexSystem_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TRDBCodes_IndexSystem_idx" ON public."TRDBCodes" USING btree ("RDBSystemCode");


--
-- Name: TRDBCodes_RDBCode_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TRDBCodes_RDBCode_idx" ON public."TRDBCodes" USING btree ("RDBCode");


--
-- Name: TRDBSystems_CountryCode_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TRDBSystems_CountryCode_idx" ON public."TRDBSystems" USING btree ("CountryCode");


--
-- Name: TRDB_CountryCode_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TRDB_CountryCode_idx" ON public."TRDB" USING btree ("CountryCode");


--
-- Name: TRDB_PrimaryKey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TRDB_PrimaryKey_idx" ON public."TRDB" USING btree ("CODE");


--
-- Name: TRDB_TRDBRDBCode_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TRDB_TRDBRDBCode_idx" ON public."TRDB" USING btree ("RDBCode");


--
-- Name: TSample_CountsheetCODE_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TSample_CountsheetCODE_idx" ON public."TSample" USING btree ("CountsheetCODE");


--
-- Name: TSample_SiteCODE_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TSample_SiteCODE_idx" ON public."TSample" USING btree ("SiteCODE");


--
-- Name: TSeasonActiveAdult_CODE_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TSeasonActiveAdult_CODE_idx" ON public."TSeasonActiveAdult" USING btree ("CODE");


--
-- Name: TSeasonActiveAdult_CountryID_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TSeasonActiveAdult_CountryID_idx" ON public."TSeasonActiveAdult" USING btree ("CountryCode");


--
-- Name: TSeasonActiveAdult_HSeason_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TSeasonActiveAdult_HSeason_idx" ON public."TSeasonActiveAdult" USING btree ("HSeason");


--
-- Name: TSiteOtherProxies_OtherProxyID_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TSiteOtherProxies_OtherProxyID_idx" ON public."TSiteOtherProxies" USING btree ("OtherProxyID");


--
-- Name: TSiteOtherProxies_TSiteRefSiteCODE_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TSiteOtherProxies_TSiteRefSiteCODE_idx" ON public."TSiteOtherProxies" USING btree ("SiteCODE");


--
-- Name: TSiteRef_TSiteRefSiteCODE_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TSiteRef_TSiteRefSiteCODE_idx" ON public."TSiteRef" USING btree ("SiteCODE");


--
-- Name: TSite_IDBy_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TSite_IDBy_idx" ON public."TSite" USING btree ("IDBy");


--
-- Name: TSite_SiteCODE_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TSite_SiteCODE_idx" ON public."TSite" USING btree ("SiteCODE");


--
-- Name: TSite_SiteName_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TSite_SiteName_idx" ON public."TSite" USING btree ("SiteName");


--
-- Name: TSpeciesAssociations_AssociatedSpeciesCODE_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TSpeciesAssociations_AssociatedSpeciesCODE_idx" ON public."TSpeciesAssociations" USING btree ("AssociatedSpeciesCODE");


--
-- Name: TSpeciesAssociations_AssociationType_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TSpeciesAssociations_AssociationType_idx" ON public."TSpeciesAssociations" USING btree ("AssociationType");


--
-- Name: TSpeciesAssociations_CODE_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TSpeciesAssociations_CODE_idx" ON public."TSpeciesAssociations" USING btree ("CODE");


--
-- Name: TSpeciesAssociations_RefKey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TSpeciesAssociations_RefKey_idx" ON public."TSpeciesAssociations" USING btree ("Ref");


--
-- Name: TSpeciesAssociations_SpeciesAssociationID_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TSpeciesAssociations_SpeciesAssociationID_idx" ON public."TSpeciesAssociations" USING btree ("SpeciesAssociationID");


--
-- Name: TSynonymNotes_CODE_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TSynonymNotes_CODE_idx" ON public."TSynonymNotes" USING btree ("CODE");


--
-- Name: TSynonymNotes_Ref_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TSynonymNotes_Ref_idx" ON public."TSynonymNotes" USING btree ("Notes");


--
-- Name: TSynonym_CODE_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TSynonym_CODE_idx" ON public."TSynonym" USING btree ("CODE");


--
-- Name: TSynonym_Ref_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TSynonym_Ref_idx" ON public."TSynonym" USING btree ("Ref");


--
-- Name: TTaxoNotes_PrimaryKey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TTaxoNotes_PrimaryKey_idx" ON public."TTaxoNotes" USING btree ("CODE");


--
-- Name: TTaxoNotes_RefKey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TTaxoNotes_RefKey_idx" ON public."TTaxoNotes" USING btree ("Ref");


--
-- Name: TbirmBEETLEdat_ICODE_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TbirmBEETLEdat_ICODE_idx" ON public."TbirmBEETLEdat" USING btree ("CODE");


--
-- Name: TbirmBEETLEdat_IMCRRow_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TbirmBEETLEdat_IMCRRow_idx" ON public."TbirmBEETLEdat" USING btree ("MCRRow");


--
-- Name: TAttributes TAttributes_CODE_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TAttributes"
    ADD CONSTRAINT "TAttributes_CODE_fk" FOREIGN KEY ("CODE") REFERENCES public."INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TBiology TBiology_CODE_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TBiology"
    ADD CONSTRAINT "TBiology_CODE_fk" FOREIGN KEY ("CODE") REFERENCES public."INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TBiology TBiology_Ref_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TBiology"
    ADD CONSTRAINT "TBiology_Ref_fk" FOREIGN KEY ("Ref") REFERENCES public."TBiblio"("REFERENCE") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TCountsheet TCountsheet_SiteCODE_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TCountsheet"
    ADD CONSTRAINT "TCountsheet_SiteCODE_fk" FOREIGN KEY ("SiteCODE") REFERENCES public."TSite"("SiteCODE") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TDatesCalendar TDatesCalendar_SampleCODE_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TDatesCalendar"
    ADD CONSTRAINT "TDatesCalendar_SampleCODE_fk" FOREIGN KEY ("SampleCODE") REFERENCES public."TSample"("SampleCODE") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TDatesPeriod TDatesPeriod_SampleCODE_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TDatesPeriod"
    ADD CONSTRAINT "TDatesPeriod_SampleCODE_fk" FOREIGN KEY ("SampleCODE") REFERENCES public."TSample"("SampleCODE") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TDatesRadio TDatesRadio_SampleCODE_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TDatesRadio"
    ADD CONSTRAINT "TDatesRadio_SampleCODE_fk" FOREIGN KEY ("SampleCODE") REFERENCES public."TSample"("SampleCODE") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TDistrib TDistrib_CODE_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TDistrib"
    ADD CONSTRAINT "TDistrib_CODE_fk" FOREIGN KEY ("CODE") REFERENCES public."INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TDistrib TDistrib_Ref_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TDistrib"
    ADD CONSTRAINT "TDistrib_Ref_fk" FOREIGN KEY ("Ref") REFERENCES public."TBiblio"("REFERENCE") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TEcoBugs TEcoBugs_BugsEcoCODE_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TEcoBugs"
    ADD CONSTRAINT "TEcoBugs_BugsEcoCODE_fk" FOREIGN KEY ("BugsEcoCODE") REFERENCES public."TEcoDefBugs"("BugsEcoCODE") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TEcoBugs TEcoBugs_CODE_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TEcoBugs"
    ADD CONSTRAINT "TEcoBugs_CODE_fk" FOREIGN KEY ("CODE") REFERENCES public."INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TEcoDefKoch TEcoDefKoch_KochGroup_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TEcoDefKoch"
    ADD CONSTRAINT "TEcoDefKoch_KochGroup_fk" FOREIGN KEY ("KochGroup") REFERENCES public."TEcoDefGroups"("EcoGroupCode") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TEcoKoch TEcoKoch_BugsKochCode_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TEcoKoch"
    ADD CONSTRAINT "TEcoKoch_BugsKochCode_fk" FOREIGN KEY ("BugsKochCode") REFERENCES public."TEcoDefKoch"("BugsKochCode") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TEcoKoch TEcoKoch_CODE_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TEcoKoch"
    ADD CONSTRAINT "TEcoKoch_CODE_fk" FOREIGN KEY ("CODE") REFERENCES public."INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TFossilUncertainty TFossilUncertainty_FossilBugsCODE_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TFossilUncertainty"
    ADD CONSTRAINT "TFossilUncertainty_FossilBugsCODE_fk" FOREIGN KEY ("FossilBugsCODE") REFERENCES public."TFossil"("FossilBugsCODE");


--
-- Name: TFossil TFossil_CODE_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TFossil"
    ADD CONSTRAINT "TFossil_CODE_fk" FOREIGN KEY ("CODE") REFERENCES public."INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TFossil TFossil_SampleCODE_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TFossil"
    ADD CONSTRAINT "TFossil_SampleCODE_fk" FOREIGN KEY ("SampleCODE") REFERENCES public."TSample"("SampleCODE") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TKeys TKeys_CODE_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TKeys"
    ADD CONSTRAINT "TKeys_CODE_fk" FOREIGN KEY ("CODE") REFERENCES public."INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TMCRNames TMCRNames_CODE_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TMCRNames"
    ADD CONSTRAINT "TMCRNames_CODE_fk" FOREIGN KEY ("CODE") REFERENCES public."INDEX"("CODE") ON UPDATE CASCADE;


--
-- Name: TMCRSummaryData TMCRSummaryData_CODE_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TMCRSummaryData"
    ADD CONSTRAINT "TMCRSummaryData_CODE_fk" FOREIGN KEY ("CODE") REFERENCES public."INDEX"("CODE") ON UPDATE CASCADE;


--
-- Name: TPeriods TPeriods_PeriodRef_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TPeriods"
    ADD CONSTRAINT "TPeriods_PeriodRef_fk" FOREIGN KEY ("PeriodRef") REFERENCES public."TBiblio"("REFERENCE") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TRDBSystems TRDBSystems_CountryCode_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TRDBSystems"
    ADD CONSTRAINT "TRDBSystems_CountryCode_fk" FOREIGN KEY ("CountryCode") REFERENCES public."TCountry"("CountryCode") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TRDBSystems TRDBSystems_Ref_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TRDBSystems"
    ADD CONSTRAINT "TRDBSystems_Ref_fk" FOREIGN KEY ("Ref") REFERENCES public."TBiblio"("REFERENCE") ON UPDATE CASCADE;


--
-- Name: TRDB TRDB_CODE_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TRDB"
    ADD CONSTRAINT "TRDB_CODE_fk" FOREIGN KEY ("CODE") REFERENCES public."INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TRDB TRDB_CountryCode_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TRDB"
    ADD CONSTRAINT "TRDB_CountryCode_fk" FOREIGN KEY ("CountryCode") REFERENCES public."TCountry"("CountryCode") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TSample TSample_SiteCODE_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TSample"
    ADD CONSTRAINT "TSample_SiteCODE_fk" FOREIGN KEY ("SiteCODE") REFERENCES public."TSite"("SiteCODE") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TSeasonActiveAdult TSeasonActiveAdult_CODE_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TSeasonActiveAdult"
    ADD CONSTRAINT "TSeasonActiveAdult_CODE_fk" FOREIGN KEY ("CODE") REFERENCES public."INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TSeasonActiveAdult TSeasonActiveAdult_CountryCode_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TSeasonActiveAdult"
    ADD CONSTRAINT "TSeasonActiveAdult_CountryCode_fk" FOREIGN KEY ("CountryCode") REFERENCES public."TCountry"("CountryCode") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TSiteOtherProxies TSiteOtherProxies_SiteCODE_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TSiteOtherProxies"
    ADD CONSTRAINT "TSiteOtherProxies_SiteCODE_fk" FOREIGN KEY ("SiteCODE") REFERENCES public."TSite"("SiteCODE") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TSiteRef TSiteRef_Ref_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TSiteRef"
    ADD CONSTRAINT "TSiteRef_Ref_fk" FOREIGN KEY ("Ref") REFERENCES public."TBiblio"("REFERENCE") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TSiteRef TSiteRef_SiteCODE_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TSiteRef"
    ADD CONSTRAINT "TSiteRef_SiteCODE_fk" FOREIGN KEY ("SiteCODE") REFERENCES public."TSite"("SiteCODE") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TSpeciesAssociations TSpeciesAssociations_CODE_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TSpeciesAssociations"
    ADD CONSTRAINT "TSpeciesAssociations_CODE_fk" FOREIGN KEY ("CODE") REFERENCES public."INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TSynonym TSynonym_CODE_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TSynonym"
    ADD CONSTRAINT "TSynonym_CODE_fk" FOREIGN KEY ("CODE") REFERENCES public."INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TTaxoNotes TTaxoNotes_CODE_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TTaxoNotes"
    ADD CONSTRAINT "TTaxoNotes_CODE_fk" FOREIGN KEY ("CODE") REFERENCES public."INDEX"("CODE") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TTaxoNotes TTaxoNotes_Ref_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TTaxoNotes"
    ADD CONSTRAINT "TTaxoNotes_Ref_fk" FOREIGN KEY ("Ref") REFERENCES public."TBiblio"("REFERENCE") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TbirmBEETLEdat TbirmBEETLEdat_CODE_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TbirmBEETLEdat"
    ADD CONSTRAINT "TbirmBEETLEdat_CODE_fk" FOREIGN KEY ("CODE") REFERENCES public."INDEX"("CODE") ON UPDATE CASCADE;


--
-- PostgreSQL database dump complete
--


