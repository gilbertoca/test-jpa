package org.datanucleus.test;

import org.junit.*;
import javax.persistence.*;
import java.util.List;
import static org.junit.Assert.*;
import onetoone.domain.*;
import org.datanucleus.util.NucleusLogger;

public class SimpleTest
{
    @Test
    public void testSimple()
    {
        NucleusLogger.GENERAL.info(">> test START");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MyTest");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
//        try
//        {
            tx.begin();

	    ParentC parentC1 = new ParentC("Jakab Gipsz");
	    ChildC child1 = new ChildC("java");

	    em.persist(parentC1);
	    child1.setParentC(parentC1);
	    parentC1.setchild(child1);
	    em.persist(child1);

	    ParentC parentC2 = new ParentC("Captain Nemo");
	    ChildC child2 = new ChildC("java java");

	    em.persist(parentC2);
	    child2.setParentC(parentC2);
	    parentC2.setchild(child2);
	    em.persist(child2);

	    em.flush();

	    Query query = em.createQuery("SELECT b FROM ParentC b");
	    List<ParentC> list = (List<ParentC>) query.getResultList();
	    System.out.println(list);

	    query = em.createQuery("SELECT b FROM ChildC b");
	    List<ChildC> dList = (List<ChildC>) query.getResultList();
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
