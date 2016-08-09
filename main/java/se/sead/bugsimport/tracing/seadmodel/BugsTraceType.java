package se.sead.bugsimport.tracing.seadmodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by erer0001 on 2016-05-18.
 */
public enum BugsTraceType {
    INSERT,
    ERROR,
    UPDATE
}
