package org.test.bookpubstarter.dbcount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public class DbCountRunner implements CommandLineRunner {
    protected final Logger logger = LoggerFactory.getLogger(DbCountRunner.class);
    private Collection<CrudRepository> repositories;

    public DbCountRunner(Collection<CrudRepository> repositories) {
        this.repositories = repositories;
    }

    @Override
    public void run(String... strings) throws Exception {
        // lamda 需要jdk8
        repositories.forEach(crudRepository -> {
            logger.info(String.format("%s has %s entries",
                    getRepositoryName(crudRepository.getClass()),
                    crudRepository.count()));
        });
    }

    private static String getRepositoryName(Class crudRepositoryClass) {
        for (Class repositoryInterface : crudRepositoryClass.getInterfaces()) {
            if (repositoryInterface.getName().startsWith("org.test.bookpub.repository")) {
                return repositoryInterface.getSimpleName();
            }
        }
        return "UnknownRepository";
    }
}
