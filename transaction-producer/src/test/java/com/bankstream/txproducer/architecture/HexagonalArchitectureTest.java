package com.bankstream.txproducer.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

/**
 * These tests are the actual enforcement of "strict hexagonal
 * architecture" — not just a diagram in the README. If someone (including
 * future-me) accidentally imports {@code org.springframework.*} into the
 * domain package, or has the domain reach into infrastructure, the build
 * fails here.
 */
class HexagonalArchitectureTest {

    private static final String BASE_PACKAGE = "com.bankstream.txproducer";

    private final com.tngtech.archunit.core.domain.JavaClasses classes = new ClassFileImporter()
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
    void applicationMustNotDependOnSpringFramework() {
        ArchRule rule = noClasses()
                .that().resideInAPackage(BASE_PACKAGE + ".application..")
                .should().dependOnClassesThat().resideInAnyPackage("org.springframework..");
        rule.check(classes);
    }

    @Test
    void domainMustNotDependOnInfrastructure() {
        ArchRule rule = noClasses()
                .that().resideInAPackage(BASE_PACKAGE + ".domain..")
                .should().dependOnClassesThat().resideInAPackage(BASE_PACKAGE + ".infrastructure..");
        rule.check(classes);
    }

    @Test
    void applicationMustNotDependOnInfrastructure() {
        ArchRule rule = noClasses()
                .that().resideInAPackage(BASE_PACKAGE + ".application..")
                .should().dependOnClassesThat().resideInAPackage(BASE_PACKAGE + ".infrastructure..");
        rule.check(classes);
    }

    @Test
    void domainMustNotDependOnApplication() {
        ArchRule rule = ArchRuleDefinition.noClasses()
                .that().resideInAPackage(BASE_PACKAGE + ".domain..")
                .should().dependOnClassesThat().resideInAPackage(BASE_PACKAGE + ".application..");
        rule.check(classes);
    }
}
