package fi.om.municipalityinitiative.util;

import org.junit.Test;

import java.math.BigInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ReflectionUtilsTest {


    @Test
    public void copies_fields() {
        Parent parent = new Parent();
        parent.stringValue = "strinasd";
        parent.doubleValue = 34345.3453454;
        parent.longValue = 34345345345L;

        Child child = new Child();
        child.bigIntegerValue = new BigInteger("345345345");

        ReflectionUtils.copyFieldValuesToChild(parent, child);
        assertThat(child.stringValue, is(parent.stringValue));
        assertThat(child.doubleValue, is(parent.doubleValue));
        assertThat(child.getLongValue(), is(parent.longValue));
        assertThat(child.bigIntegerValue, is(new BigInteger("345345345")));

    }

    class Parent {
        public String stringValue;
        Double doubleValue;
        private long longValue;

        public Long getLongValue() {
            return longValue;
        }
    }

    class Child extends Parent {
        private BigInteger bigIntegerValue;
    }
}
