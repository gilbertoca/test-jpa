package onetoone.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * Reference: http://www.java2s.com/Tutorial/Java/0355__JPA/Catalog0355__JPA.htm
 */
public class MainB {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistenceUnit");
    static EntityManager em = emf.createEntityManager();

    public static void main(String[] a) throws Exception {
        em.getTransaction().begin();

        ParentB parentB1 = new ParentB("Jakab Gipsz");
        ChildB child1 = new ChildB("java");

        em.persist(parentB1);
        child1.setParentB(parentB1);
        parentB1.setchild(child1);
        em.persist(child1);

        ParentB parentB2 = new ParentB("Captain Nemo");
        ChildB child2 = new ChildB("java java");

        em.persist(parentB2);
        child2.setParentB(parentB2);
        parentB2.setchild(child2);
        em.persist(child2);

        em.flush();

        Query query = em.createQuery("SELECT b FROM ParentB b");
        List<ParentB> list = (List<ParentB>) query.getResultList();
        System.out.println(list);

        query = em.createQuery("SELECT b FROM ChildB b");
        List<ChildB> dList = (List<ChildB>) query.getResultList();
        System.out.println(dList);

        em.getTransaction().commit();
        em.close();
        emf.close();
    }
}
