package se.sead.bugsimport.tracing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.bugsimport.rdbsystems.bugsmodel.BugsRDBSystem;
import se.sead.bugsimport.rdbsystems.seadmodel.RdbSystem;
import se.sead.bugsimport.tracing.accessors.*;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.repositories.BugsTraceRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SeadDataFromTraceHelperTest.Config.class)
@ActiveProfiles("test")
@Sql(statements = {
        "insert into bugs_import.bugs_trace " +
                "(bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id) values "  +
                "('bugstable', 'test:compressed', '1', 'seadtable', 1);",
        "insert into bugs_import.bugs_trace " +
                "(bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id) values " +
                "('nextbugstable', 'test:compressed', '1', 'seadtable', 2);",
        "insert into bugs_import.bugs_trace " +
                "(bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id) values " +
                "('bugstable', 'test:nextcompressed', '2', 'nextseadtable', 3);",
        "insert into bugs_import.bugs_trace " +
                "(bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id) values " +
                "('bugstable', 'test:CasedIdentifier', 'CI', 'nextseadtable', 3);",
        "insert into bugs_import.bugs_trace " +
                "(bugs_table, bugs_data, bugs_identifier, sead_table, sead_reference_id) values " +
                "('bugstable', 'test:CaseIdentifier', 'IC2', 'nextestseadtable', 4);"
})
public class SeadDataFromTraceHelperTest {

    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig {}

    @Autowired
    private BugsTraceRepository traceRepository;

    @Test
    public void bugsIdentifierWithoutSeadTableName(){
        MockSeadDataFromTraceHelper traceHelper = new MockSeadDataFromTraceHelper("bugstable", false);
        traceHelper.setRepository(traceRepository);
        BugsTrace latest = traceHelper.getLatest("1");
        assertEquals("bugstable", latest.getBugsTable());
        assertEquals("test:compressed", latest.getAccessInformationData());
        assertEquals("1", latest.getBugsIdentifier());
        assertEquals("seadtable", latest.getSeadTable());
        assertEquals(1, (int) latest.getSeadId());
    }

    @Test
    public void compressedDataForIdentificationWithoutSeadTableName(){
        MockSeadDataFromTraceHelper traceHelper = new MockSeadDataFromTraceHelper("bugstable", true);
        traceHelper.setRepository(traceRepository);
        BugsTrace latest = traceHelper.getLatest("test:compressed");
        assertEquals("bugstable", latest.getBugsTable());
        assertEquals("test:compressed", latest.getAccessInformationData());
        assertEquals("1", latest.getBugsIdentifier());
        assertEquals("seadtable", latest.getSeadTable());
        assertEquals(1, (int) latest.getSeadId());
    }

    @Test
    public void bugsIdentifierWithSeadTableName(){
        MockSeadDataFromTraceHelper traceHelper = new MockSeadDataFromTraceHelper("bugstable", "nextseadtable", false);
        traceHelper.setRepository(traceRepository);
        BugsTrace latest = traceHelper.getLatest("2");
        assertEquals("bugstable", latest.getBugsTable());
        assertEquals("test:nextcompressed", latest.getAccessInformationData());
        assertEquals("2", latest.getBugsIdentifier());
        assertEquals("nextseadtable", latest.getSeadTable());
        assertEquals(3, (int) latest.getSeadId());
    }

    @Test
    public void compressedDataWithSeadTableName(){
        MockSeadDataFromTraceHelper traceHelper = new MockSeadDataFromTraceHelper("bugstable", "nextseadtable", true);
        traceHelper.setRepository(traceRepository);
        BugsTrace latest = traceHelper.getLatest("test:nextcompressed");
        assertEquals("bugstable", latest.getBugsTable());
        assertEquals("test:nextcompressed", latest.getAccessInformationData());
        assertEquals("2", latest.getBugsIdentifier());
        assertEquals("nextseadtable", latest.getSeadTable());
        assertEquals(3, (int) latest.getSeadId());
    }

    @Test
    public void bugsIdentifierWithoutSeadTableNameIgnoreCase(){
        MockSeadDataFromTraceHelper traceHelper = new MockSeadDataFromTraceHelper(new IgnoreCaseBugsIdentifier("bugstable"));
        traceHelper.setRepository(traceRepository);
        BugsTrace latest = traceHelper.getLatest("ci");
        assertEquals("bugstable", latest.getBugsTable());
        assertEquals("test:CasedIdentifier", latest.getAccessInformationData());
        assertEquals("CI", latest.getBugsIdentifier());
        assertEquals("nextseadtable", latest.getSeadTable());
        assertEquals(3, (int) latest.getSeadId());
    }

    @Test
    public void compressedDataWIthoutSeadTableNameIgnoreCase(){
        MockSeadDataFromTraceHelper traceHelper = new MockSeadDataFromTraceHelper(new IgnoreCaseCompressedData("bugstable"));
        traceHelper.setRepository(traceRepository);
        BugsTrace latest = traceHelper.getLatest("test:casedidentifier");
        assertEquals("bugstable", latest.getBugsTable());
        assertEquals("test:CasedIdentifier", latest.getAccessInformationData());
        assertEquals("CI", latest.getBugsIdentifier());
        assertEquals("nextseadtable", latest.getSeadTable());
        assertEquals(3, (int) latest.getSeadId());
    }

    @Test
    public void bugsIdentifierIgnoreCaseWithSeadTableName(){
        MockSeadDataFromTraceHelper traceHelper = new MockSeadDataFromTraceHelper(new IgnoreCaseBugsIdentifierWithSeadDataTable("bugstable", "nextestseadtable"));
        traceHelper.setRepository(traceRepository);
        BugsTrace latest = traceHelper.getLatest("ic2");
        assertEquals("bugstable", latest.getBugsTable());
        assertEquals("test:CaseIdentifier", latest.getAccessInformationData());
        assertEquals("IC2", latest.getBugsIdentifier());
        assertEquals("nextestseadtable", latest.getSeadTable());
        assertEquals(4, (int) latest.getSeadId());
    }

    @Test
    public void compressedDataIgnoreCaseWithSeadTableName(){
        MockSeadDataFromTraceHelper traceHelper = new MockSeadDataFromTraceHelper(new IgnoreCaseCompressedDataWithSeadDataTable("bugstable", "nextestseadtable"));
        traceHelper.setRepository(traceRepository);
        BugsTrace latest = traceHelper.getLatest("test:caseidentifier");
        assertEquals("bugstable", latest.getBugsTable());
        assertEquals("test:CaseIdentifier", latest.getAccessInformationData());
        assertEquals("IC2", latest.getBugsIdentifier());
        assertEquals("nextestseadtable", latest.getSeadTable());
        assertEquals(4, (int) latest.getSeadId());
    }

    public static class MockSeadDataFromTraceHelper extends SeadDataFromTraceHelper<BugsRDBSystem, RdbSystem> {

        public MockSeadDataFromTraceHelper(String bugsTableName, boolean useCompressedDataForIdentification) {
            super(bugsTableName, useCompressedDataForIdentification, null);
        }

        public MockSeadDataFromTraceHelper(String bugsTableName, String seadTableName, boolean useCompressedDataForIdentification) {
            super(bugsTableName, seadTableName, useCompressedDataForIdentification, null);
        }

        public MockSeadDataFromTraceHelper(TraceAccessor traceAccessor) {
            super(traceAccessor, null);
        }

        void setRepository(BugsTraceRepository repository){
            super.traceRepository = repository;
        }
    }
}