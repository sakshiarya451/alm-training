package com.adobe.learning.core.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({ MockitoExtension.class })
class EmbeddableLrngWidgetConfigTest {

    @InjectMocks
    private EmbeddableLrngWidgetConfig embeddableLrngWidgetConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetName() {
        embeddableLrngWidgetConfig.setName("Test Name");
        assertEquals("Test Name", embeddableLrngWidgetConfig.getName());
    }

    @Test
    void testSetName() {
        embeddableLrngWidgetConfig.setName("Test Name");
        assertEquals("Test Name", embeddableLrngWidgetConfig.getName());
    }

    @Test
    void testGetRef() {
        embeddableLrngWidgetConfig.setRef("Test Ref");
        assertEquals("Test Ref", embeddableLrngWidgetConfig.getRef());
    }

    @Test
    void testSetRef() {
        embeddableLrngWidgetConfig.setRef("Test Ref");
        assertEquals("Test Ref", embeddableLrngWidgetConfig.getRef());
    }

    @Test
    void testGetWidgetRef() {
        embeddableLrngWidgetConfig.setWidgetRef("Test WidgetRef");
        assertEquals("Test WidgetRef", embeddableLrngWidgetConfig.getWidgetRef());
    }

    @Test
    void testSetWidgetRef() {
        embeddableLrngWidgetConfig.setWidgetRef("Test WidgetRef");
        assertEquals("Test WidgetRef", embeddableLrngWidgetConfig.getWidgetRef());
    }

    @Test
    void testGetDescription() {
        embeddableLrngWidgetConfig.setDescription("Test Description");
        assertEquals("Test Description", embeddableLrngWidgetConfig.getDescription());
    }

    @Test
    void testSetDescription() {
        embeddableLrngWidgetConfig.setDescription("Test Description");
        assertEquals("Test Description", embeddableLrngWidgetConfig.getDescription());
    }

    @Test
    void testGetType() {
        embeddableLrngWidgetConfig.setType("Test Type");
        assertEquals("Test Type", embeddableLrngWidgetConfig.getType());
    }

    @Test
    void testSetType() {
        embeddableLrngWidgetConfig.setType("Test Type");
        assertEquals("Test Type", embeddableLrngWidgetConfig.getType());
    }

    @Test
    void testGetOptions() {
        List<EmbeddableLrngWidgetOptions> options = Collections.emptyList();
        embeddableLrngWidgetConfig.setOptions(options);
        assertEquals(options, embeddableLrngWidgetConfig.getOptions());
    }

    @Test
    void testSetOptions() {
        List<EmbeddableLrngWidgetOptions> options = Collections.emptyList();
        embeddableLrngWidgetConfig.setOptions(options);
        assertEquals(options, embeddableLrngWidgetConfig.getOptions());
    }

    @Test
    void testGetDefault() {
        embeddableLrngWidgetConfig.setDefault("Test Default");
        assertEquals("Test Default", embeddableLrngWidgetConfig.getDefault());
    }

    @Test
    void testSetDefault() {
        embeddableLrngWidgetConfig.setDefault("Test Default");
        assertEquals("Test Default", embeddableLrngWidgetConfig.getDefault());
    }

    @Test
    void testSetOptionsModifiableListException() {
        List<EmbeddableLrngWidgetOptions> options = Collections.emptyList();
        embeddableLrngWidgetConfig.setOptions(options);

        assertThrows(UnsupportedOperationException.class, () -> {
            embeddableLrngWidgetConfig.getOptions().add(new EmbeddableLrngWidgetOptions());
        });
    }
}

