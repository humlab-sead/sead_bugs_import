package se.sead.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import se.sead.Application;
import se.sead.bugsimport.tracing.TraceEventManager;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

/**
 * Created by erer0001 on 2016-05-12.
 */
public class PostEventListener {

    @Autowired
    private TraceEventManager traceEventManager;

    @PostPersist
    public void onPersist(Object entity){
        ensureAutowire();
        if(entity instanceof LoggableEntity){
            addEvent((LoggableEntity)entity, true);
        }
    }

    private void ensureAutowire(){
        Application.LazyAutowireHelper.getInstance().autowire(this, this.traceEventManager);
    }

    private void addEvent(LoggableEntity entity, boolean insert){
        traceEventManager.addEvent(entity, insert);
    }

    @PostUpdate
    public void onUpdate(Object entity){
        ensureAutowire();
        if(entity instanceof LoggableEntity){
            addEvent((LoggableEntity)entity, false);
        }
    }
}
