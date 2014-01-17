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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dbaccess.data.EmployeeCalendar;
import dbaccess.models.EmployeeCalendarModel;

/**
 * The <code>MySQLEmployeeCalendarModel</code> class implements the
 * <code>EmployeeCalendarModel</code> and handles all the database access concerning
 * employee calendars.
 */
public class MySQLEmployeeCalendarModel implements EmployeeCalendarModel {

    /**
     * The MySQL connection to use.
     */
    private Connection connection;

    /**
     * Constructor.
     *
     * @param con The MySQL connection to use.
     */
    public MySQLEmployeeCalendarModel(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addNewEmployeeCalendar(EmployeeCalendar empCal) {
        try {
            Statement stm = connection.createStatement();
            stm.execute("INSERT INTO employee_calendar VALUES (" + empCal.getId()
                    + "," + empCal.getFid_emp() + ",'" + empCal.getBegin_time()
                    + "','" + empCal.getEnd_time() + "','"
                    + empCal.getDescription() + "'," + empCal.isAvailability()
                    + "," + empCal.isFull_time() + ")");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public List<EmployeeCalendar> getEmployeeCalendar() {
        List<EmployeeCalendar> empCalList = new ArrayList<EmployeeCalendar>();
        try {
            ResultSet result = null;
            EmployeeCalendar employeeCalendar = null;
            Statement stm = connection.createStatement();
            result = stm.executeQuery("SELECT * FROM employee_calendar");

            while (result.next()) {
                employeeCalendar = EmployeeCalendar.fromResultSet(result);
                empCalList.add(employeeCalendar);
            }

            return empCalList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public EmployeeCalendar getEmployeeCalendar(int id) {
        EmployeeCalendar employeeCalendar = null;
        try {
            ResultSet result = null;
            Statement stm = connection.createStatement();
            result = stm
                    .executeQuery("SELECT * FROM employee_calendar WHERE id = "
                            + id);

            if (result.next()) {
                employeeCalendar = EmployeeCalendar.fromResultSet(result);
            }

            return employeeCalendar;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<EmployeeCalendar> getEmployeeCalendarForFID(int fid) {
        List<EmployeeCalendar> empCalList = new ArrayList<EmployeeCalendar>();
        try {
            ResultSet result = null;
            EmployeeCalendar employeeCalendar = null;
            Statement stm = connection.createStatement();
            result = stm
                    .executeQuery("SELECT * FROM employee_calendar WHERE fid_emp = "
                            + fid);

            while (result.next()) {
                employeeCalendar = EmployeeCalendar.fromResultSet(result);
                empCalList.add(employeeCalendar);
            }

            return empCalList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<EmployeeCalendar> getEmployeeCalendarInDateRange(Date from,
            Date to) {
        List<EmployeeCalendar> empCalList = new ArrayList<EmployeeCalendar>();
        try {
            ResultSet result = null;
            EmployeeCalendar employeeCalendar = null;
            Statement stm = connection.createStatement();
            result = stm
                    .executeQuery("SELECT * FROM employee_calendar WHERE begin_time > "
                            + from + " AND end_time < " + to);

            while (result.next()) {
                employeeCalendar = EmployeeCalendar.fromResultSet(result);
                empCalList.add(employeeCalendar);
            }

            return empCalList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<EmployeeCalendar> getEmployeeCalendarInDateRange(Date from,
            Date to, boolean mode2) {
        // TODO was macht es?
        return null;
    }

    @Override
    public void deleteEmployeeCalendar(int id) {
        try {
            Statement stm = connection.createStatement();
            stm.execute("DELETE * FROM employee_calendar WHERE id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
