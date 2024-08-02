package com.zzz.platform;

import com.zzz.platform.api.login.dao.LoginFailDaoTest;
import com.zzz.platform.api.login.service.CaptchaServiceTest;
import com.zzz.platform.api.login.service.LoginServiceTest;
import com.zzz.platform.api.login.service.ProtectLoginServiceTest;
import com.zzz.platform.api.login.service.ProtectedPasswordServiceTest;
import com.zzz.platform.api.user.controller.UserControllerTest;
import com.zzz.platform.api.user.dao.UserDaoTest;
import com.zzz.platform.api.user.service.UserServiceTest;
import org.junit.platform.suite.api.IncludeClassNamePatterns;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        UserControllerTest.class,
        UserDaoTest.class,
        UserServiceTest.class,
        LoginFailDaoTest.class,
        CaptchaServiceTest.class,
        LoginServiceTest.class,
        ProtectedPasswordServiceTest.class,
        ProtectLoginServiceTest.class
})
@IncludeClassNamePatterns(".*Test")
public class AllTestsSuite {
}