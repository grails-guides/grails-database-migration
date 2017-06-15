databaseChangeLog = {

    changeSet(author: "Nirav Assar (generated)", id: "1497549057046-1") {
        createTable(tableName: "person") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "personPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "age", type: "INT") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    include file: 'change-age-constraint-to-nullable.groovy'
    include file: 'add-address-fields-to-person.groovy'
    include file: 'create-address-table.groovy'
}
