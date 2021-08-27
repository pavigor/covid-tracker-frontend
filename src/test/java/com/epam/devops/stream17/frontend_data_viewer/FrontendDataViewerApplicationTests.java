package com.epam.devops.stream17.frontend_data_viewer;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FrontendDataViewerApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void dataShouldReturnHeaderMessage() throws Exception {
        WebClient client = new WebClient();
        HtmlPage page = client.getPage("http://localhost:" + port + "/data");
        HtmlTable table = (HtmlTable) page.getElementById("dataTable");
        assertThat(table).isNotNull();
        assertThat(table.getRowCount()).isNotZero();
    }


}
