package de.digitalcollections.cudami.admin.backend.impl.repository.identifiable.entity.parts;

import de.digitalcollections.cudami.admin.backend.impl.repository.RepositoryEndpoint;
import de.digitalcollections.model.api.identifiable.Identifiable;
import de.digitalcollections.model.api.identifiable.entity.parts.Webpage;
import de.digitalcollections.model.api.paging.PageResponse;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import java.util.List;
import java.util.UUID;

public interface WebpageRepositoryEndpoint extends RepositoryEndpoint {

  @RequestLine("GET /latest/webpages?pageNumber={pageNumber}&pageSize={pageSize}&sortField={sortField}&sortDirection={sortDirection}&nullHandling={nullHandling}")
  PageResponse<Webpage> find(
          @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize,
          @Param("sortField") String sortField, @Param("sortDirection") String sortDirection, @Param("nullHandling") String nullHandling
  );

  @RequestLine("GET /latest/webpages/{uuid}")
  Webpage findOne(@Param("uuid") UUID uuid);

  @RequestLine("POST /latest/webpages")
  @Headers("Content-Type: application/json")
  Webpage save(Webpage webpage);

  @RequestLine("PUT /latest/webpages/{uuid}")
  @Headers("Content-Type: application/json")
  Webpage update(@Param("uuid") UUID uuid, Webpage webpage);

  @RequestLine("GET /latest/webpages/count")
  long count();

  @RequestLine("GET /latest/webpages/{uuid}/children")
  List<Webpage> getChildren(@Param("uuid") UUID uuid);

  @RequestLine("POST /latest/websites/{parentWebsiteUuid}/webpage")
  @Headers("Content-Type: application/json")
  Webpage saveWithParentWebsite(Webpage webpage, @Param("parentWebsiteUuid") UUID parentWebsiteUuid);

  @RequestLine("POST /latest/webpages/{parentWebpageUuid}/webpage")
  @Headers("Content-Type: application/json")
  Webpage saveWithParentWebpage(Webpage webpage, @Param("parentWebpageUuid") UUID parentWebpageUuid);

  @RequestLine("GET /latest/webpages/{uuid}/identifiables")
  public List<Identifiable> getIdentifiables(UUID uuid);

  @RequestLine("POST /latest/webpages/{uuid}/identifiables/{identifiableUuid}")
  @Headers("Content-Type: application/json")
  void addIdentifiable(@Param("uuid") UUID webpageUuid, @Param("identifiableUuid") UUID identifiableUuid);

  @RequestLine("POST /latest/webpages/{uuid}/identifiables")
  @Headers("Content-Type: application/json")
  public List<Identifiable> saveIdentifiables(@Param("uuid") UUID uuid, List<Identifiable> identifiables);
}