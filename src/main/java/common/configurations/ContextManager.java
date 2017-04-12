package common.configurations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.testng.ITestContext;
import org.testng.xml.XmlTest;

import junit.textui.TestRunner;

/**
 * Helper file to initialize and access context at various levels.
 * 
 * There are three levels of context management supported in this framework.
 * 
 * Global Context - As the name says, the scope of the context variable is global
 * 
 * Test level Context - This context is specific to the testng test. 
 * All methods that belong to a specific testng test can access test level parameters using this context. 
 * 
 * Parameters defined within the test tag in testng xml will be available in test level context.
 * This context will hold all global parameters plus the test level parameters.
 * 
 * <test name="APP Test">
 *  <parameter name="app_launch_activity" value="com.flipkart.seller.activities.SplashActivity" />
 * 		<parameter name="deviceId" value="192.168.57.101:5555" />
 *		<parameter name="app_wait_activity" value="com.flipkart.seller.user.login.activities.LoginScreen" />
 *		<groups>
 *			<run>
 *				<include name="mobile" />
 *			</run>
 *		</groups>
 *		<packages>
 *			<package name="com.flipkart.seller.dashboard.tests" />
 *		</packages>
 *	</test>
 *
 * Thread Level Context:
 * Context maintained at a thread level. This will hold all global context parameters plus anything that you set in a particular thread. 
 * This would be useful when you run testng tests in parallel mode and you need to pass a data object to methods running on the same thread.
 *
 * @author sabarinath.s
 *
 */

public class ContextManager {

	private static TestContext globalContext = new TestContext();
	private static ThreadLocal<TestContext> threadLevelContext = new ThreadLocal<TestContext>();
	private static Map<XmlTest, TestContext> testLevelContext = new HashMap<XmlTest, TestContext>();

	public static void initGlobalContext(ITestContext context) {

		globalContext.buildTestContext(context);
	}

	public static void initThreadLevelContext(ITestContext context) {
		if (threadLevelContext.get() == null) {
			threadLevelContext.set(new TestContext());
			threadLevelContext.get().buildTestContext(context);
		}
	}

	public static void initTestLevelContext(ITestContext context) {
		testLevelContext.put(context.getCurrentXmlTest(),
				new TestContext());
		testLevelContext.get(context.getCurrentXmlTest()).buildTestLevelContext(
				context);
	}

	public static TestContext getGlobalContext() {
		return globalContext;
	}

	public static TestContext getThreadLevelContext() {
		return threadLevelContext.get();
	}

	public static TestContext getTestLevelContext() {
		return testLevelContext.get(((ITestContext) getThreadLevelContext()
				.getParameter(TestContext.TESTNG_CONTEXT))
				.getCurrentXmlTest());
	}

	public static TestContext getTestLevelContext(ITestContext context) {
		return testLevelContext.get(context.getCurrentXmlTest());
	}
}
