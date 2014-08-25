package onetoone.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class ParentB {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToOne
    private ChildB child;

    public ParentB() {
    }

    public ParentB(String name, ChildB child) {
        this.name = name;
        this.child = child;
    }

    public ParentB(String name) {
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

    public ChildB getchild() {
        return child;
    }

    public void setchild(ChildB child) {
        this.child = child;
    }
    @Override
    public String toString() {
        return "Parent [id=" + id + ", name=" + name + ", child="
                + child.getName() + "]";
    }
}
