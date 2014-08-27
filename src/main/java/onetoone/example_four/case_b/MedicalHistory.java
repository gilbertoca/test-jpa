package onetoone.example_four.case_b;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

@Entity
public class MedicalHistory {

    @Id
    String id; // overriding not allowed

     // default join column name is overridden
    @MapsId
    @JoinColumn(name = "FK")
    @OneToOne
    Person patient;

    public MedicalHistory(Person p) {
        this.patient = p;
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
}
