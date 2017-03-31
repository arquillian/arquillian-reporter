package org.arquillian.reporter;

import org.assertj.core.api.Assertions;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
@RunWith(Arquillian.class)
public class FailingTest {

    @Test
    @InSequence(1)
    public void runPassingTest(){
        System.out.println("This should pass");
    }

    @Test
    @InSequence(2)
    public void runFailingTest(){
        System.out.println("This should fail");
        Assertions.assertThat(1).isEqualTo(0);
    }
}
