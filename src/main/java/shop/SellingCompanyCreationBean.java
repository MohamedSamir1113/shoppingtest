package shop;

import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.*;
import shop.SellingCompanyRep;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Singleton
@Startup
public class SellingCompanyCreationBean {
    private List<SellingCompanyRep> sellingCompanyReps = new ArrayList<>();
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String OTHER_CHAR = "!@#$%&*()_+-=[]?";

    private static final String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;
    private static final int PASSWORD_LENGTH = 12;

    private static Random random = new Random();

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");

    private final EntityManager em = emf.createEntityManager();

    @Lock(LockType.WRITE)
    public void createSellingCompany(SellingCompanyRep sellingCompanyRep) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String password = generatePassword();
            sellingCompanyRep.setPassword(password);
            em.persist(sellingCompanyRep);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new RuntimeException("Error creating selling company: " + e.getMessage());
        }
    }

    public String generatePassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        int index = 0;
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            index = random.nextInt(PASSWORD_ALLOW_BASE.length());
            password.append(PASSWORD_ALLOW_BASE.charAt(index));
        }
        return password.toString();
    }
}