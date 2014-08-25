package onetoone.domain;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * Reference: http://www.java2s.com/Tutorial/Java/0355__JPA/Catalog0355__JPA.htm
 */
public class MainC {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistenceUnit");
    static EntityManager em = emf.createEntityManager();

    public static void main(String[] a) throws Exception {
        em.getTransaction().begin();

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

        em.getTransaction().commit();
        em.close();
        emf.close();

    }
}
