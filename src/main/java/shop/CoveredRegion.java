package shop;

import jakarta.ejb.Stateless;
import jakarta.persistence.*;

@Stateless
@Entity
@Table(name = "coveredregion")
public class CoveredRegion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int regionId;
    private String region;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private ShippingCompany company;

    public CoveredRegion(int regionId,String region) {
        this.regionId=regionId;
        this.region = region;
    }

    public CoveredRegion() {

    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getId() {
        return regionId;
    }

    public void setId(int regionId) {
        this.regionId = regionId;
    }

    public ShippingCompany getCompany() {
        return company;
    }

    public void setCompany(ShippingCompany company) {
        this.company = company;
    }
}
