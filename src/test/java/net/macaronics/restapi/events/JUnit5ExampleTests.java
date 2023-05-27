package net.macaronics.restapi.events;

import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.restdocs.RestDocumentationExtension;

public class JUnit5ExampleTests {


    @RegisterExtension
    final RestDocumentationExtension restDocumentation = new RestDocumentationExtension ("custom");
}
