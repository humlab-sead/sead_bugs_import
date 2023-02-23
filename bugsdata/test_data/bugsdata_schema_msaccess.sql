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

/*
PostgreSQL => MS Access
1. Remove all '"'
2. Replace "character varying" with varchar
3. "create sequence..." => AUTOINCREMENT
4. "timestamp without time zone" => datetime
*/

CREATE TABLE BuildVersionBugsdata (
    BuildCounter integer NOT NULL,
    Component varchar(20),
    DevLevel varchar(20),
    Version varchar(20),
    Build varchar(20),
    Implemented datetime,
    VersionChanges text
);

CREATE TABLE INDEX (
    CODE varchar(20) NOT NULL,
    FAMILY varchar(100),
    GENUS varchar(100),
    SPECIES varchar(70),
    AUTHORITY varchar(70)
);

CREATE TABLE TAttributes (
    CODE varchar(20),
    AttribType varchar(100),
    AttribMeasure varchar(40),
    Value real,
    AttribUnits varchar(20),
    AttributeCODE AUTOINCREMENT
);

CREATE TABLE TBiblio (
    REFERENCE varchar(120) NOT NULL,
    AUTHOR varchar(510),
    TITLE text,
    Notes text
);

CREATE TABLE TBiology (
    CODE varchar(20),
    Ref varchar(120),
    Data text,
    BiologyCODE AUTOINCREMENT
);

CREATE TABLE TCountry (
    CountryCode varchar(6) NOT NULL,
    Country varchar(100)
);

CREATE TABLE TCountsheet (
    CountsheetCODE varchar(20) NOT NULL,
    CountsheetName varchar(200),
    SiteCODE varchar(20),
    SheetContext varchar(50),
    SheetType varchar(50)
);

CREATE TABLE TDatesCalendar (
    SampleCODE varchar(20),
    Uncertainty varchar(20),
    CalendarCODE varchar(20) NOT NULL,
    Date integer,
    BCADBP varchar(20),
    DatingMethod varchar(100),
    Notes text
);

CREATE TABLE TDatesMethods (
    Abbrev varchar(100) NOT NULL,
    Method varchar(100),
    Type varchar(100),
    SortOrder integer
);

CREATE TABLE TDatesPeriod (
    PeriodDateCODE varchar(20) NOT NULL,
    SampleCODE varchar(20),
    Uncertainty varchar(20),
    PeriodCODE varchar(40),
    DatingMethod varchar(100),
    Notes text
);

CREATE TABLE TDatesRadio (
    DateCODE varchar(20) NOT NULL,
    SampleCODE varchar(20),
    LabNr varchar(40),
    Uncertainty varchar(20),
    Date integer,
    AgeErrorOrPlusError integer,
    AgeErrorMinus integer,
    DatingMethod varchar(30),
    MaterialType varchar(100),
    LabID varchar(20),
    Notes text
);

CREATE TABLE TDatesTypes (
    Type varchar(100) NOT NULL
);

CREATE TABLE TDistrib (
    CODE varchar(20),
    Ref varchar(120),
    Data text,
    DistribCODE AUTOINCREMENT
);

CREATE TABLE TEcoBugs (
    CODE varchar(20) NOT NULL,
    BugsEcoCODE varchar(14)
);

CREATE TABLE TEcoDefBugs (
    SortOrder integer NOT NULL,
    BugsEcoCODE varchar(14) NOT NULL,
    Definition varchar(100),
    Notes text,
    EcoLabel varchar(100)
);

CREATE TABLE TEcoDefGroups (
    EcoGroupCode varchar(50) NOT NULL,
    EcoName varchar(100)
);

CREATE TABLE TEcoDefKoch (
    BugsKochCode varchar(10) NOT NULL,
    KochCode varchar(4),
    FullName varchar(100),
    KochGroup varchar(50),
    Description varchar(510),
    Notes text
);

CREATE TABLE TEcoKoch (
    CODE varchar(20) NOT NULL,
    BugsKochCode varchar(10)
);

CREATE TABLE TFossil (
    FossilBugsCODE varchar(20) NOT NULL,
    CODE varchar(20),
    SampleCODE varchar(20),
    Abundance integer
);

CREATE TABLE TFossilUncertainty (
    FossilBugsCODE varchar(20) NOT NULL,
    Uncertainty varchar(510)
);

