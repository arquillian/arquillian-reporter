package org.arquillian.reporter.impl.asserts;

import org.arquillian.reporter.api.event.Identifier;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.ClassAssert;
import org.assertj.core.api.StringAssert;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class IdentifierAssert extends AbstractAssert<IdentifierAssert, Identifier> {

    public IdentifierAssert(Identifier actual) {
        super(actual, IdentifierAssert.class);
    }

    public static IdentifierAssert assertThat(Identifier actual) {
        return new IdentifierAssert(actual);
    }

    public ClassAssert sectionEventClass(){
        return new ClassAssert(actual.getSectionEventClass());
    }

    public StringAssert sectionId(){
        return new StringAssert(actual.getSectionId());
    }
}
