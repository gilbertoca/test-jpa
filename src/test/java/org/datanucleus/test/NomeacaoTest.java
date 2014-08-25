package org.datanucleus.test;

import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.*;
import javax.persistence.*;

import static org.junit.Assert.*;
import org.datanucleus.util.NucleusLogger;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.datatype.IDataTypeFactory;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

public class NomeacaoTest
{
    private static IDatabaseConnection connection;
    private static IDataSet dataset;
    private static EntityManagerFactory emf;
    private static EntityManager em;
    
    @BeforeClass
    public static void initEntityManager() throws Exception {
        emf = Persistence.createEntityManagerFactory("MyTest");
        em = emf.createEntityManager();

        // Initializes DBUnit
        // For now, getting connection from one JPA provider is impossible
        // connection = new DatabaseConnection(em.unwrap(java.sql.Connection.class));
        // So, let's take a work around ...
        //Let's create a new connection to work with DBUnit
        Class.forName("org.h2.Driver");
        connection = new DatabaseConnection(DriverManager.getConnection("jdbc:h2:file:data/sapeoDB;create=true;INIT=CREATE SCHEMA IF NOT EXISTS SAPEO;MODE=PostgreSQL;DB_CLOSE_DELAY=-1", "sa", ""),"SAPEO");

        // http://dbunit.sourceforge.net/faq.html#typefactory
        DatabaseConfig config = connection.getConfig();
        //How to get new instance of H2DataTypeFactory|OracleDataTypeFactory|PostgresqlDataTypeFactory
        IDataTypeFactory dataTypeFactory = (IDataTypeFactory) Class.forName("org.dbunit.ext.h2.H2DataTypeFactory").newInstance();
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, dataTypeFactory);
        config.setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, Boolean.TRUE);
        dataset = new FlatXmlDataSetBuilder().build(NomeacaoTest.class.getResourceAsStream("mydomain-data.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);
    }

    @AfterClass
    public static void closeEntityManager() throws SQLException, DatabaseUnitException {
        em.close();
        emf.close();
        //before we close the connection
        DatabaseOperation.DELETE_ALL.execute(connection, dataset);
        connection.close();
    }    
    @Test
    public void testSimple()
    {
        NucleusLogger.GENERAL.info(">> test START");
        EntityTransaction tx = em.getTransaction();
        try
        {
            tx.begin();

            // [INSERT code here to persist object required for testing]
            tx.commit();
        }
        catch (Throwable thr)
        {
            NucleusLogger.GENERAL.error(">> Exception thrown persisting data", thr);
            fail("Failed to persist data : " + thr.getMessage());
        }
        finally 
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            em.close();
        }

        emf.close();
        NucleusLogger.GENERAL.info(">> test END");
    }
}
