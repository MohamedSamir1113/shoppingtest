package shop;

import jakarta.persistence.*;

import java.util.List;

//@Stateless
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    private String productName;

    private double productPrice;

    private boolean available;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="name")
    private SellingCompanyRep sellingCompanyRep;

    public Product() {
    }

    public Product(int productId , String productName , double productPrice, boolean available) {
        this.productId=productId;
        this.productName=productName;
        this.productPrice = productPrice;
        this.available = available;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public SellingCompanyRep getSellingCompanyRep() {
        return sellingCompanyRep;
    }

    public void setSellingCompanyRep(SellingCompanyRep sellingCompanyRep) {
        this.sellingCompanyRep = sellingCompanyRep;
    }
}
