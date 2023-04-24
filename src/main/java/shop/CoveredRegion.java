package shop;

import jakarta.ejb.Stateless;
import jakarta.persistence.*;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Entity
@Table(name = "coveredregion")
public class CoveredRegion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @JsonbProperty
//    @JsonbTransient
    private int regionId;

//    @JsonbProperty
//    @JsonbTransient
    private String region;
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "region_company",
            joinColumns = @JoinColumn(name = "regionId"),
            inverseJoinColumns = @JoinColumn(name = "companyId"))
    private List<ShippingCompany> shippingCompanies;
    public CoveredRegion(int regionId, String region) {
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


    public List<ShippingCompany> getShippingCompanies() {
        return shippingCompanies;
    }

    public void setShippingCompanies(List<ShippingCompany> shippingCompanies) {
        this.shippingCompanies = shippingCompanies;
    }
}
