package com.javaclasses.chat.webapp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ChatControllerShould.class, LoginControllerShould.class,
        MessageControllerShould.class, RegistrationControllerShould.class})
public class AllTests {
}
