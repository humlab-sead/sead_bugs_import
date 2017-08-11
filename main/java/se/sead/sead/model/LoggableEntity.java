package se.sead.sead.model;

import se.sead.bugsimport.tracing.PostEventListener;
import se.sead.utils.errorlog.ErrorLog;
import se.sead.utils.errorlog.SingleMessageErrorLog;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<ErrorLog> errors;
    @Transient
    private boolean flagged;

    public abstract Integer getId();

    public final boolean isNewItem(){
        return getId() == null;
    }

    public List<String> getErrorMessages(){
        if(errors == null){
            return Collections.EMPTY_LIST;
        }
        return errors.stream().map(errorLog -> errorLog.getMessage()).collect(Collectors.toList());
    }

    public boolean isErrorFree(){
        return errors == null;
    }

    @Deprecated
    public void addError(String error){
        addError(new SingleMessageErrorLog(error));
    }

    public void addError(ErrorLog error){
        if(errors == null){
            errors = new ArrayList<>();
        }
        errors.add(error);
    }

    public boolean isFlagged(){
        return flagged;
    }

    public void setFlagged(boolean flagged){
        this.flagged = flagged;
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