CREATE TABLE TINDEXReplacements (
    CODE varchar(20),
    ReplacedName varchar(200) NOT NULL,
    SiteCODE varchar(20)
);

CREATE TABLE TKeys (
    CODE varchar(20),
    Ref varchar(120),
    Data text,
    KeyCODE AUTOINCREMENT
);


CREATE TABLE TLab (
    LabID varchar(20) NOT NULL,
    Labname varchar(200),
    Address varchar(510),
    Country varchar(60),
    Telephone varchar(100),
    Website text,
    email text
);

CREATE TABLE TLookupCountsheetContext (
    SheetContext varchar(50) NOT NULL,
    SortOrder integer
);

CREATE TABLE TLookupCountsheetTypes (
    CountsheetType varchar(50) NOT NULL,
    SortOrder integer
);

CREATE TABLE TLookupMonths (
    SeasonCode varchar(6) NOT NULL,
    SeasonName varchar(100),
    SortOrder integer
);

CREATE TABLE TLookupUncertaintyType (
    Uncertainty varchar(30) NOT NULL,
    SortOrder integer
);

CREATE TABLE TMCRNames (
    MCRNameTrim varchar(160),
    CompareStatus varchar(510),
    CODE varchar(20) NOT NULL,
    tempCODE double precision,
    MCRNumber integer,
    MCRName varchar(400)
);

CREATE TABLE TMCRSummaryData (
    CODE varchar(20) NOT NULL,
    TMaxLo integer,
    TMaxHi integer,
    TMinLo integer,
    TMinHi integer,
    TRangeLo integer,
    TRangeHi integer,
    COGMidTMax integer,
    COGMidTRange integer
);

CREATE TABLE TPeriods (
    PeriodCODE varchar(40) NOT NULL,
    PeriodName varchar(56),
    PeriodType varchar(100),
    PeriodDesc varchar(510),
    PeriodRef varchar(100),
    PeriodGeog varchar(200),
    Begin integer,
    BeginBCAD varchar(8),
    End integer,
    EndBCAD varchar(8),
    YearsType varchar(26)
);

CREATE TABLE TRDB (
    CODE varchar(20),
    CountryCode varchar(6),
    RDBCode AUTOINCREMENT,
    RDBCODE AUTOINCREMENT
);

CREATE TABLE TRDBCodes (
    RDBCode integer NOT NULL,
    Category varchar(8),
    RDBDefinition varchar(400),
    RDBSystemCode integer
);

CREATE TABLE TRDBSystems (
    RDBSystemCode AUTOINCREMENT,
    RDBSystem varchar(20),
    RDBVersion varchar(20),
    RDBSystemDate integer,
    RDBFirstPublished integer,
    Ref varchar(120),
    CountryCode varchar(6)
);


CREATE TABLE TSample (
    SampleCODE varchar(20) NOT NULL,
    SiteCODE varchar(20),
    X varchar(100),
    Y varchar(100),
    ZorDepthTop double precision,
    ZorDepthBot double precision,
    RefNrContext varchar(100),
    CountsheetCODE varchar(20)
);

CREATE TABLE TSeasonActiveAdult (
    CODE varchar(20),
    HSeason varchar(6),
    CountryCode varchar(6)
);

CREATE TABLE TSite (
    SiteCODE varchar(20) NOT NULL,
    SiteName varchar(100),
    Region varchar(80),
    Country varchar(40),
    NGR varchar(20),
    LatDD real,
    LongDD real,
    Alt real,
    IDBy varchar(510),
    Interp varchar(510),
    Specimens varchar(510)
);

CREATE TABLE TSiteOtherProxies (
    OtherProxyID AUTOINCREMENT,
    SiteCODE varchar(20),
    HasPollen smallint,
    HasPlantMacro smallint,
    HasDiatoms smallint,
    HasChironomids smallint,
    HasSoilChemistry smallint,
    HasIsotopes smallint,
    HasAnimalBones smallint,
    HasArchaeology smallint,
    HasMolluscs smallint
);

CREATE TABLE TSiteRef (
    SiteCODE varchar(20),
    Ref varchar(120),
    SiteRefCODE AUTOINCREMENT
);


