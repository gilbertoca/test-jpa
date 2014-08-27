package mydomain.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class MedicalHistory {
// default join column name is overridden
    @Id
    @OneToOne
    @JoinColumn(name = "FK")
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

}
