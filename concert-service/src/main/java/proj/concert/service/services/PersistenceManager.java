package proj.concert.service.services;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Singleton class that manages an EntityManagerFactory. When a
 * PersistenceManager is instantiated, it creates an EntityManagerFactory. An
 * EntityManagerFactory is required to create an EntityManager, which represents
 * a persistence context (session with a database).
 * <p>
 * When a web service application component (e.g. a resource object) requires a
 * persistence context, it should call the PersistentManager's
 * createEntityManager() method to acquire one.
 * <p>
 * This class is complete - you do not need to modify it.
 */
public class PersistenceManager {
    private static PersistenceManager instance = null;

    private EntityManagerFactory entityManagerFactory;

    protected PersistenceManager() {
        entityManagerFactory = Persistence.createEntityManagerFactory("proj.concert");
    }

    public EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public static PersistenceManager instance() {
        if (instance == null) {
            instance = new PersistenceManager();
        }
        return instance;
    }

    // FOR TESTING ONLY! Will wipe the database.
    public void reset() {
        entityManagerFactory.close();
        entityManagerFactory = Persistence.createEntityManagerFactory("proj.concert");
    }

}
