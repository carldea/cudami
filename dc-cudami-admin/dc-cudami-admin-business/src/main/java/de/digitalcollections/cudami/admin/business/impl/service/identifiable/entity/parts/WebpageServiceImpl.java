package de.digitalcollections.cudami.admin.business.impl.service.identifiable.entity.parts;

import de.digitalcollections.cudami.admin.backend.api.repository.identifiable.NodeRepository;
import de.digitalcollections.cudami.admin.backend.api.repository.identifiable.entity.parts.WebpageRepository;
import de.digitalcollections.cudami.admin.business.api.service.exceptions.IdentifiableServiceException;
import de.digitalcollections.cudami.admin.business.api.service.identifiable.entity.parts.WebpageService;
import de.digitalcollections.model.api.identifiable.entity.Entity;
import de.digitalcollections.model.api.identifiable.entity.parts.Webpage;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for Webpage handling.
 *
 * @param <E> entity type
 */
@Service
//@Transactional(readOnly = true)
public class WebpageServiceImpl<E extends Entity> extends EntityPartServiceImpl<Webpage, E> implements WebpageService<E> {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebpageServiceImpl.class);

  @Autowired
  public WebpageServiceImpl(WebpageRepository<E> repository) {
    super(repository);
  }

  @Override
  public List<Webpage> getChildren(Webpage webpage) {
    return ((NodeRepository) repository).getChildren(webpage);
  }

  @Override
  public List<Webpage> getChildren(UUID uuid) {
    return ((NodeRepository) repository).getChildren(uuid);
  }

  @Override
  //  @Transactional(readOnly = false)
  public Webpage saveWithParentWebsite(Webpage webpage, UUID parentWebsiteUuid) throws IdentifiableServiceException {
    try {
      return ((WebpageRepository) repository).saveWithParentWebsite(webpage, parentWebsiteUuid);
    } catch (Exception e) {
      LOGGER.error("Cannot save top-level webpage " + webpage + ": ", e);
      throw new IdentifiableServiceException(e.getMessage());
    }
  }

  @Override
  //  @Transactional(readOnly = false)
  public Webpage saveWithParentWebpage(Webpage webpage, UUID parentWebpageUuid) throws IdentifiableServiceException {
    try {
      return ((WebpageRepository) repository).saveWithParentWebpage(webpage, parentWebpageUuid);
    } catch (Exception e) {
      LOGGER.error("Cannot save webpage " + webpage + ": ", e);
      throw new IdentifiableServiceException(e.getMessage());
    }
  }
}
