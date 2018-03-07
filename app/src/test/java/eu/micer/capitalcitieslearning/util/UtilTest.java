package eu.micer.capitalcitieslearning.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;

public class UtilTest {
    @Mock
    Util instance;
    @InjectMocks
    Util util;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIsNullOrEmptyNonEmptyString() {
        boolean result = util.isNullOrEmpty("string");

        Assert.assertEquals(false, result);
    }

    @Test
    public void testIsNullOrEmptyEmptyString() {
        boolean result = util.isNullOrEmpty("");

        Assert.assertEquals(true, result);
    }

    @Test
    public void testIsNullOrEmptyNullString() {
        String testString = null;

        boolean result = util.isNullOrEmpty(testString);

        Assert.assertEquals(true, result);
    }

    @Test
    public void testIsNullOrEmpty2NonEmptyList() {
        boolean result = util.isNullOrEmpty(Collections.singletonList("String"));

        Assert.assertEquals(false, result);
    }

    @Test
    public void testIsNullOrEmpty2EmptyList() {
        boolean result = util.isNullOrEmpty(new ArrayList());

        Assert.assertEquals(true, result);
    }

    @Test
    public void testIsNullOrEmpty2NullString() {
        ArrayList testList = null;

        boolean result = util.isNullOrEmpty(testList);

        Assert.assertEquals(true, result);
    }
}