CREATE TABLE TSpeciesAssociations (
    SpeciesAssociationID AUTOINCREMENT,
    CODE varchar(20),
    AssociatedSpeciesCODE varchar,
    AssociationType varchar(100),
    Ref varchar(120)
);

CREATE TABLE TSynonym (
    CODE varchar(20) NOT NULL,
    SynGenus varchar(100),
    SynSpecies varchar(100),
    SynAuthority varchar(100),
    Ref varchar(120),
    Notes varchar(510),
    SynonymCODE AUTOINCREMENT
);

CREATE TABLE TSynonymNotes (
    CODE varchar(20) NOT NULL,
    Notes varchar(510),
    SynonymNoteCODE AUTOINCREMENT
);

CREATE TABLE TTaxoNotes (
    CODE varchar(20),
    Ref varchar(120),
    Data text,
    TaxoNoteCODE AUTOINCREMENT
);

ALTER TABLE ONLY BuildVersionBugsdata
    ADD CONSTRAINT BuildVersionBugsdata_pkey PRIMARY KEY (BuildCounter);

ALTER TABLE ONLY INDEX
    ADD CONSTRAINT INDEX_pkey PRIMARY KEY (CODE);

ALTER TABLE ONLY TAttributes
    ADD CONSTRAINT TAttributes_pkey PRIMARY KEY (AttributeCODE);

ALTER TABLE ONLY TBiblio
    ADD CONSTRAINT TBiblio_pkey PRIMARY KEY (REFERENCE);

ALTER TABLE ONLY TBiology
    ADD CONSTRAINT TBiology_pkey PRIMARY KEY (BiologyCODE);

ALTER TABLE ONLY TCountry
    ADD CONSTRAINT TCountry_pkey PRIMARY KEY (CountryCode);

ALTER TABLE ONLY TCountsheet
    ADD CONSTRAINT TCountsheet_pkey PRIMARY KEY (CountsheetCODE);

ALTER TABLE ONLY TDatesCalendar
    ADD CONSTRAINT TDatesCalendar_pkey PRIMARY KEY (CalendarCODE);

ALTER TABLE ONLY TDatesMethods
    ADD CONSTRAINT TDatesMethods_pkey PRIMARY KEY (Abbrev);

ALTER TABLE ONLY TDatesPeriod
    ADD CONSTRAINT TDatesPeriod_pkey PRIMARY KEY (PeriodDateCODE);

ALTER TABLE ONLY TDatesRadio
    ADD CONSTRAINT TDatesRadio_pkey PRIMARY KEY (DateCODE);

ALTER TABLE ONLY TDatesTypes
    ADD CONSTRAINT TDatesTypes_pkey PRIMARY KEY (Type);

ALTER TABLE ONLY TDistrib
    ADD CONSTRAINT TDistrib_pkey PRIMARY KEY (DistribCODE);

ALTER TABLE ONLY TEcoDefBugs
    ADD CONSTRAINT TEcoDefBugs_pkey PRIMARY KEY (BugsEcoCODE);

ALTER TABLE ONLY TEcoDefGroups
    ADD CONSTRAINT TEcoDefGroups_pkey PRIMARY KEY (EcoGroupCode);

ALTER TABLE ONLY TEcoDefKoch
    ADD CONSTRAINT TEcoDefKoch_pkey PRIMARY KEY (BugsKochCode);

ALTER TABLE ONLY TFossilUncertainty
    ADD CONSTRAINT TFossilUncertainty_pkey PRIMARY KEY (FossilBugsCODE);

ALTER TABLE ONLY TFossil
    ADD CONSTRAINT TFossil_pkey PRIMARY KEY (FossilBugsCODE);

ALTER TABLE ONLY TINDEXReplacements
    ADD CONSTRAINT TINDEXReplacements_pkey PRIMARY KEY (ReplacedName);

ALTER TABLE ONLY TKeys
    ADD CONSTRAINT TKeys_pkey PRIMARY KEY (KeyCODE);

ALTER TABLE ONLY TLab
    ADD CONSTRAINT TLab_pkey PRIMARY KEY (LabID);

ALTER TABLE ONLY TLookupCountsheetContext
    ADD CONSTRAINT TLookupCountsheetContext_pkey PRIMARY KEY (SheetContext);

