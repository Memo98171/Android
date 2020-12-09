package com.memo_s.criminalintent.database;

public class CrimeDbSchema {

    //Define inner class to describe our table
    public static final class CrimeTable{
        public static final String NAME = "crimes";

        //Define inner class to describe the column
        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String REQUIRED = "required";
            public static final String SUSPECT = "suspect";
        }
    }
}
