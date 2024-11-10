package qa.guru.model;

import com.fasterxml.jackson.annotation.JsonSetter;

public class Material {

    @JsonSetter("materialName")
    private String name;
    private Double cost;
    private Integer quantity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