ALTER TABLE ONLY TLookupCountsheetTypes
    ADD CONSTRAINT TLookupCountsheetTypes_pkey PRIMARY KEY (CountsheetType);

ALTER TABLE ONLY TLookupMonths
    ADD CONSTRAINT TLookupMonths_pkey PRIMARY KEY (SeasonCode);

ALTER TABLE ONLY TLookupUncertaintyType
    ADD CONSTRAINT TLookupUncertaintyType_pkey PRIMARY KEY (Uncertainty);

ALTER TABLE ONLY TMCRNames
    ADD CONSTRAINT TMCRNames_pkey PRIMARY KEY (CODE);

ALTER TABLE ONLY TMCRSummaryData
    ADD CONSTRAINT TMCRSummaryData_pkey PRIMARY KEY (CODE);

ALTER TABLE ONLY TPeriods
    ADD CONSTRAINT TPeriods_pkey PRIMARY KEY (PeriodCODE);

ALTER TABLE ONLY TRDBCodes
    ADD CONSTRAINT TRDBCodes_pkey PRIMARY KEY (RDBCode);

ALTER TABLE ONLY TRDBSystems
    ADD CONSTRAINT TRDBSystems_pkey PRIMARY KEY (RDBSystemCode);

ALTER TABLE ONLY TRDB
    ADD CONSTRAINT TRDB_pkey PRIMARY KEY (RDBCODE);

ALTER TABLE ONLY TSample
    ADD CONSTRAINT TSample_pkey PRIMARY KEY (SampleCODE);

ALTER TABLE ONLY TSiteOtherProxies
    ADD CONSTRAINT TSiteOtherProxies_pkey PRIMARY KEY (OtherProxyID);

ALTER TABLE ONLY TSiteRef
    ADD CONSTRAINT TSiteRef_pkey PRIMARY KEY (SiteRefCODE);

ALTER TABLE ONLY TSite
    ADD CONSTRAINT TSite_pkey PRIMARY KEY (SiteCODE);

ALTER TABLE ONLY TSpeciesAssociations
    ADD CONSTRAINT TSpeciesAssociations_pkey PRIMARY KEY (SpeciesAssociationID);

ALTER TABLE ONLY TSynonymNotes
    ADD CONSTRAINT TSynonymNotes_pkey PRIMARY KEY (SynonymNoteCODE);

ALTER TABLE ONLY TSynonym
    ADD CONSTRAINT TSynonym_pkey PRIMARY KEY (SynonymCODE);

ALTER TABLE ONLY TTaxoNotes
    ADD CONSTRAINT TTaxoNotes_pkey PRIMARY KEY (TaxoNoteCODE);

CREATE INDEX BuildVersionBugsdata_Component_idx ON BuildVersionBugsdata USING btree (Component);

CREATE INDEX INDEX_IAuthority_idx ON INDEX USING btree (AUTHORITY);

CREATE INDEX INDEX_IFamily_idx ON INDEX USING btree (FAMILY);

CREATE INDEX INDEX_IGenus_idx ON INDEX USING btree (GENUS);

CREATE INDEX INDEX_ISpecies_idx ON INDEX USING btree (SPECIES);

CREATE INDEX TAttributes_PrimaryKey_idx ON TAttributes USING btree (CODE);

CREATE INDEX TBiblio_REFERENCE_idx ON TBiblio USING btree (REFERENCE);

CREATE INDEX TBiology_PrimaryKey_idx ON TBiology USING btree (CODE);

CREATE INDEX TBiology_RefKey_idx ON TBiology USING btree (Ref);

CREATE INDEX TCountry_CountryCode_idx ON TCountry USING btree (CountryCode);

CREATE INDEX TCountsheet_ICountsheetName_idx ON TCountsheet USING btree (CountsheetName);

CREATE INDEX TCountsheet_ISiteCODE_idx ON TCountsheet USING btree (SiteCODE);

CREATE INDEX TCountsheet_SheetContext_idx ON TCountsheet USING btree (SheetContext);

CREATE INDEX TCountsheet_SheetType_idx ON TCountsheet USING btree (SheetType);

CREATE INDEX TDatesCalendar_Date_idx ON TDatesCalendar USING btree (Date);

CREATE INDEX TDatesCalendar_TDatesCalenderSampleCODE_idx ON TDatesCalendar USING btree (SampleCODE);

