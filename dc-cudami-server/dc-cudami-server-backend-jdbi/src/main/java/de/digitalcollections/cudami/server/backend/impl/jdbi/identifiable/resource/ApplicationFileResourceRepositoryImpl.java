package de.digitalcollections.cudami.server.backend.impl.jdbi.identifiable.resource;

import de.digitalcollections.cudami.server.backend.api.repository.identifiable.IdentifierRepository;
import de.digitalcollections.cudami.server.backend.api.repository.identifiable.resource.ApplicationFileResourceRepository;
import de.digitalcollections.cudami.server.backend.impl.jdbi.identifiable.IdentifiableRepositoryImpl;
import de.digitalcollections.model.api.identifiable.resource.ApplicationFileResource;
import de.digitalcollections.model.api.paging.SearchPageRequest;
import de.digitalcollections.model.api.paging.SearchPageResponse;
import de.digitalcollections.model.impl.identifiable.parts.LocalizedTextImpl;
import de.digitalcollections.model.impl.identifiable.resource.ApplicationFileResourceImpl;
import java.util.Locale;
import java.util.Map;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ApplicationFileResourceRepositoryImpl
    extends IdentifiableRepositoryImpl<ApplicationFileResource>
    implements ApplicationFileResourceRepository {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(ApplicationFileResourceRepositoryImpl.class);

  public static final String MAPPING_PREFIX = "fr";
  public static final String TABLE_ALIAS = "f";
  public static final String TABLE_NAME = "fileresources_application";

  public static String getSqlInsertFields() {
    return FileResourceMetadataRepositoryImpl.getSqlInsertFields();
  }

  /* Do not change order! Must match order in getSqlInsertFields!!! */
  public static String getSqlInsertValues() {
    return FileResourceMetadataRepositoryImpl.getSqlInsertValues();
  }

  public static String getSqlSelectAllFields(String tableAlias, String mappingPrefix) {
    return getSqlSelectReducedFields(tableAlias, mappingPrefix);
  }

  public static String getSqlSelectReducedFields(String tableAlias, String mappingPrefix) {
    return FileResourceMetadataRepositoryImpl.getSqlSelectReducedFields(tableAlias, mappingPrefix);
  }

  public static String getSqlUpdateFieldValues() {
    return FileResourceMetadataRepositoryImpl.getSqlUpdateFieldValues();
  }

  private final FileResourceMetadataRepositoryImpl fileResourceMetadataRepositoryImpl;

  @Autowired
  public ApplicationFileResourceRepositoryImpl(
      Jdbi dbi,
      IdentifierRepository identifierRepository,
      FileResourceMetadataRepositoryImpl fileResourceMetadataRepositoryImpl) {
    super(
        dbi,
        identifierRepository,
        TABLE_NAME,
        TABLE_ALIAS,
        MAPPING_PREFIX,
        ApplicationFileResourceImpl.class,
        getSqlSelectAllFields(TABLE_ALIAS, MAPPING_PREFIX),
        getSqlSelectReducedFields(TABLE_ALIAS, MAPPING_PREFIX),
        getSqlInsertFields(),
        getSqlInsertValues(),
        getSqlUpdateFieldValues());
    this.fileResourceMetadataRepositoryImpl = fileResourceMetadataRepositoryImpl;
  }

  @Override
  public SearchPageResponse<ApplicationFileResource> find(SearchPageRequest searchPageRequest) {
    String commonSql =
        fileResourceMetadataRepositoryImpl.getCommonFileResourceSearchSql(tableName, tableAlias);
    return find(searchPageRequest, commonSql, Map.of("searchTerm", searchPageRequest.getQuery()));
  }

  @Override
  protected String[] getAllowedOrderByFields() {
    return new String[] {"created", "filename", "lastModified", "sizeInBytes"};
  }

  @Override
  protected String getColumnName(String modelProperty) {
    if (modelProperty == null) {
      return null;
    }
    switch (modelProperty) {
      case "created":
        return tableAlias + ".created";
      case "filename":
        return tableAlias + ".filename";
      case "lastModified":
        return tableAlias + ".last_modified";
      case "sizeInBytes":
        return tableAlias + ".size_in_bytes";
      default:
        return null;
    }
  }

  @Override
  public ApplicationFileResource save(ApplicationFileResource fileResource) {
    if (fileResource.getLabel() == null && fileResource.getFilename() != null) {
      // set a default label = filename (an empty label violates constraint)
      fileResource.setLabel(new LocalizedTextImpl(Locale.ROOT, fileResource.getFilename()));
    }
    super.save(fileResource);
    ApplicationFileResource result = findOne(fileResource.getUuid());
    return result;
  }

  @Override
  public ApplicationFileResource update(ApplicationFileResource fileResource) {
    super.update(fileResource);
    ApplicationFileResource result = findOne(fileResource.getUuid());
    return result;
  }
}
