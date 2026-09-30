package com.bankstream.balanceconsumer.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

/**
 * NOTE: {@code application} is deliberately NOT held to the
 * Spring-framework-free rule anymore — {@code BalanceService} now carries
 * {@code @Service}, same conscious choice made for transaction-producer's
 * {@code TransactionService} and fraud-watcher's {@code FraudDetectionService}.
 * {@code domain} still is, and still must never depend on Spring or on
 * infrastructure.
 */
class HexagonalArchitectureTest {

    private static final String BASE_PACKAGE = "com.bankstream.balanceconsumer";

    private final JavaClasses classes = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages(BASE_PACKAGE);

    @Test
    void domainMustNotDependOnSpringFramework() {
        ArchRule rule = noClasses()
                .that().resideInAPackage(BASE_PACKAGE + ".domain..")
                .should().dependOnClassesThat().resideInAnyPackage("org.springframework..");
        rule.check(classes);
    }

    @Test
    void domainAndApplicationMustNotDependOnInfrastructure() {
        ArchRule rule = noClasses()
                .that().resideInAnyPackage(BASE_PACKAGE + ".domain..", BASE_PACKAGE + ".application..")
                .should().dependOnClassesThat().resideInAPackage(BASE_PACKAGE + ".infrastructure..");
        rule.check(classes);
    }
}