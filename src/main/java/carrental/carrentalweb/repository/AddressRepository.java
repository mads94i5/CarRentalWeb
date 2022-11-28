package carrental.carrentalweb.repository;

import carrental.carrentalweb.entities.Address;

import carrental.carrentalweb.entities.PickupPoint;
import carrental.carrentalweb.records.DatabaseRecord;
import carrental.carrentalweb.services.DatabaseService;
import carrental.carrentalweb.utilities.DatabaseRequestBody;
import carrental.carrentalweb.utilities.DatabaseResponse;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Repository
public class AddressRepository {

  private final DatabaseService databaseService;

  public AddressRepository(DatabaseService databaseService) {
    this.databaseService = databaseService;
  }


  public boolean createAddress(Address newAddress) {
    String query = "INSERT INTO address(street, city, zipCode, country, updated_at, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";

    DatabaseRequestBody requestBody = new DatabaseRequestBody(
        newAddress.getStreet(),
        newAddress.getCity(),
        newAddress.getZipCode(),
        newAddress.getCountry());
    newAddress.getCreatedAt();
    newAddress.getUpdatedAt();

    DatabaseResponse databaseResponse = databaseService.executeUpdate(query, requestBody);
    return databaseResponse.isSuccessful();
  }

  public List<Address> getAddressList() {
    String query = "SELECT * FROM address";
    DatabaseResponse databaseResponse = databaseService.executeQuery(query, new DatabaseRequestBody());
    return parseResponse(databaseResponse);
  }

  public Address findAddressById(Long id) {
    String sql = "SELECT * FROM pickup_points WHERE id= ?";
    DatabaseRequestBody body = new DatabaseRequestBody(id);
    DatabaseResponse databaseResponse = databaseService.executeQuery(sql, body);
    return parseResponseFirst(databaseResponse);
  }

  public Address parseResponseFirst(DatabaseResponse databaseResponse) {
    List<Address> addresses = parseResponse(databaseResponse);
    if (addresses.size() == 0) return null;
    else return addresses.get(0);
  }


  private List<Address> parseResponse(DatabaseResponse databaseResponse) {
    List<Address> addresses = new LinkedList<Address>();
    while (databaseResponse.hasNext()) {
      DatabaseRecord record = databaseResponse.next();

      addresses.add(
          new Address(
              (Long) record.map().get("id"),
              (String) record.map().get("street"),
              (String) record.map().get("city"),
              (String) record.map().get("zipCode"),
              (String) record.map().get("country"),
              (LocalDateTime) record.map().get("created_at"),
              (LocalDateTime) record.map().get("updated_at")
          ));
    }

    return addresses;
  }
}