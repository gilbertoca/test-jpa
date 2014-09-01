package org.hibernate.test;

import org.junit.*;
import javax.persistence.*;
import java.util.List;
import onetoone.domain.*;


public class SimpleBTest {

    @Test
    public void testSimple() {
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SimpleBTest");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
//        try
//        {
        tx.begin();

        ParentB parentB1 = new ParentB("Jakab Gipsz");
        ChildB child1 = new ChildB("java");

        em.persist(parentB1);
        child1.setParentB(parentB1);
        parentB1.setChild(child1);
        em.persist(child1);

        ParentB parentB2 = new ParentB("Captain Nemo");
        ChildB child2 = new ChildB("java java");

        em.persist(parentB2);
        child2.setParentB(parentB2);
        parentB2.setChild(child2);
        em.persist(child2);

        em.flush();

        Query query = em.createQuery("SELECT b FROM ParentB b");
        List<ParentB> list = (List<ParentB>) query.getResultList();
        System.out.println(list);

        query = em.createQuery("SELECT b FROM ChildB b");
        List<ChildB> dList = (List<ChildB>) query.getResultList();
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
