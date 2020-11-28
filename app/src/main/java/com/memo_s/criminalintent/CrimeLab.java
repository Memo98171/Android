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
    private Map<UUID,Crime> mCrimes;

    public static CrimeLab get(Context context){
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context){
        mCrimes = new LinkedHashMap<>();
        Log.d("CrimeLab", "new crime");
    }

    public void addCrime(UUID id, Crime c){
        mCrimes.put(id, c);
        Log.d("CrimeLab", "add crime");
    }

    public List<Crime> getCrimes(){
        Log.d("CrimeLab", "get crimes"+ mCrimes.values());
        return new ArrayList<>(mCrimes.values());
    }

    public Crime getCrime(UUID id){
        Log.d("CrimeLab", "get crime");

        return mCrimes.get(id);
    }
}
