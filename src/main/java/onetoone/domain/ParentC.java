package onetoone.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "parent_c")
public class ParentC implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "parentC")
    private ChildC child;

    public ParentC() {
    }

    public ParentC(String name, ChildC child) {
        this.name = name;
        this.child = child;
    }

    public ParentC(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChildC getChild() {
        return child;
    }

    public void setChild(ChildC child) {
        this.child = child;
    }
    @Override
    public String toString() {
        return "Parent [id=" + id + ", name=" + name + ", child="
                + child.getName() + "]";
    }
}
