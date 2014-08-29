package onetoone.example_four.case_b;

import java.util.List;
import javax.persistence.*;
import org.datanucleus.util.NucleusLogger;
import org.junit.*;

public class SimpleCaseBTest {

    @Test
    public void testSimple() {
        NucleusLogger.GENERAL.info(">> test START");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SimpleCaseBTest");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
//        try
//        {
        tx.begin();

        Person person1 = new Person(1, "Jakab Gipsz");
        MedicalHistory medicalHistory1 = new MedicalHistory();
        person1.setMedicalHistory(medicalHistory1);
        em.persist(person1);

        Person person2 = new Person(2, "Captain Nemo");
        MedicalHistory medicalHistory2 = new MedicalHistory();
        person2.setMedicalHistory(medicalHistory2);
        em.persist(person2);

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
        NucleusLogger.GENERAL.info(">> test END");
    }
}
