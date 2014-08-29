package onetoone.example_four.case_b;

import java.util.List;
import javax.persistence.*;
import org.junit.*;

public class SimpleCaseBTest {

    @Test
    public void testSimple() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SimpleCaseBTest");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
//        try
//        {
        tx.begin();

        Person person1 = new Person(1, "Jakab Gipsz");
        em.persist(person1);
        MedicalHistory medicalHistory1 = new MedicalHistory(person1);
        em.persist(medicalHistory1);

        Person person2 = new Person(2, "Captain Nemo");
        em.persist(person2);
        MedicalHistory medicalHistory2 = new MedicalHistory(person2 );
        em.persist(medicalHistory2);

        em.flush();

        Query query = em.createQuery("SELECT b FROM Person b");
        List<Person> list = (List<Person>) query.getResultList();
        System.out.println(list);

        query = em.createQuery("SELECT b FROM MedicalHistory b");
        List<MedicalHistory> dList = (List<MedicalHistory>) query.getResultList();
        System.out.println(dList);

        tx.commit();
//        }
//        catch (Throwable thr)
//        {
//            NucleusLogger.GENERAL.error(">> Exception thrown persisting data", thr);
//            fail("Failed to persist data : " + thr.getMessage());
//        }
//        finally 
//        {
//            if (tx.isActive())
//            {
//                tx.rollback();
//            }
//            em.close();
//        }

        emf.close();
    }
}
