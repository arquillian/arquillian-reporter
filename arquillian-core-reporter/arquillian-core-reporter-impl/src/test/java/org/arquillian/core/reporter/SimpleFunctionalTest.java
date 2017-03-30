package org.arquillian.core.reporter;

import org.assertj.core.api.Assertions;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SimpleFunctionalTest {

    @Test
    public void invokeSimpleTest(){
        Result result = JUnitCore.runClasses(SimpleTest.class);
        Assertions.assertThat(result.getFailureCount()).isEqualTo(0);
    }

    @AfterClass
    public static void afterClass(){

    }
}
