package se.sead.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import se.sead.Application;
import se.sead.bugsimport.tracing.TraceEventManager;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.PostPersist;

/**
 * Created by erer0001 on 2016-05-12.
 */
public class PostEventListener {

    @Autowired
    private TraceEventManager traceEventManager;

    @PostPersist
    public void event(Object entity){
        ensureAutowire();
        if(entity instanceof LoggableEntity){
            traceEventManager.addEvent((LoggableEntity) entity);
        }
    }

    private void ensureAutowire(){
        Application.LazyAutowireHelper.getInstance().autowire(this, this.traceEventManager);
    }
}