CREATE INDEX TDatesMethods_IdxSortOrder_idx ON TDatesMethods USING btree (SortOrder);

CREATE INDEX TDatesPeriod_IndexPeriod_idx ON TDatesPeriod USING btree (PeriodCODE);

CREATE INDEX TDatesPeriod_TDatesPeriodSampleCODE_idx ON TDatesPeriod USING btree (SampleCODE);

CREATE INDEX TDatesRadio_IndexAge_idx ON TDatesRadio USING btree (Date);

CREATE INDEX TDatesRadio_IndexMethod_idx ON TDatesRadio USING btree (DatingMethod);

CREATE INDEX TDatesRadio_LabNr_idx ON TDatesRadio USING btree (LabNr);

CREATE INDEX TDatesRadio_SampleCODE_idx ON TDatesRadio USING btree (SampleCODE);

CREATE INDEX TDatesRadio_TDatesRadioLabID_idx ON TDatesRadio USING btree (LabID);

CREATE INDEX TDistrib_PrimaryKey_idx ON TDistrib USING btree (CODE);

CREATE INDEX TDistrib_RefKey_idx ON TDistrib USING btree (Ref);

CREATE INDEX TEcoBugs_IdxCODE_idx ON TEcoBugs USING btree (CODE);

CREATE INDEX TEcoBugs_IdxEcoCODE_idx ON TEcoBugs USING btree (BugsEcoCODE);

CREATE INDEX TEcoDefBugs_IdxSortOrder_idx ON TEcoDefBugs USING btree (SortOrder);

CREATE INDEX TEcoDefKoch_BugsKochCode_idx ON TEcoDefKoch USING btree (BugsKochCode);

CREATE INDEX TEcoDefKoch_KochCode_idx ON TEcoDefKoch USING btree (KochCode);

CREATE INDEX TEcoKoch_CODE_idx ON TEcoKoch USING btree (CODE);

CREATE INDEX TEcoKoch_RobinsonCODE_idx ON TEcoKoch USING btree (BugsKochCode);

CREATE INDEX TFossilUncertainty_FossilBugsCODE_idx ON TFossilUncertainty USING btree (FossilBugsCODE);

CREATE INDEX TFossil_CODE_idx ON TFossil USING btree (CODE);

CREATE INDEX TFossil_FossilBugsCODE_idx ON TFossil USING btree (FossilBugsCODE);

CREATE INDEX TFossil_SampleCODE_idx ON TFossil USING btree (SampleCODE);

CREATE INDEX TINDEXReplacements_CODE_idx ON TINDEXReplacements USING btree (CODE);

CREATE INDEX TINDEXReplacements_SiteCODE_idx ON TINDEXReplacements USING btree (SiteCODE);

CREATE INDEX TKeys_PrimaryKey_idx ON TKeys USING btree (CODE);

CREATE INDEX TKeys_RefKey_idx ON TKeys USING btree (Ref);

CREATE INDEX TLab_IndexLabName_idx ON TLab USING btree (Labname);

CREATE INDEX TLab_Indexemail_idx ON TLab USING btree (email);

CREATE INDEX TLab_Indexwebsite_idx ON TLab USING btree (Website);

CREATE INDEX TLab_LabID_idx ON TLab USING btree (LabID);

CREATE INDEX TLookupCountsheetContext_SortOrder_idx ON TLookupCountsheetContext USING btree (SortOrder);

CREATE INDEX TLookupCountsheetTypes_SortOrder_idx ON TLookupCountsheetTypes USING btree (SortOrder);

CREATE INDEX TLookupMonths_SortOrder_idx ON TLookupMonths USING btree (SortOrder);

CREATE INDEX TLookupUncertaintyType_SortOrder_idx ON TLookupUncertaintyType USING btree (SortOrder);

CREATE INDEX TMCRNames_tempCODE_idx ON TMCRNames USING btree (tempCODE);

CREATE INDEX TPeriods_IndexBegin_idx ON TPeriods USING btree (Begin);

CREATE INDEX TPeriods_IndexEnd_idx ON TPeriods USING btree (End);

CREATE INDEX TRDBCodes_IndexSystem_idx ON TRDBCodes USING btree (RDBSystemCode);

