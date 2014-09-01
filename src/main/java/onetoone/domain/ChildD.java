package onetoone.domain;


import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "child_d")
public class ChildD implements Serializable {
    @Id
    private Long id;
    
    @OneToOne    
    @MapsId
    private ParentD parentD;
    
    private String name;

    public ChildD() {
        super();
    }

    public ChildD(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ParentD getParentD() {
        return parentD;
    }

    public void setParentD(ParentD parentD) {
        this.parentD = parentD;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
