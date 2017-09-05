package org.test.bookpub.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.test.bookpub.entity.Author;

/**
 * 使用注解直接暴露rest服务
 * @author yunsheng
 */
@RepositoryRestResource(collectionResourceRel = "writers", path = "writers")
public interface AuthorRepository extends PagingAndSortingRepository<Author, Long> {
}
