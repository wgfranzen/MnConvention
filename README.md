# MNConvention

MnConvention is a post-processor for reading the CSV generated from the BitForms containing registration information.  The registration information needs to be parsed to translate escaped characters and specific unicode characters from the array of selected seminars.  Ultimately, this will produce an Excel XLS file that can be read as an Excel Spreadsheet.

MnConvention is licensed under the GNU Affero General Public License, see [License](license.html).

## How It Works

MnConvention has a website collecting registration information.  That information is periodically extracted to a csv file.
The csv file contains information about the registrant, their meal choice and their preferences for viewing seminars.  
There are three key interests in the information:

* The basic information about the registrant
* The meal choice, and counts of the different meal choices
* The seminar preferences, and the counts of possible attendance for a specific seminar to help determine room size

The MnConvention application reads the CSV file and creates an Excel workbook with four sheets:

* A Registration Detail sheet
* A Seminar Detail sheet
* A Meals Summary sheet
* A Seminar Summary sheet

## Data Source

The data sourced is from the website bit form which captures registrant data.  The registrant data is needed to generate
conference badges, track dietary needs, and help determine which room should be used for what particular seminar.

Additional data sources may be used to enhance and/or validate the data.

### Data Source Fields

All fields are comma separated. All Fields are text fields when initially processed.

* <b>Entry ID</b>: The entry id. This is a unique identifier of a registrant.
* <b>First Name</b>: The registrant's first name.
* <b>Email</b>: The registrant's email.
* <b>Last Name</b>: The registrant's last name.
* <b>Callsign</b>: The registrant's call sign.
* <b>City</b>: The registrant's city.
* <b>How did you hear about the convention?</b>: How the registrant heard about the conference.
* <b>Paypal</b>: The paypal token for payment processing.  When this is empty, the registrant did not pay.
* <b>Lunch</b>: The registrant's desired meal.
* <b>State</b>: The registrant's state.
* <b>Zip Code</b>: The registrant's postal code.
* <b>Address</b>: The registrant's street address.
* <b>Seminars: There are 25 Seminars to choose from; you may choose up to 5. This is to help us schedule seminars accordingly. </b>: An array of the registrant's preferred seminars.  This array is enclosed in quotes, and the elements are separated by commas.  Each element of the array has also been processed through an encoding process to escape forward slashes and convert embedded double quotes to unicode left/right double quotes.  In raw form, this is quite messy.
* <b>Phone</b>: The registrant's telephone number.
* <b>User</b>: Internal identifier of a known (to the application) person.  This is typically web developers.
* <b>Status</b>: The status (presumably) of the registration.  All data to this point has been blank.
* <b>Refer URL</b>: The URL of the form capturing registrant information.
* <b>Device</b>: The source device used by the registrant to register.
* <b>IP address</b>: The dotted ip address of the device the registrant used to register.
* <b>Created Time</b>: The created date time.  This is in UTC from the website, however not in specific UTC date format.
* <b>Modified Time</b>: The modified date time.  This is in UTC from the website, however not in specific UTC date format.

### Resultant Worksheets in the Excel Workbook

The input fields are parsed, and as noted below, some fields are transformed.

#### Registrations

The Registrations worksheet is the normalized registration data consisting of the registrant information.  This contains no seminar information.

* <b>Entry ID</b>: The entry id. This is a unique identifier of a registrant.
* <b>First Name</b>: The registrant's first name.
* <b>Last Name</b>: The registrant's last name.
* <b>Call</b>: The registrant's call sign.
* <b>eMail</b>: The registrant's email.
* <b>Phone</b>: The registrant's telephone number, as entered.
* <b>Street</b>: The registrant's street address.
* <b>City</b>: The registrant's city.
* <b>State</b>: The registrant's state.e
* <b>Postal Code</b>: The registrant's postal code.
* <b>Desired Meal</b>: The registrant's desired meal.
* <b>Paypal Token</b>: The PayPal token for payment processing.  When this is empty, the registrant did not pay.
* <b>form URL</b>: The URL of the form capturing registrant information.
* <b>Source Device</b>: The source device used by the registrant to register.
* <b>IP Address</b>: The dotted ip address of the device the registrant used to register.
* <b>Created</b>: The created date time.
* <b>Modified</b>: The modified date time.

#### SeminarPreferences

The SeminarPreferences normalizes each registrant's list of desired seminars.  This means for each registrant,
there may be up to five separate lines. Each row is identified by the Entry Id and Ordinal.

* <b>Seminar</b>: The name of the seminar.
* <b>Entry ID</b>: The entry id. This is a unique identifier of a registrant.
* <b>First Name</b>: The registrant's first name.
* <b>Last Name</b>: The registrant's last name.
* <b>Call</b>: The registrant's call sign.
* <b>Ordinal</b>: The ordinal position of the seminar in the array, zero-based.
* <b>Created</b>: The created date time.
* <b>Modified</b>: The modified date time.

#### MealCounts

The MealCounts worksheet is a summary work sheet.  Each distinct meal selection is counted and then placed in descending
count sequence.  This means the most popular meals are listed first, then the next most popular, and so on.

* <b>Desired Meal</b>: The desired meal.
* <b>Count</b>: The aggregate count of registrants with that meal choice.

#### SeminarCounts

The SeminarCounts worksheet is a summary work sheet.  Each distinct seminar selection is counted and then placed in
descending count sequence.  This means the most popular seminars are listed first, then the next most popular, and so on.

* <b>Seminar</b>: The name of the seminar.
* <b>Count</b>: The aggregate count of registrants with that seminar choice.

