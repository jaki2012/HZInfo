package sys;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hnac.hzinfo.modules.sys.dao.MenuDao;
import com.hnac.hzinfo.modules.sys.entity.Menu;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring-context.xml",
		"classpath*:spring-context-jedis.xml" })
public class MenuTest {
	@Autowired
	MenuDao menuDao ;
	
	@Test
	public void testGetMenu(){
		Menu memu = menuDao.get("1");
		System.out.println(memu.getName());
	}
	
	//@Test
	public void testGetAllList(){
		
		@SuppressWarnings("deprecation")
		List<Menu> list = menuDao.findAllList();
		for(Menu menu:list){
			System.out.println(menu.getName());
		}
	}
	//@Test
	public void testInsert(){
		System.out.println(menuDao.bindMenuByRole("D0BF11B4-E47A-1992-D5B9-3F8727B66AD6", "4"));
	}
}
