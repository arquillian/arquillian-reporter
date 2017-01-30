package org.arquillian.reporter.impl.asserts;

import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.StringAssert;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class StringKeyAssert extends ObjectAssert<StringKey> {

    public StringKeyAssert(StringKey actual) {
        super(actual);
    }

    public static StringKeyAssert assertThat(StringKey actual) {
        return new StringKeyAssert(actual);
    }

    public StringKeyAssert isUnknownStringKey() {
        isNotNull();
        Assertions.assertThat(actual).as("The string key should be an instance of UnknownStringKey")
            .isInstanceOf(UnknownStringKey.class);
        return this;
    }

    public StringKeyAssert isNotUnknownStringKey() {
        isNotNull();
        Assertions.assertThat(actual).as("The string key should not be an instance of UnknownStringKey")
            .isNotInstanceOf(UnknownStringKey.class);
        return this;
    }

    public StringAssert value(){
        return new StringAssert(actual.getValue());
    }

    public StringAssert description(){
        return new StringAssert(actual.getDescription());
    }

    public StringAssert icon(){
        return new StringAssert(actual.getIcon());
    }
}