CREATE INDEX TRDBCodes_RDBCode_idx ON TRDBCodes USING btree (RDBCode);

CREATE INDEX TRDBSystems_CountryCode_idx ON TRDBSystems USING btree (CountryCode);

CREATE INDEX TRDB_CountryCode_idx ON TRDB USING btree (CountryCode);

CREATE INDEX TRDB_PrimaryKey_idx ON TRDB USING btree (CODE);

CREATE INDEX TRDB_TRDBRDBCode_idx ON TRDB USING btree (RDBCode);

CREATE INDEX TSample_CountsheetCODE_idx ON TSample USING btree (CountsheetCODE);

CREATE INDEX TSample_SiteCODE_idx ON TSample USING btree (SiteCODE);

CREATE INDEX TSeasonActiveAdult_CODE_idx ON TSeasonActiveAdult USING btree (CODE);

CREATE INDEX TSeasonActiveAdult_CountryID_idx ON TSeasonActiveAdult USING btree (CountryCode);

CREATE INDEX TSeasonActiveAdult_HSeason_idx ON TSeasonActiveAdult USING btree (HSeason);

CREATE INDEX TSiteOtherProxies_OtherProxyID_idx ON TSiteOtherProxies USING btree (OtherProxyID);

CREATE INDEX TSiteOtherProxies_TSiteRefSiteCODE_idx ON TSiteOtherProxies USING btree (SiteCODE);

CREATE INDEX TSiteRef_TSiteRefSiteCODE_idx ON TSiteRef USING btree (SiteCODE);

CREATE INDEX TSite_IDBy_idx ON TSite USING btree (IDBy);

CREATE INDEX TSite_SiteCODE_idx ON TSite USING btree (SiteCODE);

CREATE INDEX TSite_SiteName_idx ON TSite USING btree (SiteName);

CREATE INDEX TSpeciesAssociations_AssociatedSpeciesCODE_idx ON TSpeciesAssociations USING btree (AssociatedSpeciesCODE);

CREATE INDEX TSpeciesAssociations_AssociationType_idx ON TSpeciesAssociations USING btree (AssociationType);

CREATE INDEX TSpeciesAssociations_CODE_idx ON TSpeciesAssociations USING btree (CODE);

CREATE INDEX TSpeciesAssociations_RefKey_idx ON TSpeciesAssociations USING btree (Ref);

CREATE INDEX TSpeciesAssociations_SpeciesAssociationID_idx ON TSpeciesAssociations USING btree (SpeciesAssociationID);

CREATE INDEX TSynonymNotes_CODE_idx ON TSynonymNotes USING btree (CODE);

CREATE INDEX TSynonymNotes_Ref_idx ON TSynonymNotes USING btree (Notes);

CREATE INDEX TSynonym_CODE_idx ON TSynonym USING btree (CODE);

CREATE INDEX TSynonym_Ref_idx ON TSynonym USING btree (Ref);

CREATE INDEX TTaxoNotes_PrimaryKey_idx ON TTaxoNotes USING btree (CODE);

CREATE INDEX TTaxoNotes_RefKey_idx ON TTaxoNotes USING btree (Ref);

