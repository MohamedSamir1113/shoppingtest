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

@Path("/coveredregion")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CoveredRegionResource {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
    private final EntityManager entityManager = emf.createEntityManager();
    @Resource
    private UserTransaction userTransaction;

    @POST
    @Path("/addCoveredRegion")
    public void addCoveredRegion(String regionName, int companyId) {
        ShippingCompany company = entityManager.find(ShippingCompany.class, companyId);
        if (company == null) {
            throw new IllegalArgumentException("Company not found");
        }
        CoveredRegion region = new CoveredRegion();
        region.setRegion(regionName);
        region.setCompany(company);
        entityManager.persist(region);
    }

    @DELETE
    @Path("/removeCoveredRegion")
    public void removeCoveredRegion(int regionId) {
        CoveredRegion region = entityManager.find(CoveredRegion.class, regionId);
        if (region == null) {
            throw new IllegalArgumentException("Region not found");
        }
        entityManager.remove(region);
    }
    @GET
    @Path("/{companyId}")
    public List<CoveredRegion> getCoveredRegionsByCompany(int companyId) {
        ShippingCompany company = entityManager.find(ShippingCompany.class, companyId);
        if (company == null) {
            throw new IllegalArgumentException("Company not found");
        }
        return entityManager.createQuery("SELECT c FROM CoveredRegion c WHERE c.company = :company", CoveredRegion.class).getResultList();

    }

    @GET
    @Path("/{regionId}")
    public CoveredRegion getCoveredRegionId(@PathParam("regionId") int regionId) {
        CoveredRegion region = entityManager.find(CoveredRegion.class, regionId);
        return region;
    }

    @PUT
    @Path("/updateCoveredRegion/{regionId}")
    public String updateCoveredRegion(@PathParam("regionId") int regionId, CoveredRegion region) {
        try {
            CoveredRegion coveredRegionFromDB = entityManager.find(CoveredRegion.class, regionId);
            coveredRegionFromDB.setRegion(region.getRegion());
//            shippingCompanyFromDB.setId(company.getId());

            return "coveredRegion updated successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "coveredRegion update failed!";
        }
    }
}
