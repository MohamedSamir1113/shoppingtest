package shop;

import jakarta.ejb.Stateless;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Stateless
@Entity
@Table(name = "sellingcompanyrep")
public class SellingCompanyRep {
    @Id
    String name;
    String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}