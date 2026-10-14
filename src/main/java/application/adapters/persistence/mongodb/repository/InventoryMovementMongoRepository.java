package application.adapters.persistence.mongodb.repository;

import application.adapters.persistence.mongodb.document.MotionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryMovementMongoRepository extends MongoRepository<MotionDocument, String> {
}
