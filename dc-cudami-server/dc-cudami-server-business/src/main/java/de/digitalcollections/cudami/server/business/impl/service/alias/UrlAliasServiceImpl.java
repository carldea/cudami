package de.digitalcollections.cudami.server.business.impl.service.alias;

import de.digitalcollections.cudami.server.backend.api.repository.alias.UrlAliasRepository;
import de.digitalcollections.cudami.server.business.api.service.alias.UrlAliasService;
import de.digitalcollections.cudami.server.business.api.service.exceptions.CudamiServiceException;
import de.digitalcollections.model.alias.LocalizedUrlAliases;
import de.digitalcollections.model.alias.UrlAlias;
import de.digitalcollections.model.paging.SearchPageRequest;
import de.digitalcollections.model.paging.SearchPageResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Service implementation for UrlAlias handling. */
@Service
public class UrlAliasServiceImpl implements UrlAliasService {

  private final UrlAliasRepository repository;

  @Autowired
  public UrlAliasServiceImpl(UrlAliasRepository repository) {
    this.repository = repository;
  }

  @Override
  public UrlAlias findOne(UUID uuid) throws CudamiServiceException {
    if (uuid == null) {
      return null;
    }

    try {
      return repository.findOne(uuid);
    } catch (Exception e) {
      throw new CudamiServiceException("Cannot findOne with uuid=" + uuid + ": " + e, e);
    }
  }

  @Override
  public boolean delete(UUID uuid) throws CudamiServiceException {
    return UrlAliasService.super.delete(uuid);
  }

  @Override
  public boolean delete(List<UUID> uuids) throws CudamiServiceException {
    return false;
  }

  @Override
  public UrlAlias save(UrlAlias urlAlias) throws CudamiServiceException {
    return null;
  }

  @Override
  public SearchPageResponse<LocalizedUrlAliases> find(SearchPageRequest pageRequest)
      throws CudamiServiceException {
    return null;
  }
}
