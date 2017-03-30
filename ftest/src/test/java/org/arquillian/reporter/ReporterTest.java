package org.arquillian.reporter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.assertj.core.api.Assertions.assertThat;

public class ReporterTest {

    @Before
    public void setUp() {
        Result result = JUnitCore.runClasses(GreeterTest.class);
    }

    @Test
    public void verify_test_report_creation() throws FileNotFoundException {
        Gson gson = new Gson();

        File file = new File("target/report.json");
        JsonArray json = gson.fromJson(new FileReader(file), JsonArray.class);

        assertThat(file).exists();

        assertThat(json).contains();



        // JSONAssert.assertEquals(expectedJSONString, actualJSON, strictMode);

    }


}
