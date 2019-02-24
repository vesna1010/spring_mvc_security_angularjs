package com.vesna1010.onlinebooks.test.repository;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.vesna1010.onlinebooks.config.ConfigurationForDatabase;
import com.vesna1010.onlinebooks.test.BaseTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ConfigurationForDatabase.class })
public abstract class BaseRepositoryTest extends BaseTest {

}