ALTER TABLE ONLY TAttributes
    ADD CONSTRAINT TAttributes_CODE_fk FOREIGN KEY (CODE) REFERENCES INDEX(CODE) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TBiology
    ADD CONSTRAINT TBiology_CODE_fk FOREIGN KEY (CODE) REFERENCES INDEX(CODE) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TBiology
    ADD CONSTRAINT TBiology_Ref_fk FOREIGN KEY (Ref) REFERENCES TBiblio(REFERENCE) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TCountsheet
    ADD CONSTRAINT TCountsheet_SiteCODE_fk FOREIGN KEY (SiteCODE) REFERENCES TSite(SiteCODE) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TDatesCalendar
    ADD CONSTRAINT TDatesCalendar_SampleCODE_fk FOREIGN KEY (SampleCODE) REFERENCES TSample(SampleCODE) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TDatesPeriod
    ADD CONSTRAINT TDatesPeriod_SampleCODE_fk FOREIGN KEY (SampleCODE) REFERENCES TSample(SampleCODE) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TDatesRadio
    ADD CONSTRAINT TDatesRadio_SampleCODE_fk FOREIGN KEY (SampleCODE) REFERENCES TSample(SampleCODE) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TDistrib
    ADD CONSTRAINT TDistrib_CODE_fk FOREIGN KEY (CODE) REFERENCES INDEX(CODE) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TDistrib
    ADD CONSTRAINT TDistrib_Ref_fk FOREIGN KEY (Ref) REFERENCES TBiblio(REFERENCE) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TEcoBugs
    ADD CONSTRAINT TEcoBugs_BugsEcoCODE_fk FOREIGN KEY (BugsEcoCODE) REFERENCES TEcoDefBugs(BugsEcoCODE) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TEcoBugs
    ADD CONSTRAINT TEcoBugs_CODE_fk FOREIGN KEY (CODE) REFERENCES INDEX(CODE) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TEcoDefKoch
    ADD CONSTRAINT TEcoDefKoch_KochGroup_fk FOREIGN KEY (KochGroup) REFERENCES TEcoDefGroups(EcoGroupCode) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TEcoKoch
    ADD CONSTRAINT TEcoKoch_BugsKochCode_fk FOREIGN KEY (BugsKochCode) REFERENCES TEcoDefKoch(BugsKochCode) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TEcoKoch
    ADD CONSTRAINT TEcoKoch_CODE_fk FOREIGN KEY (CODE) REFERENCES INDEX(CODE) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TFossilUncertainty
    ADD CONSTRAINT TFossilUncertainty_FossilBugsCODE_fk FOREIGN KEY (FossilBugsCODE) REFERENCES TFossil(FossilBugsCODE);

ALTER TABLE ONLY TFossil
    ADD CONSTRAINT TFossil_CODE_fk FOREIGN KEY (CODE) REFERENCES INDEX(CODE) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TFossil
    ADD CONSTRAINT TFossil_SampleCODE_fk FOREIGN KEY (SampleCODE) REFERENCES TSample(SampleCODE) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TKeys
    ADD CONSTRAINT TKeys_CODE_fk FOREIGN KEY (CODE) REFERENCES INDEX(CODE) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TMCRNames
    ADD CONSTRAINT TMCRNames_CODE_fk FOREIGN KEY (CODE) REFERENCES INDEX(CODE) ON UPDATE CASCADE;

ALTER TABLE ONLY TMCRSummaryData
    ADD CONSTRAINT TMCRSummaryData_CODE_fk FOREIGN KEY (CODE) REFERENCES INDEX(CODE) ON UPDATE CASCADE;

ALTER TABLE ONLY TPeriods
    ADD CONSTRAINT TPeriods_PeriodRef_fk FOREIGN KEY (PeriodRef) REFERENCES TBiblio(REFERENCE) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TRDBSystems
    ADD CONSTRAINT TRDBSystems_CountryCode_fk FOREIGN KEY (CountryCode) REFERENCES TCountry(CountryCode) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TRDBSystems
    ADD CONSTRAINT TRDBSystems_Ref_fk FOREIGN KEY (Ref) REFERENCES TBiblio(REFERENCE) ON UPDATE CASCADE;

ALTER TABLE ONLY TRDB
    ADD CONSTRAINT TRDB_CODE_fk FOREIGN KEY (CODE) REFERENCES INDEX(CODE) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TRDB
    ADD CONSTRAINT TRDB_CountryCode_fk FOREIGN KEY (CountryCode) REFERENCES TCountry(CountryCode) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TSample
    ADD CONSTRAINT TSample_SiteCODE_fk FOREIGN KEY (SiteCODE) REFERENCES TSite(SiteCODE) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TSeasonActiveAdult
    ADD CONSTRAINT TSeasonActiveAdult_CODE_fk FOREIGN KEY (CODE) REFERENCES INDEX(CODE) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TSeasonActiveAdult
    ADD CONSTRAINT TSeasonActiveAdult_CountryCode_fk FOREIGN KEY (CountryCode) REFERENCES TCountry(CountryCode) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TSiteOtherProxies
    ADD CONSTRAINT TSiteOtherProxies_SiteCODE_fk FOREIGN KEY (SiteCODE) REFERENCES TSite(SiteCODE) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TSiteRef
    ADD CONSTRAINT TSiteRef_Ref_fk FOREIGN KEY (Ref) REFERENCES TBiblio(REFERENCE) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TSiteRef
    ADD CONSTRAINT TSiteRef_SiteCODE_fk FOREIGN KEY (SiteCODE) REFERENCES TSite(SiteCODE) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TSpeciesAssociations
    ADD CONSTRAINT TSpeciesAssociations_CODE_fk FOREIGN KEY (CODE) REFERENCES INDEX(CODE) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TSynonym
    ADD CONSTRAINT TSynonym_CODE_fk FOREIGN KEY (CODE) REFERENCES INDEX(CODE) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TTaxoNotes
    ADD CONSTRAINT TTaxoNotes_CODE_fk FOREIGN KEY (CODE) REFERENCES INDEX(CODE) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TTaxoNotes
    ADD CONSTRAINT TTaxoNotes_Ref_fk FOREIGN KEY (Ref) REFERENCES TBiblio(REFERENCE) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY TbirmBEETLEdat
    ADD CONSTRAINT TbirmBEETLEdat_CODE_fk FOREIGN KEY (CODE) REFERENCES INDEX(CODE) ON UPDATE CASCADE;

