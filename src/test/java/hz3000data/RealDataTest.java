/**
 * 
 */
package hz3000data;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author ypj
 * 2016年12月10日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring-context.xml",
		"classpath*:spring-context-jedis.xml" })
public class RealDataTest {

}
