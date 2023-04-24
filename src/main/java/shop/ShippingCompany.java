package shop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Entity
@Table(name = "shippingcompany")
public class ShippingCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @JsonbProperty
//    @JsonbTransient
    private int companyId;
//    @JsonbProperty
//    @JsonbTransient
    private String name;
//    @JsonbProperty
//    @JsonbTransient
    @ManyToMany(mappedBy = "shippingCompanies",fetch=FetchType.EAGER)
    @JsonIgnore
    private List<CoveredRegion> coveredRegions ;

    public ShippingCompany(int companyId,String name ) {

        this.companyId=companyId;
        this.name = name;
    }

    public ShippingCompany() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return companyId;
    }

    public void setId(int companyId) {
        this.companyId = companyId;
    }


    public List<CoveredRegion> getCoveredRegions() {
        return coveredRegions;
    }

    public void setCoveredRegions(List<CoveredRegion> coveredRegions) {
        this.coveredRegions = coveredRegions;
    }


}