ALTER TABLE ONLY TCountsheet
    ADD CONSTRAINT fk_TCountsheet_TLookupCountsheetContext_1 FOREIGN KEY (SheetContext) REFERENCES TLookupCountsheetContext(SheetContext);

ALTER TABLE ONLY TCountsheet
    ADD CONSTRAINT fk_TCountsheet_TLookupCountsheetTypes_1 FOREIGN KEY (SheetType) REFERENCES TLookupCountsheetTypes(CountsheetType);

ALTER TABLE ONLY TDatesCalendar
    ADD CONSTRAINT fk_TDatesCalendar_TDatesMethods_1 FOREIGN KEY (DatingMethod) REFERENCES TDatesMethods(Abbrev);

ALTER TABLE ONLY TDatesPeriod
    ADD CONSTRAINT fk_TDatesPeriod_TDatesMethods_1 FOREIGN KEY (DatingMethod) REFERENCES TDatesMethods(Abbrev);

ALTER TABLE ONLY TDatesPeriod
    ADD CONSTRAINT fk_TDatesPeriod_TPeriods_1 FOREIGN KEY (PeriodCODE) REFERENCES TPeriods(PeriodCODE);

ALTER TABLE ONLY TDatesRadio
    ADD CONSTRAINT fk_TDatesRadio_TDatesMethods_1 FOREIGN KEY (DatingMethod) REFERENCES TDatesMethods(Abbrev);

ALTER TABLE ONLY TDatesRadio
    ADD CONSTRAINT fk_TDatesRadio_TLab_1 FOREIGN KEY (LabID) REFERENCES TLab(LabID) ON DELETE CASCADE;

ALTER TABLE ONLY TKeys
    ADD CONSTRAINT fk_TKeys_TBiblio_1 FOREIGN KEY (Ref) REFERENCES TBiblio(REFERENCE);

ALTER TABLE ONLY TRDBCodes
    ADD CONSTRAINT fk_TRDBCodes_TRDBSystems_1 FOREIGN KEY (RDBSystemCode) REFERENCES TRDBSystems(RDBSystemCode);

ALTER TABLE ONLY TRDB
    ADD CONSTRAINT fk_TRDB_TRDBCodes_1 FOREIGN KEY (RDBCode) REFERENCES TRDBCodes(RDBCode);

ALTER TABLE ONLY TSample
    ADD CONSTRAINT fk_TSample_TCountsheet_1 FOREIGN KEY (CountsheetCODE) REFERENCES TCountsheet(CountsheetCODE);

ALTER TABLE ONLY TSpeciesAssociations
    ADD CONSTRAINT fk_TSpeciesAssociations_INDEX_1 FOREIGN KEY (AssociatedSpeciesCODE) REFERENCES INDEX(CODE);

ALTER TABLE ONLY TSpeciesAssociations
    ADD CONSTRAINT fk_TSpeciesAssociations_TBiblio_1 FOREIGN KEY (Ref) REFERENCES TBiblio(REFERENCE);

ALTER TABLE ONLY TSynonym
    ADD CONSTRAINT fk_TSynonym_TBiblio_1 FOREIGN KEY (Ref) REFERENCES TBiblio(REFERENCE);
--
