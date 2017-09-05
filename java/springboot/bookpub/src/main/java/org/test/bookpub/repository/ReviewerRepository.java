package org.test.bookpub.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.test.bookpub.entity.Publisher;

@RepositoryRestResource
public interface ReviewerRepository extends
        PagingAndSortingRepository<Publisher.Reviewer, Long> {
}