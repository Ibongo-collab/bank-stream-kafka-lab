# BankStream Kafka Lab

Trois microservices Java 21 / Spring Boot 3, chacun en **architecture
hexagonale stricte**, pensés pour apprendre Kafka par la pratique plutôt
que pour être un portfolio "complet" dès le départ. Le code Kafka
lui-même (producers, listeners, config) n'est **pas encore écrit** — c'est
volontaire, on le construit ensemble à l'étape suivante.

## Les 3 services

| Service | Rôle | Port |
|---|---|---|
| `transaction-producer` | Soumet des transactions bancaires (dépôt/retrait) | 8081 |
| `balance-consumer` | Maintient le solde par compte | 8082 |
| `fraud-watcher` | Détecte les transactions suspectes (montant élevé, vélocité) | 8083 |

**Le pattern qu'on va observer une fois Kafka branché** : les trois
services communiqueront uniquement via le topic `transactions` —
`balance-consumer` et `fraud-watcher` seront deux **consumer groups
différents**, chacun recevant une copie indépendante de tous les
messages (fan-out), alors qu'ajouter une deuxième instance d'un même
service (ex: 2 instances de `balance-consumer`) se partagerait les
partitions au sein du **même** groupe (load balancing). C'est exactement
la distinction qu'on veut observer en pratique.

## Pourquoi "architecture hexagonale stricte"

Dans chaque service, `domain` et `application` ne contiennent **aucune
annotation Spring** — zéro `@Service`, zéro `@Component`, zéro import
`org.springframework.*`. Tout le câblage se fait explicitement dans
`infrastructure/config/BeanConfiguration.java` via des méthodes `@Bean`.

Ce n'est pas juste une déclaration d'intention dans ce README : chaque
service a un test ArchUnit (`architecture/HexagonalArchitectureTest.java`)
qui **fait échouer le build** si cette règle est violée — si demain
quelqu'un importe `org.springframework.stereotype.Service` dans
`domain/`, le test le détecte immédiatement.

Conséquence concrète : `TransactionServiceTest`, `BalanceTest`,
`FraudDetectionServiceTest`, etc. sont des tests JUnit purs, sans
`@SpringBootTest`, sans contexte Spring à démarrer — ils s'exécutent en
quelques millisecondes.

## Le pattern "port + adaptateur stub"

Chaque service a un port sortant qui sera un jour branché sur Kafka :

- `transaction-producer` → `TransactionPublisherPort`, actuellement
  implémenté par `LoggingTransactionPublisherAdapter` (logue au lieu de
  publier)
- `balance-consumer` / `fraud-watcher` → pas de port Kafka entrant
  pour l'instant ; à la place, un endpoint REST temporaire
  (`POST /api/v1/balances/apply`, `POST /api/v1/fraud/evaluate`) simule
  ce que le futur `@KafkaListener` fera

Chaque classe temporaire est marquée `TEMPORARY` dans sa Javadoc et
contient un commentaire `TODO(kafka-lab)` à l'endroit exact où le code
Kafka viendra. Rien d'autre ne changera quand on branchera Kafka — ni le
domaine, ni les tests applicatifs, ni les contrôleurs REST (qui seront
simplement supprimés une fois les listeners Kafka en place).

## Build tool

Maven multi-module (pas Gradle) : un `pom.xml` racine (`packaging=pom`,
`<modules>`) qui importe la BOM `spring-boot-dependencies` dans son
`dependencyManagement`, et un `pom.xml` par service qui hérite de ce
parent. Chaque module se build indépendamment :

```bash
mvn -pl transaction-producer -am package
```

(`-am` = "also make", construit aussi les modules dont celui-ci dépend —
inutile ici puisque les 3 services n'ont aucune dépendance entre eux, mais
c'est l'habitude à prendre pour un vrai reactor multi-module).

## Dépendances incluses (par service)

