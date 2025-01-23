package Houzen.model;

import org.springframework.data.annotation.Id;
// import Houzen.model.User;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

public class PropertyModel {

    @Id
    private String id;
    @NotNull(message = "Property name cannot be null")
    private String name;
    @NotNull(message = "Property type cannot be null")
    private String type;
    @NotNull(message = "Property address cannot be null")
    private String address;
    @NotNull(message = "Property state cannot be null")
    private String state;
    @NotNull(message = "Number of rooms cannot be null")
    private int numberofRooms;
    @NotNull(message = "Number of bathrooms cannot be null")
    private int numberofBathrooms;
    @NotNull(message = "Property description cannot be null")
    private String description;
    @NotNull(message = "Property images cannot be null")
    private String images;
    @NotNull(message = "Property rent cannot be null")
    private String rent;
    private StatusEnum status = StatusEnum.ACTIVE;
    @DBRef
    private User agent;
    @CreatedDate
    private Date created_at;
    @LastModifiedDate
    private Date updated_at;

    public PropertyModel() {
    }
    // getters and setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public int getNumberofRooms() {
        return numberofRooms;
    }
    public void setNumberofRooms(int numberofRooms) {
        this.numberofRooms = numberofRooms;
    }
    public int getNumberofBathrooms() {
        return numberofBathrooms;
    }
    public void setNumberofBathrooms(int numberofBathrooms) {
        this.numberofBathrooms = numberofBathrooms;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getImages() {
        return images;
    }
    public void setImages(String images) {
        this.images = images;
    }
    public String getRent() {
        return rent;
    }
    public void setRent(String rent) {
        this.rent = rent;
    }
    public StatusEnum getStatus() {
        return status;
    }
    public void setStatus(StatusEnum status) {
        this.status = status;
    }
    public User getAgent() {
        return agent;
    }
    public void setAgent(User agent) {
        this.agent = agent;
    }
    public Date getCreated_at() {
        return created_at;
    }
    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
    public Date getUpdated_at() {
        return updated_at;
    }
    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
    // toString method
    @Override
    public String toString() {
        return "PropertyModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", address='" + address + '\'' +
                ", state='" + state + '\'' +
                ", numberofRooms='" + numberofRooms + '\'' +
                ", numberofBathrooms='" + numberofBathrooms + '\'' +
                ", description='" + description + '\'' +
                ", images='" + images + '\'' +
                ", rent='" + rent + '\'' +
                ", status='" + status + '\'' +
                ", agent=" + agent +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
   

}
