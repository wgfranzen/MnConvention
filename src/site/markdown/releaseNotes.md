# Release Notes

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
