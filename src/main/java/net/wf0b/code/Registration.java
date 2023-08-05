/*
 * Copyright (c) 2023 by WF0B, Bill Franzen.  All Rights Reserved.
 *
 *  Registration.java is part of MnConvention.
 *
 *  MnConvention is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  MnConvention is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with Foobar. If not, see <https://www.gnu.org/licenses/>.
 */

package net.wf0b.code;

import org.apache.commons.csv.CSVRecord;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/**
 * A Data Container Class for the Minnesota Section Convention Registration.
 */
public class Registration {

    /**
     * The entry id.
     */
    private int entryID;

    /**
     * The registrant's first name.
     */
    private String firstName;

    /**
     * The registrant's last name.
     */
    private String lastName;

    /**
     * The registrant's email.
     */
    private String email;

    /**
     * The registrant's call sign.
     *
     * A value of NOCALL indicates no call sign.
     */
    private String callSign;

    /**
     * The registrant's city.
     */
    private String city;

    /**
     * How the registrant heard about the conference.
     */
    private String heardAbout;

    /**
     * The paypal token for payment processing.
     */
    private String payPalToken;

    /**
     * The registrant's desired meal.
     */
    private String desiredMeal;

    /**
     * The registrant's state.
     */
    private String state;

    /**
     * The registrant's postal code.
     */
    private String postalCode;

    /**
     * The registrant's street address.
     */
    private String streetAddress;

    /**
     * An array of the registrant's preferred seminars.
     */
    private String[] seminars;

    /**
     * The registrant's telephone number.
     */
    private String phoneNbr;

    /**
     * Internal identifier of a known (to the application) person.
     */
    private String userIdentifier;

    /**
     * The status (presumably) of the registration.
     */
    private String status;

    /**
     * The URL of the form.
     */
    private String formURL;

    /**
     * The source device used by the registrant to register.
     */
    private String sourceDevice;
    /**
     * The dotted ip address of the device the registrant used to register.
     */
    private String ipAddress;

    /**
     * The created date time.
     */
    private Instant created;

    /**
     * The modified date time.
     */
    private Instant modified;

    /**
     * The formatter used for parsing create and modified dates.
     */
    private static DateTimeFormatter YMD_HMS_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Instantiates the registration from the CSV Record
     * @param record the CSV Record
     */
    public Registration(CSVRecord record) {
        entryID = Integer.parseInt(record.get(0));
        firstName = record.get(1);
        email = record.get(2);
        lastName = record.get(3);
        callSign = record.get(4);
        city = record.get(5);
        heardAbout = record.get(6);
        payPalToken = record.get(7);
        desiredMeal = record.get(8);
        state = record.get(9);
        postalCode = record.get(10);
        streetAddress = record.get(11);
        seminars = parseSeminars(record.get(12));
        phoneNbr = record.get(13);
        userIdentifier = record.get(14);
        status = record.get(15);
        formURL = record.get(16);
        sourceDevice = record.get(17);
        ipAddress = record.get(18);
        created = parseDateTime(record.get(19));
        modified = parseDateTime(record.get(20));
    }

    /**
     * Gets the Entry ID
     * @return the Entry ID
     */
    public int getEntryID() {
        return entryID;
    }
    /**
     * Gets the first name
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }
    /**
     * Gets the last name
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }
    /**
     * Gets the email
     * @return the email
     */
    public String getEmail() {
        return email;
    }
    /**
     * Gets the call sign
     * @return the call sign
     */
    public String getCallSign() {
        return callSign;
    }
    /**
     * Gets the city
     * @return the city
     */
    public String getCity() {
        return city;
    }
    /**
     * Gets the Heard About
     * @return the Heard About
     */
    public String getHeardAbout() {
        return heardAbout;
    }
    /**
     * Gets the PayPal token
     * @return the PayPal token
     */
    public String getPayPalToken() {
        return payPalToken;
    }
    /**
     * Gets the desired meal
     * @return the desired meal
     */
    public String getDesiredMeal() {
        return desiredMeal;
    }
    /**
     * Gets the state
     * @return the state
     */
    public String getState() {
        return state;
    }
    /**
     * Gets the postal code
     * @return the postal code
     */
    public String getPostalCode() {
        return postalCode;
    }
    /**
     * Gets the street address
     * @return the street address
     */
    public String getStreetAddress() {
        return streetAddress;
    }
    /**
     * Gets the seminar array list
     * @return the seminar array list
     */
    public String[] getSeminars() {
        return seminars;
    }
    /**
     * Gets the phone number
     * @return the phone number
     */
    public String getPhoneNbr() {
        return phoneNbr;
    }
    /**
     * Gets the user identifier
     * @return the user identifier
     */
    public String getUserIdentifier() {
        return userIdentifier;
    }
    /**
     * Gets the status
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets the form's URL
     * @return the form's URL
     */
    public String getFormURL() {
        return formURL;
    }

