package com.memo_s.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class CrimeListFragment extends Fragment {
    private static final int REQUEST_CRIME = 1;
    private static final String TAG = "CrimeListFragment";

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    private int listItemPosition;
    private static final String SAVED_LIST_ITEM_POSITION = "saved_list_item_position";

    private boolean mSubtitleView;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Log.d(TAG, "prima di savedInstance");
        if(savedInstanceState != null){
            Log.d(TAG, "if savedInstance");
            mSubtitleView = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
            listItemPosition = savedInstanceState.getInt(SAVED_LIST_ITEM_POSITION);
        }

        updateUI();
        return view;
    }

    //Si usa onResume() invece che onStart per motivi di sicurezza nell'aggiornare
    //il view fragment.
    //Se la mia attività è in pausa e usiamo onStart(), questa non verrà caricata
    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume called");

        updateUI();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPaused called");
    }


    private void updateUI(){
        Log.d("CrimeListFragment", "aperto updateUI");

        CrimeLab crimeLab = CrimeLab.get(getActivity());
        Log.d("UpdateUI", "parte1");

        List<Crime> crimes = crimeLab.getCrimes();
        Log.d("UpdateUI", "parte2");


        if(mAdapter == null){
            Log.d("upDateUI", "in if");
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }else{
            Log.d("upDateUI", "in else");
            mAdapter.notifyItemChanged(listItemPosition);
        }
        updateSubtitle();
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Crime mCrime;
        private ImageView mSolvedImageView;

        private Button mContactPoliceButton;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
            super(inflater.inflate(viewType, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = itemView.findViewById(R.id.crime_title);
            mDateTextView = itemView.findViewById(R.id.crime_date);
            mSolvedImageView = itemView.findViewById(R.id.imageView);

        }

        public void bind(Crime crime){
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());

            String pattern = "EEEE, MMMM, dd, yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String dateString = simpleDateFormat.format(mCrime.getDate());
            mDateTextView.setText(dateString);

            if(mCrime.isRequiresPolice()){
                mContactPoliceButton = itemView.findViewById(R.id.contact_police_button);
                mContactPoliceButton.setOnClickListener(v -> Toast.makeText(getActivity(), "Police contacted for: " + mCrime.getTitle(), Toast.LENGTH_SHORT).show());
            }else{
                mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
            }
        }

        @Override
        public void onClick(View view){
            listItemPosition = getAdapterPosition();
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            startActivityForResult(intent, REQUEST_CRIME);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater, parent, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        @Override
        public int getItemViewType(int position){
            if(mCrimes.get(position).isRequiresPolice()){
                return R.layout.list_item_crime_police;
            }else{
                return R.layout.list_item_crime;
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(mSubtitleView){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.new_crime:
                Log.d(TAG, "newCrime in");
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime.getId(), crime);
                Intent intent = CrimePagerActivity
                        .newIntent(getActivity(), crime.getId());
                startActivity(intent);
                Log.d(TAG, "finito newCrime");
                return true;
            case R.id.show_subtitle:
                mSubtitleView = !mSubtitleView;
                getActivity().invalidateOptionsMenu();
                updateUI();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void updateSubtitle(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        Log.d("upDateSubtitile", "prima");
        int crimeCount = crimeLab.getCrimes().size();
        Log.d("upDateSubtitile", "dopo");

        String subTitle = getString(R.string.subtitle_format, crimeCount);

        if(!mSubtitleView){
            subTitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subTitle);
        Log.d("upDateSubtitile", "finito Activity");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleView);
        savedInstanceState.putInt(SAVED_LIST_ITEM_POSITION, listItemPosition);
    }
}
