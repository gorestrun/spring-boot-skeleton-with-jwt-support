- Configured for tWAS + Oracle 12c+
- Vertical slice architecture

## Unit Tests
- In `application.properties`, use `spring.profiles.active=local, test`
- If you are using "Run As JUnit Test" in the IDE, 

> - Run > Run Configurations... > JUnit > *configuration_name* > Test "tab" > Run all tests in the selected project, package or source folder:<br>
> *Choose* src/test/java<br>
> - Run > Run Configurations... > JUnit > *configuration_name* > Classpath "tab" > User Entries > Advanced... > Add folders<br>
> *Add* `src/test/resources/sql`<br>


- If you are using gradle, on the command prompt, in the project root directory, run 
    - `gradlew cleanTest`
    - `gradlew test`

## Integration Tests
- In `application.properties`, use `spring.profiles.active=local, integration-test`
- If you are using "Run As JUnit Test" in the IDE, 

> - Run > Run Configurations... > JUnit > *configuration_name* > Test "tab" > Run all tests in the selected project, package or source folder:<br>
> *Choose* src/integrationTest/java<br>
> - Run > Run Configurations... > JUnit > *configuration_name* > Classpath "tab" > User Entries > Advanced... > Add folders<br>
> *Add* `src/test/resources/sql`<br>

- If you are using gradle, on the command prompt, in the project root directory, run 
    - `gradlew cleanIntegrationTest`
    - `gradlew integrationTest`
