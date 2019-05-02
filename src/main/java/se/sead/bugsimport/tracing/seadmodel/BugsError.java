package se.sead.bugsimport.tracing.seadmodel;

import javax.persistence.*;

@Entity
@Table(name="bugs_errors", schema = "bugs_import")
@SequenceGenerator(name="error_seq", sequenceName = "bugs_errors_bugs_error_id_seq", schema = "bugs_import")
public class BugsError extends BugsInformation{

    @Id
    @GeneratedValue(generator = "error_seq", strategy = GenerationType.IDENTITY)
    @Column(name="bugs_error_id", nullable = false)
    private Integer id;
    @Column
    private String message;

    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return super.toString() + " BugsError{" +
                "message='" + message + '\'' +
                '}';
    }
}
