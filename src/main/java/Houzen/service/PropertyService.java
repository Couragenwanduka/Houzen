package Houzen.service;

import Houzen.model.PropertyModel;
import Houzen.Repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {
    // Implement property related operations here
    private final PropertyRepository propertyRepository;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }
    public PropertyModel findByName(String name) {
        return propertyRepository.findByName(name);
    }
    public PropertyModel findByAddress(String address) {
        return propertyRepository.findByAddress(address);
    }
    public PropertyModel findByType(String type) {
        return propertyRepository.findByType(type);
    }
    public PropertyModel findByState(String state) {
        return propertyRepository.findByState(state);
    }
    public PropertyModel findByNumberofRooms(int numberofRooms) {
        return propertyRepository.findByNumberofRooms(numberofRooms);
    }
    public PropertyModel findByNumberofBathrooms(int numberofBathrooms) {
        return propertyRepository.findByNumberofBathrooms(numberofBathrooms);
    }
    public PropertyModel findByDescription(String description) {
        return propertyRepository.findByDescription(description);
    }
    public PropertyModel findByImages(String images) {
        return propertyRepository.findByImages(images);
    }
    public PropertyModel findByRent(String rent) {
        return propertyRepository.findByRent(rent);
    }
    public PropertyModel findByStatus(String status) {
        return propertyRepository.findByStatus(status);
    }
    public PropertyModel findByAgent(String agent) {
        return propertyRepository.findByAgent(agent);
    }
    public PropertyModel savePropertyModel(PropertyModel propertyModel) {
        return propertyRepository.save(propertyModel);
    }

}
