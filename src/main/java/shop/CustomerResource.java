package shop;

import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.*;
import jakarta.transaction.NotSupportedException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/customer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {


    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");

    private final EntityManager entityManager = emf.createEntityManager();

    @Resource
    private UserTransaction userTransaction;

    @GET
    @Path("/welcome")
    public String welcomeMessage() {
        return "Hello Customer!";
    }

    @POST
    @Path("/register")
    public String registerCustomer(Customer customer) {
        try {
            userTransaction.begin();
        } catch (NotSupportedException | SystemException e) {
            throw new RuntimeException(e);
        }
        entityManager.persist(customer);
        try {
            userTransaction.commit();
        } catch (RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException |
                 IllegalStateException | SystemException e) {
            e.printStackTrace();
        }
        return "Customer registered successfully!\n" + "Welcome Customer: " + customer.getName();
    }

    @POST
    @Path("/login")
    public String loginCustomer(Customer customer) {
        Customer customerFromDB = entityManager.find(Customer.class, customer.getUsername());
        if (customerFromDB.getPassword().equals(customer.getPassword())) {
            return "Customer logged in successfully!";
        } else {
            return "Customer login failed!";
        }
    }

    // method to get all customers
    @GET
    @Path("/getAllCustomers")
    public List<Customer> getAllCustomers() {
        return entityManager.createQuery("SELECT c FROM Customer c", Customer.class).getResultList();
    }

    @GET
    @Path("/{username}")
    public Customer getCustomerByUsername(@PathParam("username") String username) {
        Customer customer = entityManager.find(Customer.class, username);
        return customer;
    }

    @PUT
    @Path("/updateCustomer/{username}")
    public String updateCustomer(@PathParam("username") String username, Customer customer) {
        try {
            Customer customerFromDB = entityManager.find(Customer.class, username);
            customerFromDB.setName(customer.getName());
            customerFromDB.setEmail(customer.getEmail());
            customerFromDB.setPassword(customer.getPassword());
            customerFromDB.setAddress(customer.getAddress());
            customerFromDB.setPhone(customer.getPhone());
            entityManager.merge(customerFromDB);
            return "Customer updated successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Customer update failed!";
        }
    }

    @DELETE
    @Path("/deleteCustomer/{username}")
    public String deleteCustomer(@PathParam("username") String username) {
        try {
            Customer customer = entityManager.find(Customer.class, username);
            entityManager.remove(customer);
            return "Customer deleted successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Customer deletion failed!";
        }
    }


}

