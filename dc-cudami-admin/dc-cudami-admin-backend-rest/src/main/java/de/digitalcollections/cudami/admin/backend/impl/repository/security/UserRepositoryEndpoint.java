package de.digitalcollections.cudami.admin.backend.impl.repository.security;

import de.digitalcollections.core.model.api.paging.PageResponse;
import de.digitalcollections.cudami.model.impl.security.UserImpl;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import java.util.List;
import java.util.UUID;

public interface UserRepositoryEndpoint {

  @RequestLine("GET /v1/users?role=ADMIN&enabled=true")
  List<UserImpl> findActiveAdminUsers();

  @RequestLine("GET /v1/users?pageNumber={pageNumber}&pageSize={pageSize}&sortField={sortField}&sortDirection={sortDirection}&nullHandling={nullHandling}")
  PageResponse<UserImpl> find(
          @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize,
          @Param("sortField") String sortField, @Param("sortDirection") String sortDirection, @Param("nullHandling") String nullHandling
  );

  @RequestLine("GET /v1/users?email={email}")
  UserImpl findByEmail(@Param("email") String email);

  @RequestLine("GET /v1/users/{uuid}")
  UserImpl findOne(@Param("uuid") UUID uuid);

  @RequestLine("POST /v1/users")
  @Headers("Content-Type: application/json")
  UserImpl save(UserImpl user);

  @RequestLine("PUT /v1/users/{uuid}")
  @Headers("Content-Type: application/json")
  UserImpl update(@Param("uuid") UUID uuid, UserImpl user);
}