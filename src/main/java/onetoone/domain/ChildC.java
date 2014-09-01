package onetoone.domain;


import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "child_C")
public class ChildC implements Serializable {
    @Id
    @Column(name = "parent_id")
    private Long id;
    
    @OneToOne    
    @MapsId
    @JoinColumn(name = "parent_id")
    private ParentC parentC;
    
    @Column(name = "name")
    private String name;

    public ChildC() {
        super();
    }

    public ChildC(String name) {
        this.name = name;
    }

    public ParentC getParentC() {
        return parentC;
    }

    public void setParentC(ParentC parentC) {
        this.parentC = parentC;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
