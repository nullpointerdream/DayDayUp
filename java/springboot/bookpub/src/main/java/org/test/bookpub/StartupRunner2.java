package org.test.bookpub;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

/**
 * Created by yangyunsheng on 2017/7/12.
 */
@Order(value = 2)
public class StartupRunner2 implements CommandLineRunner{

    Log logger = LogFactory.getLog(getClass());

    @Override
    public void run(String... strings) throws Exception {
        logger.info(strings[0]);
    }
}
