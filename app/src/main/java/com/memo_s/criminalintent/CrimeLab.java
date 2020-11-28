package com.memo_s.criminalintent;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimes;

    public static CrimeLab get(Context context){
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context){
        mCrimes = new ArrayList<Crime>();
        Log.d("CrimeLab", "new crime");
    }

    public void addCrime(Crime c){
        mCrimes.add(c);
        Log.d("CrimeLab", "add crime");
    }

    public List<Crime> getCrimes(){
        Log.d("CrimeLab", "get crimes"+ mCrimes);
        return mCrimes;
    }

    public Crime getCrime(UUID id){
        Log.d("CrimeLab", "get crime");

        for(Crime crime : mCrimes){
            if(crime.getId().equals(id)){
                return crime;
            }
        }
        return null;
    }

    public Boolean deleteCrime(UUID id){
        for(Crime crime : mCrimes){
            if(crime.getId().equals(id)){
                mCrimes.remove(crime);
                return true;
            }
        }
        return false;
    }
}
