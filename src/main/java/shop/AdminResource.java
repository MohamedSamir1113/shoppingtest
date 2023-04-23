package shop;

import jakarta.annotation.Resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import jakarta.persistence.Query;
import jakarta.transaction.*;
import jakarta.transaction.NotSupportedException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Random;

@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminResource {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");

    private final EntityManager entityManager = emf.createEntityManager();

    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String OTHER_CHAR = "!@#$%&*()_+-=[]?";

    private static final String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;
    private static final int PASSWORD_LENGTH = 12;

    private static Random random = new Random();

    @Resource
    private UserTransaction userTransaction;


    private ShippingCompanyResource companyResource;


    private CoveredRegionResource regionResource;

    @GET
    @Path("/welcome")
    public String welcomeMessage() {
        return "Hello Customer!";
    }

    @POST
    @Path("/register")
    public String registerAdmin(Admin admin) {
        try {
            userTransaction.begin();
        } catch (NotSupportedException | SystemException e) {
            throw new RuntimeException(e);
        }
        entityManager.persist(admin);
        try {
            userTransaction.commit();
        } catch (RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException |
                 IllegalStateException | SystemException e) {
            e.printStackTrace();
        }
        return "Admin registered successfully!\n" + "Welcome Admin: " + admin.getName();
    }

    @POST
    @Path("/login")
    public String loginCustomer(Admin admin) {
        Admin adminFromDB = entityManager.find(Admin.class, admin.getUsername());
        if (adminFromDB.getPassword().equals(admin.getPassword())) {
            return "Admin logged in successfully!";
        } else {
            return "Admin login failed!";
        }
    }

    // method to get all customers
    @GET
    @Path("/getAllAdmins")
    public List<Admin> getAllAdmins() {
        return entityManager.createQuery("SELECT a FROM Admin a", Admin.class).getResultList();
    }

    @GET
    @Path("/{username}")
    public Admin getAdminByUsername(@PathParam("username") String username) {
        Admin admin = entityManager.find(Admin.class, username);
        return admin;
    }

    @PUT
    @Path("/updateAdmin/{username}")
    public String updateAdmin(@PathParam("username") String username, Admin admin) {

        Admin adminFromDB = entityManager.find(Admin.class, username);
        adminFromDB.setName(admin.getName());
        adminFromDB.setEmail(admin.getEmail());
        adminFromDB.setPassword(admin.getPassword());
        adminFromDB.setAddress(admin.getAddress());
        adminFromDB.setPhone(admin.getPhone());


        try {
            userTransaction.begin();
        } catch (NotSupportedException | SystemException e) {
            throw new RuntimeException(e);
        }
        entityManager.merge(adminFromDB);
        try {
            userTransaction.commit();
        } catch (RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException |
                 IllegalStateException | SystemException e) {
            e.printStackTrace();
        }
        return "Admin updated successfully!";

    }

    @DELETE
    @Path("/deleteAdmin/{username}")
    public String deleteAdmin(@PathParam("username") String username) {
        Admin admin = entityManager.find(Admin.class, username);
        try {
            userTransaction.begin();
        } catch (NotSupportedException | SystemException e) {
            throw new RuntimeException(e);
        }
        entityManager.remove(admin);
        try {
            userTransaction.commit();
        } catch (RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException |
                 IllegalStateException | SystemException e) {
            e.printStackTrace();
        }
        return "Admin deleted successfully!";
    }


    @POST
    @Path("/createSellingCompany")
    public String createSellingCompany(SellingCompanyRep sellingCompanyRep) {
        try {
            userTransaction.begin();
        } catch (NotSupportedException | SystemException e) {
            throw new RuntimeException(e);
        }
        String password = generateRandomPassword();
        sellingCompanyRep.setPassword(password);
        entityManager.persist(sellingCompanyRep);
        try {
            userTransaction.commit();
        } catch (RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException |
                 IllegalStateException |
                 SystemException e) {
            e.printStackTrace();
        }
        return "selling company createdsuccessfully!\n" + " welcome " + sellingCompanyRep.getName() + " your password " + sellingCompanyRep.getPassword();
    }

    @GET
    @Path("/getAllSellingCompanyRep")
    public List<SellingCompanyRep> getAllSellingCompanyRep() {
        return entityManager.createQuery("SELECT s FROM SellingCompanyRep s", SellingCompanyRep.class).getResultList();
    }

    //    public void addCompanyToRegion(int companyId, int regionId) {
//        ShippingCompany company = companyResource.getShippingCompanyId(companyId);
//        CoveredRegion region = regionResource.getCoveredRegionId(regionId);
//        if (company != null && region != null) {
//            region.setCompany(company);
//            regionResource.updateCoveredRegion(regionId,region);
//        }
//    }
    public static String generateRandomPassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        int index = 0;
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            index = random.nextInt(PASSWORD_ALLOW_BASE.length());
            password.append(PASSWORD_ALLOW_BASE.charAt(index));
        }
        return password.toString();
    }

}

