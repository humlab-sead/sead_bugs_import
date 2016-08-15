package se.sead.sead.model;

import javax.persistence.*;

@Entity
@Table(name="tbl_sample_group_sampling_contexts")
@SequenceGenerator(name="sampling_context_id_seq",sequenceName = "tbl_sample_group_sampling_contexts_sampling_context_id_seq")
public class SamplingContext extends LoggableEntity{

    @Id
    @GeneratedValue(generator = "sampling_context_id_seq", strategy = GenerationType.AUTO)
    @Column(name="sampling_context_id", nullable = false)
    private Integer id;
    @Column(name="sampling_context", nullable = false)
    private String name;
    @Column(name="description")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id){
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SamplingContext that = (SamplingContext) o;

        if (!id.equals(that.id)) return false;
        if (!name.equals(that.name)) return false;
        return description != null ? description.equals(that.description) : that.description == null;

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SamplingContext{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
