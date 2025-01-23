package Houzen.Repository;

import Houzen.model.PropertyModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends MongoRepository<PropertyModel, String> {
    PropertyModel findByName(String name);
    PropertyModel findByAddress(String address);
    PropertyModel findByType(String type);
    PropertyModel findByState(String state);
    PropertyModel findByNumberofRooms(int numberofRooms);
    PropertyModel findByNumberofBathrooms(int numberofBathrooms);
    PropertyModel findByDescription(String description);
    PropertyModel findByImages(String images);
    PropertyModel findByRent(String rent);
    PropertyModel findByStatus(String status);
    PropertyModel findByAgent(String agent);

}
