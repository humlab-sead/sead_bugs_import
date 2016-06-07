package se.sead.bugsimport.tracing;

import org.springframework.stereotype.Component;
import se.sead.sead.model.LoggableEntity;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;

import javax.persistence.Table;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by erer0001 on 2016-05-12.
 */
@Component
public class TraceEventManager {

    private List<BugsTrace> tracesForInsertions;

    public TraceEventManager(){
        reset();
    }

    public void reset(){
        tracesForInsertions = Collections.EMPTY_LIST;
    }

    public List<BugsTrace> getTracesAndReset(){
        List<BugsTrace> copy = this.tracesForInsertions;
        reset();
        return copy;
    }

    public void addEvent(LoggableEntity entity){
        initEventList();
        String tableName = getTableNameFromClassAnnotation(entity);
        BugsTrace trace = new BugsTrace();
        trace.setSeadTable(tableName);
        trace.setSeadId(entity.getId());
        tracesForInsertions.add(trace);
    }

    private void initEventList(){
        if(tracesForInsertions == Collections.EMPTY_LIST){
            tracesForInsertions = new ArrayList<>();
        }
    }

    private String getTableNameFromClassAnnotation(Object entity) {
        Class entityClass = entity.getClass();
        Annotation annotation = entityClass.getAnnotation(Table.class);
        if(annotation instanceof Table){
            return ((Table) annotation).name();
        }
        throw new IllegalStateException("No table name defined for entity: " + entity);
    }
}
