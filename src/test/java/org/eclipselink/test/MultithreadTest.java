package org.openjpa.test;

import org.junit.*;
import javax.persistence.*;

import static org.junit.Assert.*;

public class MultithreadTest
{
    @Test
    public void testMulti()
    {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("SimpleBTest");

        try
        {
            // Persist some data
            EntityManager em = emf.createEntityManager();
            EntityTransaction tx = em.getTransaction();
            try
            {
                tx.begin();

                // [Add persistence of sample data for the test]

                tx.commit();
            }
            catch (Throwable thr)
            {
                fail("Exception persisting data : " + thr.getMessage());
            }
            finally
            {
                if (tx.isActive())
                {
                    tx.rollback();
                }
                em.close();
            }

            // Create the Threads
            int THREAD_SIZE = 500;
            final String[] threadErrors = new String[THREAD_SIZE];
            Thread[] threads = new Thread[THREAD_SIZE];
            for (int i = 0; i < THREAD_SIZE; i++)
            {
                final int threadNo = i;
                threads[i] = new Thread(new Runnable()
                {
                    public void run()
                    {
                        String errorMsg = performTest(emf);
                        threadErrors[threadNo] = errorMsg;
                    }
                });
            }

            // Run the Threads
            for (int i = 0; i < THREAD_SIZE; i++)
            {
                threads[i].start();
            }
            for (int i = 0; i < THREAD_SIZE; i++)
            {
                try
                {
                    threads[i].join();
                }
                catch (InterruptedException e)
                {
                    fail(e.getMessage());
                }
            }

            // Process any errors from threads and fail the test if any failed
            for (String error : threadErrors)
            {
                if (error != null)
                {
                    fail(error);
                }
            }
        }
        finally
        {
            // [Clean up data]
        }

        emf.close();
    }

    /**
     * Method to perform the test for a Thread.
     * @param emf The EntityManagerFactory
     * @return A string which is null if the EM operations are successful
     */
    protected String performTest(EntityManagerFactory emf)
    {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try
        {
            tx.begin();

            // [Add persistence code to perform what is needed by this EM]

            tx.commit();
        }
        catch (Throwable thr)
        {
            return "Exception performing test : " + thr.getMessage();
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            em.close();
        }
        return null;
    }
}
