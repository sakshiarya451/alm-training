/*
 * Copyright 2021 Adobe. All rights reserved. This file is licensed to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR REPRESENTATIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.adobe.learning.core.entity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.MockitoAnnotations;

@ExtendWith({ MockitoExtension.class })
class CustomerCommerceEntityTest {

  private String data;
  private CustomerCommerceEntity customerCommerceEntity;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    data =
        "{\"firstname\":\"Adobe CP\",\"lastname\":\"Test\",\"email\":\"adobetestcp@adobetest.com\"}";
    customerCommerceEntity = new CustomerCommerceEntity();
  }

  @Test
  void testCommerceEntity() {
    Gson gson = new Gson();
    CustomerCommerceEntity entity = gson.fromJson(data, CustomerCommerceEntity.class);
    assertNotNull(entity);
    assertTrue("adobetestcp@adobetest.com".equals(entity.getEmail()));
    assertTrue("Adobe CP".equals(entity.getFirstname()));
    assertTrue("Test".equals(entity.getLastname()));
  }

  @Test
  void testGetAndSetFirstname() {
      customerCommerceEntity.setFirstname("John");
      assertEquals("John", customerCommerceEntity.getFirstname());
  }

  @Test
  void testGetAndSetLastname() {
      customerCommerceEntity.setLastname("Doe");
      assertEquals("Doe", customerCommerceEntity.getLastname());
  }

  @Test
  void testGetAndSetEmail() {
      customerCommerceEntity.setEmail("john.doe@example.com");
      assertEquals("john.doe@example.com", customerCommerceEntity.getEmail());
  }
}

