package net.macaronics.restapi.common;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
@Ignore //테스트 클래스로 사용하지 않는다
public class BaseControllerTest {

    @Autowired
    public  MockMvc mockMvc;

    @Autowired
    public ObjectMapper objectMapper;

}
