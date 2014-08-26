package onetoone.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "parent_d")
public class ParentD implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToOne(mappedBy = "parentD")
    private ChildD child;

    public ParentD() {
    }

    public ParentD(String name, ChildD child) {
        this.name = name;
        this.child = child;
    }

    public ParentD(String name) {
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

    public ChildD getChild() {
        return child;
    }

    public void setChild(ChildD child) {
        this.child = child;
    }
    @Override
    public String toString() {
        return "Parent [id=" + id + ", name=" + name + ", child="
                + child.getName() + "]";
    }
}
