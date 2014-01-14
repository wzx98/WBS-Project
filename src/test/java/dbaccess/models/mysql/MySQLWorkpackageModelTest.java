/*
 * The WBS-­Tool is a project managment tool combining the Work Breakdown
 * Structure and Earned Value Analysis
 * Copyright (C) 2013 FH-­Bingen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY;; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package dbaccess.models.mysql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.notNullValue;

import dbaccess.data.Workpackage;
import dbaccess.models.WorkpackageModel;
import org.junit.*;
import sqlutils.TestDBConnector;
import sqlutils.TestData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MySQLWorkpackageModelTest {

    private WorkpackageModel wpModel;

    @Before
    public final void setup() {
        wpModel = new MySQLWorkpackageModel(TestDBConnector.getConnection());
    }

    @After
    public final void cleanup() {
        wpModel = null;
    }

    @Test
    public final void testGetWorkpackage() {
        List<Workpackage> wpList = wpModel.getWorkpackage();
        assertThat(wpList, notNullValue());

        assertThat(wpList.size(), equalTo(8));

        assertThat(wpList.get(0).getName(), equalTo("Mauerbau"));
        assertThat(wpList.get(1).getName(), equalTo("Vorbereitung"));
        assertThat(wpList.get(2).getName(), equalTo("Inspektion der Baustelle"));
        assertThat(wpList.get(3).getName(), equalTo("Entwurfszeichnung"));
        assertThat(wpList.get(4).getName(), equalTo("Material Beschaffung"));
        assertThat(wpList.get(5).getName(), equalTo("Bauen"));
        assertThat(wpList.get(6).getName(), equalTo("Zement Mischen"));
        assertThat(wpList.get(7).getName(), equalTo("Stein auf Stein"));
    }

    @Test
    public final void testGetWorkpackage_OnlyLeavesTrue() {
        List<Workpackage> wpList = wpModel.getWorkpackage(true);
        assertThat(wpList, notNullValue());

        assertThat(wpList.size(), equalTo(5));

        assertThat(wpList.get(0).getName(), equalTo("Inspektion der Baustelle"));
        assertThat(wpList.get(1).getName(), equalTo("Entwurfszeichnung"));
        assertThat(wpList.get(2).getName(), equalTo("Material Beschaffung"));
        assertThat(wpList.get(3).getName(), equalTo("Zement Mischen"));
        assertThat(wpList.get(4).getName(), equalTo("Stein auf Stein"));
    }

    @Test
    public final void testGetWorkpackage_OnlyLeavesFalse() {
        List<Workpackage> wpList = wpModel.getWorkpackage(false);
        assertThat(wpList, notNullValue());

        assertThat(wpList.size(), equalTo(8));

        assertThat(wpList.get(0).getName(), equalTo("Mauerbau"));
        assertThat(wpList.get(1).getName(), equalTo("Vorbereitung"));
        assertThat(wpList.get(2).getName(), equalTo("Inspektion der Baustelle"));
        assertThat(wpList.get(3).getName(), equalTo("Entwurfszeichnung"));
        assertThat(wpList.get(4).getName(), equalTo("Material Beschaffung"));
        assertThat(wpList.get(5).getName(), equalTo("Bauen"));
        assertThat(wpList.get(6).getName(), equalTo("Zement Mischen"));
        assertThat(wpList.get(7).getName(), equalTo("Stein auf Stein"));
    }

    @Test
    public final void testGetWorkpackage_ByStringID1() {
        final String projectID = "0.0.0.0";

        Workpackage projectWP = wpModel.getWorkpackage(projectID);

        assertThat(projectWP, notNullValue());

        // validate the db fields
        assertThat(projectWP.getId(), equalTo(1));
        assertThat(projectWP.getProjectID(), equalTo(1));
        assertThat(projectWP.getEmployeeID(), equalTo(2));
        assertThat(projectWP.getParentID(), equalTo(0));
        assertThat(projectWP.getPositionID(), equalTo(1));
        assertThat(projectWP.getName(), equalTo("Mauerbau"));
        assertThat(projectWP.getDescription(), equalTo("Projekt"));
        assertThat(projectWP.getBac(), equalTo(7.0));
        assertThat(projectWP.getAc(), equalTo(2.625));
        assertThat(projectWP.getEv(), equalTo(750.0));
        assertThat(projectWP.getEtc(), equalTo(4.5));
        assertThat(projectWP.getEac(), equalTo(1350.0));
    }

    @Test
    public final void testGetWorkpackage_ByStringID2() {
        String wpID = "1.0.0.0";
        Workpackage wp = wpModel.getWorkpackage(wpID);

        assertThat(wp, notNullValue());

        // validate the db fields
        assertThat(wp.getId(), equalTo(2));
        assertThat(wp.getProjectID(), equalTo(1));
        assertThat(wp.getEmployeeID(), equalTo(2));
        assertThat(wp.getParentID(), equalTo(1));
        assertThat(wp.getPositionID(), equalTo(1));
        assertThat(wp.getName(), equalTo("Vorbereitung"));
        assertThat(wp.getDescription(), equalTo("Vorbereitende Arbeiten"));
        assertThat(wp.getBac(), equalTo(2.0));
        assertThat(wp.getAc(), equalTo(2.125));
        assertThat(wp.getEv(), equalTo(600.0));
        assertThat(wp.getEtc(), equalTo(0.0));
        assertThat(wp.getEac(), equalTo(0.0));
    }

    @Test
    public final void testGetWorkpackagesInDateRange1() throws ParseException {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        final String startDateString = "2014-01-02 00:00:01";
        final String endDateString = "2014-01-03 09:59:59";

        final Date startDate = format.parse(startDateString);
        final Date endDate = format.parse(endDateString);

        List<Workpackage> wpList = wpModel.getWorkpackagesInDateRange(startDate, endDate);
        assertThat(wpList, notNullValue());

        assertThat(wpList.size(), equalTo(1));
    }

    @Test
    public final void testGetWorkpackagesInDateRange2() throws ParseException {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        final String startDateString = "2014-01-01 00:00:01";
        final String endDateString = "2014-01-01 08:01:01";

        final Date startDate = format.parse(startDateString);
        final Date endDate = format.parse(endDateString);

        List<Workpackage> wpList = wpModel.getWorkpackagesInDateRange(startDate, endDate);
        assertThat(wpList, notNullValue());

        assertThat(wpList.size(), equalTo(3));
    }

    @Test
    public final void testGetWorkpackagesInDateRange3() throws ParseException {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        final String startDateString = "2014-01-03 10:01:01";
        final String endDateString = "2014-01-04 10:00:00";

        final Date startDate = format.parse(startDateString);
        final Date endDate = format.parse(endDateString);

        List<Workpackage> wpList = wpModel.getWorkpackagesInDateRange(startDate, endDate);
        assertThat(wpList, notNullValue());

        assertThat(wpList.size(), equalTo(0));
    }

    @Test
    public final void testUpdateWorkpackage() {
        String stringID = "3.1.0.0";

        Workpackage wpOld = wpModel.getWorkpackage(stringID);
        assertThat(wpOld, notNullValue());
        assertThat(wpOld.getEtc(), equalTo(1.0));

        wpOld.setEtc(1.5);
        wpModel.updateWorkpackage(wpOld);

        Workpackage wpNew = wpModel.getWorkpackage(stringID);
        assertThat(wpNew.getEtc(), equalTo(1.5));

        // change test data back to original state
        TestData.reloadData(TestDBConnector.getConnection());
    }

    @Test
    public final void testDeleteWorkpackage() {
        // TODO: implement testDeleteWorkpackage
    }

}






