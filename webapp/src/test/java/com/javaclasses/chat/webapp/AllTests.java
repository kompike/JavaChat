package com.javaclasses.chat.webapp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ChatControllerShould.class, UserControllerShould.class})
public class AllTests {
}
