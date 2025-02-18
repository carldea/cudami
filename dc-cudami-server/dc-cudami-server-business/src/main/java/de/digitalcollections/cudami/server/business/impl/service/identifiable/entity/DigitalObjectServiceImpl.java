package de.digitalcollections.cudami.server.business.impl.service.identifiable.entity;

import de.digitalcollections.cudami.server.backend.api.repository.identifiable.entity.DigitalObjectRepository;
import de.digitalcollections.cudami.server.business.api.service.identifiable.entity.CollectionService;
import de.digitalcollections.cudami.server.business.api.service.identifiable.entity.DigitalObjectService;
import de.digitalcollections.cudami.server.business.api.service.identifiable.entity.ProjectService;
import de.digitalcollections.model.filter.Filtering;
import de.digitalcollections.model.identifiable.entity.Collection;
import de.digitalcollections.model.identifiable.entity.DigitalObject;
import de.digitalcollections.model.identifiable.entity.Project;
import de.digitalcollections.model.identifiable.entity.work.Item;
import de.digitalcollections.model.identifiable.resource.FileResource;
import de.digitalcollections.model.identifiable.resource.ImageFileResource;
import de.digitalcollections.model.paging.SearchPageRequest;
import de.digitalcollections.model.paging.SearchPageResponse;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Service for Digital Object handling. */
@Service
public class DigitalObjectServiceImpl extends EntityServiceImpl<DigitalObject>
    implements DigitalObjectService {

  private static final Logger LOGGER = LoggerFactory.getLogger(DigitalObjectServiceImpl.class);

  private final CollectionService collectionService;
  private final ProjectService projectService;

  @Autowired
  public DigitalObjectServiceImpl(
      DigitalObjectRepository repository,
      CollectionService collectionService,
      ProjectService projectService) {
    super(repository);
    this.collectionService = collectionService;
    this.projectService = projectService;
  }

  @Override
  public boolean delete(UUID uuid) {
    // Check for existance. If not given, return false.
    DigitalObject existingDigitalObject = get(uuid);
    if (existingDigitalObject == null) {
      return false;
    }

    // Remove connection to collections
    collectionService.removeDigitalObjectFromAllCollections(existingDigitalObject);

    // Remove connection to projects
    projectService.removeDigitalObjectFromAllProjects(existingDigitalObject);

    // Remove preview images
    deleteFileResources(existingDigitalObject.getUuid());

    // Remove identifiers
    repository.deleteIdentifiers(existingDigitalObject.getUuid());

    // Remove the digitalObject itself
    repository.delete(uuid);

    return true;
  }

  @Override
  public void deleteFileResources(UUID digitalObjectUuid) {
    ((DigitalObjectRepository) repository).deleteFileResources(digitalObjectUuid);
  }

  @Override
  public SearchPageResponse<Collection> getActiveCollections(
      DigitalObject digitalObject, SearchPageRequest searchPageRequest) {
    Filtering filtering = filteringForActive();
    searchPageRequest.add(filtering);
    return ((DigitalObjectRepository) repository).getCollections(digitalObject, searchPageRequest);
  }

  @Override
  public SearchPageResponse<Collection> getCollections(
      UUID digitalObjectUuid, SearchPageRequest searchPageRequest) {
    return ((DigitalObjectRepository) repository)
        .getCollections(digitalObjectUuid, searchPageRequest);
  }

  @Override
  public List<FileResource> getFileResources(UUID digitalObjectUuid) {
    return ((DigitalObjectRepository) repository).getFileResources(digitalObjectUuid);
  }

  @Override
  public List<ImageFileResource> getImageFileResources(UUID digitalObjectUuid) {
    return ((DigitalObjectRepository) repository).getImageFileResources(digitalObjectUuid);
  }

  @Override
  public Item getItem(UUID digitalObjectUuid) {
    return ((DigitalObjectRepository) repository).getItem(digitalObjectUuid);
  }

  @Override
  public List<Locale> getLanguagesOfCollections(UUID uuid) {
    return ((DigitalObjectRepository) this.repository).getLanguagesOfCollections(uuid);
  }

  @Override
  public List<Locale> getLanguagesOfProjects(UUID uuid) {
    return ((DigitalObjectRepository) this.repository).getLanguagesOfProjects(uuid);
  }

  @Override
  public SearchPageResponse<Project> getProjects(
      UUID digitalObjectUuid, SearchPageRequest searchPageRequest) {
    return ((DigitalObjectRepository) repository).getProjects(digitalObjectUuid, searchPageRequest);
  }

  @Override
  public List<FileResource> saveFileResources(
      UUID digitalObjectUuid, List<FileResource> fileResources) {
    return ((DigitalObjectRepository) repository)
        .saveFileResources(digitalObjectUuid, fileResources);
  }
}
