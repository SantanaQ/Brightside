plugins {
    application
    java
    jacoco
}

group = "org.example"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}

application {
    mainClass = "org.main.Main"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.google.code.gson:gson:2.11.0")
}

sourceSets {
    main {
        java {
            setSrcDirs(listOf("src/main/java"))
        }
    }
    test {
        java {
            setSrcDirs(listOf("src/test/java"))
        }
    }
}


tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport) //Ruft funktion auf, wenn "./gradlew test" im Terminal eingegeben wird.
}

tasks.jacocoTestReport { //Der Bericht wird immer nach dem Testlauf generiert. Findet man in "/build/reports/tests/test/index.html"
    dependsOn(tasks.test) // Die Tests müssen vor dem Bericht ausgeführt werden
    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.required.set(true)
    }
}