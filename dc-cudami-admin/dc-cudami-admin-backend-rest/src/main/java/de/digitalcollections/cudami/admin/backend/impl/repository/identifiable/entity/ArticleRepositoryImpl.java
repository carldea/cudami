package de.digitalcollections.cudami.admin.backend.impl.repository.identifiable.entity;

import de.digitalcollections.cudami.admin.backend.api.repository.identifiable.entity.ArticleRepository;
import de.digitalcollections.model.api.identifiable.Identifiable;
import de.digitalcollections.model.api.identifiable.entity.Article;
import de.digitalcollections.model.api.paging.PageRequest;
import de.digitalcollections.model.api.paging.PageResponse;
import de.digitalcollections.model.impl.identifiable.entity.ArticleImpl;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleRepositoryImpl<A extends Article, I extends Identifiable> extends EntityRepositoryImpl<A> implements ArticleRepository<A, I> {

  @Autowired
  private ArticleRepositoryEndpoint endpoint;

  @Override
  public void addIdentifiable(UUID articleUuid, UUID identifiableUuid) {
    endpoint.addIdentifiable(articleUuid, identifiableUuid);
  }

  @Override
  public long count() {
    return endpoint.count();
  }

  @Override
  public A create() {
    return (A) new ArticleImpl();
  }

  @Override
  public PageResponse<A> find(PageRequest pageRequest) {
    FindParams f = getFindParams(pageRequest);
    PageResponse<Article> pageResponse = endpoint.find(f.getPageNumber(), f.getPageSize(), f.getSortField(), f.getSortDirection(), f.getNullHandling());
    return getGenericPageResponse(pageResponse);
  }

  @Override
  public A findOne(UUID uuid) {
    return (A) endpoint.findOne(uuid);
  }

  @Override
  public A save(A identifiable) {
    return (A) endpoint.save(identifiable);
  }

  @Override
  public A update(A identifiable) {
    return (A) endpoint.update(identifiable.getUuid(), identifiable);
  }

  @Override
  public List<Identifiable> getIdentifiables(A article) {
    return getIdentifiables(article.getUuid());
  }

  private List<Identifiable> getIdentifiables(UUID uuid) {
    return endpoint.getIdentifiables(uuid);
  }

  @Override
  public List<Identifiable> saveIdentifiables(A article, List<Identifiable> identifiables) {
    return endpoint.saveIdentifiables(article.getUuid(), identifiables);
  }
}