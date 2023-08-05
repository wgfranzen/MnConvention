/*
 * Copyright (c) 2023 by WF0B, Bill Franzen.  All Rights Reserved.
 *
 *  MnConvention.java is part of MnConvention.
 *
 *  MnConvention is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  MnConvention is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with Foobar. If not, see <https://www.gnu.org/licenses/>.
 */

package net.wf0b.code;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import picocli.CommandLine;

import java.io.*;
import java.nio.file.Files;
import java.time.Instant;
import java.util.*;

/**
 * MnConvention is a post-processor for reading the CSV generated from the BitForms containing registration
 * information.  The registration information needs to be parsed to translate escaped
 * characters and specific unicode characters from the array of selected seminars.
 * Ultimately, this will produce an Excel XLS file that can be read as an Excel Spreadsheet.
 */
@CommandLine.Command(name = "MnConvention", mixinStandardHelpOptions = true,
version = "net.wf0b.code.MnConvention.MnConvention-2023.0.0-SNAPSHOT",
footer = "Copyright (c) 2023 by WF0B, Bill Franzen.  All Rights Reserved.\n\n" +
        "Licensed under GNU AFFERO GENERAL PUBLIC LICENSE",
description = "MnConvention is a post-processor for reading the CSV generated from the BitForms containing " +
        "registration information.  The registration information needs to be parsed to translate escaped " +
        "characters and specific unicode characters from the array of selected seminars.  Ultimately, " +
        "this will produce an Excel XLS file that can be read as an Excel Spreadsheet.\n")
public class MnConvention implements Runnable {

    /**
     * The list of columns in the Registation Workbook
     */
    private static final String[] REGISTRATION_HEADINGS = { "Entry ID", "First Name", "Last Name", "Call", "eMail",
            "Phone", "Street", "City", "State", "Postal Code", "Desired Meal", "Paypal Token", "form URL",
            "Source Device", "IP Address","Created", "Modified" };

    /**
     * The widths of the columns in the Registration Workbook
     */
    private static final int[] REGISTRATION_WIDTHS = { 8, 12, 12, 7, 25, 12, 25, 15, 12, 12, 14, 20, 36, 20, 13, 20, 20 };
    /**
     * The list of columns in the Preferences Workbook
     */
    private static final String[] PREFERENCE_HEADINGS = { "Seminar", "Entry ID", "First Name", "Last Name", "Call",
            "Ordinal", "Created", "Modified"};

    /**
     * The list of column widths in the Preferences Workbook
     */
    private static final int[] PREFERENCE_WIDTHS = { 75, 8, 12, 12, 7, 7, 20, 20};

    /**
     * The list of Meal Headings
     */
    private static final String[] MEAL_HEADINGS = {"Desired Meal", "Count"};

    /**
     * The list of column widths for Meals
     */
    private static final int[] MEAL_WIDTHS = {14, 8};

    /**
     * the list of Seminar Headings
     */
    private static final String[] SEMINAR_HEADINGS = { "Seminar", "Counts"};

    /**
     * the list of column widths for Seminars
     */
    private static final int[] SEMINAR_WIDTHS = {75, 8};

    /**
     * The input comma-separated value file.
     *
     * Note: this file is a generally accepted CSV file with certain escapes contained in the data.
     * These escapes include escapes for a forward slash as well as translating an ascii double quote
     * into unicode left and right double quote characters.  These are processed as part of the selected
     * seminar field, containing a quoted array of values that are in comma-separated.
     */
    @CommandLine.Option(names = { "-i", "--input"}, paramLabel = "input CSV file",
            description = "the input file downloaded from the web application",
            required = true)
    private File inputFile;

    /**
     * The Excel file.
     */
    @CommandLine.Option(names = {"-o", "--output"}, paramLabel = "output XLS file",
            description = "the standardized output file based on the input file",
            required = true)
    private File outputFile;

    /**
     * The Array List of Registrations
     */
    private ArrayList<Registration> registrations = new ArrayList<>();

