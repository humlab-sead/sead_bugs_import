package se.sead.bugsimport.periods.bugsmodel;

import se.sead.bugsimport.tracing.seadmodel.BugsTrace;

public class PeriodFromTraceBuilder {

    private String originalData;

    public PeriodFromTraceBuilder(BugsTrace trace){
        originalData = trace.getAccessInformationData();
        if(originalData == null || originalData.isEmpty()){
            throw new NullPointerException("No compressed data for original Period");
        }
    }

    public Period createPeriodFromTrace() {
        removeSurroundingCurlyBraces();
        String[] parts = originalData.split("\\" + PeriodBugsTable.BUGS_DATA_DELIMITER);
        return createPeriodFromTrace(parts);
    }

    private void removeSurroundingCurlyBraces() {
        originalData = originalData.replace("{", "").replace("}", "");
    }

    private Period createPeriodFromTrace(String[] parts){
        Period period = new Period();
        period.setPeriodCode(parts[0]);
        period.setName(parts[1]);
        period.setType(parts[2]);
        period.setDesc(parts[3]);
        period.setRef(parts[4]);
        period.setGeography(parts[5]);
        period.setBegin(convertToInteger(parts[6]));
        period.setBeginBCad(parts[7]);
        period.setEnd(convertToInteger(parts[8]));
        period.setEndBCad(parts[9]);
        period.setYearsType(parts[10]);
        return period;
    }

    private Integer convertToInteger(String part) {
        if(part == null || part.isEmpty() || "null".equals(part)){
            return null;
        } else {
            return Integer.parseInt(part);
        }
    }
}
