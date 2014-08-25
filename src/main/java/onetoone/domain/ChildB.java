package onetoone.domain;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class ChildB {
    @Id
    @OneToOne
    private ParentB parentB;
    private String name;

    public ChildB() {
        super();
    }

    public ChildB(String name) {
        this.name = name;
    }

    public ParentB getParentB() {
        return parentB;
    }

    public void setParentB(ParentB parentB) {
        this.parentB = parentB;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
