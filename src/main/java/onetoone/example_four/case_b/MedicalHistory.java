package onetoone.example_four.case_b;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

@Entity
public class MedicalHistory {

    @Id
    String id; // overriding not allowed

    @MapsId
    @OneToOne
    Person patient;

    public MedicalHistory() {
    }

    public Person getPatient() {
        return patient;
    }

    public void setPatient(Person patient) {
        this.patient = patient;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}
