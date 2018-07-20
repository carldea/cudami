package de.digitalcollections.cudami.admin.backend.impl.repository.identifiable.resource;

import de.digitalcollections.core.model.api.paging.Order;
import de.digitalcollections.core.model.api.paging.PageRequest;
import de.digitalcollections.core.model.api.paging.PageResponse;
import de.digitalcollections.core.model.api.paging.Sorting;
import de.digitalcollections.cudami.admin.backend.api.repository.LocaleRepository;
import de.digitalcollections.cudami.admin.backend.api.repository.identifiable.resource.WebpageRepository;
import de.digitalcollections.cudami.model.impl.identifiable.parts.MultilanguageDocumentImpl;
import de.digitalcollections.cudami.model.impl.identifiable.parts.TextImpl;
import de.digitalcollections.cudami.model.impl.identifiable.resource.WebpageImpl;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WebpageRepositoryImpl implements WebpageRepository<WebpageImpl> {

  @Autowired
  private WebpageRepositoryEndpoint endpoint;

  @Autowired
  LocaleRepository localeRepository;

  @Override
  public long count() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public WebpageImpl create() {
    Locale defaultLocale = localeRepository.getDefault();
    WebpageImpl webpage = new WebpageImpl();
    webpage.setLabel(new TextImpl(defaultLocale, ""));
    webpage.setDescription(new MultilanguageDocumentImpl(defaultLocale));
    webpage.setText(new MultilanguageDocumentImpl(defaultLocale));
    return webpage;
  }

  @Override
  public PageResponse<WebpageImpl> find(PageRequest pageRequest) {
    int pageNumber = pageRequest.getPageNumber();
    int pageSize = pageRequest.getPageSize();

    Sorting sorting = pageRequest.getSorting();
    Iterator<Order> iterator = sorting.iterator();

    // FIXME add support for multiple sort fields
    String sortField = "";
    String sortDirection = "";
    String nullHandling = "";
//    while (iterator.hasNext()) {
    if (iterator.hasNext()) {
      Order order = iterator.next();
      sortField = order.getProperty() == null ? "" : order.getProperty();
      sortDirection = order.getDirection() == null ? "" : order.getDirection().name();
      nullHandling = order.getNullHandling() == null ? "" : order.getNullHandling().name();
    }

    return endpoint.find(pageNumber, pageSize, sortField, sortDirection, nullHandling);
  }

  @Override
  public WebpageImpl findOne(UUID uuid) {
    return endpoint.findOne(uuid);
  }

  @Override
  public WebpageImpl save(WebpageImpl webpage) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public WebpageImpl saveWithParentWebsite(WebpageImpl webpage, UUID parentWebsiteUUID) {
    return (WebpageImpl) endpoint.saveWithParentWebsite(webpage, parentWebsiteUUID);
  }

  @Override
  public WebpageImpl saveWithParentWebpage(WebpageImpl webpage, UUID parentWebpageUUID) {
    return (WebpageImpl) endpoint.saveWithParentWebpage(webpage, parentWebpageUUID);
  }

  @Override
  public WebpageImpl update(WebpageImpl webpage) {
    return (WebpageImpl) endpoint.update(webpage.getUuid(), webpage);
  }
}
