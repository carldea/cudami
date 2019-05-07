package de.digitalcollections.cudami.admin.business.api.service.identifiable;

import de.digitalcollections.cudami.admin.business.api.service.exceptions.IdentifiableServiceException;
import de.digitalcollections.model.api.identifiable.Identifiable;
import de.digitalcollections.model.api.paging.PageRequest;
import de.digitalcollections.model.api.paging.PageResponse;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.validation.Errors;

public interface IdentifiableService<I extends Identifiable> {

  long count();

  I create();

  PageResponse<I> find(PageRequest pageRequest);

  List<I> find(String searchTerm, int maxResults);

  I get(UUID uuid);

  I get(UUID uuid, Locale locale);

  I save(I identifiable, Errors results) throws IdentifiableServiceException;

  I update(I identifiable, Errors results) throws IdentifiableServiceException;
}
