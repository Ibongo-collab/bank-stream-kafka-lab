package com.bankstream.balanceconsumer.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

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
    void applicationMustNotDependOnSpringFramework() {
        ArchRule rule = noClasses()
                .that().resideInAPackage(BASE_PACKAGE + ".application..")
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
