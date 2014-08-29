package onetoone.example_four.case_b;

import javax.persistence.*;

@Entity
public class Person {

    @Id
    String ssn;

    String name;

    public Person() {
    }

    public Person(String ssn, String name) {
        this.ssn = ssn;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return ssn;
    }
}
