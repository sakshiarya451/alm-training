package com.adobe.learning.core.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class})
class EmbeddableLrngWidgetOptionsTest {

    @InjectMocks
    private EmbeddableLrngWidgetOptions embeddableLrngWidgetOptions;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testName() {
        embeddableLrngWidgetOptions.setName("Test Name");
        assertEquals("Test Name", embeddableLrngWidgetOptions.getName());
    }

    @Test
    void testDescription() {
        embeddableLrngWidgetOptions.setDescription("Test Description");
        assertEquals("Test Description", embeddableLrngWidgetOptions.getDescription());
    }

    @Test
    void testRef() {
        embeddableLrngWidgetOptions.setRef("Test Ref");
        assertEquals("Test Ref", embeddableLrngWidgetOptions.getRef());
    }

    @Test
    void testType() {
        embeddableLrngWidgetOptions.setType("Test Type");
        assertEquals("Test Type", embeddableLrngWidgetOptions.getType());
    }

    @Test
    void testHelpx() {
        embeddableLrngWidgetOptions.setHelpx("Test Helpx");
        assertEquals("Test Helpx", embeddableLrngWidgetOptions.getHelpx());
    }

    @Test
    void testDefaultValue() {
        embeddableLrngWidgetOptions.setDefaultValue("Default Value");
        assertEquals("Default Value", embeddableLrngWidgetOptions.getDefaultValue());
    }

    @Test
    void testMandatory() {
        embeddableLrngWidgetOptions.setMandatory(true);
        assertTrue(embeddableLrngWidgetOptions.getMandatory());
    }

    @Test
    void testHidden() {
        embeddableLrngWidgetOptions.setHidden(true);
        assertTrue(embeddableLrngWidgetOptions.getHidden());
    }
}

