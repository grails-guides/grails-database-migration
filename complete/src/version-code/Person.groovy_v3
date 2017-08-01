package demo

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class Person {
    String name
    int age

    static hasMany = [addresses: Address]

    String toString() {
        name
    }

    static constraints = {
        age(nullable: true)
    }
}
