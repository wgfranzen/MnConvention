/*
 * Copyright (c) 2024 by WF0B, Bill Franzen.  All Rights Reserved.
 *
 *  Transaction.java is part of MnConvention.
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
import org.apache.poi.ss.formula.functions.T;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Transaction {

    private String date;

    private String number;

    private String description;

    private String notes;

    private static final String commodity = "CURRENCY::USD";

    private String action;

    private String memo;

    private String account;

    private Double amount;

    private static final String reconcile = "N";

    private static final String price = "1";

    private static Transaction clone(Transaction transaction) {
        Transaction clone = new Transaction();
        clone.date = transaction.date;
        clone.number = transaction.number;
        clone.description = transaction.description;
        clone.notes = transaction.notes;
        clone.action = transaction.action;
        clone.memo = transaction.memo;
        clone.account = transaction.account;
        clone.amount = transaction.amount;
        return clone;
    }

    public static ArrayList<Transaction> postPayPal(CSVRecord record) {
        Transaction t1 = new Transaction();
        t1.date = record.get(0);
        t1.number = record.get(12);
        t1.description = record.get(4);
        t1.notes = record.get(15);
        t1.memo =  record.get(3) + ";" + record.get(10) + ";" + record.get(13);
        switch (t1.description) {
            case "Donation Payment":
                return postDonation(record, t1);
            case "Express Checkout Payment":
                switch (record.get(15)) {
                    case "form-id:3;entry-id:null;field-key:b3-19":
                        return postPromotion(record, t1);
                    case "form-id:6;entry-id:null;field-key:b6-39":
                        return postAdmission(record, t1);
                    default:
                        throw new RuntimeException(record.get(15) + " Express Checkout Payment not recognized");
                }
            default:
                throw new RuntimeException(t1.description + " not supported");
        }
    }

    public static ArrayList<Transaction> postDonation(CSVRecord record, Transaction t1) {
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        // Post Donations
        t1.action = "PayPal Donation";
        t1.account = "Income:Donations";
        t1.amount = -1.0 * Double.parseDouble(record.get(7));
        transactions.add(t1);
        // Post A/R
        Transaction t2 = Transaction.clone(t1);
        t2.account = "Assets:Remittance Transfer Accounts:PayPal";
        t2.amount = Double.parseDouble(record.get(7));
        transactions.add(t2);
        // Record Paypal Fee
        Transaction t3 = Transaction.clone(t2);
        t3.action = "Paypal Payment";
        t3.amount = Double.parseDouble(record.get(8));
        transactions.add(t3);
        // Post Paypal Fee as Expense
        Transaction t4 = Transaction.clone(t3);
        t4.account = "Operating Expenses:Remittance Transfer Expenses:PayPal Fees";
        t4.amount = -1 * Double.parseDouble(record.get(8));
        transactions.add(t4);
        return transactions;
    }

    public static ArrayList<Transaction> postPromotion(CSVRecord record, Transaction t1) {
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        // Post Donations
        t1.action = "PayPal Promotion";
        switch (record.get(7)) {
            case "50.00":
                t1.account = "Convention Sales:Vendor Promotions:Bronze Promotion";
                break;
            case "75.00":
                t1.account = "Convention Sales:Vendor Promotions:Silver Promotion";
                break;
            case "100.00":
                t1.account = "Convention Sales:Vendor Promotions:Gold Promotion";
                break;
            case "150.00":
                t1.account = "Convention Sales:Vendor Promotions:Platinum Promotion";
                break;
            default:
                throw new RuntimeException(record.get(7) + " is not a supported promotion price");
        }
        t1.amount = -1.0 * Double.parseDouble(record.get(7));
        transactions.add(t1);
        // Post A/R
        Transaction t2 = Transaction.clone(t1);
        t2.account = "Assets:Remittance Transfer Accounts:PayPal";
        t2.amount = Double.parseDouble(record.get(7));
        transactions.add(t2);
        // Record Paypal Fee
        Transaction t3 = Transaction.clone(t2);
        t3.action = "Paypal Payment";
        t3.amount = Double.parseDouble(record.get(8));
        transactions.add(t3);
        // Post Paypal Fee as Expense
        Transaction t4 = Transaction.clone(t3);
        t4.account = "Convention Expenses:Remittance Transfer Fees:PayPal Fees";
        t4.amount = -1 * Double.parseDouble(record.get(8));
        transactions.add(t4);
        return transactions;
    }

    public static ArrayList<Transaction> postAdmission(CSVRecord record, Transaction t1) {
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        // Post Donations
        t1.action = "PayPal Promotion";
        t1.account = "Convention Sales:Registration";
        t1.amount = -1.0 * Double.parseDouble(record.get(7));
        transactions.add(t1);
        // Post A/R
        Transaction t2 = Transaction.clone(t1);
        t2.account = "Assets:Remittance Transfer Accounts:PayPal";
        t2.amount = Double.parseDouble(record.get(7));
        transactions.add(t2);
        // Record Paypal Fee
        Transaction t3 = Transaction.clone(t2);
        t3.action = "Paypal Payment";
        t3.amount = Double.parseDouble(record.get(8));
        transactions.add(t3);
        // Post Paypal Fee as Expense
        Transaction t4 = Transaction.clone(t3);
        t4.account = "Convention Expenses:Remittance Transfer Fees:PayPal Fees";
        t4.amount = -1 * Double.parseDouble(record.get(8));
        transactions.add(t4);
        return transactions;
    }

    public static void main(String[] args) throws IOException {
        String fileName = "D:\\Users\\wgfra\\IdeaProjects\\MnConvention\\src\\test\\resources\\pypL 7-31 TO 8-9-24.CSV";
        System.out.println("Date|Number|Description|Notes|Commodity|Action|Account|Amount|Memo");
        for (Transaction transaction: buildTransactions(fileName)) {
            System.out.println(transaction.toCSV());
        }
    }

    public static ArrayList<Transaction> buildTransactions(String fileName) throws IOException {
        ArrayList<Transaction> transactions  = new ArrayList<>();
        BufferedReader in = Files.newBufferedReader(Paths.get(fileName));
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
        int c = 0;
        for (CSVRecord record : records) {
            if (c != 0) {
                transactions.addAll(postPayPal(record));
            }
            c++;
        }
        in.close();
        return transactions;
    }

    public String toCSV() {
        StringBuilder sb = new StringBuilder();
        sb.append(date).append("|");
        sb.append(number).append("|");
        sb.append(description).append("|");
        sb.append(notes).append("|");
        sb.append(commodity).append("|");
        sb.append(action).append("|");
        sb.append(account).append("|");
        sb.append(amount).append("|");
        sb.append(memo);
        return sb.toString();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Transaction{");
        sb.append("date='").append(date).append('\'');
        sb.append(", number='").append(number).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", notes='").append(notes).append('\'');
        sb.append(", action='").append(action).append('\'');
        sb.append(", memo='").append(memo).append('\'');
        sb.append(", account='").append(account).append('\'');
        sb.append(", amount=").append(amount);
        sb.append('}');
        return sb.toString();
    }
}

