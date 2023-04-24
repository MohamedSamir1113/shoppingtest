package shop;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.*;
import jakarta.transaction.NotSupportedException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/shippingcompany")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShippingCompanyResource {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
    private final EntityManager entityManager = emf.createEntityManager();
    @Resource
    private UserTransaction userTransaction;

    @POST
    @Path("/createShippingCompany")
    public String createShippingCompany(ShippingCompany company) {
        try {
            userTransaction.begin();
        } catch (NotSupportedException | SystemException e) {
            throw new RuntimeException(e);
        }
        entityManager.persist(company);
        try {
            userTransaction.commit();
        } catch (RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException |
                 IllegalStateException |
                 SystemException e) {
            e.printStackTrace();
        }
        return "shipping company created successfully!\n" + " welcome " + company.getName() + "your company id is" + company.getId();
    }

//    @PUT
//    @Path("/updateShippingCompany/{id}")
//    public String updateShippingCompany(@PathParam("id") int companyId, ShippingCompany company) {
//        try {
//            ShippingCompany shippingCompanyFromDB = entityManager.find(ShippingCompany.class, companyId);
//            shippingCompanyFromDB.setName(company.getName());
////            shippingCompanyFromDB.setId(company.getId());
//
//            return "ShippingCompany updated successfully!";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "ShippingCompany update failed!";
//        }
//    }

    @DELETE
    @Path("/deleteShippingCompany/{id}")
    public String deleteShippingCompany(@PathParam("id") int companyId) {
        try {
            ShippingCompany company = entityManager.find(ShippingCompany.class, companyId);
            try {
                userTransaction.begin();
            } catch (NotSupportedException | SystemException e) {
                throw new RuntimeException(e);
            }
            entityManager.remove(company);
            try {
                userTransaction.commit();
            } catch (RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException |
                     IllegalStateException | SystemException e) {
                e.printStackTrace();
            }
            return "ShippingCompany deleted successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "ShippingCompany deletion failed!";
        }
    }

    @GET
    @Path("/{companyId}")
    public ShippingCompany getShippingCompanyId(@PathParam("companyId") int companyId) {
        ShippingCompany company = entityManager.find(ShippingCompany.class, companyId);
        return company;
    }

    @GET
    @Path("/getAllShippingCompany")
    public List<ShippingCompany> getAllShippingCompany() {
        return entityManager.createQuery("SELECT sc.companyId, sc.name, cr.regionId, cr.region FROM ShippingCompany sc INNER JOIN region_company sccr ON sc.companyId = sccr.companyId INNER JOIN CoveredRegion cr ON sccr.regionId = cr.regionId").getResultList();
    }

    @PUT
    @Path("/updateShippingCompany/{companyId}")
    public void updateById(@PathParam("companyId") int companyId, String name) {
        ShippingCompany company = getShippingCompanyId(companyId);
        if (company != null) {
            company.setName(name);
            /* update any other fields as needed */
            try {
                userTransaction.begin();
            } catch (NotSupportedException | SystemException e) {
                throw new RuntimeException(e);
            }
            entityManager.merge(company);
        }
        try {
            userTransaction.commit();
        } catch (RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException |
                 IllegalStateException | SystemException e) {
            e.printStackTrace();
        }
    }

}