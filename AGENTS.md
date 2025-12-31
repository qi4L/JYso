# Repository Guidelines
回答时使用中文来进行回答。

## Project Structure & Module Organization
- `src/main/java/com/qi4l/JYso`: core servers, controllers, gadget builders, and templates. Keep new code inside relevant packages (e.g., `controllers`, `gadgets`, `template`) to preserve classpath scanning.
- `src/test/java`: regression samples and harness stubs; mirror production packages when adding tests.
- `docs/`: usage notes and vulnerability background material—extend this when adding new exploit flows.
- `libs/`: vendor JARs referenced via `build.gradle`. Avoid committing additional binaries without documenting the source.

## Build, Test, and Development Commands
- `./gradlew clean build` (Windows: `.\gradlew.bat clean build`): compiles sources, runs unit tests, and assembles the default JARs.
- `./gradlew shadowJar`: produces the fat JAR with `com.qi4l.JYso.Starter` as the entry point in `build/libs/JYso-1.3.6.jar`.
- `java -jar build/libs/JYso-1.3.6.jar --help`: quick smoke check that lists CLI options without contacting remote targets.

## Coding Style & Naming Conventions
- Java 8 source level, 4-space indentation, UTF-8 encoding enforced by Gradle.
- Classes/interfaces: `UpperCamelCase`; methods/fields: `lowerCamelCase`; constants: `UPPER_SNAKE_CASE`.
- Preserve the existing package naming (`com.qi4l.JYso.*`) and keep controller annotations adjacent to the class declaration for discoverability.
- Favor focused helpers over long procedural methods; inline comments only when the intent is not obvious (e.g., protocol quirks).

## Testing Guidelines
- Use the default Gradle `test` task; tests should extend JUnit 4 or 5 (see existing examples under `src/test/java/com/example/demo`).
- Name test classes `*Test` and mirror the production package path.
- When adding complex gadgets, add at least a smoke test that exercises class resolution without hitting external services.

## Commit & Pull Request Guidelines
- Commit messages: short imperative subject (`Add Tomcat memshell helper`), include scope when clear (`controller`, `gadget`, `build`).
- Group related edits; avoid bundling code generation, docs, and binaries in a single commit.
- Pull requests should describe the behaviour change, list manual/automated tests, and mention any new configuration or network requirements. Attach reproduction steps or sample LDAP/HTTP commands when applicable.

## Security & Configuration Tips
- Never hard-code operator infrastructure; continue reading host/port data from CLI or encoded payload segments.
- Validate any third-party gadget updates against the `libs/` directory and document new dependencies inside `build.gradle`.
