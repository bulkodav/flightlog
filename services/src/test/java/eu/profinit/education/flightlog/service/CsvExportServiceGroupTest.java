package eu.profinit.education.flightlog.service;

import eu.profinit.education.flightlog.IntegrationTestConfig;
import eu.profinit.education.flightlog.to.FileExportTo;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = IntegrationTestConfig.class)
@Transactional
@Tag("slow")
@Tag("integration")
public class CsvExportServiceGroupTest {

    @InjectMocks
    private CsvExportService testSubject;

    // 6.1: Odstrante anotaci @Ignore, aby se test vykonaval
    //@Ignore("Tested method is not implemented yet")
    @Test
    public void testCSVExport() throws IOException, URISyntaxException {
        String fileName = "csv/expectedExport.csv";
        String expected = readFileToString(fileName)
                             .replaceAll("\\n|\\r\\n", System.getProperty("line.separator"));

        FileExportTo allFlightsAsCsv = testSubject.getAllFlightsAsCsv();
        String actual = new String(allFlightsAsCsv.getContent())
                            .replaceAll("\\n|\\r\\n", System.getProperty("line.separator"));

        assertEquals(expected, actual);
    }

    private String readFileToString(String fileName) throws URISyntaxException, IOException {
        return Files.readString(Paths.get(getClass().getClassLoader().getResource(fileName).toURI()));
    }
}