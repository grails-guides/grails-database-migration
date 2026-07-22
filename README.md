# grails-database-migration

Sample app for **Managing Your Database Schema with the Grails Database Migration Plugin (Grails 8)** (Apache Grails `8.0.0-SNAPSHOT`, JDK 21).

The guide walks through evolving a PostgreSQL schema safely across releases with Liquibase changelogs: baseline the schema from GORM domains, make columns nullable, add columns, redesign tables, and migrate existing data with custom SQL - without relying on Hibernate `dbCreate` auto-DDL.

This is the production follow-on to the [grails-data-access](https://github.com/grails-guides/grails-data-access) guide: *"Now that I've modeled my data, how do I evolve the schema safely across releases?"*

## Layout

| Directory | What it is |
|-----------|------------|
| `initial/` | Grails 8 REST API starter from [start.grails.org](https://start.grails.org) (`postgres`, `database-migration`, `views-json`, `testcontainers`, `spock`). Contains `example.Person` with `dbCreate: none` - work through the guide starting here. |
| `complete/` | The fully migrated sample - `Person` and `Address` domains, Liquibase changelogs (baseline → nullable age → address columns on `person` → split into `Address` + data migration SQL), `GET /api/persons`, and Spock specs. |

## Running

```bash
git clone -b grails8 https://github.com/grails-guides/grails-database-migration.git
cd grails-database-migration/complete
./gradlew test integrationTest
```

To follow the guide step by step, start from `initial/`:

```bash
cd grails-database-migration/initial
./gradlew test
```

Run either app locally (both require PostgreSQL on `localhost:5432`, database `devDb`):

```bash
cd grails-database-migration/initial   # after working through migrations in the guide
./gradlew bootRun

# or the finished sample:
cd grails-database-migration/complete
./gradlew bootRun
```

Example endpoint: `GET /api/persons`.

The integration spec (`DatabaseMigrationIntegrationSpec`) asserts on a **clean Testcontainers PostgreSQL database** that Liquibase tracking tables exist, `person` and `address` have the expected schema (via JDBC `DatabaseMetaData`), GORM persistence works after migrations apply, and a migration-path test proves legacy address columns are copied into `address` before they are dropped.

## Requirements

- **JDK 21** (Temurin recommended; the Gradle build enforces Java 21+)
- **Docker** running locally - required for `integrationTest` (Testcontainers PostgreSQL). Unit tests (`./gradlew test`) do not need Docker.
- **PostgreSQL 16** on `localhost:5432` - required for `./gradlew bootRun` in both `initial/` and `complete/` (default database `devDb` in `application.yml`)

If Gradle reports *"Run this build using a Java 21 or newer JVM"*, your shell or IDE is still on an older JDK:

```bash
sdk install java 21.0.6-tem
sdk default java 21.0.6-tem
java -version   # should show 21.x
```

If `integrationTest` fails with a Testcontainers / `docker.sock` error, start Docker Desktop (or your local Docker daemon) and retry.

## Migration commands

From either `initial/` or `complete/`:

```bash
# Generate initial changelog from GORM domains
./grailsw dbm-generate-gorm-changelog changelog.groovy

# Diff domain changes → new migration file
./grailsw dbm-gorm-diff change-age-constraint-to-nullable.groovy --add

# Inspect / preview / apply (release runbook)
./grailsw dbm-status
./grailsw dbm-update-sql pending.sql
./grailsw dbm-update

# Roll back the last reversible changeset
./grailsw dbm-rollback-count 1

# Tag a known-good point, preview, and roll back to it
./grailsw dbm-tag before-address-redesign
./grailsw dbm-rollback-sql before-address-redesign
./grailsw dbm-rollback before-address-redesign
```

If `./grailsw` is unavailable, use Gradle (CI/automation fallback):

```bash
./gradlew runCommand "-Pargs=dbm-update"
```

## The pattern, in one place

```yaml
# application.yml - never auto-DDL when teaching migrations
environments:
  development:
    dataSource:
      dbCreate: none
      url: jdbc:postgresql://localhost:5432/devDb?tcpKeepAlive=true

grails:
  plugin:
    databasemigration:
      updateOnStart: true
      updateOnStartFileName: changelog.groovy
```

```groovy
// grails-app/migrations/changelog.groovy
databaseChangeLog = {
    include file: 'create-person-table.groovy'
    include file: 'change-age-constraint-to-nullable.groovy'
    include file: 'add-address-fields-to-person.groovy'
    include file: 'create-address-table.groovy'
}
```

Grails 8 uses `org.apache.grails:grails-data-hibernate5-dbmigration` from the Grails BOM - not the legacy `org.grails.plugins:database-migration` coordinate.

## Guide prose

Published narrative lives on [grails.apache.org/guides](https://grails.apache.org/guides/) in [apache/grails-static-website](https://github.com/apache/grails-static-website) under `guides/grails-database-migration/v8/`.

## CI

GitHub Actions (`.github/workflows/grails8.yml`) runs `./gradlew test` for `initial` and `complete`, and `./gradlew integrationTest` for `complete`, on pushes and PRs to the `grails8` branch.
