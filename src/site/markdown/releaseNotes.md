# Release Notes

## 2023.0.1-SNAPSHOT

With the 2023-09-18 csv feed, a new column was discovered called <code>Divider</code>, after <code>Phone</code> and before <code>User</code>.
Data Inspection showed the values for <code>Divider</code> to be all blank, so the column was ignored.   


Class <code>Registration</code> was modified to not read <code>Divider</code>.

## 2023.0.0-SNAPSHOT

The initial release for the 2023 Minnesota Section Convention.

*  Processes an input csv (comma separated value) file with form specific fields.  (When the form changes, the fields and or field order may change).
*  Translates escaped slash \\/ to slash / in field.
*  Translates left double quote \u201c and right double quote \u201d to a ascii double quote.
*  Splits the Seminar Array (a quoted string with comma separated elements) into upto five separate preference fields.
*  Exports file as an Excel Spreadsheet.
    *  There is a Registration Detail Worksheet.
    * There is a Seminar Detail Worksheet.
    * There is a Meal Summary Worksheet.
    * There is a Seminar Summary Worksheet.
