package eu.micer.capitalcitieslearning.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;

public class CommonUtilTest {
    @Mock
    CommonUtil instance;
    @InjectMocks
    CommonUtil commonUtil;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIsNullOrEmptyNonEmptyString() {
        boolean result = commonUtil.isNullOrEmpty("string");

        Assert.assertEquals(false, result);
    }

    @Test
    public void testIsNullOrEmptyEmptyString() {
        boolean result = commonUtil.isNullOrEmpty("");

        Assert.assertEquals(true, result);
    }

    @Test
    public void testIsNullOrEmptyNullString() {
        String testString = null;

        boolean result = commonUtil.isNullOrEmpty(testString);

        Assert.assertEquals(true, result);
    }

    @Test
    public void testIsNullOrEmpty2NonEmptyList() {
        boolean result = commonUtil.isNullOrEmpty(Collections.singletonList("String"));

        Assert.assertEquals(false, result);
    }

    @Test
    public void testIsNullOrEmpty2EmptyList() {
        boolean result = commonUtil.isNullOrEmpty(new ArrayList());

        Assert.assertEquals(true, result);
    }

    @Test
    public void testIsNullOrEmpty2NullString() {
        ArrayList testList = null;

        boolean result = commonUtil.isNullOrEmpty(testList);

        Assert.assertEquals(true, result);
    }
}