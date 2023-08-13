package com.sohclick.hrps.payroll;

import com.sohclick.hrps.payroll.HrpsPayrollApp;
import com.sohclick.hrps.payroll.config.AsyncSyncConfiguration;
import com.sohclick.hrps.payroll.config.EmbeddedKafka;
import com.sohclick.hrps.payroll.config.EmbeddedMongo;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { HrpsPayrollApp.class, AsyncSyncConfiguration.class })
@EmbeddedMongo
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@EmbeddedKafka
public @interface IntegrationTest {
    // 5s is Spring's default https://github.com/spring-projects/spring-framework/blob/main/spring-test/src/main/java/org/springframework/test/web/reactive/server/DefaultWebTestClient.java#L106
    String DEFAULT_TIMEOUT = "PT10S";

    String DEFAULT_ENTITY_TIMEOUT = "PT10S";
}
