<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>ISPW</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>15</maven.compiler.source>
        <maven.compiler.target>15</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <sonar.organization>damianlopatowski</sonar.organization>
        <sonar.host.url>https://sonarcloud.io</sonar.host.url>
        <javafx.version>17.0.2</javafx.version> <!-- Specifica la versione di JavaFX -->
    </properties>

    <dependencies>
        <!-- Dipendenze JavaFX -->
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-controls</artifactId>
            <version>${javafx.version}</version>
        </dependency>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-fxml</artifactId>
            <version>${javafx.version}</version>
        </dependency>

        <!-- Dipendenza per MySQL JDBC -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.23</version> <!-- Usa una versione compatibile -->
        </dependency>

        <!-- Dipendenza per SLF4J (Logging, utile per il JDBC) -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.30</version>
        </dependency>

        <!-- Dipendenza per Logback (Log Framework compatibile con SLF4J) -->
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.3</version>
        </dependency>
    </dependencies>

    <repositories>
        <repository>
            <id>central</id>
            <url>https://repo.maven.apache.org/maven2</url>
        </repository>
    </repositories>

    <build>
        <plugins>
            <!-- Plugin JavaFX -->
            <plugin>
                <groupId>org.openjfx</groupId>
                <artifactId>javafx-maven-plugin</artifactId>
                <version>0.0.8</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>run</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <mainClass>org.example.LoginPage</mainClass> <!-- Cambia con il nome della tua classe principale -->
                </configuration>
            </plugin>

            <!-- Plugin SonarQube per l'analisi PHP -->
            <plugin>
                <groupId>org.sonarsource.scanner.maven</groupId>
                <artifactId>sonar-maven-plugin</artifactId>
                <version>3.9.0.2155</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>sonar</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

</project>
