package se.sead.sead.model;

import se.sead.bugsimport.tracing.PostEventListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@MappedSuperclass
@EntityListeners({PostEventListener.class})
public abstract class LoggableEntity {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date_updated")
    private Date dateUpdated;

    @Transient
    private boolean markedForDeletion;
    @Transient
    private boolean updated = false;
    @Transient
    private List<String> errors;

    public abstract Integer getId();

    public final boolean isNewItem(){
        return getId() == null;
    }

    public List<String> getErrors(){
        if(errors == null){
            return Collections.EMPTY_LIST;
        }
        return errors;
    }

    public boolean isErrorFree(){
        return errors == null;
    }

    public void addError(String error){
        if(errors == null){
            errors = new ArrayList<>();
        }
        errors.add(error);
    }

    public void markForDeletion(){
        if(getId() != null){
            markedForDeletion = true;
        }
    }

    public boolean isMarkedForDeletion(){
        return markedForDeletion;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
}
