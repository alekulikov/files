package qa.guru.tests;


import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import qa.guru.model.Order;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FilesParsingTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private final ClassLoader classLoader = getClass().getClassLoader();

    private InputStream getResourceByExtension(String fileExtension) throws Exception {
        ZipInputStream zis = new ZipInputStream(classLoader.getResourceAsStream("sample.zip"));
        ZipEntry zipEntry;
        while ((zipEntry = zis.getNextEntry()) != null) {
            if (zipEntry.getName().endsWith(fileExtension)) {
                return zis;
            }
        }
        return InputStream.nullInputStream();
    }

    @Test
    void pdfFileParsingTest() throws Exception {
        try (var pdfInputStream = getResourceByExtension(".pdf")) {
            var pdf = new PDF(pdfInputStream);
            Assertions.assertTrue(pdf.text.contains("This is a simple PDF file. Fun fun fun."));
        }
    }

    @Test
    void xlsFileParsingTest() throws Exception {
        try (var xlsInputStream = getResourceByExtension(".xls")) {
            var xls = new XLS(xlsInputStream);
            var actualId = xls.excel.getSheetAt(0).getRow(2)
                    .getCell(7).getNumericCellValue();
            Assertions.assertEquals(1582, actualId);
        }
    }

    @Test
    void csvFileParsingTest() throws Exception {
        var csvParserBuilder = new CSVParserBuilder()
                .withSeparator(';')
                .build();
        try (var csvInputStreamReader = new InputStreamReader(
                getResourceByExtension(".csv"));
             var csvReader = new CSVReaderBuilder(csvInputStreamReader)
                     .withCSVParser(csvParserBuilder)
                     .build()
        ) {
            var csvData = csvReader.readAll();
            Assertions.assertEquals(6, csvData.size());
            Assertions.assertArrayEquals(new String[]{"booker12", "9012", "Rachel", "Booker"},
                    csvData.get(1));
        }
    }

    @Test
    void jsonFileParsingTest() throws Exception {
        try (var jsonInputStream = classLoader.getResourceAsStream("sample.json")) {
            var actualOrder = mapper.readValue(jsonInputStream, Order.class);
            Assertions.assertEquals(1224.50, actualOrder.getTotalCost());
            Assertions.assertEquals(2, actualOrder.getMaterials().size());
        }
    }
}