package org.arquillian.environment.reporter.impl;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class SimpleTest {

    @Deployment
    public static Archive deploy(){
        return ShrinkWrap.create(WebArchive.class);
    }

    @Test
    public void runTest(){

    }
}