    /**
     * Gets the source device
     * @return the source device
     */
    public String getSourceDevice() {
        return sourceDevice;
    }

    /**
     * Gets the ip address
     * @return the ip address
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Gets the created
     * @return the created
     */
    public Instant getCreated() {
        return created;
    }

    /**
     * Gets the modified
     * @return the modified
     */
    public Instant getModified() {
        return modified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Registration that = (Registration) o;
        return entryID == that.entryID && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email) && Objects.equals(callSign, that.callSign) && Objects.equals(city, that.city) && Objects.equals(heardAbout, that.heardAbout) && Objects.equals(payPalToken, that.payPalToken) && Objects.equals(desiredMeal, that.desiredMeal) && Objects.equals(state, that.state) && Objects.equals(postalCode, that.postalCode) && Objects.equals(streetAddress, that.streetAddress) && Arrays.equals(seminars, that.seminars) && Objects.equals(phoneNbr, that.phoneNbr) && Objects.equals(userIdentifier, that.userIdentifier) && Objects.equals(status, that.status) && Objects.equals(formURL, that.formURL) && Objects.equals(sourceDevice, that.sourceDevice) && Objects.equals(ipAddress, that.ipAddress) && Objects.equals(created, that.created) && Objects.equals(modified, that.modified);
    }

    /**
     * Performs a transformation of a Date and Time in yyyy-MM-dd HH:mm:ss format where the
     * field is in UTC as provided (but not stated) in the Date Time.
     * @param field  The source field prior to the transformation
     * @return The transformed field
     */
    private Instant parseDateTime(String field) {
        if (field == null) return null;
        if (field.isEmpty()) return null;
        return Instant.parse(field.replace(" ", "T") + "Z");
    }

    /**
     * Transforms the seminar array, separated by commas, into separate elements in the array.
     *
     * This also cleans up some parts of the field by:
     * <ol>
     * <li>replacing an escaped forward slash with just the slash</li>
     * <li>replacing the unicode left double quote with an ascii double quote</li>
     * <li>replacing the unicode right double quote with an ascii double quote</li>
     * </ol>
     * @param field the input field.
     * @return the output array.
     */
    private String[] parseSeminars(String field) {
        return field.replace("\\/","/")     // replaces the escaped forward slash
                .replace("\\u201c","\"")    // replaces left quote with ascii quote
                .replace("\\u201d", "\"")   // replaces right quote with ascii quote
                .split(",");                            // and splits based on the comma
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(entryID, firstName, lastName, email, callSign, city, heardAbout, payPalToken, desiredMeal, state, postalCode, streetAddress, phoneNbr, userIdentifier, status, formURL, sourceDevice, ipAddress, created, modified);
        result = 31 * result + Arrays.hashCode(seminars);
        return result;
    }

    @Override
    public String toString() {
        return "Registration{" +
                "entryID=" + entryID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", callSign='" + callSign + '\'' +
                ", city='" + city + '\'' +
                ", heardAbout='" + heardAbout + '\'' +
                ", payPalToken='" + payPalToken + '\'' +
                ", desiredMeal='" + desiredMeal + '\'' +
                ", state='" + state + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", streetAddress='" + streetAddress + '\'' +
                ", seminars=" + Arrays.toString(seminars) +
                ", phoneNbr='" + phoneNbr + '\'' +
                ", userIdentifier='" + userIdentifier + '\'' +
                ", status='" + status + '\'' +
                ", formURL='" + formURL + '\'' +
                ", sourceDevice='" + sourceDevice + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                '}';
    }
}
