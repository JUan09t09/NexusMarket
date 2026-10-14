package application.domain.ports.in;

import application.domain.models.Return;

public interface ProcessReturnUseCase {
    Return execute(String returnId, String operatorId, String storeId);
}
