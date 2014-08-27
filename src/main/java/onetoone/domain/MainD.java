package onetoone.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * Reference: http://www.java2s.com/Tutorial/Java/0355__JPA/Catalog0355__JPA.htm
 */
public class MainD {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistenceUnit");
    static EntityManager em = emf.createEntityManager();

    public static void main(String[] a) throws Exception {
        em.getTransaction().begin();

        ParentD parentD1 = new ParentD("Jakab Gipsz");
        ChildD child1 = new ChildD("java");

        em.persist(parentD1);
        child1.setParentD(parentD1);
        em.persist(child1);

        ParentD parentD2 = new ParentD("Captain Nemo");
        ChildD child2 = new ChildD("java java");

        em.persist(parentD2);
        child2.setParentD(parentD2);
        em.persist(child2);

        em.flush();

        Query query = em.createQuery("SELECT b FROM ParentD b");
        List<ParentD> list = (List<ParentD>) query.getResultList();
        System.out.println(list);

        query = em.createQuery("SELECT b FROM ChildD b");
        List<ChildD> dList = (List<ChildD>) query.getResultList();
        System.out.println(dList);

        em.getTransaction().commit();
        em.close();
        emf.close();
    }
}
