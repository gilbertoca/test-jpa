package onetoone.example_four.case_a;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
//org.hibernate.MappingException: Composite-id class must implement Serializable: onetoone.example_four.case_a.MedicalHistory
public class MedicalHistory implements Serializable{
// default join column name is overridden
    @Id
    @OneToOne
    @JoinColumn(name = "FK")
    Person patient;

    public MedicalHistory() {
    }
    public MedicalHistory(Person p) {
        this.patient = p;
    }

    public Person getPatient() {
        return patient;
    }

    public void setPatient(Person patient) {
        this.patient = patient;
    }

}
