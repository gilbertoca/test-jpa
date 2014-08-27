package org.datanucleus.test;

import org.junit.*;
import javax.persistence.*;
import java.util.List;
import static org.junit.Assert.*;
import onetoone.domain.*;
import org.datanucleus.util.NucleusLogger;

public class SimpleDTest
{
    @Test
    public void testSimple()
    {
        NucleusLogger.GENERAL.info(">> test START");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SimpleDTest");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
//        try
//        {
            tx.begin();

	    ParentD ParentD1 = new ParentD("Jakab Gipsz");
	    ChildD child1 = new ChildD("java");

	    em.persist(ParentD1);
	    child1.setParentD(ParentD1);
	    em.persist(child1);

	    ParentD ParentD2 = new ParentD("Captain Nemo");
	    ChildD child2 = new ChildD("java java");

	    em.persist(ParentD2);
	    child2.setParentD(ParentD2);
	    em.persist(child2);

	    em.flush();

	    Query query = em.createQuery("SELECT b FROM ParentD b");
	    List<ParentD> list = (List<ParentD>) query.getResultList();
	    System.out.println(list);

	    query = em.createQuery("SELECT b FROM ChildD b");
	    List<ChildD> dList = (List<ChildD>) query.getResultList();
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