- `spring-boot-starter-web` + `-validation` — API REST
- `spring-boot-starter-actuator` + `micrometer-registry-prometheus` — santé/métriques, prêt pour un scrape Prometheus
- `springdoc-openapi-starter-webmvc-ui` — Swagger UI (`/swagger-ui.html`)
- `resilience4j-spring-boot3` + `spring-boot-starter-aop` — prêt pour circuit breaker/retry (utile dès que le publisher/listener Kafka existera)
- `mapstruct` — mapping domaine ↔ DTO à la frontière des adaptateurs
- `lombok` — utilisé uniquement dans l'infrastructure, jamais dans `domain`/`application`
- `archunit-junit5` — enforcement de l'architecture (voir plus haut)
- `testcontainers` (junit-jupiter) — prêt pour les tests d'intégration Kafka à venir

## Lancer en local

```bash
docker compose up --build
```

- `transaction-producer` : http://localhost:8081/swagger-ui.html
- `balance-consumer` : http://localhost:8082/swagger-ui.html
- `fraud-watcher` : http://localhost:8083/swagger-ui.html

### Tester le flux manuellement (sans Kafka, pour l'instant)

```bash
# 1. Soumettre une transaction
curl -X POST http://localhost:8081/api/v1/transactions \
  -H "Content-Type: application/json" \
  -d '{"accountId":"11111111-1111-1111-1111-111111111111","type":"DEPOSIT","amount":150000,"currencyCode":"XAF"}'

# 2. Appliquer manuellement cette même transaction au solde
#    (c'est cette étape qu'un @KafkaListener fera automatiquement plus tard)
curl -X POST http://localhost:8082/api/v1/balances/apply \
  -H "Content-Type: application/json" \
  -d '{"transactionId":"...","accountId":"11111111-1111-1111-1111-111111111111","type":"DEPOSIT","amount":150000,"currencyCode":"XAF"}'

# 3. Vérifier le solde
curl http://localhost:8082/api/v1/balances/11111111-1111-1111-1111-111111111111

# 4. Évaluer la même transaction contre les règles de fraude
curl -X POST http://localhost:8083/api/v1/fraud/evaluate \
  -H "Content-Type: application/json" \
  -d '{"transactionId":"...","accountId":"11111111-1111-1111-1111-111111111111","type":"DEPOSIT","amount":150000,"currencyCode":"XAF"}'
```

## Prochaines étapes (à faire ensemble)

1. Ajouter Kafka au `docker-compose.yml` (broker KRaft + Kafka UI)
2. `transaction-producer` : remplacer `LoggingTransactionPublisherAdapter`
   par un vrai `KafkaTransactionPublisherAdapter`
3. `balance-consumer` et `fraud-watcher` : ajouter un adaptateur entrant
   `@KafkaListener` par service, chacun dans son propre consumer group,
   et supprimer les endpoints REST temporaires
4. Observer le rebalancing en scalant `balance-consumer` à 2-3 instances
5. Ajouter la gestion d'erreurs (poison pill → DLT)
6. Ajouter un commit manuel des offsets et discuter at-least-once

## Persistence (pas encore, mais planifié)

Aucune base de données pour l'instant — chaque port sortant de
persistence (`TransactionRepositoryPort`, `BalanceRepositoryPort`,
`FraudAlertRepositoryPort`, `TransactionHistoryPort`) a un adaptateur
`InMemory*` (`ConcurrentHashMap`), volontairement, pour garder Kafka comme
seule variable à apprendre. **MongoDB** remplacera ces adaptateurs plus
tard — grâce au port qui les isole, l'ajout se limite à une nouvelle
classe d'adaptateur par service (`Mongo*RepositoryAdapter`) implémentant
le même port, sans toucher au domaine, à l'application, ni aux tests
unitaires existants. On le fera une fois Kafka posé, pour ne pas mélanger
les deux apprentissages.

## Déploiement cible (plus tard)

Docker Compose pour le local ; déploiement final sur **AWS ECR + ECS
Fargate**. La structure multi-module Maven et les Dockerfiles multi-stage
sont déjà prêts pour ça — il restera à écrire le Terraform et le pipeline
GitHub Actions une fois la partie Kafka posée.
