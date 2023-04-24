package shop;

import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.transaction.*;
import jakarta.transaction.NotSupportedException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import javax.json.bind.annotation.JsonbProperty;
import java.util.List;
@Path("/sellingCompanyRep")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class SellingCompanyRepResource {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");

    private final EntityManager entityManager = emf.createEntityManager();

    @Resource
    private UserTransaction userTransaction;

    @POST
    @Path("/createSellingCompany")
    public String createSellingCompany(SellingCompanyRep sellingCompanyRep) {
        try {
            userTransaction.begin();
        } catch (NotSupportedException | SystemException e) {
            throw new RuntimeException(e);
        }
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
        return entityManager.createQuery("SELECT c FROM SellingCompanyRep c", SellingCompanyRep.class).getResultList();
    }

    @POST
    @Path("/login")
    public String loginCustomer(SellingCompanyRep sellingCompanyRep) {
        SellingCompanyRep sellingCompanyRepFromDB = entityManager.find(SellingCompanyRep.class, sellingCompanyRep.getName());
        if (sellingCompanyRepFromDB.getPassword().equals(sellingCompanyRep.getPassword())) {
            return "Selling Company Rep logged in successfully!";
        } else {
            return "Selling Company Rep login failed!";
        }
    }
//        Admin adminFromDB = entityManager.find(Admin.class, admin.getUsername());
//        if (adminFromDB.getPassword().equals(admin.getPassword())) {
//            return "Admin logged in successfully!";
//        } else {
//            return "Admin login failed!";
//        }
//    }

}