    /**
     * Summarizes the meals to their specific count
     */
    private TreeMap<String, Integer> meals = new TreeMap<>();

    /**
     * Summarizes seminars to their specific count
     */
    private TreeMap<String, Integer> seminars = new TreeMap<>();

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        try {
            loadRegistrations();
        } catch (IOException e) {
            throw new RuntimeException("Error processing Input", e);
        }
        // for (Registration registration : registrations) { System.out.println(registration.toString()); }

        try {
            writeWorkBook();
        } catch (IOException e) {
            throw new RuntimeException("Error processing output", e);
        }


    }

    /**
     * The process of reading the input file, parsing registration, and then retaining as the array list.
     * @throws IOException any IO Exception processing the input file.
     */
    private void loadRegistrations() throws IOException {
            Reader in = Files.newBufferedReader(inputFile.toPath());
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
            int c = 0;
            for (CSVRecord record : records) {
                if (c != 0) {
                    registrations.add(new Registration(record));
                }
                c++;
            }
            in.close();
    }

    /**
     * Creates and writes the Excel workbook
     * @throws IOException throws an IO Exception for writing the workbook
     */
    private void writeWorkBook() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        // create the header font
        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setBold(true);

        // create the detail font
        Font detailFont = workbook.createFont();
        detailFont.setFontHeightInPoints((short) 11);

        // Create Data Format for the workbook
        DataFormat df = workbook.createDataFormat();

        // Create header left justified text cell style
        CellStyle hct = workbook.createCellStyle();
        hct.setFont(headerFont);
        hct.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        hct.setBorderBottom(BorderStyle.THICK);
        hct.setAlignment(HorizontalAlignment.CENTER);


        // Create detailed centered text cell style
        CellStyle dct = workbook.createCellStyle();
        dct.setFont(detailFont);
        dct.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        dct.setAlignment(HorizontalAlignment.CENTER);

        // Create left justified text cell style
        CellStyle lj = workbook.createCellStyle();
        lj.setFont(detailFont);
        lj.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        lj.setAlignment(HorizontalAlignment.LEFT);

        // Create the date/time cell style
        CellStyle dt = workbook.createCellStyle();
        dt.setFont(detailFont);
        //TODO Fix to date/time format
        dt.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        dt.setAlignment(HorizontalAlignment.CENTER);


        // Create a Number (integer) cell style
        CellStyle ni = workbook.createCellStyle();
        ni.setFont(detailFont);
        ni.setDataFormat(df.getFormat("###0"));
        ni.setAlignment(HorizontalAlignment.CENTER);

        // Create sheets in workbook
        Sheet conferenceRegistration = workbook.createSheet();
        workbook.setSheetName(0, "Registrations");
        Sheet seminarPreferences = workbook.createSheet();
        workbook.setSheetName(1, "SeminarPreferences");
        Sheet mealCounts = workbook.createSheet();
        workbook.setSheetName(2, "MealCounts");
        Sheet seminarCounts = workbook.createSheet();
        workbook.setSheetName(3, "SeminarCounts");

        // Populate Registrations
        createHeader(conferenceRegistration, REGISTRATION_HEADINGS, REGISTRATION_WIDTHS, hct);
        createRegistrationDetail(conferenceRegistration, registrations, ni, dct, lj);

        // Populate Preferences
        createHeader(seminarPreferences, PREFERENCE_HEADINGS, PREFERENCE_WIDTHS, hct);
        createPreferenceDetail(seminarPreferences, registrations, ni, dct, lj);

        // Meal Summary
        createHeader(mealCounts, MEAL_HEADINGS, MEAL_WIDTHS, hct);
        System.out.print("Meals counted: ");
        createSummaryDetail(mealCounts, meals, ni, dct, lj);


        // Seminar Summary
        createHeader(seminarCounts, SEMINAR_HEADINGS, SEMINAR_WIDTHS, hct);
        System.out.print("Seminars counted: ");
        createSummaryDetail(seminarCounts, seminars, ni, dct, lj);


        FileOutputStream out = new FileOutputStream(outputFile);
        workbook.write(out);
        out.close();

    }

    /**
     * Creates the header row
     * @param sheet the worksheet
     * @param values the array of column names
     * @param widths the array of column widths
     * @param headingStyle the style of the heading, typically bold, larger font, and thick bottom border
     */
    private void createHeader(Sheet sheet, String[] values, int[] widths, CellStyle headingStyle) {
        int rowNbr = 0;
        Row r = sheet.createRow(rowNbr);
        for (int colNbr = 0; colNbr < values.length; colNbr++) {
            Cell c = r.createCell(colNbr);
            c.setCellValue(values[colNbr]);
            c.setCellStyle(headingStyle);
        }
        for (int colNbr = 0; colNbr < widths.length; colNbr++) {
            sheet.setColumnWidth(colNbr, widths[colNbr]*256);
        }
    }

    /**
     * Create a Cell
     * @param r the row
     * @param colNbr the column number
     * @param value the value
     * @param style the style
     */
   private void createCell(Row r, int colNbr, Integer value, CellStyle style) {
        Cell c = r.createCell(colNbr);
        c.setCellValue(value);
        c.setCellStyle(style);
    }

    /**
     * Create a Cell
     * @param r the row
     * @param colNbr the column number
     * @param value the value
     * @param style the style
     */
    private void createCell(Row r, int colNbr, String value, CellStyle style) {
        Cell c = r.createCell(colNbr);
        c.setCellValue(value);
        c.setCellStyle(style);
    }

    /**
     * Create a Cell
     * @param r the row
     * @param colNbr the column number
     * @param value the value
     * @param style the style
     */
    private void createCell(Row r, int colNbr, Instant value, CellStyle style) {
        Cell c = r.createCell(colNbr);
        if (value != null) c.setCellValue(value.toString()); else c.setCellValue("");
        c.setCellStyle(style);
    }
    /**
     * populates the detail rows of the registration sheet.  Note that Row 0 has already been populated.
     * @param sheet the registration sheet
     * @param registrations the registrations
     * @param numberStyle a number style
     * @param centeredStyle a centered text style
     * @param leftJustified a left justified style
     */
    private void createRegistrationDetail(Sheet sheet, ArrayList<Registration> registrations,
                                          CellStyle numberStyle, CellStyle centeredStyle,
                                          CellStyle leftJustified) {
        int rowNbr = 0;
        int colNbr;
        Cell c;

        for (Registration registration : registrations) {
            rowNbr++;
            Row r = sheet.createRow(rowNbr);
            createCell(r, 0, registration.getEntryID(), numberStyle);
            createCell(r, 1, registration.getFirstName(), centeredStyle);
            createCell(r, 2, registration.getLastName(), centeredStyle);
            createCell(r, 3, registration.getCallSign(), centeredStyle);
            createCell(r, 4, registration.getEmail(), centeredStyle);
            createCell(r, 5, registration.getPhoneNbr(), centeredStyle);
            createCell(r, 6, registration.getStreetAddress(), centeredStyle);
            createCell(r, 7, registration.getCity(), centeredStyle);
            createCell(r, 8, registration.getState(), centeredStyle);
            createCell(r, 9, registration.getPostalCode(), centeredStyle);
            createCell(r, 10, registration.getDesiredMeal(), centeredStyle);
            increment(meals, registration.getDesiredMeal());
            createCell(r, 11, registration.getPayPalToken(), centeredStyle);
            createCell(r, 12, registration.getFormURL(), centeredStyle);
            createCell(r, 13, registration.getSourceDevice(), centeredStyle);
            createCell(r, 14, registration.getIpAddress(), centeredStyle);
            createCell(r, 15, registration.getCreated(), leftJustified);
            createCell(r, 16, registration.getModified(), leftJustified);
        }
        System.out.println("Registrations recorded: " + rowNbr);

    }

    /**
     * Creates the Preference Details
     * @param sheet the Sheet
     * @param registrations the array of registrations
     * @param numberStyle the number style
     * @param centeredStyle the centered style
     * @param leftJustified left justified style
     */

    private void createPreferenceDetail(Sheet sheet, ArrayList<Registration> registrations,
                                        CellStyle numberStyle, CellStyle centeredStyle,
                                        CellStyle leftJustified) {
        int rowNbr = 0;
        int colNbr;
        Cell c;

        for (Registration registration : registrations) {
            for (int seminarNbr = 0; seminarNbr < registration.getSeminars().length; seminarNbr++) {
                rowNbr++;
                Row r = sheet.createRow(rowNbr);
                createCell(r, 0, registration.getSeminars()[seminarNbr], leftJustified);
                increment(seminars, registration.getSeminars()[seminarNbr]);
                createCell(r, 1, registration.getEntryID(), numberStyle);
                createCell(r, 2, registration.getFirstName(), centeredStyle);
                createCell(r, 3, registration.getLastName(), centeredStyle);
                createCell(r, 4, registration.getCallSign(), centeredStyle);
                createCell(r, 5, seminarNbr, numberStyle);
                createCell(r, 6, registration.getCreated(), leftJustified);
                createCell(r, 7, registration.getModified(), leftJustified);
            }
        }
        System.out.println("Seminar Preferences recorded: " + rowNbr);
    }

    /**
     * From a list of values and the number of occurrences of that value, generate a descending order list by number of
     * occurrences and place that in the specific worksheet. Row 0 (the header row) is assumed to be populated.
     * @param sheet the specific worksheet
     * @param counts The TreeMap of values with their associated occurrence counts
     * @param numberStyle the number style
     * @param centeredStyle the centered style
     * @param leftJustified the left justified style
     */
    private void createSummaryDetail(Sheet sheet, TreeMap<String, Integer> counts,
                CellStyle numberStyle, CellStyle centeredStyle,
                CellStyle leftJustified) {

        int rowNbr = 0;
        int colNbr;
        Cell c;

        // An ArrayList of the counted values is used in the event there are different values with the same number of
        // occurrences.
        ArrayList<String> list;

        // This is the TreeMap organized by number of occurrences.  The ArrayList allows a number of different values
        // to hold the same number of occurrences.
        TreeMap<Integer, ArrayList<String>> sorted = new TreeMap<>(Collections.reverseOrder());

        // populate the sorted descending list
        for (String key: counts.keySet()) {
            if (sorted.containsKey(counts.get(key))) {
                sorted.get(counts.get(key)).add(key);
            } else {
                list = new ArrayList<>();
                list.add(key);
                sorted.put(counts.get(key), list);
            }
        }

        // process the sorted list (in reverse order)
        Set set = sorted.entrySet();
        Iterator i = set.iterator();
        while (i.hasNext()) {

            // me.getKey() is the occurrence count (goes in column 1)
            // me.getValue() is the list of values with that occurrence count
            Map.Entry me = (Map.Entry) i.next();

            list = (ArrayList<String>) me.getValue();

            // for each value
            for (int j = 0; j < list.size(); j++) {
                // increment the row
                rowNbr++;
                Row r = sheet.createRow(rowNbr);

                // establish the first column as the value
                createCell(r, 0, list.get(j), leftJustified);

                // establish the second column as the number of occurrences
                createCell(r, 1, (Integer) me.getKey(), numberStyle);
            }
        }
        System.out.println(rowNbr);
    }

    /**
     * provides the means to count particular values
     * @param counter the list of values to be counted
     * @param value the value to be counted
     */
    private void increment(TreeMap<String, Integer> counter, String value) {
        if (value.isBlank()) return;
        Integer current = 0;
        if (counter.containsKey(value)) {
            current = counter.get(value);
            counter.replace(value, current, current+1);
        } else {
            counter.put(value, current+1);
        }
        // System.out.println(value + ":" + counter.get(value));
    }
    /**
     * The main entry point for MnConvention.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        int exitCode = new CommandLine(new MnConvention()).execute(args);
    }

}
