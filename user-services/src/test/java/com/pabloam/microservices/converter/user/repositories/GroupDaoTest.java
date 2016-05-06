/**
 * 
 */
package com.pabloam.microservices.converter.user.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pabloam.microservices.converter.user.config.DatabaseConfiguration;
import com.pabloam.microservices.converter.user.model.Group;

/**
 * @author PabloAM
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({ "jpa, h2" })
@SpringApplicationConfiguration(classes = DatabaseConfiguration.class)
@Transactional
public class GroupDaoTest {

	@Autowired
	GroupDao groupDao;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.user.repositories.GroupDao#findByDescription(java.lang.String)}
	 * .
	 */
	@Test
	public void testFindByDescription() throws Exception {
		/*
		 * Insert a bunch of groups and try to get one
		 */
		List<String> searchList = saveGroupsAndGetEven(10, "gr_");

		String expected = searchList.get(searchList.size() - 1);

		Group g = this.groupDao.findByDescription(expected);
		assertNotNull(g);
		assertEquals(expected, g.getDescription());
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.user.repositories.GroupDao#findMultipleByDescriptionList(java.util.List)}
	 * .
	 */
	@Test
	public void testFindMultipleByDescriptionList() throws Exception {
		/*
		 * Insert a bunch of groups and try to get some of them
		 */
		List<String> searchList = saveGroupsAndGetEven(10, "gr_");

		List<Group> actual = this.groupDao.findMultipleByDescriptionList(searchList);
		assertEquals(searchList.size(), actual.size());
		actual.stream().forEach(g -> assertTrue(searchList.contains(g.getDescription())));
	}

	private List<String> saveGroupsAndGetEven(int max, String groupPrefix) {

		List<String> searchList = new ArrayList<String>();

		for (int i = 1; i <= max; i++) {
			Group g = new Group();
			g.setDescription(groupPrefix + i);
			groupDao.save(g);

			// Save even
			if (i % 2 == 0) {
				searchList.add(g.getDescription());
			}
		}
		return searchList;
	}

}
